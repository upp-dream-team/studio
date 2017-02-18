package dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import models.Album;
import rowmappers.AlbumRowMapper;

@Repository
public class AlbumDaoImpl implements AlbumDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;  
	  
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {  
	    this.jdbcTemplate = jdbcTemplate;  
	}
	
	public List<Album> get() {

		String SQL = "select * from album";
		List <Album> albums = jdbcTemplate.query(SQL, new AlbumRowMapper());
		return albums;
	}
 
}
