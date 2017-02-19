package dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import models.Album;
import models.Musician;

public interface AlbumDao {
	
	public List<Album> get(int limit, int offset, String filterQuery);
	public Album getById(int id);
	public int insert(Album album);
	public int update(Album album);
	public int delete(int id);
	public int getNumOfAlbums(String filterQuery);
}
