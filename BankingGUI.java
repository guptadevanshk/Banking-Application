package bankingapp;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Map;

public class BankingGUI extends JFrame {
    private final BankService bank = new BankService();
    private final Color navy = new Color(13, 20, 43);
    private final Color panel = new Color(248, 250, 252);
    private final Color blue = new Color(0, 145, 190);
    private final Color red = new Color(224, 48, 67);
    private final Color ink = new Color(22, 48, 79);
    private final JPanel root = new JPanel(null);
    private Account activeAccount;

    public BankingGUI() {
        setTitle("Banking Application");
        setSize(1000, 650);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        root.setBackground(navy);
        setContentPane(root);
        showLogin();
    }

    private JLabel label(String text, int x, int y, int w, int h, int size, Color color, boolean bold) {
        JLabel l = new JLabel(text);
        l.setBounds(x, y, w, h);
        l.setForeground(color);
        l.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        return l;
    }

    private JTextField field(int x, int y, int w) {
        JTextField f = new JTextField();
        f.setBounds(x, y, w, 34);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(205, 215, 225)), new EmptyBorder(5, 8, 5, 8)));
        return f;
    }

    private JPasswordField password(int x, int y, int w) {
        JPasswordField f = new JPasswordField();
        f.setBounds(x, y, w, 34);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBorder(new CompoundBorder(new LineBorder(new Color(205, 215, 225)), new EmptyBorder(5, 8, 5, 8)));
        return f;
    }

    private JButton button(String text, int x, int y, int w, int h, Color color, Runnable action) {
        JButton b = new JButton(text);
        b.setBounds(x, y, w, h);
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> action.run());
        return b;
    }

    private JPanel card(int x, int y, int w, int h) {
        JPanel p = new JPanel(null);
        p.setBounds(x, y, w, h);
        p.setBackground(panel);
        p.setBorder(new LineBorder(new Color(205, 218, 230), 1));
        return p;
    }

    private void reset() {
        root.removeAll();
        root.repaint();
    }

    private void showLogin() {
        activeAccount = null;
        reset();
        JPanel login = card(75, 125, 330, 365);
        login.add(label("System Login", 20, 18, 290, 32, 22, ink, true));
        login.add(label("Account Number", 25, 75, 270, 20, 13, ink, true));
        JTextField account = field(25, 98, 280);
        login.add(account);
        login.add(label("PIN", 25, 145, 270, 20, 13, ink, true));
        JPasswordField pin = password(25, 168, 280);
        login.add(pin);
        login.add(button("Login as User", 25, 225, 280, 38, blue, () -> {
            try {
                activeAccount = bank.login(account.getText().trim(), new String(pin.getPassword()));
                showDashboard();
            } catch (BankingException ex) { error(ex.getMessage()); }
        }));
        login.add(button("Login as Admin", 25, 272, 280, 38, blue, this::showAdmin));
        login.add(button("Create New Account", 25, 319, 280, 34, new Color(40, 155, 125), this::showCreateAccount));
        root.add(login);

        JPanel team = card(435, 125, 490, 365);
        team.setBackground(new Color(23, 35, 61));
        team.setBorder(new LineBorder(new Color(55, 80, 110), 1));
        team.add(label("Project Team Members", 22, 18, 440, 30, 21, new Color(180, 220, 245), true));
        String[] members = {
            "• Devansh Kumar Gupta",
            "• Gyni Sai Koushik",
            "• L Jayant",
            "• Raveena Kumari",
            "• Varsha Kumari"
        };
        int yy = 82;
        for (String m : members) {
            team.add(label(m, 28, yy, 430, 30, 15, Color.WHITE, false));
            yy += 43;
        }
        root.add(team);
        root.add(label("BANKING APPLICATION  |  JAVA SWING", 75, 525, 600, 25, 12, new Color(145, 165, 190), true));
        root.revalidate(); root.repaint();
    }

    private void showCreateAccount() {
        reset();
        JPanel create = card(75, 105, 390, 430);
        create.add(label("Create New Account", 20, 18, 350, 32, 21, ink, true));
        create.add(label("Account Holder Name", 20, 78, 340, 20, 13, ink, true));
        JTextField name = field(20, 102, 345);
        create.add(name);
        create.add(label("Create 4-digit PIN", 20, 155, 340, 20, 13, ink, true));
        JPasswordField pin = password(20, 179, 345);
        create.add(pin);
        create.add(label("Opening Balance (₹)", 20, 232, 340, 20, 13, ink, true));
        JTextField opening = field(20, 256, 345);
        opening.setText("0");
        create.add(opening);
        create.add(button("Create Account", 20, 310, 345, 38, blue, () -> {
            try {
                double amount = Double.parseDouble(opening.getText().trim());
                Account created = bank.createAccount(name.getText(), new String(pin.getPassword()), amount);
                JOptionPane.showMessageDialog(this,
                    "Account created successfully!\\nAccount Number: " + created.getAccountNumber()
                    + "\\nPlease remember your account number and PIN.");
                showCreateAccount();
            } catch (NumberFormatException ex) { error("Enter a valid opening balance."); }
            catch (BankingException ex) { error(ex.getMessage()); }
        }));
        create.add(button("Logout / Back", 20, 360, 345, 35, red, this::showLogin));
        root.add(create);

        JPanel active = card(495, 105, 430, 430);
        active.setBackground(new Color(23, 35, 61));
        active.setBorder(new LineBorder(new Color(55, 80, 110), 1));
        active.add(label("Active Accounts Database", 18, 15, 390, 30, 20, new Color(180, 220, 245), true));
        JTextArea list = new JTextArea();
        list.setEditable(false);
        list.setBackground(new Color(45, 59, 82));
        list.setForeground(Color.WHITE);
        list.setFont(new Font("Consolas", Font.PLAIN, 12));
        StringBuilder rows = new StringBuilder("ACCOUNT NUMBER       HOLDER\\n");
        rows.append("------------------------------------\\n");
        for (Account a : bank.getAccounts().values()) {
            rows.append(a.getAccountNumber()).append("   ").append(a.getHolderName()).append("\\n");
        }
        list.setText(rows.toString());
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBounds(18, 55, 394, 350);
        active.add(scroll);
        root.add(active);
        root.revalidate(); root.repaint();
    }

    private void showDashboard() {
        reset();
        JPanel account = card(75, 85, 340, 465);
        account.add(label("Account: " + activeAccount.getAccountNumber(), 18, 18, 310, 25, 15, ink, true));
        JLabel balance = label("Balance: ₹" + String.format("%.2f", activeAccount.getBalance()),
                18, 52, 310, 28, 20, new Color(28, 145, 75), true);
        account.add(balance);
        account.add(label("Enter Amount", 18, 100, 300, 20, 13, ink, true));
        JTextField amount = field(18, 124, 300);
        account.add(amount);

        Runnable refresh = () -> {
            balance.setText("Balance: ₹" + String.format("%.2f", activeAccount.getBalance()));
            showHistory();
        };
        account.add(button("Deposit", 18, 178, 145, 38, blue, () -> transact(amount, true, refresh)));
        account.add(button("Withdraw", 173, 178, 145, 38, blue, () -> transact(amount, false, refresh)));
        account.add(button("Check Balance", 18, 230, 145, 38, blue, () -> {
            JOptionPane.showMessageDialog(this, "Available balance: ₹" + String.format("%.2f", activeAccount.getBalance()));
        }));
        account.add(button("View History", 173, 230, 145, 38, blue, this::showHistory));
        account.add(button("Logout", 18, 305, 300, 38, red, this::showLogin));
        root.add(account);

        JPanel history = card(450, 85, 475, 465);
        history.setBackground(new Color(23, 35, 61));
        history.setBorder(new LineBorder(new Color(55, 80, 110), 1));
        history.add(label("Transaction History", 18, 15, 430, 30, 20, new Color(180, 220, 245), true));
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setBackground(new Color(45, 59, 82));
        area.setForeground(Color.WHITE);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(18, 55, 438, 385);
        history.add(scroll);
        history.putClientProperty("historyArea", area);
        root.add(history);
        showHistory();
        root.revalidate(); root.repaint();
    }

    private void transact(JTextField amountField, boolean deposit, Runnable refresh) {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (deposit) activeAccount.deposit(amount); else activeAccount.withdraw(amount);
            JOptionPane.showMessageDialog(this, deposit ? "Deposit successful." : "Withdrawal successful.");
            amountField.setText("");
            refresh.run();
        } catch (NumberFormatException ex) { error("Enter a valid numeric amount."); }
        catch (BankingException ex) { error(ex.getMessage()); }
    }

    private void showHistory() {
        for (Component c : root.getComponents()) {
            if (c instanceof JPanel p && p.getClientProperty("historyArea") instanceof JTextArea area) {
                StringBuilder text = new StringBuilder();
                for (Transaction t : activeAccount.getTransactions()) text.append(t).append("\n");
                area.setText(text.toString());
                area.setCaretPosition(area.getDocument().getLength());
            }
        }
    }

    private void showAdmin() {
        reset();
        JPanel admin = card(75, 75, 850, 500);
        admin.add(label("Admin Dashboard", 20, 15, 800, 35, 23, ink, true));
        admin.add(label("Registered accounts: " + bank.getAccountCount(), 20, 55, 400, 25, 15, ink, true));
        JTextArea data = new JTextArea();
        data.setEditable(false);
        data.setFont(new Font("Consolas", Font.PLAIN, 13));
        StringBuilder sb = new StringBuilder("ACCOUNT NUMBER       ACCOUNT HOLDER          BALANCE\n");
        sb.append("----------------------------------------------------------\n");
        for (Map.Entry<String, Account> entry : bank.getAccounts().entrySet()) {
            Account a = entry.getValue();
            sb.append(a.getAccountNumber()).append("   ").append(a.getHolderName())
              .append("   ₹").append(String.format("%.2f", a.getBalance())).append("\n");
        }
        data.setText(sb.toString());
        JScrollPane scroll = new JScrollPane(data);
        scroll.setBounds(20, 95, 810, 330);
        admin.add(scroll);
        admin.add(button("Back to Login", 20, 445, 810, 35, red, this::showLogin));
        root.add(admin);
        root.revalidate(); root.repaint();
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Banking Application", JOptionPane.ERROR_MESSAGE);
    }
}
