package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dao.AlbumDao;
import models.Album;

@Service
public class AlbumServiceImpl implements AlbumService{

	@Autowired
	private AlbumDao albumDao;

	public List<Album> get(int limit, int offset, String filterQuery) {
		return albumDao.get(limit, offset, filterQuery);
	}

	public int getNumOfAlbums(String filterQuery) {
		return albumDao.getNumOfAlbums(filterQuery);
	}

	public void createAlbum(Album a) {
		albumDao.insert(a);
	}

	public void updateAlbum(Album a) {
		albumDao.update(a);
	}

	public void deleteAlbum(int id) {
		albumDao.delete(id);
	}
	

}
