package sample.Server;

import sample.BankClasses.*;
import sample.Controllers.UsersTable;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private String operationType;
    private User user1;
    private User user2;
    private int amount;
    private Employee employee;
    private Transaction transaction;
    private Message message;
    private Deposit deposit;
    private Credit credit;
    private ArrayList<User> users;
    private ArrayList<Transaction> transactions;
    private ArrayList<Message> messages;
    private ArrayList<Deposit> deposits;
    private ArrayList<UsersTable> tables;

    public Employee getEmployee() {
        return employee;
    }

    public ArrayList<UsersTable> getTables() {
        return tables;
    }

    public Request(){}

    public Request(String operationType) {
        this.operationType = operationType;
    }

    public Request(String operationType, Message message) {
        this.operationType = operationType;
        this.message = message;
    }

    public Request(String operationType, Employee employee) {
        this.operationType = operationType;
        this.employee = employee;
    }

    public Request(String operationType, User user1, User user2) {
        this.operationType = operationType;
        this.user1 = user1;
        this.user2 = user2;
    }

    public Request(String operationType, User user1, int amount) {
        this.operationType = operationType;
        this.user1 = user1;
        this.amount = amount;
    }

    public Request(String operationType, User user1) {
        this.operationType = operationType;
        this.user1 = user1;
    }

    public Request(String operationType, Transaction transaction) {
        this.operationType = operationType;
        this.transaction = transaction;
    }

    public Request(String operationType, Deposit deposit) {
        this.operationType = operationType;
        this.deposit = deposit;
    }

    public Request(String operationType, Credit credit) {
        this.operationType = operationType;
        this.credit = credit;
    }

    public Request(String operationType, ArrayList<User> users, ArrayList<Transaction> transactions, ArrayList<Message> messages, ArrayList<Deposit> deposits) {
        this.operationType = operationType;
        this.users = users;
        this.transactions = transactions;
        this.messages = messages;
        this.deposits = deposits;
    }

    public String getOperationType() {
        return operationType;
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public int getAmount() {
        return amount;
    }

    public Request(String operationType, ArrayList<UsersTable> tables){
        this.operationType = operationType;
        this.tables = tables;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public ArrayList<Deposit> getDeposits() {
        return deposits;
    }

    public Message getMessage() {
        return message;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public Credit getCredit() {
        return credit;
    }
}
