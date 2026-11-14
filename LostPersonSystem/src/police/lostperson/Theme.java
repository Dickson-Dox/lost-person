package police.lostperson;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

/** Shared colors, fonts, an HTML-wrap helper and a styled-button factory. */
public final class Theme {
    static final Color NAVY = new Color(10, 36, 99);
    static final Color GOLD = new Color(212, 175, 55);
    static final Color RED = new Color(176, 24, 36);
    static final Font PLAIN = new Font("SansSerif", Font.PLAIN, 14);
    static final Font BOLD = new Font("SansSerif", Font.BOLD, 14);

    private Theme() { }

    /** Wraps text in HTML with a fixed width so long values wrap inside a JLabel. */
    static String html(String text, int widthPx) {
        String safe = text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\r", "")
                .replace("\n", "<br>");
        return "<html><body style='width:" + widthPx + "px'>" + safe + "</body></html>";
    }

    /** Creates a flat, colored button that renders the same on every look and feel. */
    static JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setUI(new BasicButtonUI()); // honors custom background colors
        b.setFont(BOLD);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setOpaque(true);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
