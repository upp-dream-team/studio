package dao;

import java.util.List;
import models.Rozpodil;

/**
 * Created by o.khomandiak on 04.04.2017.
 */
public interface RozpodilDao {
    List<Integer> getMusiciansBySong(int SongId);
    public Double getRozpodilForSong(int songId);
    public List<Rozpodil> getRozpodilsBySongIdIncludingMusician(int songId) ;
    public Double getRozpodilForMusicianAndSong(int musicianId, int songId);
    public int insert(Rozpodil rozpodil);
    public int update(Rozpodil rozpodil);
	public int delete(int musicianId, int songId);
}
