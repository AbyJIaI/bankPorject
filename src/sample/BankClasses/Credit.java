package sample.BankClasses;

import java.io.Serializable;
import java.util.Date;

public class Credit extends Operation implements Serializable {
    private int id;
    private int id_client;
    private int months;
    private double payout_amount;
    private boolean status;
    private int paid;
    private Date date;

    public Credit(int id, int id_client, int months, double payout_amount, boolean status, int paid, Date date) {
        this.id = id;
        this.id_client = id_client;
        this.months = months;
        this.payout_amount = payout_amount;
        this.status = status;
        this.paid = paid;
        this.date = date;
    }

    public Credit(int id_client, int months, double payout_amount, boolean status, int paid, Date date) {
        this.id_client = id_client;
        this.months = months;
        this.payout_amount = payout_amount;
        this.status = status;
        this.paid = paid;
        this.date = date;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public double getPayout_amount() {
        return payout_amount;
    }

    public void setPayout_amount(double payout_amount) {
        this.payout_amount = payout_amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
