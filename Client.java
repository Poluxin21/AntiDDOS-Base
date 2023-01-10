import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) throws IOException {
        // Cria um socket de cliente e se conecta ao servidor na porta 10000
        Socket clientSocket = new Socket("localhost", 10000);
        System.out.println("Conectado ao servidor");

        // Cria um fluxo de entrada para ler dados do servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        // Lê a mensagem de boas-vindas do servidor
        String welcomeMessage = in.readLine();
        System.out.println("Mensagem do servidor: " + welcomeMessage);

        // Fecha a conexão com o servidor
        // clientSocket.close();
    }
}
