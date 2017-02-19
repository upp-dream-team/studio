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
	
	public Album getById(int id) {
		/*
		 * String sql = "SELECT * FROM CUSTOMER WHERE CUST_ID = ?";

	Customer customer = (Customer)getJdbcTemplate().queryForObject(
			sql, new Object[] { custId }, new CustomerRowMapper());
		 * */
		
		String query = "select * from album where id = ?";
		//Album album = (Album)jdbcTemplate.queryForObject(query, new Object[] )
		return null;
	}

	public int insert(Album album) {
		String query = "INSERT INTO album (title, record_date, price, gonorar_percent, chief_part, chief) VALUES (?, ?, ?, ?, ?, ?)";
		return jdbcTemplate.update(query, new Object[] {album.getTitle(), album.getRecordDate(), album.getPrice(), album.getRoyaltiesPercentage(), album.getChiefRoyaltiesPercentage(), album.getProducerFk()});
	}

	public int update(Album album) {
		String query = "UPDATE album SET title = ?";
		return 0;
		// jdbcTemplate.update("update mytable set name = ? where id = ?", name, id);
	}

	public boolean delete(Album album) {
		// TODO Auto-generated method stub
		return false;
	}
 
}
