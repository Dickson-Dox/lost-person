package police.lostperson;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Application entry point.
 *
 * Compile:  javac -d out src/police/lostperson/*.java
 * Run:      java -cp out police.lostperson.LostPersonApp
 */
public class LostPersonApp {

    public static void main(String[] args) {
        // Build all Swing windows on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Cross-platform look so custom button colors render the same everywhere
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
                // fall back to the default look and feel
            }
            new LoginFrame().setVisible(true);
        });
    }
}
