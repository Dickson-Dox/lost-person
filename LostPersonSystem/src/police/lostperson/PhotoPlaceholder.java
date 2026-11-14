package police.lostperson;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/** Grey silhouette used where a photo would go; scales to any size. */
public class PhotoPlaceholder extends JComponent {

    PhotoPlaceholder() {
        this(150, 180);
    }

    PhotoPlaceholder(int width, int height) {
        Dimension d = new Dimension(width, height);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int arc = Math.max(16, w / 10);

        // Frame and background
        g2.setColor(new Color(230, 232, 238));
        g2.fillRoundRect(0, 0, w - 1, h - 1, arc, arc);
        g2.setColor(Theme.NAVY);
        g2.setStroke(new BasicStroke(Math.max(2f, w / 60f)));
        g2.drawRoundRect(1, 1, w - 3, h - 3, arc, arc);

        // Head and shoulders (proportional to the component size)
        g2.setColor(new Color(175, 180, 192));
        int head = (int) (w * 0.37);
        g2.fillOval((w - head) / 2, (int) (h * 0.16), head, head);
        int shoulders = (int) (w * 0.73);
        g2.fillArc((w - shoulders) / 2, (int) (h * 0.55), shoulders, shoulders, 0, 180);

        // Caption
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("SansSerif", Font.BOLD, Math.max(12, w / 12)));
        String text = "PHOTO";
        int tw = g2.getFontMetrics().stringWidth(text);
        g2.drawString(text, (w - tw) / 2, h - h / 15);

        g2.dispose();
    }
}
