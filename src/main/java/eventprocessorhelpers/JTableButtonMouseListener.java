package eventprocessorhelpers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JTable;

public class JTableButtonMouseListener extends MouseAdapter {
    private final JTable table;
    private final int rowHeight;

    public JTableButtonMouseListener(JTable table, int rowHeight) {
        this.table = table;
        this.rowHeight = rowHeight;
    }

    public void mouseClicked(MouseEvent e) {
        int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
        int row    = e.getY()/rowHeight;

                /*Checking the row or column is valid or not*/
        if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
            Object value = table.getValueAt(row, column);
            if (value instanceof JButton) {
                /*perform a click event*/
                ((JButton)value).doClick();
            }
        }
    }
}

