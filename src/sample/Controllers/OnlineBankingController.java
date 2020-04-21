package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.BankClasses.Client;
import sample.BankClasses.Employee;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class OnlineBankingController {
    private ArrayList<User> users = new ArrayList<>();
    private User user;

    @FXML
    private Button guideButton;

    @FXML
    private Button depositButton;

    @FXML
    private Button creditButton;

    @FXML
    private Button exitButton;

    private ClientHandler clientHandler = new ClientHandler();
    @FXML
    public void initialize() throws SQLException, IOException {
        clientHandler.writeRequest("GET_USERS");
        if (clientHandler.checkRequest("USERS_GOT")) {
            users = ClientHandler.users;
        }
        clientHandler.writeRequest("GET_USER");
        if (clientHandler.checkRequest("TAKE")){
            user = ClientHandler.user;
        }
        exitButton.setOnAction(event -> {
            change(exitButton, "clientmenu");
        });
        creditButton.setOnAction(event -> {
            change(creditButton, "credit");
        });
        guideButton.setOnAction(event -> {
            change(guideButton, "guide");
        });
        depositButton.setOnAction(event -> {
            change(depositButton, "deposit");
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
