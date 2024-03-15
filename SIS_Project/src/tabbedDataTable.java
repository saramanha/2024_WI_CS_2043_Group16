import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

public class tabbedDataTable {
    public static JPanel createTabbedDataTable(Object[][][] data, Object[][] columnNames) {
    	JPanel tabbedDataTablePanel = new JPanel();
    	tabbedDataTablePanel.setLayout(new BorderLayout());
    	tabbedDataTablePanel.setBorder(new EmptyBorder(0, 10, 0, 10)); // Add spacing on the sides of the table

        JTabbedPane tabbedPane = new JTabbedPane();

        for (int i = 0; i < data.length; i++) {
            JTable table = new JTable(new DefaultTableModel(data[i], columnNames[i]));
            table.setDefaultEditor(Object.class, null); // Make the table non-editable
            JScrollPane scrollPane = new JScrollPane(table);
            tabbedPane.addTab("Tab " + (i + 1), scrollPane);
            
            int numRows = Math.min(table.getRowCount(), 10);
            table.setPreferredScrollableViewportSize(new Dimension(table.getPreferredScrollableViewportSize().width, table.getRowHeight() * numRows));
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        }

        tabbedDataTablePanel.add(tabbedPane, BorderLayout.CENTER);
        return tabbedDataTablePanel;
    }
}
