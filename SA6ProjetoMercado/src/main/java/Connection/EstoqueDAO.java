package Connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Model.Estoque;

public class EstoqueDAO {
    // atributo
    private Connection connection;
    private List<Estoque> estoques;

    // construtor
    public EstoqueDAO() {
        this.connection = ConnectionFactory.getConnection();
    }

    // criar Tabela
    public void criaTabela() {

        String sql = "CREATE TABLE IF NOT EXISTS estoque_mercado (nomeProduto VARCHAR(255),codBarras VARCHAR(255) PRIMARY KEY,quantidade VARCHAR(255),preco VARCHAR(255))";
        try (Statement stmt = this.connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela criada com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar a tabela: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.closeConnection(this.connection);
        }
    }

    // Listar todos os valores cadastrados
    public List<Estoque> listarTodos() {
        PreparedStatement stmt = null; // Declaração do objeto PreparedStatement para executar a consulta
        ResultSet rs = null; // Declaração do objeto ResultSet para armazenar os resultados da consulta
        estoques = new ArrayList<>(); // Cria uma lista para armazenar os carros recuperados do banco de dados

        try {
            stmt = connection.prepareStatement("SELECT * FROM estoque_mercado");
            // Prepara a consulta SQL para selecionar todos os registros da tabela
            rs = stmt.executeQuery();
            // Executa a consulta e armazena os resultados no ResultSet

            while (rs.next()) {
                // Para cada registro no ResultSet, cria um objeto Carros com os valores do
                // registro
                Estoque estoque = new Estoque(
                        rs.getString("nomeProduto"),
                        rs.getString("codBarras"),
                        rs.getString("quantidade"),
                        rs.getString("preco"));
                estoques.add(estoque); // Adiciona o objeto Carros à lista de carros
            }
        } catch (SQLException ex) {
            System.out.println(ex); // Em caso de erro durante a consulta, imprime o erro
        } finally {
            ConnectionFactory.closeConnection(connection, stmt, rs); // Fecha a conexão, o PreparedStatement e o
                                                                     // ResultSet
        }
        return estoques; // Retorna a lista de carros recuperados do banco de dados
    }

    // Cadastrar Carro no banco
    public void cadastrar(String nomeProduto, String codBarras, String quantidade, String preco) {
        PreparedStatement stmt = null;
        // Define a instrução SQL parametrizada para cadastrar na tabela
        String sql = "INSERT INTO estoque_mercado (nomeProduto, codBarras, quantidade, preco) VALUES (?, ?, ?, ?)";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeProduto);
            stmt.setString(2, codBarras);
            stmt.setString(3, quantidade);
            stmt.setString(4, preco);
            stmt.executeUpdate();
            System.out.println("Dados inseridos com sucesso");
            JOptionPane.showMessageDialog(null, "Você Cadastrou o produto com sucesso ✅");
        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                JOptionPane.showMessageDialog(null, "\"Erro: A placa inserida já existe na tabela.\"");
            } else {
                throw new RuntimeException("Erro ao inserir dados no banco de dados.", e);
            }
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    // Atualizar dados no banco
    public void atualizar(String nomeProduto, String quantidade, String preco, String codBarras) {
        PreparedStatement stmt = null;
        // Define a instrução SQL parametrizada para atualizar dados pela placa
        String sql = "UPDATE estoque_mercado SET nomeProduto = ?, quantidade = ?, preco = ? WHERE codBarras = ?";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomeProduto);
            stmt.setString(2, quantidade);
            stmt.setString(3, preco);
            // placa é chave primaria não pode ser alterada.
            stmt.setString(5, codBarras);
            stmt.executeUpdate();

            System.out.println("Dados atualizados com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar dados no banco de dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

    // Apagar dados do banco
    public void apagar(String codBarras) {
        PreparedStatement stmt = null;
        // Define a instrução SQL parametrizada para apagar dados pelo código barras
        String sql = "DELETE FROM estoque_mercado WHERE codBarras = ?";
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, codBarras);
            stmt.executeUpdate(); // Executa a instrução SQL
            System.out.println("Dado apagado com sucesso");
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao apagar dados no banco de dados.", e);
        } finally {
            ConnectionFactory.closeConnection(connection, stmt);
        }
    }

}
