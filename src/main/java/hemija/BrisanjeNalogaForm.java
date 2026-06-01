package hemija;

import javax.swing.*;
import java.sql.*;

public class BrisanjeNalogaForm extends JFrame {

    private String username;

    public BrisanjeNalogaForm(String username) {
        this.username = username;
        setTitle("Brisanje naloga");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Unesite lozinku za potvrdu:");
        JPasswordField passField = new JPasswordField(15);
        JButton obrisiButton = new JButton("Obriši nalog");
        JButton backButton = new JButton("Nazad");

        obrisiButton.addActionListener(e -> {
            String lozinka = new String(passField.getPassword());

            if (proveriiObrisiNalog(lozinka)) {
                JOptionPane.showMessageDialog(this, "Nalog uspešno obrisan!");
                new MainFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Pogrešna lozinka!");
            }
        });

        backButton.addActionListener(e -> {
            new MainMenu(username).setVisible(true);
            dispose();
        });

        panel.add(label);
        panel.add(passField);
        panel.add(obrisiButton);
        panel.add(backButton);

        add(panel);
    }

    private boolean proveriiObrisiNalog(String lozinka) {
        String proveraQuery = "SELECT * FROM korisnici WHERE username = ? AND password = ?";
        String brisanjeQuery = "DELETE FROM korisnici WHERE username = ? AND password = ?";

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement proveraStmt = conn.prepareStatement(proveraQuery)) {
            proveraStmt.setString(1, username);
            proveraStmt.setString(2, lozinka);
            ResultSet rs = proveraStmt.executeQuery();

            if (rs.next()) {
                PreparedStatement brisanjeStmt = conn.prepareStatement(brisanjeQuery);
                brisanjeStmt.setString(1, username);
                brisanjeStmt.setString(2, lozinka);
                brisanjeStmt.executeUpdate();
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}