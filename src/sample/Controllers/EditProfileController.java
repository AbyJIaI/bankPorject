package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.BankClasses.User;
import sample.Database.Database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditProfileController {

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private ComboBox<Integer> ageComboBox;
    private ObservableList<Integer> ages_ol = FXCollections.observableArrayList();
    private ArrayList<Integer> ages_al = new ArrayList<>();

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button editButton;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize(){
        for (int i = 16; i < 81;i++){
            ages_al.add(i);
        }
        editButton.setOnAction(event -> {
            User user = Database.getUser();
            if (!nameTextField.getText().equals("")){
                user.setName(nameTextField.getText());
            }
            if (!surnameTextField.getText().equals("")){
                user.setSurname(surnameTextField.getText());
            }
            if (!usernameTextField.getText().equals("")){
                user.setUsername(usernameTextField.getText());
            }
            if (!phoneTextField.getText().equals("")){
                user.setPhone(phoneTextField.getText());
            }
            if (!emailTextField.getText().equals("")){
                user.setEmail(emailTextField.getText());
            }
            if (ageComboBox.getSelectionModel().getSelectedItem() != null){
                user.setAge(ageComboBox.getSelectionModel().getSelectedItem());
            }
            Database.setUser(user);
            try {
                new Database().updateUser(user);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        ages_ol.setAll(ages_al);
        ageComboBox.setItems(ages_ol);
        exitButton.setOnAction(event -> {
            change(exitButton, "myprofile");
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