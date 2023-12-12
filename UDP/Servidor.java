package UDP;

import java.io.*;
import java.net.*;

class Servidor {
    private static int portaServidor = 6789;

    public static String lerString () throws Exception {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      return in.readLine();
    }

    public static void main(String argv[]){

        try {
            
            DatagramSocket serverSocket = new DatagramSocket(portaServidor);

            DatagramPacket receivePacket = new DatagramPacket(new byte[0], 0);

            System.out.println("Aguardando datagrama do cliente....");
            serverSocket.receive(receivePacket);

            System.out.println("RECEIVED: " + new String(receivePacket.getData()));
            InetAddress ipCliente = receivePacket.getAddress();
            int portaCliente = receivePacket.getPort();

            DatagramPacket sendPacket = new DatagramPacket(new byte[0], 0, ipCliente, portaCliente);
            serverSocket.send(sendPacket);
            System.out.println("Enviado...");

        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            ServerSocket socket = new ServerSocket(portaServidor); //conectado ao cliente
            Socket conexao = socket.accept();

            BufferedReader entrada =  new BufferedReader(new InputStreamReader(conexao.getInputStream())); //variavel que a mensagem usa pra sair para o servidor
            DataOutputStream saida = new DataOutputStream(conexao.getOutputStream()); //variavel que a mensagem usa pra receber do servidor

            while(true){
                
                System.out.println(entrada.readLine()); //printando a mensagem do cliente
                saida.writeBytes(lerString() + '\n'); //escrevendo a mensagem do servidor
            }

            /*  socket.close(); //fechamento da conexão */
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
