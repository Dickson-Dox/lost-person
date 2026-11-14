package police.lostperson;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 * Public-facing "MISSING PERSON" notice (kiosk / website preview style).
 * Read-only, large fonts, dark blue and white. Data comes straight from the
 * Person record the admin just saved.
 */
public class PublicDisplayFrame extends JFrame {

    // Change this to your local emergency number
    private static final String EMERGENCY_NUMBER = "999";

    private static final Color DEEP_NAVY = new Color(6, 24, 70);
    private static final Color LIGHT_BLUE = new Color(170, 195, 235);
    private static final Color MUTED = new Color(90, 105, 140);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter REPORT_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    PublicDisplayFrame(Person p) {
        super("Public Notice - Missing Person - " + p.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // closing never exits the app
        setLayout(new BorderLayout());

        // The poster itself (header / body / footer)
        JPanel poster = new JPanel(new BorderLayout());
        poster.setBackground(Color.WHITE);
        poster.add(buildHeader(p), BorderLayout.NORTH);
        poster.add(buildBody(p), BorderLayout.CENTER);
        poster.add(buildFooter(p), BorderLayout.SOUTH);

        // Scrollable so it still works on small screens
        JScrollPane scroll = new JScrollPane(poster);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);
        add(buildButtonBar(), BorderLayout.SOUTH);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(Math.min(1000, screen.width - 40), Math.min(820, screen.height - 60));
        setLocationRelativeTo(null);
    }

    // ------------------------------------------------------------- header

    /** Police logo placeholder, department name and the big MISSING PERSON title. */
    private JPanel buildHeader(Person p) {
        JPanel header = new JPanel(new BorderLayout());

        // Top bar: logo placeholder + department name
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.NAVY);
        top.setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30));

        JPanel names = new JPanel();
        names.setLayout(new BoxLayout(names, BoxLayout.Y_AXIS));
        names.setOpaque(false);
        names.add(Box.createVerticalGlue());
        names.add(centered("POLICE DEPARTMENT", 28, Font.BOLD, Color.WHITE));
        names.add(centered("MISSING PERSONS UNIT", 15, Font.PLAIN, LIGHT_BLUE));
        names.add(Box.createVerticalGlue());

        JPanel spacer = new JPanel(); // balances the logo so the text stays centered
        spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(84, 96));

        top.add(new ShieldIcon(84, 96), BorderLayout.WEST);
        top.add(names, BorderLayout.CENTER);
        top.add(spacer, BorderLayout.EAST);

        // Title strip
        JPanel strip = new JPanel();
        strip.setLayout(new BoxLayout(strip, BoxLayout.Y_AXIS));
        strip.setBackground(DEEP_NAVY);
        strip.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 5, 0, Theme.GOLD),
                BorderFactory.createEmptyBorder(18, 10, 16, 10)));
        strip.add(centered("MISSING PERSON", 68, Font.BOLD, Color.WHITE));
        strip.add(centered("HAVE YOU SEEN THIS PERSON?  PLEASE HELP US FIND THEM",
                18, Font.BOLD, LIGHT_BLUE));
        strip.add(Box.createVerticalStrut(6));
        strip.add(centered("Case No. " + p.getId() + "   |   Reported "
                + p.getReportedOn().format(REPORT_FMT), 13, Font.PLAIN, LIGHT_BLUE));

        header.add(top, BorderLayout.NORTH);
        header.add(strip, BorderLayout.CENTER);
        return header;
    }

    // --------------------------------------------------------------- body

    /** Large photo placeholder, big name, key stats and the detail rows. */
    private JPanel buildBody(Person p) {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.NORTHWEST;

        // Photo placeholder (left)
        g.gridx = 0;
        g.gridy = 0;
        g.insets = new Insets(0, 0, 0, 35);
        body.add(new PhotoPlaceholder(240, 300), g);

        // Name and key stats (right)
        JPanel identity = new JPanel();
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));
        identity.setOpaque(false);

        JLabel name = new JLabel(Theme.html(p.getFullName(), 520));
        name.setFont(new Font("SansSerif", Font.BOLD, 46));
        name.setForeground(Theme.NAVY);
        name.setAlignmentX(Component.LEFT_ALIGNMENT);
        identity.add(name);
        identity.add(Box.createVerticalStrut(18));
        identity.add(statLine("AGE", p.getAge() + " years old"));
        identity.add(statLine("GENDER", p.getGender()));
        identity.add(statLine("HEIGHT", p.getHeight().isEmpty() ? "Not provided" : p.getHeight()));

        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(0, 0, 0, 0);
        body.add(identity, g);

        // Detail rows (full width, below)
        JPanel details = new JPanel(new GridBagLayout());
        details.setOpaque(false);
        details.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, Theme.NAVY),
                BorderFactory.createEmptyBorder(14, 0, 0, 0)));

        int row = 0;
        addDetail(details, row++, "Last Seen At", p.getLastSeenLocation());
        addDetail(details, row++, "Date Last Seen", p.getDateLastSeen().format(DATE_FMT));
        addDetail(details, row++, "Clothing", p.getClothing());
        addDetail(details, row++, "Distinctive Features", p.getFeatures());
        addDetail(details, row++, "Additional Notes", p.getNotes());

        g.gridx = 0;
        g.gridy = 1;
        g.gridwidth = 2;
        g.insets = new Insets(30, 0, 0, 0);
        body.add(details, g);

        return body;
    }

    /** "AGE   34 years old" style line with a small label and a big value. */
    private JPanel statLine(String label, String value) {
        JPanel line = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 3));
        line.setOpaque(false);
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(MUTED);
        l.setPreferredSize(new Dimension(110, 30));

        JLabel v = new JLabel(value);
        v.setFont(new Font("SansSerif", Font.BOLD, 26));
        v.setForeground(Theme.NAVY);

        line.add(l);
        line.add(v);
        return line;
    }

    /** One "LABEL / value" row; long values wrap automatically. */
    private void addDetail(JPanel panel, int row, String label, String value) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = row;
        g.insets = new Insets(9, 0, 9, 20);
        g.anchor = GridBagConstraints.NORTHWEST;

        JLabel l = new JLabel(label.toUpperCase());
        l.setFont(new Font("SansSerif", Font.BOLD, 15));
        l.setForeground(Theme.NAVY);
        g.gridx = 0;
        g.weightx = 0;
        panel.add(l, g);

        JLabel v = new JLabel(Theme.html(value.isEmpty() ? "Not provided" : value, 620));
        v.setFont(new Font("SansSerif", Font.PLAIN, 20));
        v.setForeground(new Color(30, 30, 40));
        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(v, g);
    }

    // ------------------------------------------------------------- footer

    /** Emergency line and contact person, side by side on dark blue. */
    private JPanel buildFooter(Person p) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Theme.NAVY);

        JPanel columns = new JPanel(new GridLayout(1, 2));
        columns.setOpaque(false);
        columns.setBorder(BorderFactory.createEmptyBorder(22, 20, 10, 20));
        columns.add(contactColumn("POLICE EMERGENCY LINE", "Call immediately", EMERGENCY_NUMBER, false));
        columns.add(contactColumn("CONTACT PERSON", p.getContactName(), p.getContactPhone(), true));

        JLabel note = centered("Any information, however small, could help bring this person home safely.",
                13, Font.PLAIN, LIGHT_BLUE);
        JPanel noteWrap = new JPanel(new BorderLayout());
        noteWrap.setOpaque(false);
        noteWrap.setBorder(BorderFactory.createEmptyBorder(0, 10, 16, 10));
        noteWrap.add(note, BorderLayout.CENTER);

        footer.add(columns, BorderLayout.CENTER);
        footer.add(noteWrap, BorderLayout.SOUTH);
        return footer;
    }

    private JPanel contactColumn(String title, String line, String phone, boolean divider) {
        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setOpaque(false);
        if (divider) {
            col.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, LIGHT_BLUE));
        }
        col.add(centered(title, 14, Font.BOLD, LIGHT_BLUE));
        col.add(Box.createVerticalStrut(6));
        col.add(centered(line, 20, Font.BOLD, Color.WHITE));
        col.add(centered(phone, 36, Font.BOLD, Color.WHITE));
        return col;
    }

    // ------------------------------------------------------------ buttons

    /** Print (placeholder) and Close. */
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        bar.setBackground(new Color(240, 242, 247));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 204, 214)));

        JButton print = Theme.button("Print", Theme.NAVY, Color.WHITE);
        JButton close = Theme.button("Close", new Color(225, 228, 235), Theme.NAVY);

        print.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Printing is not available yet in this demo version.",
                "Print", JOptionPane.INFORMATION_MESSAGE));
        close.addActionListener(e -> dispose());

        bar.add(print);
        bar.add(close);
        return bar;
    }

    /** Centered label helper (used in BoxLayout columns). */
    private JLabel centered(String text, int size, int style, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", style, size));
        l.setForeground(color);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
}
