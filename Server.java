// **SCRIPT ANTIDDOS CRIADO POR POLUX, NAO REMOVA ESSES CREDITOS**

import java.net.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Server {
    static int init = 0;
    public static void main(String[] args) throws IOException {

          // Cria uma array para armazenar IPS conectados
          List<String> connectedIps = new ArrayList<>();

          // Cria um hash para IPS e Portas que serão bloqueados
          Set<String> blockedIps = new HashSet<>();
          Set<Integer> blockedPorts = new HashSet<>();

        try (ServerSocket serverSocket = new ServerSocket(10000)) {
              System.out.println("Servidor iniciado na porta 10000");
              while (true) {
                  try (Socket socket = serverSocket.accept()) {
                      String clientIp = socket.getInetAddress().getHostAddress();
                      int clientPort = socket.getPort();

                      // Verificação do IP
                      if (blockedIps.contains(clientIp) || blockedPorts.contains(clientPort)) {
                          // Bloqueia o IP e Fecha a conexão
                          System.out.println("[ANTIDDOS]" + clientIp + ":" + clientPort);
                          socket.close();
                      } else {
                          // Aceita Conexão e adiciona a um array
                          System.out.println("[Conexão Aceita] " + clientIp + ":" + clientPort);
                          connectedIps.add(clientIp);
                          System.out.println(connectedIps);
                      }
                      // Verifica se existe o IP na lista, Se sim. Bloqueia o IP e a Porta
                      String IPtoCheck = clientIp;
                      int count = Collections.frequency(connectedIps, IPtoCheck);
                      if (count == 1) {
                          blockedIps.add(clientIp);
                          // Remova o "//" Caso queira que a porta seja bloqueada tambem
                          // blockedPorts.add(clientPort);
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
