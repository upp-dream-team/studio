package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import models.License;

public class LicenseRowMapper implements RowMapper<License>{

	public License mapRow(ResultSet rs, int rowNum) throws SQLException {
		License r = new License();
		r.setId(rs.getInt("id"));
		r.setClient(rs.getString("client"));
		r.setDate(rs.getDate("sell_date"));
		r.setPrice(rs.getFloat("price"));
		r.setPeriod(rs.getInt("period"));
		r.setAlbumId(rs.getInt("album_id")); 
		r.setSellingId(rs.getInt("selling_id"));
		r.setLicense(true);
		return r;
	}

}