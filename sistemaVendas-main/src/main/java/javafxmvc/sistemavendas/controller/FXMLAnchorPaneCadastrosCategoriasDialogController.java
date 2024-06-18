package javafxmvc.sistemavendas.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.sistemavendas.model.domain.Categoria;

import java.net.URL;
import java.util.ResourceBundle;

public class FXMLAnchorPaneCadastrosCategoriasDialogController implements Initializable {

  
    @FXML
    private Label labelCategoriaNomeDescricao;

    @FXML
    private TextField textFieldCategoriaNomeDescricao;

    @FXML
    private Button buttonConfirmar;

    @FXML
    private Button buttonCancelar;

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Categoria categoria;

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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        this.textFieldCategoriaNomeDescricao.setText(categoria.getDescricao());        
    }
    
        
    @FXML
    public void handleButtonConfirmar(){     
        categoria.setDescricao(textFieldCategoriaNomeDescricao.getText());

        buttonConfirmarClicked = true;
        dialogStage.close();        
    }
    
    @FXML
    public void handleButtonCancelar(){
        getDialogStage().close();        
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
    }    
    
}
