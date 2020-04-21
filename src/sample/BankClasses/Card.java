package sample.BankClasses;

import java.io.Serializable;

public class Card implements Serializable {
    private String cardNumber;
    private double amount;
    private double bonus;
    private double deposit;

    public Card(String cardNumber, double amount, double bonus, double deposit) {
        this.cardNumber = cardNumber;
        this.amount = amount;
        this.bonus = bonus;
        this.deposit = deposit;
    }

    public Card() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }
}
