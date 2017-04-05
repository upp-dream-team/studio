package eventprocessorhelpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import eventprocessors.MusicianEventProcessorImpl;

public class SwingUtils {

	public static ImageIcon createImageIcon(String path, String description) {
      java.net.URL imgURL = MusicianEventProcessorImpl.class.getResource(path);
      
      if (imgURL != null) {
         return new ImageIcon(imgURL, description);
      } else {            
         System.err.println("Couldn't find file: " + path);
         return null;
      }
   }
	
	public static JTextField createTextFIeldWithPlaceholder(int cols, final String placeholder){
		final JTextField res = new JTextField(cols);
		res.setText(placeholder);
		res.setForeground(Color.LIGHT_GRAY);
		res.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent e) {
				if (res.getText().equals(placeholder)){
					res.setText("");
					res.setForeground(Color.BLACK);
				}
			}

			public void focusLost(FocusEvent e) {
				if (res.getText() == null || res.getText().isEmpty()){
					res.setText(placeholder);
					res.setForeground(Color.LIGHT_GRAY);
				}
			}
			
		});
		return res;
	}

	public static JLabel createJLabelWithSpecifiedFont(String string, Font font) {
		JLabel res = new JLabel(string);
		res.setFont(font);
		return res;
	}
	
	public static DefaultTableModel  getDefaultTableModel()
	{
		return new DefaultTableModel() {
			@Override
			public Class<?> getColumnClass(int column) {
				if (getRowCount() > 0) {
					Object value = getValueAt(0, column);
					if (value != null) {
						return getValueAt(0, column).getClass();
					}
				}
				return super.getColumnClass(column);
			}
			
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
	}
}
