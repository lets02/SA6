package Controller;

import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Connection.EstoqueDAO;
import Model.Estoque;

public class EstoqueControl {
    // atributos
    private List<Estoque> estoques;
    private DefaultTableModel tableModel;
    private JTable table;

    // construtor
    public EstoqueControl(List<Estoque> estoques, DefaultTableModel tableModel, JTable table) {
        this.estoques = estoques;
        this.tableModel = tableModel;
        this.table = table;
    }

    private void atualizarTabela() {
        tableModel.setRowCount(0); // Limpa todas as linhas existentes na tabela
        estoques = new EstoqueDAO().listarTodos(); // Obtém os carros atualizados do banco de dados
        for (Estoque estoque : estoques) {
            // Adiciona os dados de cada carro como uma nova linha na tabela Swing
            tableModel.addRow(new Object[] { estoque.getNomeProduto(), estoque.getCodBarras(), estoque.getquantidade(),
                    estoque.getpreco() });
        }
    }

    public void cadastrar(String nomeProduto, String codBarras, String quantidade, String preco) {
        new EstoqueDAO().cadastrar(nomeProduto, codBarras, quantidade, preco);
        // Chama o método de cadastro no banco de dados
        atualizarTabela(); // Atualiza a tabela de exibição após o cadastro
    }

    public void atualizar(String nomeProduto, String codBarras, String quantidade, String preco) {
        new EstoqueDAO().atualizar(nomeProduto, codBarras, quantidade, preco);
        // Chama o método de atualização no banco de dados
        atualizarTabela(); // Atualiza a tabela de exibição após a atualização
    }

    public void apagar(String codBarras) {
        new EstoqueDAO().apagar(codBarras);
        // Chama o método de exclusão no banco de dados
        atualizarTabela(); // Atualiza a tabela de exibição após a exclusão
    }
}
