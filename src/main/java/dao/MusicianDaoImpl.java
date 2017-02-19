package dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import models.Musician;
import rowmappers.MusicianRowMapper;

@Repository
public class MusicianDaoImpl  implements MusicianDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Musician> get(int limit, int offset, String filterQuery) {
		if(filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM musician WHERE LOWER(musician.name) LIKE ? || LOWER(phone) LIKE ? LIMIT ? OFFSET ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			List <Musician> musicians = jdbcTemplate.query(SQL, new Object[]{wildcard, wildcard, limit, offset},  new MusicianRowMapper());
			return musicians;
		} else {
			String SQL = "SELECT * FROM musician LIMIT ? OFFSET ?";
			List <Musician> musicians = jdbcTemplate.query(SQL, new Object[]{ limit, offset},  new MusicianRowMapper());
			return musicians;
		}
	}

	public Musician getById(int id) {
		String sql = "SELECT * FROM musician WHERE id = ?";
		Musician musician = (Musician)jdbcTemplate.queryForObject(sql, new Object[] { id }, new MusicianRowMapper());
		return musician;
	}

	public int insert(Musician musician) {
		String query = "INSERT INTO musician (name, phone) VALUES (?, ?)";
		return jdbcTemplate.update(query, new Object[] {musician.getName(), musician.getPhone()});
	}

	public int update(Musician musician) {
		String query = "UPDATE musician SET name = ? , phone = ? WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] {musician.getName(), musician.getPhone(), musician.getId()});
	}

	public int delete(int id) {
		String query = "DELETE FROM musician WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] {id});
	}

	public int getNumOfMusicians(String filterQuery) {
		if(filterQuery != null && !filterQuery.isEmpty()) {
			String query = "SELECT COUNT(*) FROM musician WHERE LOWER(musician.name) LIKE ? || LOWER(phone) LIKE ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			return jdbcTemplate.queryForObject(query, new Object[] {wildcard, wildcard}, Integer.class);
		} else {
			String query = "SELECT COUNT(*) FROM musician";
			return jdbcTemplate.queryForObject(query, Integer.class);
		}
	}

}
