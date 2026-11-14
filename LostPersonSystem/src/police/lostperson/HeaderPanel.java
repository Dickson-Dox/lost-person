package police.lostperson;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.*;

/** Navy banner with the shield logo placeholder and system name. */
public class HeaderPanel extends JPanel {
    HeaderPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.NAVY);
        setBorder(BorderFactory.createEmptyBorder(22, 20, 22, 20));

        ShieldIcon shield = new ShieldIcon();
        shield.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("POLICE LOST PERSON", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Management System - Admin Login", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(Theme.GOLD);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(shield);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(4));
        add(subtitle);
    }
}
