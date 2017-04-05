package services;

import java.util.ArrayList;
import java.util.List;

import models.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.MusicianDao;
import models.Musician;

@Component
public class MusicianServiceImpl  implements MusicianService{

	@Autowired
	private MusicianDao musicianDao;

	@Autowired
    private InstrumentService instrumentService;
	
	public List<Musician> getMusicians(String filterQuery, int start, int end) {
	    List<Musician> musicians = musicianDao.get(end-start, start, filterQuery);
	    List<Musician> copy = new ArrayList<Musician>();
	    for(Musician musician: musicians) {
            List<Instrument> instruments = instrumentService.getInstrumentsByMusician(musician);
            musician.setInstruments(instruments);
            copy.add(musician);
	    }
		return copy;
	}

	public int getNumOfMusicians(String filterQuery) {
		return musicianDao.getNumOfMusicians(filterQuery);
	}

	public void createMusician(Musician m) {
		musicianDao.insert(m);
	}

	public void updateMusician(Musician musician) {
		musicianDao.update(musician);	
	}

	public void deleteMusician(int id) {
		musicianDao.delete(id);
	}

	public List<String> getMuscianNames() {
		return musicianDao.getMusicianNames();
	}

	public Musician getByName(String name) {
		return musicianDao.getByName(name);
	}

}
