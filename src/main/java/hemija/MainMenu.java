package hemija;

import javax.swing.*;

public class MainMenu extends JFrame {

    private String username;

    public MainMenu(String username) {
        this.username = username;
        setTitle("Glavni meni - " + username);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Dobrodošli, " + username + "!");
        JButton laboratorijеButton = new JButton("Pregled laboratorija i istraživača");
        JButton azurirajButton = new JButton("Ažuriranje naloga");
        JButton obrisiButton = new JButton("Brisanje naloga");
        JButton odjavaButton = new JButton("Odjava");

        laboratorijеButton.addActionListener(e -> {
            new LaboratorijeForm(username).setVisible(true);
            dispose();
        });

        azurirajButton.addActionListener(e -> {
            new AzuriranjeNalogaForm(username).setVisible(true);
            dispose();
        });

        obrisiButton.addActionListener(e -> {
            new BrisanjeNalogaForm(username).setVisible(true);
            dispose();
        });

        odjavaButton.addActionListener(e -> {
            new MainFrame().setVisible(true);
            dispose();
        });

        panel.add(label);
        panel.add(laboratorijеButton);
        panel.add(azurirajButton);
        panel.add(obrisiButton);
        panel.add(odjavaButton);

        add(panel);
    }
}