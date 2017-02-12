package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.*;

import models.Album;

public class AlbumRowMapper implements RowMapper<Album> {
	
	public Album mapRow(ResultSet rs, int rowNum) throws SQLException {
		System.out.println("in album rowmapper");
		Album album = new Album();
		album.setId(rs.getInt("id"));
		album.setTitle(rs.getString("title"));
		return album;
	}

}
