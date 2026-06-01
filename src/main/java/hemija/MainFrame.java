package hemija;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("Hemija Lab");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Dobrodošli u Hemija Lab!");
        JButton loginButton = new JButton("Prijava");
        JButton registerButton = new JButton("Registracija");

        loginButton.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });

        registerButton.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });

        panel.add(label);
        panel.add(loginButton);
        panel.add(registerButton);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}