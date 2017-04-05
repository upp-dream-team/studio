package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import models.Rozpodil;

public class RozpodilRowMapper implements RowMapper<Rozpodil>{

	public Rozpodil mapRow(ResultSet rs, int rowNum) throws SQLException {
		Rozpodil r = new Rozpodil();
		r.setSongId(rs.getInt("song_id"));
		r.setMusicianId(rs.getInt("musician_id"));
		r.setChastka(rs.getDouble("gonorar_chastka"));
		return r;
	}
}
