package javafxmvc.sistemavendas.model.database;

import javafxmvc.sistemavendas.model.domain.Categoria;
import javafxmvc.sistemavendas.model.domain.Cliente;
import javafxmvc.sistemavendas.model.domain.Produto;
import javafxmvc.sistemavendas.model.domain.Venda;
import javafxmvc.sistemavendas.model.domain.ItemDeVenda;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseMySQL implements Database {

    private Connection connection;

    @Override
    public Connection conectar() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String url = "jdbc:mariadb://127.0.0.1:3306/javafxmvc";
            String user = "root";
            String password = "root";

            this.connection = DriverManager.getConnection(url, user, password);
            criarTabelas();
            return this.connection;
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void desconectar(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void criarTabelas() {
        criarTabelaClientes();
        criarTabelaCategorias();
        criarTabelaProdutos();
        criarTabelaVendas();
        criarTabelaItensDeVenda();
    }

    private void criarTabelaClientes() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes ("
                + "cdCliente INT AUTO_INCREMENT PRIMARY KEY,"
                + "nome VARCHAR(100),"
                + "cpf VARCHAR(14),"
                + "telefone VARCHAR(20)"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criarTabelaCategorias() {
        String sql = "CREATE TABLE IF NOT EXISTS categorias ("
                + "cdCategoria INT AUTO_INCREMENT PRIMARY KEY,"
                + "nome VARCHAR(50),"
                + "descricao TEXT"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criarTabelaProdutos() {
        String sql = "CREATE TABLE IF NOT EXISTS produtos ("
                + "cdProduto INT AUTO_INCREMENT PRIMARY KEY,"
                + "nome VARCHAR(100),"
                + "preco DOUBLE,"
                + "quantidade INT,"
                + "cdCategoria INT,"
                + "FOREIGN KEY (cdCategoria) REFERENCES categorias(cdCategoria)"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criarTabelaVendas() {
        String sql = "CREATE TABLE IF NOT EXISTS vendas ("
                + "cdVenda INT AUTO_INCREMENT PRIMARY KEY,"
                + "data DATE,"
                + "valor DOUBLE,"
                + "pago BOOLEAN,"
                + "cdCliente INT,"
                + "FOREIGN KEY (cdCliente) REFERENCES clientes(cdCliente)"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criarTabelaItensDeVenda() {
        String sql = "CREATE TABLE IF NOT EXISTS itensDeVenda ("
                + "cdItemDeVenda INT AUTO_INCREMENT PRIMARY KEY,"
                + "quantidade INT,"
                + "valor DOUBLE,"
                + "cdProduto INT,"
                + "cdVenda INT,"
                + "FOREIGN KEY (cdProduto) REFERENCES produtos(cdProduto),"
                + "FOREIGN KEY (cdVenda) REFERENCES vendas(cdVenda)"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
