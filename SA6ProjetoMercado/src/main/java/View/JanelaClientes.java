package View;

import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import Connection.ClientesDAO;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import Controller.ClientesControl;
import Model.ClientesVIP;

public class JanelaClientes extends JPanel {
    // atributos - componentes
    // campo de texto - JTextField
    private JTextField inputCpf;
    private JTextField inputNome;
    private JTextField inputTelefone;
    // campo escrito - JLabel
    private JLabel labelCpf;
    private JLabel labelNome;
    private JLabel labelTelefone;

    private DefaultTableModel tableModel; // lógica
    private JTable table; // visual
    private List<ClientesVIP> clientes = new ArrayList<>();
    private int linhaSelecionada = -1;
    private JButton cadastrarButton, apagarButton, editarButton;

    public JanelaClientes() {
        JPanel frame1 = new JPanel();
        JPanel inputFrame = new JPanel();
        JPanel botoes = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        frame1.setLayout(new BorderLayout());

        // construir a tabela
        tableModel = new DefaultTableModel();
        tableModel.addColumn("CPF");
        tableModel.addColumn("Nome");
        tableModel.addColumn("Telefone");

        table = new JTable(tableModel);
        table.setBackground(Color.white);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setViewportView(table);

        // criar os componentes
        inputCpf = new JTextField(20);

        inputNome = new JTextField(20);

        inputTelefone = new JTextField(20);

        // criar os componentes - labels
        labelCpf = new JLabel("CPF");

        labelNome = new JLabel("Nome");

        labelTelefone = new JLabel("Telefone");

        // botões
        cadastrarButton = new JButton("Cadastrar");
        cadastrarButton.setBackground(Color.white);

        apagarButton = new JButton("Apagar");
        apagarButton.setBackground(Color.white);

        editarButton = new JButton("Editar");
        editarButton.setBackground(Color.white);

        // adicionar os componentes

        inputFrame.add(labelNome);
        inputFrame.add(inputNome);
        inputFrame.add(labelCpf);
        inputFrame.add(inputCpf);
        inputFrame.add(labelTelefone);
        inputFrame.add(inputTelefone);

        botoes.add(cadastrarButton);
        botoes.add(apagarButton);
        botoes.add(editarButton);

        this.add(frame1);
        frame1.add(scrollPane, BorderLayout.NORTH);
        frame1.add(inputFrame, BorderLayout.CENTER);
        frame1.add(botoes, BorderLayout.SOUTH);

        // Criar o banco de dados
        new ClientesDAO().criaTabela();

        // incluir os elementos do banco na criação do painel
        atualizarTabela();

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                linhaSelecionada = table.rowAtPoint(evt.getPoint());
                if (linhaSelecionada != -1) {
                    inputCpf.setText((String) table.getValueAt(linhaSelecionada, 0));
                    inputNome.setText((String) table.getValueAt(linhaSelecionada, 1));
                    inputTelefone.setText((String) table.getValueAt(linhaSelecionada, 2));
                }
            }
        });

        ClientesControl operacoes = new ClientesControl(clientes, tableModel, table);

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputCpf.getText().isEmpty() || inputNome.getText().isEmpty()
                        || inputTelefone.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "ATENÇÃO! \nExistem campos em branco");
                } else {
                    if (!validarFormatoCPF(inputCpf.getText())) {
                        JOptionPane.showMessageDialog(null,
                                "CPF inválido! O CPF deve conter apenas números e ter 11 dígitos.");
                    } else if (!inputTelefone.getText().matches("[0-9]+") || inputTelefone.getText().length() < 11) {
                        JOptionPane.showMessageDialog(null, "O campo 'Telefone' deve conter apenas números.");
                        JOptionPane.showMessageDialog(null, "Adicione  no seguinte formato 19999999999.");
                    } else if (!inputNome.getText().matches("[a-zA-ZÀ-ú\\s]+")) {
                        JOptionPane.showMessageDialog(null, "O campo 'Nome' deve conter apenas letras.");
                    }

                    else {
                        // Chama o método "cadastrar" do objeto operacoes com os valores dos campos de
                        // entrada
                        operacoes.cadastrar(inputCpf.getText(), inputNome.getText(), inputTelefone.getText());

                        // Limpa os campos de entrada após a operação de cadastro
                        inputCpf.setText("");
                        inputNome.setText("");
                        inputTelefone.setText("");
                    }
                }

            }
        });

        editarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputCpf.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione algo para editar");
                } else {
                    operacoes.atualizar(labelCpf.getText(), labelNome.getText(),
                            labelTelefone.getText());

                    // Limpa os campos de entrada após a operação de atualização
                    inputCpf.setText("");
                    inputNome.setText("");
                    inputTelefone.setText("");
                    JOptionPane.showMessageDialog(null, "Informação editada com Sucesso!");
                }

            }
        });

        apagarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputCpf.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Selecione um cliente para apagar.");
                } else {
                    int resposta = JOptionPane.showConfirmDialog(null, "Tem certeza de que deseja apagar os campos?",
                            "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (resposta == JOptionPane.YES_OPTION) {
                        // Chama o método "apagar" do objeto operacoes com o valor do campo de entrada
                        // "placa"
                        operacoes.apagar(inputCpf.getText());
                        JOptionPane.showMessageDialog(null, "O Cliente " + inputNome.getText() + " de CPF "
                                + inputCpf.getText() + " foi deletado!");
                        // Limpa os campos de entrada após a operação de exclusão
                        inputCpf.setText("");
                        inputNome.setText("");
                        inputTelefone.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "O cliente não foi deletado!");
                    }
                }
            }
        });
    }

    // atualizar Tabela de Carros com o Banco de Dados
    private void atualizarTabela() {
        // atualizar tabela pelo banco de dados
        tableModel.setRowCount(0);
        clientes = new ClientesDAO().listarTodos();
        for (ClientesVIP cliente : clientes) {
            tableModel.addRow(
                    new Object[] { cliente.getCpf(), cliente.getNome(), cliente.getTelefone() });
        }
    }

    private boolean validarFormatoCPF(String cpf) {
        // Remove caracteres não numéricos do CPF
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF possui 11 dígitos
        return cpf.length() == 11;
    }
}
