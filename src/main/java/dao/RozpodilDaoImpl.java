package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by o.khomandiak on 04.04.2017.
 */
@Repository
public class RozpodilDaoImpl implements RozpodilDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Integer> getMusiciansBySong(int SongId) {
        String SQL = "SELECT musician_id FROM rozpodil WHERE song_id = ? ";
        //List<Integer> ids = jdbcTemplate.query(SQL, SongId);
        return null;
    }
}
