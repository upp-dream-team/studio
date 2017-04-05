package dao;

import models.Instrument;
import models.Musician;

import java.util.List;

public interface InstrumentDao {
    List<Instrument> get(int limit, int offset, String filterQuery);
    
    Instrument get(int id);
    
    void add(Instrument instrument);

    int delete(int id);

    int update(Instrument instrument);

    int getNumOfInstruments(String filterQuery);
    
    List<Instrument> getInstrumentsByMusician(Musician musician);

}
