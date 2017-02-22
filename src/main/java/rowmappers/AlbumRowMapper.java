package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.*;

import models.Album;

public class AlbumRowMapper implements RowMapper<Album> {
	
	public Album mapRow(ResultSet rs, int rowNum) throws SQLException {
		System.out.println("In album row mapper");
		Album album = new Album();
		album.setId(rs.getInt("id"));
		album.setTitle(rs.getString("title"));
		album.setRecordDate(rs.getDate("record_date"));
		album.setPrice(rs.getDouble("price"));
		album.setMusicianRoyalties(rs.getDouble("gonorar_percent"));
		album.setProducerRoyalties(rs.getDouble("chief_part"));
		album.setProducerFk(rs.getInt("chief"));
		return album;
	}

}
