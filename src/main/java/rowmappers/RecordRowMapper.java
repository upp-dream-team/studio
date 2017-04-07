package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import models.Record;

public class RecordRowMapper implements RowMapper<Record>{

	public Record mapRow(ResultSet rs, int rowNum) throws SQLException {
		Record r = new Record();
		r.setId(rs.getInt("id"));
		r.setClient(rs.getString("client"));
		r.setDate(rs.getDate("sell_date"));
		r.setQuantity(rs.getInt("quantity"));
		r.setAlbumId(rs.getInt("album_id"));
		r.setSellingId(rs.getInt("selling_id"));
		r.setIncomePerCent(1.0 - (rs.getDouble("gonorar_percent") + rs.getDouble("chief_part")));
		r.setLicense(false);
		return r;
	}

}
