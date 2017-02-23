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
			String query = "SELECT * FROM song LIMIT ? OFFSET ?";
			List <Song> songs = jdbcTemplate.query(query, new Object[]{ limit, offset},  new SongRowMapper());
			return songs;
		}
	}

	public List<Song> get(String song) {
		String SQL = "SELECT * FROM song WHERE LOWER(song.title) LIKE ?";
		return jdbcTemplate.query(SQL, new SongRowMapper(), song);
	}

	public void add(Song song) {
		String query = "INSERT INTO song (author, title, album) VALUES (?, ?, ?)";
		jdbcTemplate.update(query, song.getAuthor(), song.getTitle(), song.getAlbumFk());
	}

	public int delete(int id) {
		String query = "DELETE FROM song WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] {id});
	}

}