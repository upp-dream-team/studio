package eventprocessors;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;

import models.License;

public interface LicenseEventProcessor {
	
	public JPanel process(Dimension sizeOfParentElement);
	
	JButton buildEditButton(License l);
	JButton buildDeleteButton(License l);

}
