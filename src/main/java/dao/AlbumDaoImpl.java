package dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import models.Album;
import models.Musician;
import rowmappers.AlbumRowMapper;
import rowmappers.MusicianRowMapper;

@Repository
public class AlbumDaoImpl implements AlbumDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	  
	public List<Album> get(int limit, int offset, String filterQuery) {
		if(filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM album WHERE LOWER(musician.title) LIKE ? LIMIT ? OFFSET ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			List <Album> albums = jdbcTemplate.query(SQL, new Object[]{wildcard, limit, offset},  new AlbumRowMapper());
			return albums;
		} else {
			String SQL = "SELECT * FROM album LIMIT ? OFFSET ?";
			List <Album> albums = jdbcTemplate.query(SQL, new Object[]{ limit, offset},  new AlbumRowMapper());
			return albums;
		}
	}

	public Album getById(int id) {
		String query = "select * from album where id = ?";
		Album album = (Album)jdbcTemplate.queryForObject(query, new Object[] { id }, new AlbumRowMapper());
		return album;
	}

	public int insert(Album a) {
		String query = "INSERT INTO album (title, record_date, price, gonorar_percent, chief_part, chief) VALUES (?, ?, ?, ?, ?, ?)";
		int r = jdbcTemplate.update(query, new Object[] {a.getTitle(), a.getRecordDate(), a.getPrice(), a.getMusicianRoyalties(), a.getProducerRoyalties(), a.getProducerFk()});
		// need to set foreign key for the songs;
		return r;
	}

	public int update(Album a) {
		String query = "UPDATE album SET title = ?, record_date = ?, price = ?, gonorar_percent = ?, chief_part = ?, chief = ? WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] {a.getTitle(), a.getRecordDate(), a.getPrice(), a.getMusicianRoyalties(), a.getProducerRoyalties(), a.getProducer()});
	}

	public int delete(int id) {
		String query = "DELETE FROM album WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] {id});
	}

	public int getNumOfAlbums(String filterQuery) {
		if(filterQuery != null && !filterQuery.isEmpty()) {
			String query = "SELECT COUNT(*) FROM album WHERE LOWER(album.title) LIKE ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			return jdbcTemplate.queryForObject(query, new Object[] {wildcard}, Integer.class);
		} else {
			String query = "SELECT COUNT(*) FROM album";
			return jdbcTemplate.queryForObject(query, Integer.class);
		}
	}
}
