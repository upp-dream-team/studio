package eventprocessors;

import java.awt.Dimension;

import javax.swing.JPanel;

public interface InstrumentEventProcessor {

    public JPanel process(Dimension sizeOfParentElement);
}