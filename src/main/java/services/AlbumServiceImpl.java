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
	
	public List<Album> getAll() {
		System.out.println("in album dao");
		return albumDao.get();
	}

}
