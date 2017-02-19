package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.MusicianDao;
import models.Musician;

@Component
public class MusicianServiceImpl  implements MusicianService{

	@Autowired
	private MusicianDao musicianDao;
	
	public List<Musician> getMusicians(String filterQuery, int start, int end) {
		return musicianDao.get(end-start, start, filterQuery);
	}

	public int getNumOfMusicians(String filterQuery) {
		return musicianDao.getNumOfMusicians(filterQuery);
	}

	public void createMusician(Musician m) {
		musicianDao.insert(m);
	}

}
