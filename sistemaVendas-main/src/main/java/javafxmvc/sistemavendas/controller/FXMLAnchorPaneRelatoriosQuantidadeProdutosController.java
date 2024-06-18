package javafxmvc.sistemavendas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafxmvc.sistemavendas.model.dao.ProdutoDAO;
import javafxmvc.sistemavendas.model.database.Database;
import javafxmvc.sistemavendas.model.database.DatabaseFactory;
import javafxmvc.sistemavendas.model.domain.Categoria;
import javafxmvc.sistemavendas.model.domain.Produto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneRelatoriosQuantidadeProdutosController implements Initializable {

    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, Integer> tableColumnProdutoCodigo;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;
    @FXML
    private TableColumn<Produto, Double> tableColumnProdutoPreco;
    @FXML
    private TableColumn<Produto, Integer> tableColumnProdutoQuantidade;
    @FXML
    private TableColumn<Produto, Categoria> tableColumnProdutoCategoria;
    @FXML
    private Button buttonImprimir;

    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;

    // Atributos para manipulação de Banco de dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);
        carregarTableViewProdutos();
        compilarRelatorioJasper(); // Chama a compilação do relatório ao inicializar
    }

    public void carregarTableViewProdutos() {
        tableColumnProdutoCodigo.setCellValueFactory(new PropertyValueFactory<>("cdProduto"));
        tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnProdutoPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        tableColumnProdutoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnProdutoCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        listProdutos = produtoDAO.listar();

        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        tableViewProdutos.setItems(observableListProdutos);
    }

    @FXML
    public void handleImprimir() {
        URL url = getClass().getResource("/javafxmvc/sistemavendas/relatorios/JAVAFXMVCRelatorioProdutos.jasper");

        if (url != null) {
            System.out.println("URL do relatório encontrada: " + url);

            try {
                JasperPrint jasperPrint = JasperFillManager.fillReport(url.openStream(), null, connection);
                JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
                jasperViewer.setVisible(true);
            } catch (JRException | IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Resource not found: JAVAFXMVCRelatorioProdutos.jasper");
        }
    }


    private void compilarRelatorioJasper() {
        try {
            // Caminho para o arquivo .jrxml
            String jrxmlFile = "src/main/resources/javafxmvc/sistemavendas/relatorios/JAVAFXMVCRelatorioProdutos.jrxml";
            String jasperFile = "src/main/resources/javafxmvc/sistemavendas/relatorios/JAVAFXMVCRelatorioProdutos.jasper";

            JasperCompileManager.compileReportToFile(jrxmlFile, jasperFile);

            System.out.println("Relatório compilado com sucesso para .jasper");
        } catch (JRException e) {
            e.printStackTrace();
        }
    }

}

