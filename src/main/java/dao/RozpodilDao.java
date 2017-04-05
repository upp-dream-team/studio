package dao;

import java.util.List;

/**
 * Created by o.khomandiak on 04.04.2017.
 */
public interface RozpodilDao {
    List<Integer> getMusiciansBySong(int songId);
}
