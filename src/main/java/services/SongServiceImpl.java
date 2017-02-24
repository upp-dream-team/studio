package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.AlbumDao;
import dao.SongDao;
import models.Song;

@Service
public class SongServiceImpl implements SongService{

	@Autowired
	private SongDao songDao;
	
	@Autowired
	private AlbumDao albumDao;
	
	public List<Song> get(int start, int end, String filterQuery) {
		List<Song> songs= songDao.get(end-start, start, filterQuery);
		for (Song s : songs){
			if (s.getAlbumFk() != null && s.getAlbumFk() != 0)
				s.setAlbum(albumDao.getById(s.getAlbumFk()));
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
