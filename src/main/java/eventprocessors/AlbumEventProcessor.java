package eventprocessors;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface AlbumEventProcessor {

	public JPanel process(Dimension sizeOfParentElement);
	
}
