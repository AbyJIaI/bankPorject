package sample.Server;

import sample.BankClasses.*;
import sample.Controllers.UsersTable;
import sample.Main;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ClientHandler {
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    public static ArrayList<User> users;
    public static ArrayList<Transaction> transactions;
    public static ArrayList<Message> messages;
    public static ArrayList<Deposit> deposits;
    public static ArrayList<UsersTable> tables;
    public static User user;
    public static double interestRate = 9.5;
    public static double creditRate = 10.0;

    static {
        try {
            oos = new ObjectOutputStream(Main.socket.getOutputStream());
            ois = new ObjectInputStream(Main.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRequest(String operationType) throws IOException {
        oos.writeObject(new Request(operationType));
    }

    public void writeRequest(String operationType, User user) throws IOException {
        oos.writeObject(new Request(operationType, user));
    }

    public void writeRequest(String operationType, Message message) throws IOException {
        oos.writeObject(new Request(operationType, message));
    }

    public void writeRequest(String operationType, Employee employee) throws IOException {
        oos.writeObject(new Request(operationType, employee));
    }

    public void writeRequest(String operationType, User user1, User user2) throws IOException {
        oos.writeObject(new Request(operationType, user1, user2));
    }

    public void writeRequest(String operationType, Credit credit) throws IOException {
        oos.writeObject(new Request(operationType, credit));
    }

    public void writeObject(String operationType, User user, int amount) throws IOException {
        oos.writeObject(new Request(operationType,user,amount));
    }

    public void writeObject(String operationType, Transaction transaction) throws IOException {
        oos.writeObject(new Request(operationType, transaction));
    }

    public void writeObject(String operationType, Deposit deposit) throws IOException {
        oos.writeObject(new Request(operationType, deposit));
    }

    public void writeObject(String operationType, ArrayList<User> users, ArrayList<Message> messages,
                            ArrayList<Transaction> transactions, ArrayList<Deposit> deposits) throws IOException {
        oos.writeObject(new Request(operationType,users,transactions,messages, deposits));
    }


    public boolean checkRequest(String operationType){
        try {
            Request request;
            request = (Request) ois.readObject();
            if (request.getOperationType().equals(operationType)){
                users = request.getUsers();
                transactions = request.getTransactions();
                messages = request.getMessages();
                deposits = request.getDeposits();
                user = request.getUser1();
                tables = request.getTables();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
