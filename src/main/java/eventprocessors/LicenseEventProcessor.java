package eventprocessors;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface LicenseEventProcessor {
	
	public JPanel process(Dimension sizeOfParentElement);

}
