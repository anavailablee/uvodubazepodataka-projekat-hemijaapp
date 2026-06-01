package hemija;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class LaboratorijeForm extends JFrame {

    public LaboratorijeForm(String username) {
        setTitle("Laboratorije i istraživači");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Laboratorija", "Bezbednosna klasa", "Istraživač", "Stepen obrazovanja", "Specijalizacija"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        String query = "SELECT l.naziv, l.bezbednosna_klasa, " +
                "CONCAT(i.ime, ' ', i.prezime) AS istrazivac, " +
                "i.stepen_obrazovanja, i.oblast_specijalizacije " +
                "FROM Laboratorija l " +
                "JOIN Izvodjenje iz ON l.id = iz.id_laboratorije " +
                "JOIN TimIzvodjenja ti ON iz.id = ti.id_izvodjenja " +
                "JOIN Istrazivac i ON ti.id_istrazivaca = i.id " +
                "GROUP BY l.id, i.id " +
                "ORDER BY l.naziv, i.prezime";

        try (Connection conn = DataBaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("naziv"),
                        rs.getString("bezbednosna_klasa"),
                        rs.getString("istrazivac"),
                        rs.getString("stepen_obrazovanja"),
                        rs.getString("oblast_specijalizacije")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Greška pri učitavanju podataka!");
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton backButton = new JButton("Nazad");
        backButton.addActionListener(e -> {
            new MainMenu(username).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        add(scrollPane, "Center");
        add(buttonPanel, "South");
    }
}