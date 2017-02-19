package eventprocessorhelpers;

import javax.swing.ImageIcon;

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
}
