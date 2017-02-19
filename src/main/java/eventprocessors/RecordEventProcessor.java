package eventprocessors;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface RecordEventProcessor {
	
	public JPanel process(Dimension sizeOfParentElement);

}
