package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import sample.BankClasses.Client;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class RegisterController {
    private ArrayList<User> users = new ArrayList<>();
    private ClientHandler clientHandler = new ClientHandler();
    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private ComboBox<Integer> ageComboBox;
    private ObservableList<Integer> ages = FXCollections.observableArrayList();
    private ArrayList<Integer> ages_al = new ArrayList<>();

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button registerButton;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize() throws IOException {
        clientHandler.writeRequest("GET_USERS");
        if (clientHandler.checkRequest("USERS_GOT")) {
            users = ClientHandler.users;
        }
        for (int i = 16; i <= 100;i++){
            ages_al.add(i);
        }
        ages.setAll(ages_al);
        ageComboBox.setItems(ages);
        exitButton.setOnAction(event -> {
            change(exitButton, "sample");
        });
        registerButton.setOnAction(event -> {
            Client client = new Client();
            int corrects = 0;
            if (check(nameTextField)){
                corrects++;
                client.setName(nameTextField.getText());
            }
            if (check(surnameTextField)){
                corrects++;
                client.setSurname(surnameTextField.getText());
            }
            if (check(usernameTextField)){
                if (checkUsername(users, usernameTextField.getText())) {
                    client.setUsername(usernameTextField.getText());
                    corrects++;
                }else {
                    exc(usernameTextField);
                }
            }
            if (check(passwordTextField)){
                client.setPassword(passwordTextField.getText());
                corrects++;
            }
            if (check(phoneNumberTextField)){
                if (checkPhone(users, phoneNumberTextField.getText())) {
                    client.setPhone(phoneNumberTextField.getText());
                    corrects++;
                }else {
                    exc(phoneNumberTextField);
                }
            }
            if (check(emailTextField)){
                if (checkEmail(users, emailTextField.getText())) {
                    client.setEmail(emailTextField.getText());
                    corrects++;
                }else {
                    exc(emailTextField);
                }
            }
            if (ageComboBox.getSelectionModel().getSelectedItem() != null){
                corrects++;
                client.setAge(ageComboBox.getValue());
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(ageComboBox.getPromptText() + " field is empty!!!");
                alert.showAndWait();
            }
            if (corrects == 7){
                client.setDate(new Date());
                client.setRole("Client");
                client.getCard().setCardNumber(generateCardNumber(users));
                try {
                    clientHandler.writeRequest("ADD_USER", client);
                    clientHandler.writeRequest("ADD_CARD",client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("You are successfully registered!");
                alert.showAndWait();
                change(registerButton, "sample");
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
    public boolean check(TextField textField) {
        if (textField.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(textField.getPromptText() + " field is empty!!!");
            alert.showAndWait();
            return false;
        }else {
            return true;
        }
    }
    public String generateCardNumber(ArrayList<User> users){
        String cardNumber = "51694931";
        for (int i = 0 ; i < 2;i++) {
            int a = ThreadLocalRandom.current().nextInt(1000, 9999);
            cardNumber += String.valueOf(a);
        }
        boolean correct = true;
        for (User user : users) {
            if (user.getCard().getCardNumber().equals(cardNumber)) {
                correct = false;
                break;
            }
        }
        if (!correct){
            while (!correct){
                cardNumber = "51694931";
                for (int i = 0 ; i < 8;i++) {
                    int a = ThreadLocalRandom.current().nextInt(0, 10);
                    cardNumber += String.valueOf(a);
                }

                correct = true;
                for (User user : users) {
                    if (user.getCard().getCardNumber().equals(cardNumber)) {
                        correct = false;
                        break;
                    }
                }
            }
        }
        return cardNumber;
    }
    public boolean checkUsername(ArrayList<User> users, String username){
        for (User user : users) {
            if (user.getUsername().equals(username)){
                return false;
            }
        }
        return true;
    }
    public boolean checkPhone(ArrayList<User> users, String phone){
        for (User user : users) {
            if (user.getPhone().equals(phone)){
                return false;
            }
        }
        return true;
    }
    public boolean checkEmail(ArrayList<User> users, String email){
        for (User user : users) {
            if (user.getEmail().equals(email)){
                return false;
            }
        }
        return true;
    }
    public void exc(TextField textField){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("This " + textField.getPromptText().toLowerCase() + " is already exist\nChoose another one!");
        alert.showAndWait();
    }
}
