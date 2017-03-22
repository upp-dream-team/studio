package eventprocessors;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface FinancialAffairsEventProcessor {
	
	public JPanel process(Dimension sizeOfParentElement);

}
