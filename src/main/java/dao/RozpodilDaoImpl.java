package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import models.Rozpodil;
import rowmappers.RozpodilRowMapper;

import java.util.List;

/**
 * Created by o.khomandiak on 04.04.2017.
 */
@Repository
public class RozpodilDaoImpl implements RozpodilDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    

	@Autowired
	private MusicianDao musicianDao;

    public List<Integer> getMusiciansBySong(int SongId) {
        String SQL = "SELECT musician_id FROM rozpodil WHERE song_id = ? ";
        //List<Integer> ids = jdbcTemplate.query(SQL, SongId);
        return null;
    }
    
    public Double getRozpodilForSong(int songId) {
    	String SQL = "SELECT SUM(gonorar_chastka) FROM rozpodil WHERE song_id = ?";
		Double res = jdbcTemplate.queryForObject(SQL, new Object[] { songId }, Double.class);
		if(res == null)
			return 0.0;
		else
			return res;
    }
    
    public Double getRozpodilForMusicianAndSong(int musicianId, int songId) {
    	String SQL = "SELECT gonorar_chastka FROM rozpodil WHERE musician_id = ? AND song_id = ?";
		Double res = jdbcTemplate.queryForObject(SQL, new Object[] { musicianId, songId }, Double.class);
		if(res == null)
			return 0.0;
		else
			return res;
    }
    
    public List<Rozpodil> getRozpodilsBySongIdIncludingMusician(int songId) {
		String sql = "SELECT * FROM rozpodil WHERE song_id = ?";
		List <Rozpodil> rozpodils = jdbcTemplate.query(sql, new Object[]{ songId},  new RozpodilRowMapper());
		if(rozpodils != null) {
			for(Rozpodil r : rozpodils) {
				r.setMusician(musicianDao.getById(r.getMusicianId())); 
			}
		}
		
		return rozpodils;
	}

	public int insert(Rozpodil rozpodil) {
		String query = "INSERT INTO rozpodil (song_id, musician_id, gonorar_chastka) VALUES (?, ?,?)";
		return jdbcTemplate.update(query, new Object[] {rozpodil.getSongId(), rozpodil.getMusicianId(), rozpodil.getChastka()});
	}

	public int delete(int musicianId, int songId) {
		String query = "DELETE FROM rozpodil WHERE song_id = ? AND musician_id = ?";
		return jdbcTemplate.update(query, new Object[] {songId, musicianId});
	}

	public int update(Rozpodil rozpodil) {
		String query = "UPDATE rozpodil SET gonorar_chastka = ?  WHERE song_id = ? AND musician_id = ?";
		return jdbcTemplate.update(query, new Object[] {rozpodil.getChastka(), rozpodil.getSongId(), rozpodil.getMusicianId()});
	}
}
