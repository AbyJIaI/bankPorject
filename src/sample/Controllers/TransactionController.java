package sample.Controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.BankClasses.Client;
import sample.BankClasses.Transaction;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class TransactionController {
    private ArrayList<Transaction> transactions = new ArrayList<>();

    @FXML
    private Tab tabPhone;

    @FXML
    private Button exitButton;

    @FXML
    private TextField phoneTextField;

    @FXML
    private Label noticeLabel;

    @FXML
    private TextField moneyTextField;

    @FXML
    private Button transferButton;

    @FXML
    private Label enteredMoneyLabel;

    @FXML
    private TextField cardTextField;

    @FXML
    private Label cardNoticeLabel;

    @FXML
    private TextField cardMoneyTextField;

    @FXML
    private Label inputLabel;

    @FXML
    private Button cardTransferButton;

    @FXML
    private TextField amountTextField;

    @FXML
    private TextArea historyOfTransaction;

    private ClientHandler clientHandler = new ClientHandler();

    @FXML
    public void initialize() throws IOException {
        exitButton.setOnAction(event -> {
            change(exitButton, "mybank");
        });
        final User[] to_user = new User[1];
        final User[] to_user1 = new User[1];
        clientHandler.writeRequest("GET_USER");
        if (clientHandler.checkRequest("TAKE")){
            User client = ClientHandler.user;
            clientHandler.writeRequest("GET_USERS_TRANSACTIONS", client);
            if (clientHandler.checkRequest("TAKE2")){
                ArrayList<User> users = ClientHandler.users;
                transactions = ClientHandler.transactions;
                String history = historyTrs(transactions, users, client);
                historyOfTransaction.setText(history);
                String amount = String.valueOf(client.getCard().getAmount());
                amountTextField.setText(amount + " T");
                phoneTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    int plus = 0;
                    int minuses = 0;
                    int digits = 0;
                    String[] forms;
                    forms = newValue.split("-");
                    for (int i = 0; i < newValue.length(); i++) {
                        if (newValue.charAt(i) == '+') {
                            plus++;
                        }
                        if (newValue.charAt(i) == '-') {
                            minuses++;
                        }
                        if (Character.isDigit(newValue.charAt(i))) {
                            digits++;
                        }
                    }
                    if (newValue.length() == 16 && plus == 1 && minuses == 4 && digits == 11 &&
                            forms[0].length() == 2 &&
                            forms[1].length() == 3 &&
                            forms[2].length() == 3 &&
                            forms[3].length() == 2 &&
                            forms[4].length() == 2) {
                        String phonenumber = "8" + forms[1] + forms[2] + forms[3] + forms[4];
                        boolean find = false;
                        String name = "";
                        for (User user : users) {
                            if (user.getPhone().equals(phonenumber)) {
                                if (user.getCard().getCardNumber() != null) {
                                    find = true;
                                    if (client.getPhone().equals(phonenumber)) {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setContentText("You can not transfer money to yourself!");
                                        alert.showAndWait();
                                        phoneTextField.setText("");
                                        break;
                                    }
                                    to_user[0] = user;
                                    name = user.getName() + " " + user.getSurname();
                                    break;
                                }
                            }
                        }
                        if (find) {
                            noticeLabel.setText(name);
                        } else {
                            noticeLabel.setText("No such user with this number");
                        }
                    } else {
                        noticeLabel.setText("Check data entry!");
                    }
                    if (newValue.isEmpty()) {
                        noticeLabel.setText("You didn't provide phone number!");
                    }
                });
                final double[] money = new double[1];
                moneyTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.isEmpty()) {
                        money[0] = Double.parseDouble(newValue);
                        if (money[0] < 100){
                            enteredMoneyLabel.setText("Minimal amount is 100 T");
                        }else {
                            enteredMoneyLabel.setText("");
                        }
                        transferButton.setText("Transfer " + money[0] + " T");
                    }else {
                        transferButton.setText("Transfer " + "0.0 T");
                    }
                });
                transferButton.setOnAction(event -> {
                    if (client.getCard().getAmount() >= money[0]) {
                        if (money[0] >= 100) {
                            try {
                                client.getCard().setAmount(client.getCard().getAmount() - money[0]);
                                to_user[0].getCard().setAmount(to_user[0].getCard().getAmount() + money[0]);
                                Transaction tr = new Transaction(client.getId(), to_user[0].getId(), money[0], new Date(), true);
                                transactions.add(tr);
                                new Database().addTransaction(tr);
                                new Database().updateAmounts(client, to_user[0]);
                                ArrayList<Transaction> transactions = new Database().getTransactions(client);
                                String history1 = historyTrs(transactions,users,client);
                                historyOfTransaction.setText(history1);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("You don't have enough money to transfer!");
                        alert.showAndWait();
                        phoneTextField.setText("");
                    }
                    amountTextField.setText(client.getCard().getAmount() + " T");
                });
                cardTextField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        int whitespaces = 0;
                        int digits = 0;
                        String[] forms = newValue.split(" ");
                        String cardnumber = "";
                        for (int i = 0 ; i < newValue.length();i++){
                            if (Character.isWhitespace(newValue.charAt(i))){
                                whitespaces++;
                            }
                            if (Character.isDigit(newValue.charAt(i))){
                                digits++;
                            }
                        }
                        if (newValue.length() == 19 && whitespaces == 3 && digits == 16 && forms.length == 4){
                            String name = "";
                            boolean find = false;
                            cardnumber += forms[0] + forms[1] + forms[2] + forms[3];
                            for (User user : users) {
                                if (user.getCard().getCardNumber().equals(cardnumber)){
                                    to_user1[0] = user;
                                    if (client.getCard().getCardNumber().equals(cardnumber)) {
                                        Alert alert = new Alert(Alert.AlertType.ERROR);
                                        alert.setContentText("You can not transfer money to yourself!");
                                        alert.showAndWait();
                                        cardTextField.setText("");
                                        break;
                                    }
                                    name = user.getName() + " " + user.getSurname();
                                    find = true;
                                    break;
                                }
                            }
                            if (find){
                                cardNoticeLabel.setText(name);
                            }else {
                                cardNoticeLabel.setText("No such user with this card number");
                            }
                        }else {
                            cardNoticeLabel.setText("Check data entry!");
                        }
                        if (newValue.isEmpty()){
                            cardNoticeLabel.setText("You didn't provide card number!");
                        }
                    }
                });
                final double[] moneycard = new double[1];
                cardMoneyTextField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if (!newValue.isEmpty()) {
                            moneycard[0] = Double.parseDouble(newValue);
                            if (moneycard[0] < 100){
                                inputLabel.setText("Minimal amount is 100 T");
                            }else {
                                inputLabel.setText("");
                            }
                            cardTransferButton.setText("Transfer " + moneycard[0] + " T");
                        }else {
                            cardTransferButton.setText("Transfer " + "0.0 T");
                        }
                    }
                });
                cardTransferButton.setOnAction(event -> {
                    if (client.getCard().getAmount() >= moneycard[0]) {
                        if (moneycard[0] >= 100) {
                            try {
                                client.getCard().setAmount(client.getCard().getAmount() - moneycard[0]);
                                to_user1[0].getCard().setAmount(to_user1[0].getCard().getAmount() + moneycard[0]);
                                Transaction tr = new Transaction(client.getId(), to_user1[0].getId(), moneycard[0], new Date(), false);
                                transactions.add(tr);
                                new Database().addTransaction(tr);//TODO
                                new Database().updateAmounts(client, to_user1[0]);
                                ArrayList<Transaction> transactions = new Database().getTransactions(client);
                                String history1 = historyTrs(transactions,users,client);
                                historyOfTransaction.setText(history1);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("You don't have enough money to transfer!");
                        alert.showAndWait();
                        phoneTextField.setText("");
                    }
                    amountTextField.setText(client.getCard().getAmount() + " T");
                });
            }
        }
        moneyTextField.setTextFormatter(new TextFormatter<Double>(change -> {
            if (!(change.getControlNewText().matches("[0-9]{0,6}"))) {
                return null;
            } else {
                return change;
            }
        }));
        cardMoneyTextField.setTextFormatter(new TextFormatter<Double>(change -> {
            if (!(change.getControlNewText().matches("[0-9]{0,6}"))) {
                return null;
            } else {
                return change;
            }
        }));
    }

    private String historyTrs(ArrayList<Transaction> transactions, ArrayList<User> users, User client) {
        String history = "";
        for (Transaction transaction : transactions) {
            User to_User = findReceiver(transaction.getId_client_to(), users);
            if (to_User != null) {
                if (transaction.getName().equals("Money transfer")) {
                    history += transaction.toString() + "\n";
                    history += "Sender: " + client.getName() + " " + client.getSurname() + "\n";
                    history += "Receiver: " + to_User.getName() + " " + to_User.getSurname() + "\n";
                    history += "Card number: " + to_User.getCard().getCardNumber() + "\n";
                    if (transaction.isFrom_mobile()) {
                        history += "Receiver's phone number: " + to_User.getPhone() + "\n";
                    }
                    history += "___________________________________________________________________\n";
                }
            }
        }
        return history;
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
    public static User findReceiver(int id, ArrayList<User> users) {
        for (User user : users) {
            if (id == user.getId()) {
                return user;
            }
        }
        return null;
    }
}
