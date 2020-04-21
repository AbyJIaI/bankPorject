package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.Server.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;

public class EmployeesShowController {
    private ClientHandler clientHandler = new ClientHandler();
    @FXML
    private TableView<UsersTable> usersList;
    @FXML
    private TableColumn<UsersTable, String> name;
    @FXML
    private TableColumn<UsersTable, String> surname;
    @FXML
    private TableColumn<UsersTable, String> username;
    @FXML
    private TableColumn<UsersTable, String> role;
    private ArrayList<UsersTable> tables;
    private ObservableList<UsersTable> observableList = FXCollections.observableArrayList();
    @FXML
    private Button exitButton;

    @FXML
    public void initialize(){
        exitButton.setOnAction(event -> {
            change(exitButton, "adminpanel");
        });

        try{
            clientHandler.writeRequest("GET_EMPLOYEES_ROLE");
            if(clientHandler.checkRequest("ROLE_EMPLOYEES")){
                tables = ClientHandler.tables;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        observableList = FXCollections.observableArrayList(tables);
        usersList.setItems(observableList);
    }

    public void change(Button button, String url) {
        button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/FXMLs/" + url + ".fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
