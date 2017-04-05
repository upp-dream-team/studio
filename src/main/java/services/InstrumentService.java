package services;

import models.Instrument;

import java.util.List;

/**
 * Created by xoma0_000 on 05.04.2017.
 */
public interface InstrumentService {
    public List<Instrument> get(int limit, int offset, String filterQuery);
    public List<Instrument> get(int id);
    public void add(Instrument instrument);
    public int delete(int id);
    public void update(Instrument instrument);
    public int getNumOfInstruments(String currentFilterQuery);
}
