package services;

import java.util.ArrayList;
import java.util.List;

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
	
	public List<Song> get(int limit, int offset, String filterQuery) {
		List<Song> songs = songDao.get(limit, offset, filterQuery);
		if(songs != null) {
			for(Song s : songs) {
				if(s.getAlbumFk() != null)
					s.setAlbum(albumDao.getById(s.getAlbumFk()));
			}
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

}
