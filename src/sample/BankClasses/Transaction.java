package sample.BankClasses;

import java.io.Serializable;
import java.util.Date;

public class Transaction extends Operation implements Serializable {
    private int id_client_from;
    private int id_client_to;
    private double amount;
    private Date date;
    private boolean from_mobile;

    public Transaction(int id, String name, int id_client_from, int id_client_to, double amount, Date date, boolean from_mobile) {
        super(id, name);
        this.id_client_from = id_client_from;
        this.id_client_to = id_client_to;
        this.amount = amount;
        this.date = date;
        this.from_mobile = from_mobile;
    }

    public Transaction(int id_client_from, int id_client_to, double amount, Date date, boolean from_mobile) {
        this.id_client_from = id_client_from;
        this.id_client_to = id_client_to;
        this.amount = amount;
        this.date = date;
        this.from_mobile = from_mobile;
    }

    public Transaction(String name, int id_client_from, int id_client_to, double amount, Date date, boolean from_mobile) {
        super(name);
        this.id_client_from = id_client_from;
        this.id_client_to = id_client_to;
        this.amount = amount;
        this.date = date;
        this.from_mobile = from_mobile;
    }

    public int getId_client_from() {
        return id_client_from;
    }

    public void setId_client_from(int id_client_from) {
        this.id_client_from = id_client_from;
    }

    public int getId_client_to() {
        return id_client_to;
    }

    public void setId_client_to(int id_client_to) {
        this.id_client_to = id_client_to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFrom_mobile() {
        return from_mobile;
    }

    public void setFrom_mobile(boolean from_mobile) {
        this.from_mobile = from_mobile;
    }

    @Override
    public String toString() {
        String amount = "Amount: " + getAmount();
        String date = "\nDate: " + getDate();
        return amount + date;
    }
}
