package eventprocessors;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import models.Record;

public interface RecordEventProcessor {
	
	public JPanel process(Dimension sizeOfParentElement);

	public JButton buildEditButton(Record r);
	public JButton buildDeleteButton(Record r);

}
