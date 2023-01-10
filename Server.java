import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Server {
    static int init = 0;
    public static void main(String[] args) throws IOException {
        // Cria um socket de servidor na porta 10000
        ServerSocket serverSocket = new ServerSocket(10000);
        // Cria o arquivo de IP
        Path filePath = Paths.get("IPS.txt");;
        System.out.println("Servidor iniciado na porta 10000");
        while (true) {
          // Fica à escuta de uma conexão de cliente
            Socket clientSocket = serverSocket.accept();
            System.out.println("Conexão de cliente aceita");
          // Lê os IPS que se conectaram recentemente
            List<String> lines = Files.readAllLines(filePath);
            for (int i = 0; i < lines.size(); i++) {
            lines.set(i, lines.get(i).replace("[]", ""));
          }

            // Obtém o endereço IP do cliente
            InetAddress clientAddress = clientSocket.getInetAddress();
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(lines + "\n" + clientAddress.getHostAddress());
            }
            System.out.println("Endereço IP do cliente: " + clientAddress.getHostAddress());


            // Obtem o tempo de conexão
            Date startTime = new Date();
            Date endTime = new Date();
            long duration = endTime.getTime() - startTime.getTime();
            long s = duration / 1000;

            // Obtém a porta do cliente e a porta do servidor
            int clientPort = clientSocket.getPort();
            int serverPort = clientSocket.getLocalPort();
            System.out.println("Porta do cliente: " + clientPort);
            System.out.println("Porta do servidor: " + serverPort);

            // Cria um fluxo de saída para enviar dados para o cliente
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Envia uma mensagem de boas-vindas para o cliente
            out.print("Conexão recebida");
            if (lines.contains(clientAddress.getHostAddress())) {
              System.out.println("[ANTIDDOS] Conexão interceptada");
              System.out.println("[ANTIDDOS] IP: " + clientAddress.getHostAddress());
              System.out.println("[ANTIDDOS] Porta: " + clientPort);
              // break;
            }
      }
    }
}
