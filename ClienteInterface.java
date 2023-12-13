import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClienteInterface extends JFrame {
    private JTextField ipServidorField;
    private String portaField;
    private JButton conectarUDPButton;
    private JButton conectarTCPButton;
    private JTextArea mensagemArea;
    private JTextField mensagemEnviarField;
    private JButton enviarButton;
    private Socket socket;
    private PrintWriter saida;

    public ClienteInterface() {
        // Configurações básicas da janela
        setTitle("Interface do Cliente");
        setSize(850, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel para campos e botão
        JPanel painelSuperior = new JPanel(new FlowLayout());
        JPanel painelInferior = new JPanel(new FlowLayout());
        JLabel ipServidorLabel = new JLabel("Endereco IPv4 do Servidor:");
        ipServidorField = new JTextField(15);
        mensagemEnviarField = new JTextField(30);

        // Definindo o valor fixo da porta como "6596"
        JLabel portaLabel = new JLabel("Porta:");
        portaField = new String("6596");

        conectarTCPButton = new JButton("Conectar TCP");
        conectarUDPButton = new JButton("Conectar UDP");
        enviarButton = new JButton("Enviar");

        painelSuperior.add(ipServidorLabel);
        painelSuperior.add(ipServidorField);
        painelSuperior.add(portaLabel);
        painelSuperior.add(new JLabel("6596"));
        painelSuperior.add(conectarTCPButton);
        painelSuperior.add(conectarUDPButton);

        // Area de mensagens
        mensagemArea = new JTextArea();
        mensagemArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensagemArea);

        // Adiciona os componentes à janela
        add(painelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);

        painelInferior.add(mensagemEnviarField);
        painelInferior.add(enviarButton);
        enviarButton.setEnabled(false);

        // Ação do botão conectar com o chat TCP
        conectarTCPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try{
                    String ipServidor = ipServidorField.getText();
                    int porta = Integer.parseInt(portaField);

                    socket = new Socket(ipServidor, porta);

                    mensagemArea
                            .append("Chat TCP\n" + "Conectado ao servidor " + ipServidor + " na porta " + porta + "\n");

                    conectarTCPButton.setEnabled(false);
                    conectarUDPButton.setEnabled(false);
                    enviarButton.setEnabled(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao servidor.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String mensagem = mensagemEnviarField.getText();
                mensagemArea.append("Cliente: " + mensagem + "\n");
                mensagemEnviarField.setText("");

                Thread leituraThread = new Thread(new LeituraDoServidor(mensagem));
            leituraThread.start();
            }
        });

        // Ação do botão conectar com o chat UDP
        conectarUDPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ipServidor = ipServidorField.getText();
                int porta = Integer.parseInt(portaField);
        
                DatagramSocket socket;
                try {
                    socket = new DatagramSocket();
                    InetAddress ipServidorAddr = InetAddress.getByName(ipServidor);
        
                    String mensagem = "Olá do Cliente UDP";
                    byte[] buffer = mensagem.getBytes();
        
                    DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, ipServidorAddr, porta);
                    socket.send(sendPacket);
        
                    byte[] receiveData = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
        
                    String mensagemRecebida = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    mensagemArea.append("Resposta do servidor UDP: " + mensagemRecebida + "\n");
        
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao servidor UDP.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ClienteInterface cliente = new ClienteInterface();
                cliente.setVisible(true);
            }
        });
    }
}

class LeituraDoServidor implements Runnable {
    private String entrada;

    public LeituraDoServidor(String mensagem) {
        this.entrada = mensagem;
    }

    @Override
    public void run() {
        String mensagem;
            StringReader stringReader = new StringReader(entrada);
            BufferedReader bufferedReader = new BufferedReader(stringReader);

        try {
            while ((mensagem = bufferedReader.readLine()) != null) {
                System.out.println("Mensagem do cliente: " + mensagem);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}