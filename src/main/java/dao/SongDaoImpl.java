package dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import models.Song;
import rowmappers.SongRowMapper;

@Repository
public class SongDaoImpl implements SongDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Song> get(int limit, int offset, String filterQuery) {
		if(filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM song WHERE LOWER(song.title) LIKE ? || LOWER(song.author) LIKE ? LIMIT ? OFFSET ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			List <Song> songs = jdbcTemplate.query(SQL, new Object[]{wildcard, wildcard, limit, offset},  new SongRowMapper());
			return songs;
		} else {
			String SQL = "SELECT * FROM song LIMIT ? OFFSET ?";
			List <Song> songs = jdbcTemplate.query(SQL, new Object[]{ limit, offset},  new SongRowMapper());
			return songs;
		}
	}

}
