package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.BankClasses.Deposit;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DepositController {
    private ClientHandler clientHandler = new ClientHandler();
    private User user;

    @FXML
    private Label interestRateLabel;

    @FXML
    private TextField amountTextField;

    @FXML
    private ComboBox<String> timeComboBox;
    private ObservableList<String> times = FXCollections.observableArrayList();
    private ArrayList<String> times_al = new ArrayList<>();

    @FXML
    private Label totalAmountLabel;

    @FXML
    private Label minimal;

    @FXML
    private Button exitButton;

    @FXML
    private Button exitButton1;

    @FXML
    private Button addButton;

    @FXML
    private Label currentMoneyLabel;

    @FXML
    private TableView<Deposit> depositTableView;
    private ObservableList<Deposit> deposits = FXCollections.observableArrayList();
    private ArrayList<Deposit> deposits_al = new ArrayList<>();
    @FXML
    private TableColumn<Deposit, Integer> initialTC;

    @FXML
    private TableColumn<Deposit, Double> interestTC;

    @FXML
    private TableColumn<Deposit, Integer> monthsTC;

    @FXML
    private TableColumn<Deposit, Integer> totalTC;

    @FXML
    private TableColumn<Deposit, Date> beginTC;

    @FXML
    private TableColumn<Deposit, Date> endTC;

    @FXML
    private TextArea fullTextArea;

    @FXML
    public void initialize() throws IOException {
        clientHandler.writeRequest("GET_DEPOSITS");
        if (clientHandler.checkRequest("TAKE4")){
            deposits_al = ClientHandler.deposits;
        }
        clientHandler.writeRequest("GET_USER");
        if (clientHandler.checkRequest("TAKE")){
            user = ClientHandler.user;
        }
        ArrayList<Deposit> myDeposits = new ArrayList<>();
        for (Deposit deposit : deposits_al) {
            if (deposit.getId_client() == user.getId()){
                myDeposits.add(deposit);
            }
        }
        depositTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null){
                String today = "Today: " + new Date();
                int spendedMonths = 0;
                int current = newValue.getInitial_amount();
                Calendar begin = Calendar.getInstance();
                Calendar now = Calendar.getInstance();
                now.setTime(new Date());
                begin.setTime(newValue.getDate_begin());
                int everyMonth = (int) Math.round(current* ClientHandler.interestRate /100);
                for (int i =1 ; i <= newValue.getMonths();i++){
                    if (now.getTime().after(begin.getTime())){
                        current = current + everyMonth;
                        begin.add(Calendar.MONTH, 1);
                        spendedMonths++;
                    }
                }
                String ended = "";
                if (now.getTime().after(newValue.getDate_end())){
                    ended = "\nYour deposit is over!";
                }
                String currentAmount = "\nEarned money: " + current;
                String month = "\nSpended months: " + spendedMonths;
                fullTextArea.setText(today + currentAmount + month + ended);
            }
        });
        deposits.setAll(myDeposits);
        depositTableView.setItems(deposits);
        initialTC.setCellValueFactory(new PropertyValueFactory<>("Initial_amount"));
        interestTC.setCellValueFactory(new PropertyValueFactory<>("Interest"));
        monthsTC.setCellValueFactory(new PropertyValueFactory<>("Months"));
        totalTC.setCellValueFactory(new PropertyValueFactory<>("Total_amount"));
        beginTC.setCellValueFactory(new PropertyValueFactory<>("date_begin"));
        endTC.setCellValueFactory(new PropertyValueFactory<>("date_end"));
        currentMoneyLabel.setText(user.getCard().getAmount() + "T");
        amountTextField.setTextFormatter(new TextFormatter<Integer>(change -> {
            if (!(change.getControlNewText().matches("[0-9]{0,7}"))) {
                return null;
            } else {
                return change;
            }
        }));
        amountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()){
                int money = Integer.parseInt(amountTextField.getText());
                if (money < 1000){
                    addButton.setDisable(true);
                    minimal.setText("Minimal amount is 1000 T");
                }else {
                    minimal.setText("");
                }
                if (timeComboBox.getSelectionModel().getSelectedItem() != null) {
                    addButton.setDisable(false);
                    String choose = timeComboBox.getSelectionModel().getSelectedItem();
                    String[] months = choose.split(" ");
                    totalAmountLabel.setText(calculator(money, Integer.parseInt(months[0])));
                }else {
                    addButton.setDisable(true);
                }
            }else {
                addButton.setDisable(true);
            }
        });
        interestRateLabel.setText(ClientHandler.interestRate + " %");
        exitButton1.setOnAction(event -> {
            change(exitButton1, "onlinebank");
        });
        String time = "1 month";
        times_al.add(time);
        for (int i = 2; i < 13;i++){
            time = i + " months";
            times_al.add(time);
        }
        times.setAll(times_al);
        timeComboBox.setItems(times);
        exitButton.setOnAction(event -> {
            change(exitButton, "onlinebank");
        });
        addButton.setOnAction(event -> {
            if (user.getCard().getAmount() < Double.parseDouble(amountTextField.getText())){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Not enough money to open deposit!");
                alert.showAndWait();
            }else {
                String choose = timeComboBox.getSelectionModel().getSelectedItem();
                String[] months = choose.split(" ");
                int month = Integer.parseInt(months[0]);
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.MONTH, month);
                Deposit deposit = new Deposit(user.getId(), Integer.parseInt(amountTextField.getText()),ClientHandler.interestRate,
                        Integer.parseInt(totalAmountLabel.getText()),month,start.getTime(), end.getTime());
                try {
                    clientHandler.writeObject("ADD_DEPOSIT",deposit);
                    new Database().updateAmount(user, Integer.parseInt(amountTextField.getText()));
                    user.getCard().setAmount(user.getCard().getAmount() - Double.parseDouble(amountTextField.getText()));
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Congratulation with getting deposit!");
                alert.showAndWait();
                change(addButton, "onlinebank");
            }
        });
    }


    public String calculator(int money, int months){
        String total = "";
        int everyMonth = (int) Math.round(money*ClientHandler.interestRate/100);
        money += everyMonth*months;
        total += money;
        return total;
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
