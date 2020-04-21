package sample.Controllers;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.BankClasses.Transaction;
import sample.BankClasses.User;
import sample.Database.Database;
import sample.Server.ClientHandler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AllTransactions {

    @FXML
    private TextArea transferTextArea;

    @FXML
    private Button exitButton;
    @FXML
    private Button printButton;
    private ClientHandler clientHandler = new ClientHandler();
    String history = "";
    @FXML
    public void initialize() throws SQLException, IOException {
        clientHandler.writeRequest("GET_USER");
        if (clientHandler.checkRequest("TAKE")){
            User user = ClientHandler.user;
            clientHandler.writeRequest("GET_USERS_TRANSACTIONS", user);
            if (clientHandler.checkRequest("TAKE2")){
                ArrayList<User> users = ClientHandler.users;
                ArrayList<Transaction> transactions = ClientHandler.transactions;
                transactions.sort((o1, o2) -> {
                    if (o1.getDate().after(o2.getDate())) {
                        return 1;
                    } else if (o1.getDate().before(o2.getDate())) {
                        return -1;
                    } else {
                        return 0;
                    }
                });
                for (Transaction transaction : transactions) {
                    if (transaction.getName().equals("Money transfer")) {
                        User user1 = findReceiver(transaction.getId_client_to(), users);
                        if (user1 != null) {
                            history += transaction.getName() + "\n";
                            history += "Amount: " + transaction.getAmount() + "\n";
                            history += "Date: " + transaction.getDate() + "\n";
                            history += "Receiver: " + user1. getName() + " " + user1.getSurname() + "\n";
                            history += "___________________________________________________________________\n";
                        }
                    } else if (transaction.getName().equals("Money refill")) {
                        User user1 = findReceiver(transaction.getId_client_from(), users);
                        if (user1 != null) {
                            history += transaction.getName() + "\n";
                            history += "Amount: " + transaction.getAmount() + "\n";
                            history += "Date: " + transaction.getDate() + "\n";
                            history += "Sender: " + user1. getName() + " " + user1.getSurname() + "\n";
                            history += "___________________________________________________________________\n";
                        }
                    }
                }
                transferTextArea.setText(history);
            }
        }
        printButton.setOnAction(event -> {
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, new FileOutputStream("Transaction.pdf"));
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            document.open();
            Anchor anchorTarget = new Anchor("Your transaction history:");
            anchorTarget.setName("BackToTop");
            Paragraph paragraph1 = new Paragraph();

            paragraph1.setSpacingBefore(50);
            paragraph1.add(anchorTarget);
            try {
                document.add(paragraph1);
                document.add(new Paragraph(history, FontFactory.getFont(FontFactory.COURIER, 14, Font.BOLD)));
            }
            catch (Exception e){
                e.printStackTrace();
            }
            document.close();
        });
        exitButton.setOnAction(event -> {
            change(exitButton, "mybank");
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
    private static User findReceiver(int id, ArrayList<User> users) {
        for (User user : users) {
            if (id == user.getId()) {
                return user;
            }
        }
        return null;
    }
}
