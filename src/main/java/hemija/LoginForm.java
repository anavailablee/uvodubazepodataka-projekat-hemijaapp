package hemija;

import javax.swing.*;
import java.sql.*;

public class LoginForm extends JFrame {

    public LoginForm() {
        setTitle("Prijava");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel userLabel = new JLabel("Korisničko ime:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Lozinka:");
        JPasswordField passField = new JPasswordField(15);
        JButton loginButton = new JButton("Prijavi se");
        JButton backButton = new JButton("Nazad");

        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (authenticate(username, password)) {
                JOptionPane.showMessageDialog(this, "Uspešna prijava!");
                new MainMenu(username).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Pogrešno korisničko ime ili lozinka!");
            }
        });

        backButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose();
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(loginButton);
        panel.add(backButton);

        add(panel);
    }

    private boolean authenticate(String username, String password) {
        String query = "SELECT * FROM korisnici WHERE username = ? AND password = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}