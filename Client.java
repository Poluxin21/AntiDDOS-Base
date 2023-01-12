import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {
    public static void main(String[] args) throws IOException {
        // endereço IP e porta do servidor
        InetAddress serverIp = InetAddress.getByName("127.0.0.1");
        int serverPort = 10000;

        // cria um socket UDP
        DatagramSocket socket = new DatagramSocket();

        // envia pacotes para o servidor
        while (true) {
            // cria o pacote a ser enviado
            byte[] data = "Hello, Server!".getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length, serverIp, serverPort);

            // envia o pacote
            socket.send(packet);

            // imprime mensagem de log
            System.out.println("Packet sent to " + serverIp + ":" + serverPort);

            // espera 1 segundo antes de enviar o próximo pacote
            Thread.sleep(1000);
        }
    }
}
