package police.lostperson;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.*;

/**
 * Admin login window: username/password form under a police-themed header.
 * Demo credentials are hard-coded (admin / police123).
 */
public class LoginFrame extends JFrame {

    // Hard-coded demo credentials (replace with a database check in production)
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "police123";

    private final JTextField usernameField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18); // masked input

    public LoginFrame() {
        // Window title
        super("Police Lost Person Management System - Admin Login");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new HeaderPanel(), BorderLayout.NORTH);
        add(buildFormPanel(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setSize(460, 520);
        setResizable(false);          // non-resizable
        setLocationRelativeTo(null);  // centered on screen
    }

    /** Builds the username/password form using GridBagLayout. */
    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 10, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username label + field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Theme.BOLD);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(userLabel, gbc);

        styleField(usernameField);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(usernameField, gbc);

        // Password label + field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Theme.BOLD);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(passLabel, gbc);

        styleField(passwordField);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(passwordField, gbc);

        // Buttons
        JButton loginButton = Theme.button("Login", Theme.NAVY, Color.WHITE);
        JButton clearButton = Theme.button("Clear", new Color(225, 228, 235), Theme.NAVY);

        loginButton.addActionListener(this::handleLogin);
        clearButton.addActionListener(e -> clearFields());

        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        buttons.setOpaque(false);
        buttons.add(loginButton);
        buttons.add(clearButton);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(22, 5, 5, 5);
        panel.add(buttons, gbc);

        // Pressing Enter triggers Login
        getRootPane().setDefaultButton(loginButton);

        return panel;
    }

    /** Applies a consistent look to text inputs. */
    private void styleField(JTextField field) {
        field.setFont(Theme.PLAIN);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 195, 205)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    /** Small footer with a demo hint. */
    private JPanel buildFooter() {
        JPanel footer = new JPanel();
        footer.setBackground(Color.WHITE);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        JLabel note = new JLabel("Authorized personnel only");
        note.setFont(new Font("SansSerif", Font.ITALIC, 12));
        note.setForeground(Color.GRAY);
        footer.add(note);
        return footer;
    }

    /** Validates credentials; opens the dashboard or shows an error. */
    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        Arrays.fill(passwordChars, '\0'); // wipe the char array

        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            dispose(); // close the login window
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocusInWindow();
        }
    }

    /** Clears both inputs and returns focus to the username box. */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocusInWindow();
    }
}
