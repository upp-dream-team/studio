package services;

import java.util.List;

import models.Musician;
import models.Song;

public interface SongService {

	public List<Song> getIncludingRelated(int limit, int offset, String filterQuery); 
	public List<Song> get(int start, int end, String filterQuery);
	public List<Song> get(String song);
	public void add(Song song);
	public int delete(int id);
	public void update(Song s);
	public int getNumOfSongs(String currentFilterQuery);
	void addMusician(String musicianName, Song song);
	void deleteMusician(String musicianName, Song song);
	List<Song> getAllFromAlbumOrWithoutAlbum(int albumId);
}
