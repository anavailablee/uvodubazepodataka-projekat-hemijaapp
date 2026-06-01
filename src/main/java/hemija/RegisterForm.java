package hemija;

import javax.swing.*;
import java.sql.*;

public class RegisterForm extends JFrame {

    public RegisterForm() {
        setTitle("Registracija");
        setSize(350, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel userLabel = new JLabel("Korisničko ime:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Lozinka:");
        JPasswordField passField = new JPasswordField(15);
        JButton registerButton = new JButton("Registruj se");
        JButton backButton = new JButton("Nazad");

        registerButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite korisničko ime i lozinku!");
                return;
            }

            if (register(username, password)) {
                JOptionPane.showMessageDialog(this, "Uspešna registracija!");
                new LoginForm().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Greška pri registraciji!");
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
        panel.add(registerButton);
        panel.add(backButton);

        add(panel);
    }

    private boolean register(String username, String password) {
        String query = "INSERT INTO korisnici (username, password) VALUES (?, ?)";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}