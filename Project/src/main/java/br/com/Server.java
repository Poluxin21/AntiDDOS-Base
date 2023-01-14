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
    public static void main(String[] args) throws IOException {
      // Cria uma array para armazenar IPS conectados
      List < String > connectedIps = new ArrayList < > ();
      List < String > tempIps = new ArrayList < > ();
      // Cria um hash para IPS e Portas que serão bloqueados
      Set < String > blockedIps = new HashSet < > ();
      Set < Integer > blockedPorts = new HashSet < > ();

      try (ServerSocket serverSocket = new ServerSocket(10000)) {
          System.out.println("Servidor iniciado na porta 10000");
          while (true) {
              try (Socket socket = serverSocket.accept()) {
                  String clientIp = socket.getInetAddress().getHostAddress();
                  int clientPort = socket.getPort();
                  System.out.println("Novo cliente conectado: " + clientIp + " na porta " + clientPort);
                  // Verifica se o IP já está na lista de conectados
                  if (connectedIps.contains(clientIp)) {
                      // Adiciona um "recebedor de pacotes"
                      tempIps.add(clientIp);
                      // Verifica se o IP está na lista de bloqueados
                      if (blockedIps.contains(clientIp)) {
                          // Fecha a conexão
                          socket.close();
                          continue;
                  }
                }
                  // Adiciona o IP à lista de conectados
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
                                  double sizeMB = size / (1024);
                                  if (connectedIps.contains(clientIp)) {
                                    while ((packet = handle.getNextPacket()) != null) {
                                      System.out.println("IP: " + clientIp + " Packet Size: " + sizeMB);
                                        if (sizeMB > threshold) {
                                            // Mude o IP "192.168.0.100" para a maquina de mitigação
                                            InetAddress redirectAddress = InetAddress.getByName("192.168.0.100");
                                            int redirectPort = 6666;
                                            InetSocketAddress redirectIp = new InetSocketAddress(redirectAddress, redirectPort);
                                            //Redireciona o tráfego para o novo endereço IP e porta
                                            socket.connect(redirectIp);
                                        }
                                    }
                                    System.out.println("Captura de pacotes finalizada");
                                }
                          }
                      }
                    } catch (NotOpenException | NullPointerException e) {
                          e.printStackTrace();
                      }

                  } catch (PcapNativeException e) {
                      e.printStackTrace();
                  }
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      } catch (IOException e) {
        e.printStackTrace();
      }
  }
}
