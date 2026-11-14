package police.lostperson;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Table of every saved record; double-click or use the button to open a poster. */
public class RecordsDialog extends JDialog {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd MMM yyyy");
    private final List<Person> records;

    RecordsDialog(Window owner, List<Person> records) {
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
        for (Person p : records) {
            model.addRow(new Object[]{
                    p.getId(), p.getFullName(), p.getAge(), p.getGender(),
                    p.getLastSeenLocation(), p.getDateLastSeen().format(DATE_FMT),
                    p.getContactName() + " (" + p.getContactPhone() + ")"});
        }

        JTable table = new JTable(model);
        table.setFont(Theme.PLAIN);
        table.setRowHeight(28);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(Theme.BOLD);
        table.getTableHeader().setBackground(Theme.NAVY);
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
        summary.setFont(Theme.BOLD);
        summary.setForeground(Theme.NAVY);
        summary.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        // Buttons
        JButton view = Theme.button("View Poster", Theme.NAVY, Color.WHITE);
        JButton close = Theme.button("Close", new Color(225, 228, 235), Theme.NAVY);
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
