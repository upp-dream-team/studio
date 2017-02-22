package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import models.Musician;

public class MusicianRowMapper implements RowMapper<Musician>{

	public Musician mapRow(ResultSet rs, int rowNum) throws SQLException {
		Musician musician = new Musician();
		musician.setId(rs.getInt("id"));
		musician.setName(rs.getString("name"));
		musician.setPhone(rs.getString("phone"));
		return musician;
	}

}
