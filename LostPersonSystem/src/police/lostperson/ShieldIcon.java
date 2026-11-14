package police.lostperson;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import javax.swing.JComponent;

/** Custom-painted shield with a star: a placeholder for a real badge image. */
public class ShieldIcon extends JComponent {
    ShieldIcon() {
        this(70, 80);
    }

    ShieldIcon(int width, int height) {
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

        // Shield outline
        Path2D shield = new Path2D.Double();
        shield.moveTo(w * 0.1, h * 0.05);
        shield.lineTo(w * 0.9, h * 0.05);
        shield.lineTo(w * 0.9, h * 0.5);
        shield.curveTo(w * 0.9, h * 0.8, w * 0.5, h * 0.97, w * 0.5, h * 0.97);
        shield.curveTo(w * 0.5, h * 0.97, w * 0.1, h * 0.8, w * 0.1, h * 0.5);
        shield.closePath();

        g2.setColor(Theme.GOLD);
        g2.fill(shield);
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2f));
        g2.draw(shield);

        // Five-pointed star in the middle
        double cx = w / 2.0;
        double cy = h * 0.46;
        double outer = w * 0.28;
        double inner = outer * 0.42;
        Path2D star = new Path2D.Double();
        for (int i = 0; i < 10; i++) {
            double r = (i % 2 == 0) ? outer : inner;
            double angle = Math.toRadians(-90 + i * 36);
            double x = cx + r * Math.cos(angle);
            double y = cy + r * Math.sin(angle);
            if (i == 0) star.moveTo(x, y); else star.lineTo(x, y);
        }
        star.closePath();
        g2.setColor(Theme.NAVY);
        g2.fill(star);

        g2.dispose();
    }
}
