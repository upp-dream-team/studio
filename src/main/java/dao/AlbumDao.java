package dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import models.Album;

public interface AlbumDao {
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate); 
	public List<Album> get() ;
}
