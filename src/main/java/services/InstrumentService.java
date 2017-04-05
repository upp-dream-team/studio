package services;

import models.Instrument;
import models.Musician;

import java.util.List;

public interface InstrumentService {
    List<Instrument> getInstrumentsByMusician(Musician musician);
    Instrument get(int id);
    List<Instrument> get(int limit, int offset, String filterQuery);
    void add(Instrument instrument);
    int delete(int id);
    void update(Instrument instrument);
    int getNumOfInstruments(String currentFilterQuery);
}
