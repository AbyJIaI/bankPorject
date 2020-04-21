package sample.Controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import sample.BankClasses.Client;
import sample.BankClasses.Employee;
import sample.BankClasses.Message;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuideController {
    private User user;
    private ArrayList<User> users = new ArrayList<>();
    private ClientHandler clientHandler = new ClientHandler();
    @FXML
    private AnchorPane pane;

    @FXML
    private TableView<Employee> employeeTableView;
    private ObservableList<Employee> employees = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Employee, String> nameTableColumn;

    @FXML
    private TableColumn<Employee, String> surnameTableColumn;

    @FXML
    private Button exitButton;

    @FXML
    private TableColumn<Employee, Integer> ageTableColumn;

    @FXML
    private TableColumn<Employee, String> opTableColumn;

    @FXML
    private TableColumn<Employee, Client> clientTableColumn;


    @FXML
    public void initialize() throws SQLException, IOException {
        clientHandler.writeRequest("GET_USER");
        if (clientHandler.checkRequest("TAKE")){
            user = ClientHandler.user;
        }
        clientHandler.writeRequest("GET_USERS");
        if (clientHandler.checkRequest("USERS_GOT")) {
            users = ClientHandler.users;
        }
        ArrayList<Employee> employees1 = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Employee) {
                if (((Employee) user).getOperationType().equals("Guide")) {
                    employees1.add((Employee) user);
                }
            }
        }
        for (Employee employee : employees1) {
            int idOfClient = new Database().hasClient(employee);
            if (idOfClient != -1){
                for (User user : users) {
                    if (user.getId() == idOfClient){
                        employee.setClient((Client) user);
                    }
                }
            }
        }
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        surnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("Surname"));
        ageTableColumn.setCellValueFactory(new PropertyValueFactory<>("Age"));
        opTableColumn.setCellValueFactory(new PropertyValueFactory<>("OperationType"));
        clientTableColumn.setCellValueFactory(new PropertyValueFactory<>("Client"));
        clientTableColumn.setCellFactory(cell -> new TableCell<>() {
            @Override
            protected void updateItem(Client item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item != null ? item.getName() + " " + item.getSurname() : "No client");
            }
        });
        employeeTableView.setOnMouseClicked(event -> {
            if (((Client) user).getEmployee() == null) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        Employee employee = employeeTableView.getSelectionModel().getSelectedItem();
                        if (employee.getClient() == null) {
                            Label label = new Label("Hello, " + user.getUsername());
                            GridPane gridPane = new GridPane();
                            gridPane.setPadding(new Insets(20));
                            gridPane.setHgap(25);
                            gridPane.setVgap(15);
                            gridPane.add(label, 0, 0);
                            Label text = new Label("Type your problem: ");
                            TextArea textField = new TextArea();
                            textField.setPrefColumnCount(20);
                            textField.setPrefRowCount(5);
                            textField.setWrapText(true);
                            textField.setTextFormatter(new TextFormatter<String>(change -> {
                                if (change.getControlNewText().length() <= 120) {
                                    return change;
                                } else {
                                    return null;
                                }
                            }));
                            Button button = new Button("Submit");
                            gridPane.add(text, 0, 1);
                            gridPane.add(textField, 1, 1);
                            gridPane.add(button, 1, 2);
                            Scene scene = new Scene(gridPane, 600, 300);
                            pane.setDisable(true);
                            Stage stage = new Stage();
                            button.setOnAction(event1 -> {
                                if (textField.getText().equals("")) {
                                    Alert alert = new Alert(Alert.AlertType.ERROR);
                                    alert.setContentText("Sorry, you don't type anything!\nWe can not submit this problem!");
                                    alert.showAndWait();
                                } else {
                                    Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
                                    try {
                                        sendMessageToEmployee((Client) user, selectedEmployee, textField.getText());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setContentText("Your problem succesfully sended!\nPlease, wait until the employee answers you!");
                                    alert.showAndWait();
                                }
                            });
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.showAndWait();
                            pane.setDisable(false);
                            change(exitButton, "onlinebank");
                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("This employee is not free!\nPlease, wait until employee will be free!");
                            alert.showAndWait();
                        }
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("We have already submit your problem!\nPlease, wait for the answer!\nThank you");
                alert.showAndWait();
            }
        });
        employees.setAll(employees1);
        employeeTableView.setItems(employees);
        exitButton.setOnAction(event -> {
            change(exitButton, "onlinebank");
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

    public void sendMessageToEmployee(Client client, Employee employee, String context) throws IOException {
        Message message = new Message(client.getId(), employee.getId(), context, false, true);
        clientHandler.writeRequest("ADD_MESSAGE", message);
    }
}
