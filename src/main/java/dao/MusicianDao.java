package dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import models.Musician;

public interface MusicianDao {
	
	public List<Musician> get(int limit, int offset, String filterQuery);
	public Musician getById(int id);
	public int insert(Musician musician);
	public int update(Musician musician);
	public int delete(int id);
	public int getNumOfMusicians(String filterQuery);
}
