package services;

import java.util.List;
import models.Song;

public interface SongService {

	public List<Song> get(int limit, int offset, String filterQuery); 
	public List<Song> get(String song);
	public void add(Song song);
	public int delete(int id);
}