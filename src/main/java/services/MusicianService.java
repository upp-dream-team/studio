package services;

import java.util.List;
import models.Musician;

public interface MusicianService {

	public List<Musician> getMusicians(String filterQuery, int start, int end);
	public int getNumOfMusicians(String filterQuery);
	public void createMusician(Musician m);
	public void updateMusician(Musician musician);
	public void deleteMusician(int id);
	public List<String> getMuscianNames();
	public Musician getByName(String name);
}
