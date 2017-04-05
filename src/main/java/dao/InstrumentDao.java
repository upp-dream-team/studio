package dao;

import models.Instrument;

import java.util.List;

public interface InstrumentDao {
    List<Instrument> get(int limit, int offset, String filterQuery);

    List<Instrument> get(int id);

    void add(Instrument instrument);

    int delete(int id);

    int update(Instrument instrument);

    int getNumOfInstruments(String filterQuery);
}
