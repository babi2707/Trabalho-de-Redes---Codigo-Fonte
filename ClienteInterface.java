import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClienteInterface extends JFrame {
    private JTextField ipServidorField;
    private String portaField;
    private JButton conectarButton;
    private JTextArea mensagemArea;

    public ClienteInterface() {
        // Configurações básicas da janela
        setTitle("Interface do Cliente");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel para campos e botão
        JPanel painelSuperior = new JPanel(new FlowLayout());
        JLabel ipServidorLabel = new JLabel("Endereco IPv4 do Servidor:");
        ipServidorField = new JTextField(15);
        JLabel portaLabel = new JLabel("Porta:");

        // Definindo o valor da porta como "6596"
        portaField = new String("6596");

        conectarButton = new JButton("Conectar");

        painelSuperior.add(ipServidorLabel);
        painelSuperior.add(ipServidorField);
        painelSuperior.add(portaLabel);
        painelSuperior.add(new JLabel("6596"));
        painelSuperior.add(conectarButton);

        // Area de mensagens
        mensagemArea = new JTextArea();
        mensagemArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(mensagemArea);

        // Adiciona os componentes à janela
        add(painelSuperior, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Configura a ação do botão conectar
        conectarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Aqui você pode colocar a lógica de conexão com o servidor
                // Utilizando os valores de IP e porta fornecidos
                String ipServidor = ipServidorField.getText();
                int porta = Integer.parseInt(portaField);

                // Exemplo de exibição de mensagem na área de texto
                mensagemArea.append("Conectado ao servidor " + ipServidor + " na porta " + porta + "\n");
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
