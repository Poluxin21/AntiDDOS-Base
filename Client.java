import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) throws UnknownHostException, IOException {
        // Endereço IP do servidor
        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
        // Porta do servidor
        int serverPort = 10000;

        // Cria um novo socket TCP
        Socket socket = new Socket();
        System.out.println("Conectando ao servidor " + serverAddress + ":" + serverPort);
        socket.connect(new InetSocketAddress(serverAddress, serverPort));

        // Cria uma string de tamanho 2 KB
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2 * 1024; i++) {
            sb.append("a");
        }
        String packetData = sb.toString();

        // Obtém o fluxo de saída do socket
        OutputStream output = socket.getOutputStream();

        // Loop para manter a conexão aberta e enviar pacotes
        while (true) {
            // Envia o pacote
            output.write(packetData.getBytes());
            System.out.println("Pacote de 2KB enviado para o servidor");
        }
    }
}
