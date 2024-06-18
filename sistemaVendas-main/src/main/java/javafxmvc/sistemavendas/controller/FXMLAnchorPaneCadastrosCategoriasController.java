
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
import javafxmvc.sistemavendas.model.database.Database;
import javafxmvc.sistemavendas.model.database.DatabaseFactory;
import javafxmvc.sistemavendas.model.domain.Categoria;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;


public class FXMLAnchorPaneCadastrosCategoriasController implements Initializable {

    
    @FXML
    private TableView<Categoria> tableViewCategorias;
    @FXML
    private TableColumn<Categoria, String> tableColumnCategoriaNomeDescricao;
    @FXML
    private Label labelCategoriaCodigo;
    @FXML
    private Label labelCategoriaDescricao;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;    
    
    
    private List<Categoria> listCategorias;
    private ObservableList<Categoria> observableListCategorias;
    

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    
    
 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);        
        carregarTableViewCategorias();
        
        // Limpando a exibição dos detalhes do cliente
        selecionarItemTableViewCategorias(null);
        
        // Listen acionado diante de quaisquer alterações na seleção de itens do TableView
        tableViewCategorias.getSelectionModel().selectedItemProperty().addListener(
                (observavble, oldValue, newValue) -> selecionarItemTableViewCategorias(newValue));
    }    
    
    
    public void carregarTableViewCategorias(){
        tableColumnCategoriaNomeDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        
        listCategorias = categoriaDAO.listar();

        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        tableViewCategorias.setItems(observableListCategorias);
    }
    
    
    public void selecionarItemTableViewCategorias(Categoria categoria){
        if (categoria != null){
            labelCategoriaCodigo.setText(String.valueOf(categoria.getCdCategoria()));
            labelCategoriaDescricao.setText(categoria.getDescricao());            
        }else{
            labelCategoriaCodigo.setText("");
            labelCategoriaDescricao.setText("");
            
        }
    }
    
    
    @FXML
    public void handleButtonInserir() throws IOException {
        Categoria categoria = new Categoria();
        boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
        if (buttonConfirmarClicked){
            categoriaDAO.inserir(categoria);
            carregarTableViewCategorias();
        }
    }
    
    
    @FXML
    public void handleButtonAlterar() throws IOException {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            boolean buttonConfirmarClicked = showFXMLAnchorPaneCadastrosCategoriasDialog(categoria);
            if (buttonConfirmarClicked) {
                categoriaDAO.alterar(categoria);
                carregarTableViewCategorias();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na Tabela!");
            alert.show();
        }
    }
    
    @FXML
    public void handleButtonRemover() throws IOException {
        Categoria categoria = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (categoria != null) {
            categoriaDAO.remover(categoria);
            carregarTableViewCategorias();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na tabela!");
            alert.show();
        }
    }
    
    public boolean showFXMLAnchorPaneCadastrosCategoriasDialog(Categoria categoria) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosCategoriasDialogController.class.getResource("/javafxmvc/sistemavendas/FXMLAnchorPaneCadastrosCategoriasDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        //Criando um Estágio de Diálogo (Stage Dialog)
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Categorias");
        
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        //Setando a cetegoria no Controller.
        FXMLAnchorPaneCadastrosCategoriasDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);

        //Mostra o Dialog e espera até que o usuário feche
        dialogStage.showAndWait();

        return controller.isButtonConfirmarClicked();
    }

    
}
