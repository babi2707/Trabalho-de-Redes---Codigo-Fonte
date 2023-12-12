import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ServidorTCP {
    private static int portaServidor = 6596;
    static int clienteContador = 0;
    private static List<PrintWriter> clientes = new ArrayList<>();

    public static void main(String argv[]) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(portaServidor)) {
            System.out.println("Servidor esperando conexões...");

            // Aceita múltiplos clientes
            while (true) {
                Socket conexao = serverSocket.accept();
                clienteContador++;
                System.out.println("Cliente " + clienteContador + " conectado.");

                // Cria uma thread para lidar com cada cliente
                Thread clientThread = new Thread(new ClienteHandler(conexao));
                clientThread.start();

                // Adiciona o PrintWriter deste cliente à lista
                PrintWriter clienteSaida = new PrintWriter(conexao.getOutputStream(), true);
                clientes.add(clienteSaida);
            }
        }
    }

    // Método para enviar a mensagem a todos os clientes
    public static void broadcast(String mensagem) {
        System.out.println("Enviando mensagem a todos os clientes: " + mensagem);
        for (PrintWriter clienteSaida : clientes) {
            clienteSaida.println("Cliente " + clienteContador + ": " + mensagem);
        }
    }
}

class ClienteHandler implements Runnable {
    private Socket conexao;

    public ClienteHandler(Socket conexao) {
        this.conexao = conexao;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));

            String mensagem;
            while ((mensagem = entrada.readLine()) != null) {
                System.out.println("Recebido do cliente " + ServidorTCP.clienteContador + ": " + mensagem);
                // Chama o método broadcast para enviar a mensagem a todos os clientes
                ServidorTCP.broadcast(mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
