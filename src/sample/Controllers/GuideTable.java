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
import sample.BankClasses.Message;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class GuideTable {
    private User user;
    private ArrayList<User> users = new ArrayList<>();

    @FXML
    private TableView<Message> messageTableView;
    private ObservableList<Message> message_ol = FXCollections.observableArrayList();
    private ArrayList<Message> messages;

    @FXML
    private TableColumn<Message, Integer> clientNameTableColumn;

    @FXML
    private TableColumn<Message, String> contextTableColumn;

    @FXML
    private TableColumn<Message, Boolean> statusTableColumn;

    @FXML
    private TableColumn<Message, Date> dateTableColumn;

    @FXML
    private Button exitButton;

    @FXML
    private TextArea textArea;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private Button submitButton;

    private ClientHandler clientHandler = new ClientHandler();

    @FXML
    public void initialize() throws SQLException, IOException {
        clientHandler.writeRequest("GET_USER");
        if (clientHandler.checkRequest("TAKE")){
            user = ClientHandler.user;
        }
        textArea.setWrapText(true);
        submitButton.setOnAction(event -> {
            if (textArea.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You did not type anything!\n");
                alert.showAndWait();
            }else {
                try {
                    new Database().setStatus(messageTableView.getSelectionModel().getSelectedItem());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Successfully answered!");
                alert.showAndWait();
                try {
                    ArrayList<Message> messages = new ArrayList<>();
                    clientHandler.writeRequest("GET_MESSAGES");
                    if (clientHandler.checkRequest("TAKE3")){
                        messages = ClientHandler.messages;
                    }
                    ArrayList<Message> newMessages = new ArrayList<>();
                    for (Message message : messages) {
                        if (message.getId_employee() == user.getId()){
                            newMessages.add(message);
                        }
                    }
                    Message selectedClient = messageTableView.getSelectionModel().getSelectedItem();
                    if (selectedClient != null) {
                        Message from_employee = new Message(selectedClient.getId_client(), selectedClient.getId_employee(),
                                textArea.getText(), true, false);
                        clientHandler.writeRequest("ADD_MESSAGE", from_employee);
                    }
                    message_ol.setAll(newMessages);
                    messageTableView.setItems(message_ol);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        clientHandler.writeRequest("GET_USERS");
        if (clientHandler.checkRequest("USERS_GOT")) {
            users = ClientHandler.users;
        }
        contextTableColumn.setCellValueFactory(new PropertyValueFactory<>("Context"));
        statusTableColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        statusTableColumn.setCellFactory(cell -> new TableCell<>(){
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "Answered":"Not answered");
            }
        });
        messageTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            textArea.setText("");
            if (newValue != null){
                String info = messageTableView.getSelectionModel().getSelectedItem().getContext();
                infoTextArea.setWrapText(true);
                infoTextArea.setText(info);
                if (newValue.isStatus()){
                    submitButton.setDisable(true);
                    textArea.setDisable(true);
                }else {
                    submitButton.setDisable(false);
                    textArea.setDisable(false);
                }
            }
        });
        clientNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("id_client"));
        clientNameTableColumn.setCellFactory(cell -> new TableCell<>(){
            @Override
            protected void updateItem(Integer item, boolean empty) {
                if (item != null) {
                    String fullName = "";
                    for (User user : users) {
                        if (user.getId() == item) {
                            fullName = user.getName() + " " + user.getSurname();
                        }
                    }
                    setText(fullName);
                }
            }
        });
        clientHandler.writeRequest("GET_MESSAGES");
        if (clientHandler.checkRequest("TAKE3")){
            messages = ClientHandler.messages;
        }
        ArrayList<Message> from_client = new ArrayList<>();
        for (Message message : messages) {
            if (message.getId_employee() == user.getId()){
                from_client.add(message);
            }
        }
        from_client.sort((o1, o2) -> {
            if (o1.getDate().before(o2.getDate())){
                return -1;
            }else if (o1.getDate().after(o2.getDate())){
                return 1;
            }else {
                return 0;
            }
        });
        message_ol.setAll(from_client);
        messageTableView.setItems(message_ol);
        exitButton.setOnAction(event -> {
            change(exitButton, "employeemenu");
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
