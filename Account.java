package bankingapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account {
    private final String accountNumber;
    private final String holderName;
    private final String pin;
    private double balance;
    private final List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String holderName, String pin, double openingBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = openingBalance;
        if (openingBalance > 0) transactions.add(new Transaction("Opening deposit", openingBalance, balance));
    }

    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }
    public boolean verifyPin(String enteredPin) { return pin.equals(enteredPin); }

    public void deposit(double amount) throws BankingException {
        validateAmount(amount);
        balance += amount;
        transactions.add(new Transaction("Deposit", amount, balance));
    }

    public void withdraw(double amount) throws BankingException {
        validateAmount(amount);
        if (amount > balance) throw new BankingException("Insufficient balance.");
        balance -= amount;
        transactions.add(new Transaction("Withdrawal", amount, balance));
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    private void validateAmount(double amount) throws BankingException {
        if (!Double.isFinite(amount) || amount <= 0) throw new BankingException("Enter an amount greater than zero.");
    }
}
