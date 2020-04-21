package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPanelController {

    @FXML
    private Button usersButton;

    @FXML
    private Button adminsButton;

    @FXML
    private Button exitButton;


    @FXML
    public void initialize(){
        usersButton.setOnAction(event -> {
            change(usersButton, "usersshow");
        });
        adminsButton.setOnAction(event -> {
            change(adminsButton, "employeesshow");
        });
        exitButton.setOnAction(event -> {
            change(exitButton, "sample");
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

