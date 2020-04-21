package sample.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;


public class MyProfileController {

    @FXML
    private ImageView userImageView;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Button editButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button messageButton;

    private ClientHandler clientHandler = new ClientHandler();

    @FXML
    public void initialize(){
        messageButton.setOnAction(event -> {
            change(messageButton, "clientmessages");
        });
        userImageView.setImage(new Image(getClass().getResourceAsStream("/sample/Images/defaultUser.png")));
        try {
            clientHandler.writeRequest("GET_USER");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clientHandler.checkRequest("TAKE")) {
            User user = ClientHandler.user;
            infoTextArea.setText(user.toString());
        }
        exitButton.setOnAction(event -> {
            change(exitButton, "mybank");
        });
        editButton.setOnAction(event -> {
            change(editButton, "editprofile");
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
