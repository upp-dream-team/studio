
package dao;

import java.util.List;

import models.Musician;
import models.Song;

public interface SongDao {

	public List<Song> get(int limit, int offset, String filterQuery);
	public List<Song> get(String song);
	public void add(Song song);
	public int delete(int id);
	public int update(Song s);
	public int getNumOfSongs(String filterQuery);
	void addMusician(Musician musician, Song song);
    void deleteMusician(Musician musician, Song song);

}
