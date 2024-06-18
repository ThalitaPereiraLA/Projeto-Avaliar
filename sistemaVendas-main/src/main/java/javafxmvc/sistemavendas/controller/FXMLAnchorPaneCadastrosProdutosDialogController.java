package javafxmvc.sistemavendas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.sistemavendas.model.dao.CategoriaDAO;
import javafxmvc.sistemavendas.model.database.Database;
import javafxmvc.sistemavendas.model.database.DatabaseFactory;
import javafxmvc.sistemavendas.model.domain.Categoria;
import javafxmvc.sistemavendas.model.domain.Produto;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastrosProdutosDialogController implements Initializable {

    @FXML
    private Label labelProdutoNome;
    @FXML
    private Label labelProdutoQuantidade;
    @FXML
    private Label labelProdutoPreco;
    @FXML
    private Label labelProdutoCategoria;
    @FXML
    private ComboBox<Categoria> comboBoxCategoria;
    @FXML
    private TextField textFieldProdutoNome;
    @FXML
    private TextField textFieldProdutoQuantidade;
    @FXML
    private TextField textFieldProdutoPreco;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    
    
    private List<Categoria> listaCategorias;
    private ObservableList<Categoria> observableListCategorias;

    
    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Produto produto;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        carregarComboBoxCategorias();
    }  
    
    public void carregarComboBoxCategorias(){
        listaCategorias = categoriaDAO.listar();
        observableListCategorias = FXCollections.observableArrayList(listaCategorias);
        comboBoxCategoria.setItems(observableListCategorias);
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonConfirmarClicked() {
        return buttonConfirmarClicked;
    }

    public void setButtonConfirmarClicked(boolean buttonConfirmarClicked) {
        this.buttonConfirmarClicked = buttonConfirmarClicked;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        textFieldProdutoNome.setText(produto.getNome());
        textFieldProdutoQuantidade.setText(Integer.toString (produto.getQuantidade()));
        textFieldProdutoPreco.setText(Double.toString (produto.getPreco()));
        comboBoxCategoria.getSelectionModel().select(produto.getCategoria());
    }
    
    
    @FXML
    public void handleButtonConfirmar(){     
        produto.setNome(textFieldProdutoNome.getText());
        produto.setQuantidade(Integer.parseInt(textFieldProdutoQuantidade.getText()));
        produto.setPreco(Double.parseDouble(textFieldProdutoPreco.getText()));
        produto.setCategoria(comboBoxCategoria.getSelectionModel().getSelectedItem());

        buttonConfirmarClicked = true;
        dialogStage.close();        
    }
    
    
    @FXML
    public void handleButtonCancelar(){
        getDialogStage().close();        
    }

    
}
