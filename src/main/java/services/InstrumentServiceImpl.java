package services;

import dao.InstrumentDao;
import models.Instrument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstrumentServiceImpl implements InstrumentService {

    @Autowired
    private InstrumentDao instrumentDao;

    public List<Instrument> get(int start, int end, String filterQuery) {
        return instrumentDao.get(end - start, start, filterQuery);
    }

    public List<Instrument> get(int id) {
        return instrumentDao.get(id);
    }

    public void add(Instrument instrument) {
        instrumentDao.add(instrument);

    }

    public int delete(int id) {
        return instrumentDao.delete(id);
    }

    public void update(Instrument s) {
        instrumentDao.update(s);
    }

    public int getNumOfInstruments(String currentFilterQuery) {
        return instrumentDao.getNumOfInstruments(currentFilterQuery);
    }
}
