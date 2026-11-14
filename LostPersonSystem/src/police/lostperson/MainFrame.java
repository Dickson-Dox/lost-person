package police.lostperson;

import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;

/** Main dashboard: menu bar, scrollable data-entry form and action buttons. */
public class MainFrame extends JFrame {

    private static final String[] GENDERS = {"-- Select --", "Male", "Female", "Other"};

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

    MainFrame() {
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
        bar.setBackground(Theme.NAVY);
        bar.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("Lost Person Information Management - Admin Panel");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        countLabel.setFont(Theme.BOLD);
        countLabel.setForeground(Theme.GOLD);

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

        genderBox.setFont(Theme.PLAIN);
        genderBox.setBackground(Color.WHITE);

        dateSpinner.setFont(Theme.PLAIN);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd MMM yyyy"));
        ((JSpinner.DefaultEditor) dateSpinner.getEditor()).getTextField().setFont(Theme.PLAIN);

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
        t.setForeground(Theme.NAVY);
        JLabel h = new JLabel(hint);
        h.setFont(new Font("SansSerif", Font.ITALIC, 12));
        h.setForeground(Theme.RED);
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
        label.setFont(Theme.BOLD);
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
        f.setFont(Theme.PLAIN);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 195, 205)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    private void styleArea(JTextArea a) {
        a.setFont(Theme.PLAIN);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
    }

    /** Bottom bar with the three action buttons. */
    private JPanel buildButtonBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 12));
        bar.setBackground(new Color(240, 242, 247));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 204, 214)));

        JButton save = Theme.button("Save / Display Information", Theme.NAVY, Color.WHITE);
        JButton clear = Theme.button("Clear Form", new Color(225, 228, 235), Theme.NAVY);
        JButton view = Theme.button("View All Records", Theme.GOLD, Theme.NAVY);

        save.addActionListener(e -> saveRecord());
        clear.addActionListener(e -> clearForm());
        view.addActionListener(e -> new RecordsDialog(this, RecordStore.getAll()).setVisible(true));

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
        Person person = new Person(
                RecordStore.nextId(),
                name, age, (String) genderBox.getSelectedItem(),
                heightField.getText().trim(), location, seen,
                clothingArea.getText().trim(), featuresArea.getText().trim(),
                contactName, phone, notesArea.getText().trim());
        RecordStore.add(person);
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
        countLabel.setText("Saved records: " + RecordStore.count());
    }
}
