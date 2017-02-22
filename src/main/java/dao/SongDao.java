
package dao;

import java.util.List;
import models.Song;

public interface SongDao {

	public List<Song> get(int limit, int offset, String filterQuery);

}
