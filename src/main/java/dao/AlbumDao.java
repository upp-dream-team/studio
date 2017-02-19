package dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import models.Album;

public interface AlbumDao {
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate); 
	public List<Album> get() ;
	public Album getById(int id);
	public int insert(Album album);
	public int update(Album album);
	public boolean delete(Album album);
}
