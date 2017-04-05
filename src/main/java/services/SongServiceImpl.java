package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dao.MusicianDao;
import dao.RozpodilDao;
import models.Musician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.AlbumDao;
import dao.MusicianDao;
import dao.SongDao;
import models.Song;

@Service
public class SongServiceImpl implements SongService{

	@Autowired
	private SongDao songDao;
	@Autowired
	private AlbumDao albumDao;
	@Autowired
	private MusicianDao musicianDao;
	@Autowired
	private RozpodilDao rozpodilDao;
	
	public List<Song> get(int start, int end, String filterQuery) {
		List<Song> songs= songDao.get(end-start, start, filterQuery);
		for (Song s : songs){
			if (s.getAlbumFk() != null && s.getAlbumFk() != 0)
				s.setAlbum(albumDao.getById(s.getAlbumFk()));
			List<Musician> musicians = new ArrayList<Musician>();
			for(int m : rozpodilDao.getMusiciansBySong(s.getId()))
			    musicians.add(musicianDao.getById(m));
			s.setMusicians(musicians);
		}
		return songs;
	}

	public List<Song> get(String song) {
		return songDao.get(song);
	}

	public void add(Song song) {
		songDao.add(song);
		
	}

	public int delete(int id) {
		return songDao.delete(id);
	}

	public void update(Song s) {
		songDao.update(s);
	}

	public int getNumOfSongs(String currentFilterQuery) {
		return songDao.getNumOfSongs(currentFilterQuery);
	}

    public void addMusician(String musicianName, Song song) {
        songDao.addMusician(musicianDao.getByName(musicianName), song);
    }

    public void deleteMusician(String musicianName, Song song) {
        songDao.deleteMusician(musicianDao.getByName(musicianName), song);
    }

}
