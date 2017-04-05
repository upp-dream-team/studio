package services;

import models.Instrument;
import models.Musician;

import java.util.List;

/**
 * Created by xoma0_000 on 05.04.2017.
 */
public interface InstrumentService {
    List<Instrument> get(int limit, int offset, String filterQuery);
    Instrument get(int id);
    void add(Instrument instrument);
    int delete(int id);
    void update(Instrument instrument);
    int getNumOfInstruments(String currentFilterQuery);
    List<Instrument> getInstrumentsByMusician(Musician musician);
}
