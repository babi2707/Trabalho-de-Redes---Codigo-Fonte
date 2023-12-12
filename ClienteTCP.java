import java.io.*;
import java.net.*;

public class ClienteTCP {
    private static String ipServidor = "192.168.0.16";
    private static int portaServidor = 6790;

    public static String lerString() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        return in.readLine();
    }

    public static void main(String argv[]) throws Exception {
        Socket socket = new Socket(ipServidor, portaServidor);

        BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

        Thread leituraThread = new Thread(new LeituraDoServidor(entrada));
        leituraThread.start();

        String mensagem;
        while ((mensagem = lerString()) != null) {
            saida.println(mensagem);
        }

        socket.close();
    }
}

class LeituraDoServidor implements Runnable {
    private BufferedReader entrada;

    public LeituraDoServidor(BufferedReader entrada) {
        this.entrada = entrada;
    }

    @Override
    public void run() {
        String mensagem;
        try {
            while ((mensagem = entrada.readLine()) != null) {
                System.out.println("Mensagem do servidor: " + mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
