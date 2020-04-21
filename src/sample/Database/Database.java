package sample.Database;

import sample.BankClasses.*;
import sample.Controllers.UsersTable;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    public static double interestRate = 9.5;
    private static User user = null;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Database.user = user;
    }

    private Connection connection = null;
    private PreparedStatement ps = null;

    public Database() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://192.168.64.2:3306/test", "abylay", "abylay");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    public void addUser(User user) {
        try {
            ps = connection.prepareStatement("INSERT INTO users(id, name, surname, username, password, age, phone, email, role, date) " +
                    "VALUES(NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getName());
            ps.setString(2, user.getSurname());
            ps.setString(3, user.getUsername());
            ps.setString(4, user.getPassword());
            ps.setInt(5, user.getAge());
            ps.setString(6, user.getPhone());
            ps.setString(7, user.getEmail());
            ps.setString(8, user.getRole());
            ps.setObject(9, user.getDate());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void addCard (User user) throws SQLException {
        ps = connection.prepareStatement("SELECT id FROM users WHERE username=?");
        ps.setString(1,user.getUsername());
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            user.setId(rs.getInt("id"));
        }
        ps.close();
        ps = connection.prepareStatement("INSERT INTO cards (id, id_client, card_number, money, bonus, blocked) " +
                "VALUES (NULL, ?, ?, 0, 0, 0)");
        ps.setInt(1, user.getId());
        ps.setString(2, user.getCard().getCardNumber());
        ps.execute();
        ps.close();
    }
    public void addTransaction(Transaction transaction) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO transactions(id, id_client_from, id_client_to, amount, date, from_mobile) " +
                "VALUES(NULL, ?, ?, ?, ?, ?)");
        ps.setInt(1, transaction.getId_client_from());
        ps.setInt(2, transaction.getId_client_to());
        ps.setDouble(3, transaction.getAmount());
        ps.setObject(4, transaction.getDate());
        ps.setBoolean(5, transaction.isFrom_mobile());
        ps.execute();
        ps.close();
    }
    public void addMessage(Message message) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO messages(id, id_client, id_employee, context, status, date, from_client) " +
                "VALUES (NULL, ?, ?, ?, ?, ?, ?)");
        ps.setInt(1, message.getId_client());
        ps.setInt(2, message.getId_employee());
        ps.setString(3, message.getContext());
        ps.setBoolean(4, message.isStatus());
        ps.setObject(5, message.getDate());
        ps.setBoolean(6, message.isFrom_client());
        ps.execute();
        ps.close();
    }
    public User setCard(User user) throws SQLException {
        ps = connection.prepareStatement("SELECT * FROM cards INNER JOIN users ON cards.id_client=?");
        ps.setInt(1, user.getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            user.getCard().setCardNumber(rs.getString("card_number"));
            user.getCard().setAmount(rs.getDouble("money"));
            user.getCard().setBonus(rs.getDouble("bonus"));
        }
        ps.close();
        return user;
    }
    public String setOperation_Type(Employee employee) throws SQLException {
        ps = connection.prepareStatement("SELECT operation_type FROM roles WHERE id_employee=?");
        ps.setInt(1, employee.getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("operation_type");
        }
        ps.close();
        return "ERROR";
    }
    public void updateUser(User user) throws SQLException{
        ps = connection.prepareStatement("UPDATE users SET name = ?, surname = ?, username = ?, age = ?, phone = ?, email = ? " +
                "WHERE id = ?");
        ps.setString(1, user.getName());
        ps.setString(2, user.getSurname());
        ps.setString(3, user.getUsername());
        ps.setInt(4, user.getAge());
        ps.setString(5, user.getPhone());
        ps.setString(6, user.getEmail());
        ps.setInt(7, user.getId());
        ps.execute();
        ps.close();
    }
    public int hasClient(User employee) throws SQLException {
        ps = connection.prepareStatement("SELECT * FROM messages WHERE id_employee=? AND status=0");
        ps.setInt(1, employee.getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt("id_client");
        }else {
            return -1;
        }
    }
    public int hasEmployee(User user) throws SQLException {
        ps = connection.prepareStatement("SELECT * FROM messages WHERE id_client=? AND status=0");
        ps.setInt(1,user.getId());
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt("id_employee");
        }else {
            return -1;
        }
    }
    public void setStatus(Message message) throws SQLException{
        ps = connection.prepareStatement("UPDATE messages SET status=1 WHERE id_employee=? AND id_client=? AND status=0");
        ps.setInt(1, message.getId_employee());
        ps.setInt(2, message.getId_client());
        ps.execute();
        ps.close();
    }
    public void updateAmounts(User from_user, User to_user) throws SQLException {
        ps = connection.prepareStatement("UPDATE cards SET money = ? WHERE id_client = ?");
        ps.setDouble(1, from_user.getCard().getAmount());
        ps.setInt(2, from_user.getId());
        ps.execute();
        ps = connection.prepareStatement("UPDATE cards SET money = ? WHERE id_client = ?");
        ps.setDouble(1, to_user.getCard().getAmount());
        ps.setInt(2, to_user.getId());
        ps.execute();
        ps.close();
    }
    public void updateAmount(User user, int amount) throws SQLException {
        ps = connection.prepareStatement("UPDATE cards SET money=? WHERE id_client=?");
        ps.setFloat(1, (float) user.getCard().getAmount() - (float)amount);
        ps.setInt(2, user.getId());
        ps.execute();
        ps.close();
    }
    public void addDeposit(Deposit deposit) throws SQLException {
        ps = connection.prepareStatement(
                "INSERT INTO deposits(id, id_client, initial_amount, interest, total_amount, months, date_begin,date_end) " +
                "VALUES(NULL, ?, ?, ?, ?, ?, ?, ?) ");
        ps.setInt(1, deposit.getId_client());
        ps.setInt(2, deposit.getInitial_amount());
        ps.setFloat(3, (float) deposit.getInterest());
        ps.setInt(4, deposit.getTotal_amount());
        ps.setInt(5, deposit.getMonths());
        ps.setObject(6, deposit.getDate_begin());
        ps.setObject(7, deposit.getDate_end());
        ps.execute();
        ps.close();
    }
    public ArrayList<Deposit> getDeposits() throws SQLException {
        ArrayList<Deposit> deposits = new ArrayList<>();
        ps = connection.prepareStatement("SELECT * FROM deposits");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int id = rs.getInt("id");
            int id_client = rs.getInt("id_client");
            int initial_amount = rs.getInt("initial_amount");
            double interest = rs.getFloat("interest");
            int total_amount = rs.getInt("total_amount");
            int months = rs.getInt("months");
            Date begin = rs.getDate("date_begin");
            Date end = rs.getDate("date_end");
            Deposit deposit = new Deposit(id,id_client,initial_amount,interest, total_amount,months,begin,end);
            deposits.add(deposit);
        }
        return deposits;
    }
    public ArrayList<Transaction> getTransactions(User user) throws SQLException {
        ArrayList<Transaction> transactions = new ArrayList<>();
        ps = connection.prepareStatement("SELECT * FROM transactions WHERE id_client_from = ?");
        ps.setInt(1, user.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String name = "Money transfer";
            int id_client_from = rs.getInt("id_client_from");
            int id_client_to = rs.getInt("id_client_to");
            double amount = rs.getDouble("amount");
            Date date = rs.getDate("date");
            boolean from_mobile = rs.getBoolean("from_mobile");
            Transaction tr = new Transaction(name, id_client_from, id_client_to, amount, date, from_mobile);
            transactions.add(tr);
        }
        ps = connection.prepareStatement("SELECT * FROM transactions WHERE id_client_to = ?");
        ps.setInt(1, user.getId());
        ResultSet rs1 = ps.executeQuery();
        while (rs1.next()) {
            String name = "Money refill";
            int id_client_from = rs1.getInt("id_client_from");
            int id_client_to = rs1.getInt("id_client_to");
            double amount = rs1.getDouble("amount");
            Date date = rs1.getDate("date");
            boolean from_mobile = rs1.getBoolean("from_mobile");
            Transaction tr1 = new Transaction(name, id_client_from, id_client_to, amount, date, from_mobile);
            transactions.add(tr1);
        }
        ps.close();
        return transactions;
    }
    public ArrayList<User> getUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        ps = connection.prepareStatement("SELECT * FROM users");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String username = rs.getString("username");
            String password = rs.getString("password");
            int age = rs.getInt("age");
            String phone = rs.getString("phone");
            String email = rs.getString("email");
            String role = rs.getString("role");
            Date date = rs.getDate("date");
            if (role.equals("Client")) {
                Client client = new Client(id, name, surname, username, password, age, phone, email);
                client = (Client) setCard(client);
                client.setRole(role);
                client.setDate(date);
                users.add(client);
            } else if (role.equals("Employee") || role.equals("Admin")) {
                Employee employee = new Employee(id, name, surname, username, password, age, phone, email, "");
                employee.setRole(role);
                employee.setDate(date);
                employee.setOperationType(setOperation_Type(employee));
                employee = (Employee) setCard(employee);
                users.add(employee);
            }
        }
        ps.close();
        return users;
    }
    public ArrayList<Message> getMessages() throws SQLException {
        ArrayList<Message> messages = new ArrayList<>();
        ps = connection.prepareStatement("SELECT * FROM messages");
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            int id = rs.getInt("id");
            int id_client = rs.getInt("id_client");
            int id_employee = rs.getInt("id_employee");
            String context = rs.getString("context");
            boolean status = rs.getBoolean("status");
            Date date = (Date) rs.getObject("date");
            boolean from_client = rs.getBoolean("from_client");
            Message message = new Message(id,id_client, id_employee,context,status, date,from_client);
            messages.add(message);
        }
        ps.close();
        return messages;
    }

    public ArrayList<UsersTable> getUsersByRole(String role) throws SQLException{
        ArrayList<UsersTable> tables = new ArrayList<>();
        ps = connection.prepareStatement("SELECT * FROM users WHERE role='" + role + "'");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String username = rs.getString("name");
            String rolee = role;
            UsersTable data = new UsersTable(name, surname, username, rolee);
            tables.add(data);
        }
        ps.close();
        return tables;
    }

    public void addCredit(Credit credit) throws SQLException {
        ps = connection.prepareStatement("INSERT INTO credits(id, id_client, months, payout_amount, status, paid, date) " +
                "VALUES(NULL , ?, ?, ?, 0, 0, ?)");
        ps.setInt(1, credit.getId_client());
        ps.setInt(2, credit.getMonths());
        ps.setFloat(3, (float) credit.getPayout_amount());
        ps.setObject(4, credit.getDate());
        ps.execute();
        ps.close();
    }
}
