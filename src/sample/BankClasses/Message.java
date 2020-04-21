package sample.BankClasses;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private int id;
    private int id_client;
    private int id_employee;
    private String context;
    private boolean status;
    private Date date;
    private boolean from_client;

    public Message(int id, int id_client, int id_employee, String context, boolean status, Date date, boolean from_client) {
        this.id = id;
        this.id_client = id_client;
        this.id_employee = id_employee;
        this.context = context;
        this.status = status;
        this.date = date;
        this.from_client = from_client;
    }

    public Message(int id_client, int id_employee, String context, boolean status, boolean from_client) {
        this.id_client = id_client;
        this.id_employee = id_employee;
        this.context = context;
        this.status = status;
        this.from_client = from_client;
        date = new Date();
    }

    public Message() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getId_employee() {
        return id_employee;
    }

    public void setId_employee(int id_employee) {
        this.id_employee = id_employee;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFrom_client() {
        return from_client;
    }

    public void setFrom_client(boolean from_client) {
        this.from_client = from_client;
    }
}
