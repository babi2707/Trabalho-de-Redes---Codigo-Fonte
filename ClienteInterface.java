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
        JLabel ipServidorLabel = new JLabel("Endereco IPv4 do Servidor:");
        ipServidorField = new JTextField(15);

        // Definindo o valor fixo da porta como "6596"
        JLabel portaLabel = new JLabel("Porta:");
        portaField = new String("6596");

        conectarTCPButton = new JButton("Conectar TCP");
        conectarUDPButton = new JButton("Conectar UDP");

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

        // Ação do botão conectar com o chat TCP
        conectarTCPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ipServidor = ipServidorField.getText();
                    int porta = Integer.parseInt(portaField);

                    mensagemArea
                            .append("Chat TCP\n" + "Conectado ao servidor " + ipServidor + " na porta " + porta + "\n");

                    socket = new Socket(ipServidor, porta);

                    BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    saida = new PrintWriter(socket.getOutputStream(), true);

                    Thread leituraThread = new Thread(new LeituraDoServidor(entrada));
                    leituraThread.start();

                    conectarTCPButton.setEnabled(false);
                    conectarUDPButton.setEnabled(false);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Erro ao conectar ao servidor.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação do botão conectar com o chat UDP
        conectarUDPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String ipServidor = ipServidorField.getText();
                int porta = Integer.parseInt(portaField);

                mensagemArea.append("Chat UDP\n" + "Conectado ao servidor " + ipServidor + " na porta " + porta + "\n");
                conectarTCPButton.setEnabled(false);
                conectarUDPButton.setEnabled(false);
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
