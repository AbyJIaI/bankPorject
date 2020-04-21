package sample.BankClasses;

import java.io.Serializable;
import java.util.Date;

public class Deposit implements Serializable {
    private int id;
    private int id_client;
    private int initial_amount;
    private double interest;
    private int total_amount;
    private int months;
    private Date date_begin;
    private Date date_end;

    public Deposit(int id, int id_client, int initial_amount, double interest, int total_amount, int months,Date date_begin, Date date_end) {
        this.id = id;
        this.id_client = id_client;
        this.initial_amount = initial_amount;
        this.interest = interest;
        this.total_amount = total_amount;
        this.date_begin = date_begin;
        this.date_end = date_end;
        this.months = months;
    }

    public Deposit(int id_client, int initial_amount, double interest, int total_amount, int months, Date date_begin, Date date_end) {
        this.id_client = id_client;
        this.initial_amount = initial_amount;
        this.interest = interest;
        this.total_amount = total_amount;
        this.months = months;
        this.date_begin = date_begin;
        this.date_end = date_end;
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

    public int getInitial_amount() {
        return initial_amount;
    }

    public void setInitial_amount(int initial_amount) {
        this.initial_amount = initial_amount;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public Date getDate_begin() {
        return date_begin;
    }

    public void setDate_begin(Date date_begin) {
        this.date_begin = date_begin;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }
}
