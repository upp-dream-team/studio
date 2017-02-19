package services;

import java.util.List;
import models.Song;

public interface SongService {

	public List<Song> get(int limit, int offset, String filterQuery); 
}
