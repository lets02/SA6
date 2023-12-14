package View;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class JanelaPrincipal extends JFrame {
    public JanelaPrincipal() {
        super("Mercado");
        setDefaultCloseOperation(2);
        JTabbedPane abas = new JTabbedPane();

        abas.add("Vendas", new JanelaVendas());
        abas.add("Estoque", new JanelaEstoque());
        abas.add("Cadastro Clientes", new JanelaClientes());
        
        this.add(abas);
        setBounds(300, 50, 700, 600);
        setResizable(true);
    }

    public void run() {
        setVisible(true);
    }
}
