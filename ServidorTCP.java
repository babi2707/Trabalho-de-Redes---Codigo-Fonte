import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServidorTCP {
    public static final String clienteContador = null;
    private static int portaServidor = 6596;
    private static int contadorClientes = 0;
    private static Map<Integer, PrintWriter> clientes = new HashMap<>();

    public static void main(String argv[]) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(portaServidor)) {
            System.out.println("Servidor esperando conexões...");

            // Aceita múltiplos clientes
            while (true) {
                Socket conexao = serverSocket.accept();
                PrintWriter clienteSaida = new PrintWriter(conexao.getOutputStream(), true);
                clientes.put(contadorClientes, clienteSaida);

                // Cria uma thread para lidar com cada cliente
                Thread clientThread = new Thread(new ClienteHandler(conexao, contadorClientes));
                clientThread.start();

                contadorClientes++;
                // Adiciona o PrintWriter deste cliente à lista
                System.out.println("Cliente " + contadorClientes + " conectado.");
            }
        }
    }

    // Método para enviar a mensagem a todos os clientes
    public static void broadcast(String mensagem) {
        System.out.println("Enviando mensagem a todos os clientes: " + mensagem);
        for (PrintWriter clienteSaida : clientes.values()) {
            clienteSaida.println("Cliente " + contadorClientes + ": " + mensagem);
        }
    }
    
}

class ClienteHandler implements Runnable {
    private Socket conexao;

    public ClienteHandler(Socket conexao, int contadorClientes) {
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
