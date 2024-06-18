
package javafxmvc.sistemavendas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafxmvc.sistemavendas.model.dao.ClienteDAO;
import javafxmvc.sistemavendas.model.dao.ProdutoDAO;
import javafxmvc.sistemavendas.model.database.Database;
import javafxmvc.sistemavendas.model.database.DatabaseFactory;
import javafxmvc.sistemavendas.model.domain.Cliente;
import javafxmvc.sistemavendas.model.domain.ItemDeVenda;
import javafxmvc.sistemavendas.model.domain.Produto;
import javafxmvc.sistemavendas.model.domain.Venda;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;



public class FXMLAnchorPaneProcessosVendasDialogController implements Initializable {

    @FXML
    private ComboBox<Cliente> comboBoxVendaCliente;
    @FXML
    private DatePicker datePickerVendaData;
    @FXML
    private CheckBox checkBoxVendaPago;
    @FXML
    private ComboBox<Produto> comboBoxVendaProduto;
    @FXML
    private TableView<ItemDeVenda> tableViewItensDeVenda;
    @FXML
    private TableColumn<ItemDeVenda, Produto> tableColumnItemDeVendaProduto;
    @FXML
    private TableColumn<ItemDeVenda, Integer> tableColumnItemDeVendaQuantidade;
    @FXML
    private TableColumn<ItemDeVenda, Double> tableColumnItemDeVendaValor;
    @FXML
    private TextField textFieldVendaValor;
    @FXML
    private TextField textFieldVendaItemDeVendaQuantidade;
    @FXML
    private Button buttonConfirmar;
    @FXML
    private Button buttonCancelar;
    @FXML
    private Button buttonAdicionar;

    private  List<Cliente> listClientes;
    private  List<Produto> listProdutos;

    private ObservableList<Cliente> observableListClientes;
    private ObservableList<Produto> observableListProdutos;
    private ObservableList<ItemDeVenda> observableListItensDeVenda;

    //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection;

    {
        assert database != null;
        connection = database.conectar();
    }

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    private Stage dialogStage;
    private boolean buttonConfirmarClicked = false;
    private Venda venda;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDAO.setConnection(connection);
        produtoDAO.setConnection(connection);

        carregarComboBoxClientes();
        carregarComboBoxProdutos();

        tableColumnItemDeVendaProduto.setCellValueFactory(new PropertyValueFactory<>("produto"));
        tableColumnItemDeVendaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        tableColumnItemDeVendaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }


    public void carregarComboBoxClientes(){
        listClientes = clienteDAO.listar();
        observableListClientes = FXCollections.observableArrayList(listClientes);
        comboBoxVendaCliente.setItems(observableListClientes);
    }

    public void carregarComboBoxProdutos(){
        listProdutos = produtoDAO.listar();
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        comboBoxVendaProduto.setItems(observableListProdutos);
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

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }


    //clique do adiciona produto
    @FXML
    public void handleButtonAdicionar() {
        Produto produto;
        ItemDeVenda itemDeVenda = new ItemDeVenda();

        if (comboBoxVendaProduto.getSelectionModel().getSelectedItem() != null) {
            produto = (Produto) comboBoxVendaProduto.getSelectionModel().getSelectedItem();

            // Verificar se o campo de quantidade não está vazio e contém apenas números
            String quantidadeString = textFieldVendaItemDeVendaQuantidade.getText().trim();
            if (!quantidadeString.isEmpty() && quantidadeString.matches("\\d+")) {
                int quantidade = Integer.parseInt(quantidadeString);

                if (produto.getQuantidade() >= quantidade) {
                    itemDeVenda.setProduto(produto);
                    itemDeVenda.setQuantidade(quantidade);
                    itemDeVenda.setValor(itemDeVenda.getProduto().getPreco() * itemDeVenda.getQuantidade());

                    venda.getItensDeVenda().add(itemDeVenda);
                    venda.setValor(venda.getValor() + itemDeVenda.getValor());

                    observableListItensDeVenda = FXCollections.observableArrayList(venda.getItensDeVenda());
                    tableViewItensDeVenda.setItems(observableListItensDeVenda);
                    textFieldVendaValor.setText(String.format("%.2f", venda.getValor()));
                } else {
                    Alert alert = new Alert (Alert.AlertType.ERROR);
                    alert.setHeaderText("Problemas na escolha do produto!");
                    alert.setContentText("Não existe a quantidade de produtos disponíveis no estoque!");
                    alert.show();
                }
            } else {
                Alert alert = new Alert (Alert.AlertType.ERROR);
                alert.setHeaderText("Quantidade inválida!");
                alert.setContentText("Por favor, digite um valor numérico válido para a quantidade.");
                alert.show();
            }
        }
    }


    @FXML
    public void handleButtonConfirmar() {
        if (validarEntradaDeDados()) {
            venda.setCliente((Cliente) comboBoxVendaCliente.getSelectionModel().getSelectedItem());
            venda.setPago (checkBoxVendaPago.isSelected());
            venda.setData(datePickerVendaData.getValue());
            buttonConfirmarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    public void handleButtonCancelar(){
        getDialogStage().close();
    }

    //Validar entrada de dados para o cadastro
    private boolean validarEntradaDeDados(){
        String errorMessage = "";

        if (comboBoxVendaCliente.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Cliente inválido!\n";
        }
        if (datePickerVendaData.getValue() == null) {
            errorMessage += "Data inválida!\n";
        }
        if (observableListItensDeVenda == null) {
            errorMessage += "Itens de venda insválidos!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //Mostrando a mensagem de erro
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setTitle("erro no cadastro");
            alert.setHeaderText("campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }


}
