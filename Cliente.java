

import java.io.*;
import java.net.*;

class Cliente {
    
    private static String ipServidor = "192.168.5.178";
    private static int portaServidor = 6789;

    public static String lerString () throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      return in.readLine();
    }



    public static void main(String argv[]){

        boolean servidorFechado = true;

        while (servidorFechado) {
            try {
            
                DatagramSocket clientSocket = new DatagramSocket(); // criando o socket
                clientSocket.setSoTimeout(10000);
                InetAddress ip = InetAddress.getByName(ipServidor); 

                System.out.println("ENVIANDO PING UDP AO SERVIDOR ");
                DatagramPacket sendPacket = new DatagramPacket(new byte[0],0, ip, portaServidor); //criando o pacote
                clientSocket.send(sendPacket); //enviando o pacote

                System.out.println("ESPERANDO RESPOSTA DO SERVIDOR");
                DatagramPacket receivePacket = new DatagramPacket(new byte[0], 0);
                clientSocket.receive(receivePacket);
                servidorFechado = false;
                System.out.println("PING UDP RECEBIDO COM SUCESSO");
                clientSocket.close();

            } catch (Exception e) {
                System.out.println("PING UDP ERROR");
            }
        }

        try {
            Socket socket = new Socket(ipServidor, portaServidor); //conectando no servidor

            DataOutputStream saida = new DataOutputStream(socket.getOutputStream()); //variavel que a mensagem usa pra sair para o servidor
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream())); //variavel que a mensagem usa pra receber do servidor

            while (true) {

                saida.writeBytes(lerString() + '\n'); //cliente manda a msg
                System.out.println(entrada.readLine()); //cliente recebe a msg

            }

            /*  socket.close(); //fechamento da conexão */
        } catch (Exception e) {
            // TODO: handle exception
        }
    }    
}
