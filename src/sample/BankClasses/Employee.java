package sample.BankClasses;

import java.io.Serializable;

public class Employee extends User implements Serializable {
    private Client client;
    private String operationType;
    public Employee(int id, String name, String surname, String username, String password, int age, String phone, String email, String operationType) {
        super(id, name, surname, username, password, age, phone, email);
        this.operationType = operationType;
        client = null;
        setRole("Employee");
    }
    public Employee(String name, String surname, String username, String password, int age, String phone, String email, String operationType) {
        super(name, surname, username, password, age, phone, email);
        this.operationType = operationType;
        client = null;
        setRole("Employee");
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    @Override
    public String toString() {
        return super.toString() + " " + operationType + " " + (client!= null?getName() + " is working with " + client.getName()
                + "(client id: " + client.getId() + ")":"Free");
    }
}
