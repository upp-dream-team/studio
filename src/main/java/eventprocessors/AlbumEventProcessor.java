package eventprocessors;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import models.Album;
import services.AlbumService;

@Component
public class AlbumEventProcessor implements EventProcessor {

	@Autowired
	private AlbumService albumService;
	
	public JPanel process() {
		System.out.println("albumService.getAll();");
		List<Album> allAlbums = albumService.getAll();
		JPanel panel = new JPanel();
		if(allAlbums != null) {
			System.out.println("there are " + allAlbums.size() + " albums");
			for(Album a : allAlbums) {
				panel.add(new JLabel(a.getTitle()));
			}
		}
		else {
			System.out.println("there are no albums");      
			panel.add(new JLabel("There are no albums"));
		}
		return panel;
	}

}
