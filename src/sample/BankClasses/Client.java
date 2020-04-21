package sample.BankClasses;

import java.io.Serializable;

public class Client extends User implements Serializable {
    private Employee employee;

    public Client(int id, String name, String surname, String username, String password, int age, String phone, String email) {
        super(id, name, surname, username, password, age, phone, email);
        employee = null;
        setRole("Client");
    }

    public Client() {
    }

    public Client(String name, String surname, String username, String password, int age, String phone, String email) {
        super(name, surname, username, password, age, phone, email);
        employee = null;
        setRole("Client");
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        String money = "\nCurrent money: " + getCard().getAmount();
        String bonus = "\nBonus: " + getCard().getBonus();
        return super.toString() + " " + money + " " + bonus;
    }
}
