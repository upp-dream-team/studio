package rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import models.Song;

public class SongRowMapper implements RowMapper<Song>{

	public Song mapRow(ResultSet rs, int arg1) throws SQLException {
		System.out.println("In song row mapper");
		Song song = new Song();
		song.setId(rs.getInt("id"));
		song.setTitle(rs.getString("title"));
		song.setAlbumFk(rs.getInt("album"));
		song.setAuthor(rs.getString("author"));
		return song;
	}

}
