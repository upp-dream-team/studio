package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import models.Instrument;

public class InstrumentRowMapper implements RowMapper<Instrument>{

	public Instrument mapRow(ResultSet rs, int rowNum) throws SQLException {
		System.out.println("in instrument rowmapper");
		Instrument instrument = new Instrument(); 
		instrument.setId(rs.getInt("id"));
		instrument.setName(rs.getString("name"));
		return instrument;
	}

}
