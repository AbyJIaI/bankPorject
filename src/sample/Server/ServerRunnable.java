package sample.Server;

import sample.Controllers.UsersTable;
import sample.Database.Database;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerRunnable extends Thread{
    private Socket socket;

    public ServerRunnable(Socket socket) {
        this.socket = socket;
    }
    private Request request = new Request();
    private Database database = new Database();

    @Override
    public void run() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            while ((request = (Request) inputStream.readObject())!= null){
                if (request.getOperationType().equals("GET_USERS")){
                    outputStream.writeObject(new Request("USERS_GOT", database.getUsers(), null,null, null));
                }
                if (request.getOperationType().equals("SET_USER")){
                    Database.setUser(request.getUser1());
                }
                if (request.getOperationType().equals("GET_USER")){
                    outputStream.writeObject(new Request("TAKE", Database.getUser()));
                }
                if (request.getOperationType().equals("GET_USERS_MESSAGES")){
                    outputStream.writeObject(new Request("TAKE1",database.getUsers(),null, database.getMessages(),null));
                }
                if (request.getOperationType().equals("GET_USERS_TRANSACTIONS")){
                    outputStream.writeObject(new Request("TAKE2",database.getUsers(),database.getTransactions(request.getUser1()),null,null));
                }
                if (request.getOperationType().equals("ADD_USER")){
                    database.addUser(request.getUser1());
                }
                if (request.getOperationType().equals("ADD_CARD")){
                    database.addCard(request.getUser1());
                }
                if (request.getOperationType().equals("ADD_MESSAGE")){
                    database.addMessage(request.getMessage());
                }
                if (request.getOperationType().equals("GET_MESSAGES")){
                    outputStream.writeObject(new Request("TAKE3",null,null,database.getMessages(),null));
                }
                if (request.getOperationType().equals("GET_DEPOSITS")){
                    outputStream.writeObject(new Request("TAKE4",null,null,null,database.getDeposits()));
                }
                if (request.getOperationType().equals("ADD_DEPOSIT")){
                    database.addDeposit(request.getDeposit());
                }
                if(request.getOperationType().equals("GET_USER_ROLE")){
                    ArrayList<UsersTable> tables = database.getUsersByRole("Client");
                    outputStream.writeObject(new Request("ROLE_USERS", tables));
                }
                if(request.getOperationType().equals("GET_EMPLOYEES_ROLE")){
                    ArrayList<UsersTable> tables = database.getUsersByRole("Employee");
                    outputStream.writeObject(new Request("ROLE_EMPLOYEES", tables));
                }
                if (request.getOperationType().equals("ADD_CREDIT")){
                    database.addCredit(request.getCredit());
                }
            }
        }catch (IOException | ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }
}
