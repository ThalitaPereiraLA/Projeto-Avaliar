package javafxmvc.sistemavendas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.sistemavendas.model.dao.CategoriaDAO;
import javafxmvc.sistemavendas.model.dao.ProdutoDAO;
import javafxmvc.sistemavendas.model.database.Database;
import javafxmvc.sistemavendas.model.database.DatabaseFactory;
import javafxmvc.sistemavendas.model.domain.Categoria;
import javafxmvc.sistemavendas.model.domain.Produto;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;


public class FXMLAnchorPaneCadastrosProdutosController implements Initializable {

    @FXML
    private TableView<Produto> tableViewProdutos;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoNome;
    @FXML
    private TableColumn<Produto, String> tableColumnProdutoQuantidade;
    @FXML
    private Label labelProdutoCodigo;
    @FXML
    private Label labelProdutoNome;
    @FXML
    private Label labelProdutoPreco;
    @FXML
    private Label labelProdutoQuantidade;
    @FXML
    private Label labelProdutoCategoria;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    
    
    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;   
    private List<Categoria> listCategorias;
    private ObservableList<Categoria> observableListCategorias;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDAO.setConnection(connection);   
        categoriaDAO.setConnection(connection);  
        
        carregarTableViewProdutos();
         
        selecionarItemTableViewProdutos(null);
        
        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewProdutos.getSelectionModel().selectedItemProperty().addListener(
                (observavble, oldValue, newValue) -> selecionarItemTableViewProdutos(newValue));
    }    
    
    
    public void carregarTableViewProdutos(){
        tableColumnProdutoNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnProdutoQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        listProdutos = produtoDAO.listar();

        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        tableViewProdutos.setItems(observableListProdutos);
    }
    
    
    public void selecionarItemTableViewProdutos(Produto produto){
        if (produto != null){
            labelProdutoCodigo.setText(String.valueOf(produto.getCdProduto()));
            labelProdutoNome.setText(produto.getNome());
            labelProdutoPreco.setText (String.valueOf(produto.getPreco()));
            labelProdutoQuantidade.setText (String.valueOf(produto.getQuantidade()));
            labelProdutoCategoria.setText(String.valueOf(produto.getCategoria()));
        } else {
            labelProdutoCodigo.setText("");
            labelProdutoNome.setText("");
            labelProdutoPreco.setText("");
            labelProdutoQuantidade.setText("");
            labelProdutoCategoria.setText("");
        }
    }
    
    
    
    @FXML
    public void handleButtonInserir() throws IOException {
        Produto produto = new Produto();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
        if (buttonConfirmarClicked){
            produtoDAO.inserir(produto);
            carregarTableViewProdutos();
        }
    }
    
    
    @FXML
    public void handleButtonAlterar() throws IOException {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosProdutosDialog(produto);
            if (buttonConfirmarClicked) {
                produtoDAO.alterar(produto);
                carregarTableViewProdutos();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na Tabela!");
            alert.show();
        }
    }
    
    @FXML
    public void handleButtonRemover() throws IOException {
        Produto produto = tableViewProdutos.getSelectionModel().getSelectedItem();
        if (produto != null) {
            produtoDAO.remover(produto);
            carregarTableViewProdutos();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na tabela!");
            alert.show();
        }
    }
    
    
    public boolean showFXMLAnchorPaneCadastrosProdutosDialog(Produto produto) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosProdutosDialogController.class.getResource("/javafxmvc/sistemavendas/FXMLAnchorPaneCadastrosProdutosDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Produtos");
        
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando o produto no Controller.
        FXMLAnchorPaneCadastrosProdutosDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);

        //Mostra o Dialog e espera até que o usuário feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

    
}
