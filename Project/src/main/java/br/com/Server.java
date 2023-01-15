import org.pcap4j.core.*;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import java.io.IOException;
import java.util.HashMap;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.net.UnknownHostException;
import java.util.Set;
import java.net.InetSocketAddress;

public class Server {
    // Cria uma array para armazenar IPS conectados
    private List < String > connectedIps = new ArrayList < > ();
    // Cria um hash para IPS e Portas que serão bloqueados
    private Set < String > blockedIps = new HashSet < > ();
    private Set < Integer > blockedPorts = new HashSet < > ();

    public static void main(String[] args) {
        new Server().start();
    }

    private void start() {
        try (ServerSocket serverSocket = new ServerSocket(10000)) {
            System.out.println("Servidor iniciado na porta 10000");
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(new ClientHandler(socket)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String clientIp = socket.getInetAddress().getHostAddress();
            int clientPort = socket.getPort();
            System.out.println("Novo cliente conectado: " + clientIp + " na porta " + clientPort);

            // Verifica se o IP já está na lista de conectados
            if (connectedIps.contains(clientIp)) {
                // Verifica se o IP está na lista de bloqueados
                if (blockedIps.contains(clientIp)) {
                    // Fecha a conexão
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
            } else {
                connectedIps.add(clientIp);
                try {
                    HashMap < String, Integer > packetSizesByIp = new HashMap < > ();
                    // Obtém a lista de interfaces de rede disponíveis
                    List < PcapNetworkInterface > interfaces = Pcaps.findAllDevs();
                    // Seleciona a primeira interface da lista
                    PcapNetworkInterface device = interfaces.get(0);
                    // Abre a interface com o modo de captura promíscuo ativado
                    PcapHandle handle = device.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
                    // Cria um filtro para capturar apenas pacotes IP, TCP e UDP
                    String filter = "";
                    try {
                        handle.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE);
                        // Inicia a captura de pacotes
                        Packet packet = handle.getNextPacket();
                        if (handle.isOpen()) {
                            System.out.println("Iniciando captura de pacotes");
                            while (packet != null) {
                                //Obtém o endereço IP do cliente
                                IpV4Packet ipV4Packet = packet.get(IpV4Packet.class);
                                //Obtém o tamanho do pacote
                                double size = packet.length();
                                // Define que o Limite é 30MB
                                double threshold = 30 * 1024 * 1024;
                                double sizeKB = size / (1024);
                                if (connectedIps.contains(clientIp)) {
                                    while ((packet = handle.getNextPacket()) != null) {
                                        System.out.println("IP: " + clientIp + " Packet Size: " + sizeKB);
                                        if (sizeKB > threshold) {
                                            try {
                                                // Obtém o endereço IP a ser bloqueado a partir dos argumentos da linha de comando
                                                String ipToBlock = clientIp;
                                                // Cria o comando para adicionar uma regra de bloqueio de IP no iptables
                                                String command = "iptables -A INPUT -s " + ipToBlock + " -j DROP";
                                                // Executa o comando no terminal
                                                Process p = Runtime.getRuntime().exec(command);
                                                p.waitFor();
                                                System.out.println("IP " + ipToBlock + " foi bloqueado com sucesso.");
                                            } catch (IOException | InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (NotOpenException | NullPointerException e) {
                        e.printStackTrace();
                    }
                } catch (PcapNativeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
