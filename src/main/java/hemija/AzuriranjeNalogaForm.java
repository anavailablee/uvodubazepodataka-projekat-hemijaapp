package hemija;

import javax.swing.*;
import java.sql.*;

public class AzuriranjeNalogaForm extends JFrame {

    private String trenutniUsername;

    public AzuriranjeNalogaForm(String username) {
        this.trenutniUsername = username;
        setTitle("Ažuriranje naloga");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel userLabel = new JLabel("Novo korisničko ime:");
        JTextField userField = new JTextField(15);
        JLabel passLabel = new JLabel("Nova lozinka:");
        JPasswordField passField = new JPasswordField(15);
        JButton azurirajButton = new JButton("Ažuriraj");
        JButton backButton = new JButton("Nazad");

        azurirajButton.addActionListener(e -> {
            String noviUsername = userField.getText();
            String novaLozinka = new String(passField.getPassword());

            if (noviUsername.isEmpty() || novaLozinka.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Unesite novo korisničko ime i lozinku!");
                return;
            }

            if (azurirajNalog(noviUsername, novaLozinka)) {
                JOptionPane.showMessageDialog(this, "Nalog uspešno ažuriran!");
                new LoginForm().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Greška pri ažuriranju!");
            }
        });

        backButton.addActionListener(e -> {
            new MainMenu(trenutniUsername).setVisible(true);
            dispose();
        });

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(azurirajButton);
        panel.add(backButton);

        add(panel);
    }

    private boolean azurirajNalog(String noviUsername, String novaLozinka) {
        String query = "UPDATE korisnici SET username = ?, password = ? WHERE username = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, noviUsername);
            stmt.setString(2, novaLozinka);
            stmt.setString(3, trenutniUsername);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}