package sample.BankClasses;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public abstract class User implements Serializable {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private int age;
    private String phone;
    private String email;
    private String role;
    private Date date;
    private Card card;
    private ArrayList<Message> messages;

    public User(int id, String name, String surname, String username, String password, int age, String phone, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        card = new Card();
        date = new Date();
        messages = new ArrayList<>();
    }

    public User(String name, String surname, String username, String password, int age, String phone, String email) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.age = age;
        this.phone = phone;
        this.email = email;
        card = new Card();
        date = new Date();
        messages = new ArrayList<>();
    }

    public User() {
        card = new Card();
        messages = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String fullName = getName() + " " + getSurname();
        String age = "\nAge: " + getAge();
        String phone = "\nPhone: " + getPhone();
        String email = "\nEmail: " + getEmail();
        String username = "\nUsername is: " + getUsername();
        String date = "\nDate of registration: " + format.format(getDate());
        String cardNumber = "\nCard number: " + (card.getCardNumber() == null ? "No card" : card.getCardNumber());
        return fullName + age + phone + email + username + cardNumber + date;
    }
}
