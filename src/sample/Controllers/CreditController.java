package sample.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.BankClasses.Credit;
import sample.BankClasses.User;
import sample.Server.ClientHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class CreditController {

    private ClientHandler clientHandler = new ClientHandler();
    private User user;

    @FXML
    private TableView<?> creditTableView;

    @FXML
    private TableColumn<?, ?> monthTC;

    @FXML
    private TableColumn<?, ?> payoutTC;

    @FXML
    private TableColumn<?, ?> statusTC;

    @FXML
    private TableColumn<?, ?> dateTC;

    @FXML
    private TextField cal_amountTextField;

    @FXML
    private ComboBox<String> cal_timeComboBox;
    private ObservableList<String> times_al = FXCollections.observableArrayList();
    private ArrayList<String> times = new ArrayList<>();

    @FXML
    private Label cal_TotalLabel;

    @FXML
    private Button calculateButton;

    @FXML
    private Button cal_exitButton;

    @FXML
    private TextField creditAmountTextField;

    @FXML
    private ComboBox<String> creditTimeComboBox;

    @FXML
    private Label creditTotalAmount;

    @FXML
    private Button creditExitButton;

    @FXML
    private Button takeCreditButton;

    @FXML
    private Button creditCalculateButton;

    @FXML
    public void initialize() throws IOException {
        clientHandler.writeRequest("GET_USER");
        if (clientHandler.checkRequest("TAKE")) {
            User user = ClientHandler.user;
        }
        cal_exitButton.setOnAction(event -> {
            change(cal_exitButton, "onlinebank");
        });
        creditExitButton.setOnAction(event -> {
            change(creditExitButton, "onlinebank");
        });
        for (int i = 1;i <=20;i++){
            times.add(i + " year");
        }
        times_al.setAll(times);
        cal_timeComboBox.setItems(times_al);
        creditTimeComboBox.setItems(times_al);
        calculateButton.setOnAction(event -> {
            double initial = Double.parseDouble(cal_amountTextField.getText());
            String[] choose = cal_timeComboBox.getSelectionModel().getSelectedItem().split(" ");

            int years = Integer.parseInt(choose[0]);
            cal_TotalLabel.setText(calculator(initial,ClientHandler.creditRate,years) + " T");
        });
        creditCalculateButton.setOnAction(event -> {
            double initial = Double.parseDouble(creditAmountTextField.getText());
            String[] choose = creditTimeComboBox.getSelectionModel().getSelectedItem().split(" ");

            int years = Integer.parseInt(choose[0]);
            creditTotalAmount.setText(calculator(initial, ClientHandler.creditRate,years) + " T");
        });
        takeCreditButton.setOnAction(event -> {
            try {
                String[] choose = creditTimeComboBox.getSelectionModel().getSelectedItem().split(" ");
                int years = Integer.parseInt(choose[0]);
                double payout_amount = Double.parseDouble(creditTotalAmount.getText())/(Double.parseDouble(choose[0])*12);
                clientHandler.writeRequest("ADD_CREDIT", new Credit(user.getId(), years, payout_amount,
                        false, 0, new Date()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            change(takeCreditButton, "onlinebank");
        });

    }
    public String calculator(double initial, double percent, int years){
        double everyMonth = 0;
        for (int i = years; i >= 1; i--){
            initial = initial + initial*percent/100;
            everyMonth = initial/(i*12);
            initial = initial - everyMonth*12;
        }
        initial = everyMonth*12*years;
        return String.valueOf(initial);
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
