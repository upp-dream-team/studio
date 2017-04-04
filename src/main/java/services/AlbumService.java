package services;

import java.util.List;
import models.Album;
import models.Musician;

public interface AlbumService {
	
	public List<Album> get(int limit, int offset, String filterQuery);
	public List<String> getAlbumTitles();
	public int getNumOfAlbums(String filterQuery);
	public void createAlbum(Album a);
	public void updateAlbum(Album a);
	public void deleteAlbum(int id);
	public Album getAlbum(String title);
}
