package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.SongDao;
import models.Song;

@Service
public class SongServiceImpl implements SongService{

	@Autowired
	private SongDao songDao;
	
	public List<Song> get(int limit, int offset, String filterQuery) {
		return songDao.get(limit, offset, filterQuery);
	}

}
