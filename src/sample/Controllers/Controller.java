package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.BankClasses.Client;
import sample.BankClasses.Employee;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;
import sample.Server.Request;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    private ArrayList<User> users = new ArrayList<>();

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    private ClientHandler clientHandler = new ClientHandler();

    @FXML
    public void initialize() throws SQLException, IOException {
        clientHandler.writeRequest("GET_USERS");
        if (clientHandler.checkRequest("USERS_GOT")) {
            users = ClientHandler.users;
        }
        loginButton.setOnAction(event -> {
            boolean correct = false;
            String username = usernameTextField.getText().trim();
            String password = passwordTextField.getText().trim();
            if (!username.equals("") && !password.equals("")) {
                for (User user : users) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        try {
                            clientHandler.writeRequest("SET_USER", user);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        correct = true;
                        break;
                    }
                }
                if (correct) {
                    try {
                        clientHandler.writeRequest("GET_USER");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (clientHandler.checkRequest("TAKE")) {
                        User user = ClientHandler.user;
                        if (user != null) {
                            if (user.getRole().equals("Client")) {
                                change(loginButton, "clientmenu");
                            } else if (user.getRole().equals("Employee")) {
                                change(loginButton, "employeemenu");
                            }else if (user.getRole().equals("Admin")){
                                change(loginButton, "adminpanel");
                            }
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Username or password is wrong!\nTry to input again!");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username or password wouldn't be empty!");
                alert.showAndWait();
            }
        });
        registerButton.setOnAction(event -> {
            change(registerButton, "register");
        });
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
