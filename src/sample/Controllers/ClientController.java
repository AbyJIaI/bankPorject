package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;

public class ClientController {
    private ClientHandler clientHandler = new ClientHandler();

    @FXML
    private Button myBankButton;

    @FXML
    private Button onlineBankingButton;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        myBankButton.setOnAction(event -> {
            change(myBankButton, "mybank");
        });
        onlineBankingButton.setOnAction(event -> {
            change(onlineBankingButton, "onlinebank");
        });
        exitButton.setOnAction(event -> {
            try {
                clientHandler.writeRequest("SET_USER", (User)null);
            } catch (IOException e) {
                e.printStackTrace();
            }
            change(exitButton, "sample");
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
}
