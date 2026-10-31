package bankingapp;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class BankService {
    private final Map<String, Account> accounts = new LinkedHashMap<>();
    private final Random random = new Random();

    public BankService() {
        Account demo = new Account("251312009099", "Demo User", "1234", 1000.00);
        accounts.put(demo.getAccountNumber(), demo);
    }

    public Account createAccount(String name, String pin, double openingBalance) throws BankingException {
        if (name == null || name.trim().length() < 2) throw new BankingException("Enter a valid account holder name.");
        if (pin == null || !pin.matches("\\d{4}")) throw new BankingException("PIN must contain exactly 4 digits.");
        if (openingBalance < 0) throw new BankingException("Opening balance cannot be negative.");
        String number;
        do {
            number = "2513" + String.format("%08d", random.nextInt(100_000_000));
        } while (accounts.containsKey(number));
        Account account = new Account(number, name.trim(), pin, openingBalance);
        accounts.put(number, account);
        return account;
    }

    public Account login(String accountNumber, String pin) throws BankingException {
        Account account = accounts.get(accountNumber);
        if (account == null || !account.verifyPin(pin)) throw new BankingException("Invalid account number or PIN.");
        return account;
    }

    public int getAccountCount() { return accounts.size(); }
    public Map<String, Account> getAccounts() { return accounts; }
}
