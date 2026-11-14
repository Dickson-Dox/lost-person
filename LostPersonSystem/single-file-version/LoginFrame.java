import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Police Lost Person Management System - Admin Login.
 *
 * Compile:  javac LoginFrame.java
 * Run:      java LoginFrame
 *
 * Demo credentials: admin / police123
 *
 * Classes in this file:
 *   LoginFrame     - the login window (entry point)
 *   HeaderPanel    - navy banner with a shield logo placeholder
 *   ShieldIcon     - small custom-painted shield component
 *   LostPerson     - data holder for one record
 *   DashboardFrame - menu bar, scrollable entry form, action buttons
 *   PublicDisplayFrame - large public poster window (kiosk / website preview)
 *   PhotoPlaceholder - silhouette drawn where a photo would go
 *   RecordsDialog  - table of all saved records
 *   UI             - shared colors, fonts, HTML helper, button factory
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
        userLabel.setFont(UI.BOLD);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(userLabel, gbc);

        styleField(usernameField);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(usernameField, gbc);

        // Password label + field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UI.BOLD);
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panel.add(passLabel, gbc);

        styleField(passwordField);
        gbc.gridx = 1; gbc.weightx = 1;
        panel.add(passwordField, gbc);

        // Buttons
        JButton loginButton = UI.button("Login", UI.NAVY, Color.WHITE);
        JButton clearButton = UI.button("Clear", new Color(225, 228, 235), UI.NAVY);

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
        field.setFont(UI.PLAIN);
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
            SwingUtilities.invokeLater(() -> new DashboardFrame().setVisible(true));
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

    /** Application entry point. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Cross-platform look so custom button colors render everywhere
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
                // fall back to the default look and feel
            }
            new LoginFrame().setVisible(true);
        });
    }
}

/** Navy banner with the shield logo placeholder and system name. */
class HeaderPanel extends JPanel {
    HeaderPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UI.NAVY);
        setBorder(BorderFactory.createEmptyBorder(22, 20, 22, 20));

        ShieldIcon shield = new ShieldIcon();
        shield.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("POLICE LOST PERSON", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Management System - Admin Login", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(UI.GOLD);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(shield);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(4));
        add(subtitle);
    }
}

/** Custom-painted shield with a star: a placeholder for a real badge image. */
class ShieldIcon extends JComponent {
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

        g2.setColor(UI.GOLD);
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
        g2.setColor(UI.NAVY);
        g2.fill(star);

        g2.dispose();
    }
}

/** Simple data holder for one lost-person record. */
class LostPerson {
    private final String id;
    private final String fullName;
    private final int age;
    private final String gender;
    private final String height;
    private final String lastSeenLocation;
    private final LocalDate dateLastSeen;
    private final String clothing;
    private final String features;
    private final String contactName;
    private final String contactPhone;
    private final String notes;
    private final LocalDateTime reportedOn = LocalDateTime.now();

    LostPerson(String id, String fullName, int age, String gender, String height,
               String lastSeenLocation, LocalDate dateLastSeen, String clothing,
               String features, String contactName, String contactPhone, String notes) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.lastSeenLocation = lastSeenLocation;
        this.dateLastSeen = dateLastSeen;
        this.clothing = clothing;
        this.features = features;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.notes = notes;
    }

    String getId() { return id; }
    String getFullName() { return fullName; }
    int getAge() { return age; }
    String getGender() { return gender; }
    String getHeight() { return height; }
    String getLastSeenLocation() { return lastSeenLocation; }
    LocalDate getDateLastSeen() { return dateLastSeen; }
    String getClothing() { return clothing; }
    String getFeatures() { return features; }
    String getContactName() { return contactName; }
    String getContactPhone() { return contactPhone; }
    String getNotes() { return notes; }
    LocalDateTime getReportedOn() { return reportedOn; }
}

/** Main dashboard: menu bar, scrollable data-entry form and action buttons. */
class DashboardFrame extends JFrame {

    private static final String[] GENDERS = {"-- Select --", "Male", "Female", "Other"};

    // Shared in-memory storage so records survive a logout/login during the same run
    private static final List<LostPerson> RECORDS = new ArrayList<>();
    private static int nextId = 1;

    // Form inputs
    private final JTextField nameField = new JTextField(25);
    private final JTextField ageField = new JTextField(25);
    private final JComboBox<String> genderBox = new JComboBox<>(GENDERS);
    private final JTextField heightField = new JTextField(25);
    private final JTextField locationField = new JTextField(25);
    private final JSpinner dateSpinner =
            new JSpinner(new SpinnerDateModel(new Date(), null, new Date(), Calendar.DAY_OF_MONTH));
    private final JTextArea clothingArea = new JTextArea(3, 25);
    private final JTextArea featuresArea = new JTextArea(3, 25);
    private final JTextField contactNameField = new JTextField(25);
    private final JTextField contactPhoneField = new JTextField(25);
    private final JTextArea notesArea = new JTextArea(4, 25);

    private final JLabel countLabel = new JLabel();

    DashboardFrame() {
        super("Lost Person Information Management - Admin Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(buildMenuBar());
        add(buildTopBar(), BorderLayout.NORTH);
        add(buildFormScroll(), BorderLayout.CENTER);
        add(buildButtonBar(), BorderLayout.SOUTH);

        updateCount();
        setSize(900, 720);
        setMinimumSize(new Dimension(700, 480));
        setLocationRelativeTo(null);
    }

    // ---------------------------------------------------------------- menu

    /** File -> Logout / Exit, Help -> About. */
    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem logout = new JMenuItem("Logout");
        JMenuItem exit = new JMenuItem("Exit");
        logout.addActionListener(e -> logout());
        exit.addActionListener(e -> exitApp());
        file.add(logout);
        file.addSeparator();
        file.add(exit);

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Lost Person Information Management\n"
                        + "Police Admin Panel - demo version 1.0\n\n"
                        + "Records are kept in memory only and are cleared when the program closes.",
                "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(about);

        bar.add(file);
        bar.add(help);
        return bar;
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(this, "Log out of the admin panel?",
                "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        }
    }

    private void exitApp() {
        int choice = JOptionPane.showConfirmDialog(this, "Exit the application?",
                "Exit", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // ------------------------------------------------------------- layout

    /** Navy title bar with a live record counter. */
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(UI.NAVY);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("Lost Person Information Management - Admin Panel");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        countLabel.setFont(UI.BOLD);
        countLabel.setForeground(UI.GOLD);

        bar.add(title, BorderLayout.WEST);
        bar.add(countLabel, BorderLayout.EAST);
        return bar;
    }

    /** The large data-entry form (GridBagLayout) wrapped in a scroll pane. */
    private JScrollPane buildFormScroll() {
        styleField(nameField);
        styleField(ageField);
        styleField(heightField);
        styleField(locationField);
        styleField(contactNameField);
        styleField(contactPhoneField);
        styleArea(clothingArea);
        styleArea(featuresArea);
        styleArea(notesArea);

        genderBox.setFont(UI.PLAIN);
        genderBox.setBackground(Color.WHITE);

        dateSpinner.setFont(UI.PLAIN);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd MMM yyyy"));
        ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setFont(UI.PLAIN);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        int row = 0;
        addHeading(form, row++, "Lost Person Details", "* Required fields");
        addRow(form, row++, "Full Name", nameField, true);
        addRow(form, row++, "Age", ageField, true);
        addRow(form, row++, "Gender", genderBox, true);
        addRow(form, row++, "Height (e.g. 5'8\" or 172 cm)", heightField, false);
        addRow(form, row++, "Last Seen Location", locationField, true);
        addRow(form, row++, "Date Last Seen", dateSpinner, true);
        addRow(form, row++, "Clothing Description", clothingArea, false);
        addRow(form, row++, "Distinctive Features / Marks", featuresArea, false);
        addRow(form, row++, "Contact Person Name", contactNameField, true);
        addRow(form, row++, "Contact Phone", contactPhoneField, true);
        addRow(form, row++, "Additional Notes", notesArea, false);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    /** Section heading spanning both columns. */
    private void addHeading(JPanel form, int row, String title, String hint) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 20));
        t.setForeground(UI.NAVY);
        JLabel h = new JLabel(hint);
        h.setFont(new Font("SansSerif", Font.ITALIC, 12));
        h.setForeground(UI.RED);
        p.add(t, BorderLayout.WEST);
        p.add(h, BorderLayout.EAST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 12, 5);
        form.add(p, gbc);
    }

    /** Adds one "label + input" row; text areas are wrapped in their own scroll pane. */
    private void addRow(JPanel form, int row, String text, JComponent input, boolean required) {
        boolean tall = input instanceof JTextArea;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = row;
        gbc.insets = new Insets(7, 5, 7, 5);

        String labelText = required
                ? "<html>" + text + " <font color='red'>*</font></html>"
                : text;
        JLabel label = new JLabel(labelText);
        label.setFont(UI.BOLD);
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.anchor = tall ? GridBagConstraints.NORTHEAST : GridBagConstraints.EAST;
        form.add(label, gbc);

        JComponent field = input;
        if (tall) {
            JScrollPane sp = new JScrollPane(input);
            sp.setBorder(BorderFactory.createLineBorder(new Color(190, 195, 205)));
            field = sp;
        }
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, gbc);
    }

    private void styleField(JTextField f) {
        f.setFont(UI.PLAIN);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 195, 205)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    private void styleArea(JTextArea a) {
        a.setFont(UI.PLAIN);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
    }

    /** Bottom bar with the three action buttons. */
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bar.setBackground(new Color(240, 242, 247));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 204, 214)));

        JButton save = UI.button("Save / Display Information", UI.NAVY, Color.WHITE);
        JButton clear = UI.button("Clear Form", new Color(225, 228, 235), UI.NAVY);
        JButton view = UI.button("View All Records", UI.GOLD, UI.NAVY);

        save.addActionListener(e -> saveRecord());
        clear.addActionListener(e -> clearForm());
        view.addActionListener(e -> new RecordsDialog(this, RECORDS).setVisible(true));

        bar.add(save);
        bar.add(clear);
        bar.add(view);
        return bar;
    }

    // ------------------------------------------------------------ actions

    /** Validates the form, stores the record, then shows the poster. */
    private void saveRecord() {
        List<String> errors = new ArrayList<>();
        JComponent focus = null;

        // Full name
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            errors.add("Full Name is required.");
            focus = nameField;
        }

        // Age (whole number 0-120)
        String ageText = ageField.getText().trim();
        int age = 0;
        if (ageText.isEmpty()) {
            errors.add("Age is required.");
            if (focus == null) focus = ageField;
        } else {
            try {
                age = Integer.parseInt(ageText);
                if (age < 0 || age > 120) {
                    errors.add("Age must be between 0 and 120.");
                    if (focus == null) focus = ageField;
                }
            } catch (NumberFormatException ex) {
                errors.add("Age must be a whole number.");
                if (focus == null) focus = ageField;
            }
        }

        // Gender
        if (genderBox.getSelectedIndex() == 0) {
            errors.add("Please select a Gender.");
            if (focus == null) focus = genderBox;
        }

        // Last seen location
        String location = locationField.getText().trim();
        if (location.isEmpty()) {
            errors.add("Last Seen Location is required.");
            if (focus == null) focus = locationField;
        }

        // Date last seen (cannot be in the future)
        LocalDate seen = ((Date) dateSpinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        if (seen.isAfter(LocalDate.now())) {
            errors.add("Date Last Seen cannot be in the future.");
            if (focus == null) focus = dateSpinner;
        }

        // Contact person
        String contactName = contactNameField.getText().trim();
        if (contactName.isEmpty()) {
            errors.add("Contact Person Name is required.");
            if (focus == null) focus = contactNameField;
        }

        String phone = contactPhoneField.getText().trim();
        String digits = phone.replaceAll("\\D", "");
        if (phone.isEmpty()) {
            errors.add("Contact Phone is required.");
            if (focus == null) focus = contactPhoneField;
        } else if (!phone.matches("[0-9+()\\-\\s]+") || digits.length() < 7 || digits.length() > 15) {
            errors.add("Contact Phone must contain 7 to 15 digits (only digits, + ( ) - allowed).");
            if (focus == null) focus = contactPhoneField;
        }

        // Show every problem at once
        if (!errors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fix the following:\n\n\u2022 " + String.join("\n\u2022 ", errors),
                    "Missing or Invalid Information", JOptionPane.WARNING_MESSAGE);
            if (focus != null) focus.requestFocusInWindow();
            return;
        }

        // Store the record in memory
        LostPerson person = new LostPerson(
                String.format("LP-%03d", nextId++),
                name, age, (String) genderBox.getSelectedItem(),
                heightField.getText().trim(), location, seen,
                clothingArea.getText().trim(), featuresArea.getText().trim(),
                contactName, phone, notesArea.getText().trim());
        RECORDS.add(person);
        updateCount();

        // Open the public display window, then reset the form for the next entry
        new PublicDisplayFrame(person).setVisible(true);
        clearForm();
    }

    /** Resets every input to its default. */
    private void clearForm() {
        nameField.setText("");
        ageField.setText("");
        genderBox.setSelectedIndex(0);
        heightField.setText("");
        locationField.setText("");
        dateSpinner.setValue(new Date());
        clothingArea.setText("");
        featuresArea.setText("");
        contactNameField.setText("");
        contactPhoneField.setText("");
        notesArea.setText("");
        nameField.requestFocusInWindow();
    }

    private void updateCount() {
        countLabel.setText("Saved records: " + RECORDS.size());
    }
}

/**
 * Public-facing "MISSING PERSON" notice (kiosk / website preview style).
 * Read-only, large fonts, dark blue and white. Data comes straight from the
 * LostPerson record the admin just saved.
 */
class PublicDisplayFrame extends JFrame {

    // Change this to your local emergency number
    private static final String EMERGENCY_NUMBER = "911";

    private static final Color DEEP_NAVY = new Color(6, 24, 70);
    private static final Color LIGHT_BLUE = new Color(170, 195, 235);
    private static final Color MUTED = new Color(90, 105, 140);
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMMM yyyy");
    private static final DateTimeFormatter REPORT_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");

    PublicDisplayFrame(LostPerson p) {
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
    private JPanel buildHeader(LostPerson p) {
        JPanel header = new JPanel(new BorderLayout());

        // Top bar: logo placeholder + department name
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UI.NAVY);
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
                BorderFactory.createMatteBorder(0, 0, 5, 0, UI.GOLD),
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
    private JPanel buildBody(LostPerson p) {
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

        JLabel name = new JLabel(UI.html(p.getFullName(), 520));
        name.setFont(new Font("SansSerif", Font.BOLD, 46));
        name.setForeground(UI.NAVY);
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
                BorderFactory.createMatteBorder(3, 0, 0, 0, UI.NAVY),
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
        v.setForeground(UI.NAVY);

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
        l.setForeground(UI.NAVY);
        g.gridx = 0;
        g.weightx = 0;
        panel.add(l, g);

        JLabel v = new JLabel(UI.html(value.isEmpty() ? "Not provided" : value, 620));
        v.setFont(new Font("SansSerif", Font.PLAIN, 20));
        v.setForeground(new Color(30, 30, 40));
        g.gridx = 1;
        g.weightx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        panel.add(v, g);
    }

    // ------------------------------------------------------------- footer

    /** Emergency line and contact person, side by side on dark blue. */
    private JPanel buildFooter(LostPerson p) {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(UI.NAVY);

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

        JButton print = UI.button("Print", UI.NAVY, Color.WHITE);
        JButton close = UI.button("Close", new Color(225, 228, 235), UI.NAVY);

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

/** Grey silhouette used where a photo would go; scales to any size. */
class PhotoPlaceholder extends JComponent {

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
        g2.setColor(UI.NAVY);
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

/** Table of every saved record; double-click or use the button to open a poster. */
class RecordsDialog extends JDialog {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private final List<LostPerson> records;

    RecordsDialog(Window owner, List<LostPerson> records) {
        super(owner, "All Lost Person Records", Dialog.ModalityType.MODELESS);
        this.records = records;
        setLayout(new BorderLayout());

        // Table model (cells are read-only)
        String[] columns = {"ID", "Full Name", "Age", "Gender", "Last Seen Location", "Date Last Seen", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (LostPerson p : records) {
            model.addRow(new Object[]{
                    p.getId(), p.getFullName(), p.getAge(), p.getGender(),
                    p.getLastSeenLocation(), p.getDateLastSeen().format(DATE_FMT),
                    p.getContactName() + " (" + p.getContactPhone() + ")"});
        }

        JTable table = new JTable(model);
        table.setFont(UI.PLAIN);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(UI.BOLD);
        table.getTableHeader().setBackground(UI.NAVY);
        table.getTableHeader().setForeground(Color.WHITE);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewSelected(table);
                }
            }
        });

        // Summary line on top
        JLabel summary = new JLabel(records.isEmpty()
                ? "No records saved yet."
                : "Total records: " + records.size() + "   (double-click a row to open its poster)");
        summary.setFont(UI.BOLD);
        summary.setForeground(UI.NAVY);
        summary.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // Buttons
        JButton view = UI.button("View Poster", UI.NAVY, Color.WHITE);
        JButton close = UI.button("Close", new Color(225, 228, 235), UI.NAVY);
        view.addActionListener(e -> viewSelected(table));
        close.addActionListener(e -> dispose());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttons.add(view);
        buttons.add(close);

        add(summary, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);

        setSize(860, 420);
        setLocationRelativeTo(owner);
    }

    /** Opens the poster for the selected row (sorting-safe). */
    private void viewSelected(JTable table) {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a record first.",
                    "No Selection", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        new PublicDisplayFrame(records.get(modelRow)).setVisible(true);
    }
}

/** Shared colors, fonts, an HTML-wrap helper and a styled-button factory. */
final class UI {
    static final Color NAVY = new Color(10, 36, 99);
    static final Color GOLD = new Color(212, 175, 55);
    static final Color RED = new Color(176, 24, 36);
    static final Font PLAIN = new Font("SansSerif", Font.PLAIN, 14);
    static final Font BOLD = new Font("SansSerif", Font.BOLD, 14);

    private UI() { }

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
