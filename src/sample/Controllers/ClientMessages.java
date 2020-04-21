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
import sample.Server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ClientMessages {

    @FXML
    private TableView<Message> tableView;
    private ObservableList<Message> messages_ol = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Message, Integer> nameTC;

    @FXML
    private TableColumn<Message, String> answerTC;

    @FXML
    private TableColumn<Message, Date> dateTC;

    @FXML
    private TextArea textArea;

    @FXML
    private Button exitButton;

    private ClientHandler clientHandler = new ClientHandler();
    @FXML
    public void initialize() throws SQLException, IOException {
        clientHandler.writeRequest("GET_USERS_MESSAGES");
        if (clientHandler.checkRequest("TAKE1")) {
            ArrayList<User> users = ClientHandler.users;
            ArrayList < Message > messages = ClientHandler.messages;
            ArrayList<Message> myMessages = new ArrayList<>();
            try {
                clientHandler.writeRequest("GET_USER");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (clientHandler.checkRequest("TAKE")){
                User user = ClientHandler.user;
                for (Message message : messages) {
                    if (message.getId_client() == user.getId()){
                        if (!message.isFrom_client()) {
                            myMessages.add(message);
                        }
                    }
                }
            }
            messages_ol.setAll(myMessages);
            tableView.setItems(messages_ol);
            nameTC.setCellFactory(cell->new TableCell<>(){
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null){
                        String name = "";
                        for (User user : users) {
                            if (user.getId() == item){
                                name = user.getName() + " " + user.getSurname();
                            }
                        }
                        setText(name);
                    }
                }
            });
        }
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!= null){
                textArea.setText(newValue.getContext());
            }
        });
        answerTC.setCellValueFactory(new PropertyValueFactory<>("Context"));
        dateTC.setCellValueFactory(new PropertyValueFactory<>("Date"));
        nameTC.setCellValueFactory(new PropertyValueFactory<>("id_employee"));
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
