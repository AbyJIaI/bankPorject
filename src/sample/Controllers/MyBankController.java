package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;

public class MyBankController {

    @FXML
    private Button myProfileButton;

    @FXML
    private Button transactionButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button showTransactionButton;

    private ClientHandler clientHandler = new ClientHandler();


    @FXML
    public void initialize() {
        showTransactionButton.setOnAction(event -> {
            try {
                clientHandler.writeRequest("GET_USER");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (clientHandler.checkRequest("TAKE")) {
                User user = ClientHandler.user;
                if (user.getCard().getCardNumber() != null) {
                    change(showTransactionButton, "alltransaction");
                } else {
                    exception();
                }
            }
        });
        exitButton.setOnAction(event -> {
            change(exitButton, "clientmenu");
        });
        myProfileButton.setOnAction(event -> {
            change(myProfileButton, "myprofile");
        });
        transactionButton.setOnAction(event -> {
            try {
                clientHandler.writeRequest("GET_USER");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (clientHandler.checkRequest("TAKE")) {
                User user = ClientHandler.user;
                if (user.getCard().getCardNumber() != null) {
                    change(transactionButton, "transaction");
                } else {
                    exception();
                }
            }
        });
    }

    public void change(Button button, String url){
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
    public void exception(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("You do not have bank card! You can not make or see transactions!");
        alert.showAndWait();
    }
}
