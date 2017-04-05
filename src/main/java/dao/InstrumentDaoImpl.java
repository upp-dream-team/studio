package dao;

import models.Instrument;
import models.Musician;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import rowmappers.InstrumentRowMapper;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InstrumentDaoImpl implements InstrumentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Instrument> get(int limit, int offset, String filterQuery) {
        if (filterQuery != null && !filterQuery.trim().isEmpty()) {
            String SQL = "SELECT * FROM instrument WHERE LOWER(instrument.id) LIKE ? || LOWER(instrument.name) LIKE ? LIMIT ? OFFSET ?";
            String wildcard = "%" + filterQuery.toLowerCase() + "%";
            List<Instrument> instruments = jdbcTemplate.query(SQL, new Object[]{wildcard, wildcard, limit, offset}, new InstrumentRowMapper());
            return instruments;
        } else {
            String query = "SELECT * FROM instrument LIMIT ? OFFSET ?";
            List<Instrument> instruments = jdbcTemplate.query(query, new Object[]{limit, offset}, new InstrumentRowMapper());
            return instruments;
        }
    }

    public Instrument get(int id) {
        String SQL = "SELECT * FROM instrument WHERE instrument.id = ?";
        return jdbcTemplate.queryForObject(SQL, new InstrumentRowMapper(), id);
    }

    public void add(Instrument instrument) {
        String query = "INSERT INTO instrument (name) VALUES  (?)";
        jdbcTemplate.update(query, instrument.getName());
    }

    public int delete(int id) {
        String query = "DELETE FROM instrument WHERE id = ?";
        return jdbcTemplate.update(query, new Object[]{id});
    }

    public int update(Instrument s) {
        String query = "UPDATE instrument SET name = ? WHERE id = ?";
        return jdbcTemplate.update(query, new Object[]{s.getName(), s.getId()});
    }

    public int getNumOfInstruments(String filterQuery) {
        if (filterQuery != null && !filterQuery.trim().isEmpty()) {
            String SQL = "SELECT COUNT(*) FROM instrument WHERE LOWER(name) LIKE ?";
            String wildcard = "%" + filterQuery.toLowerCase() + "%";
            return jdbcTemplate.queryForObject(SQL, new Object[]{wildcard}, Integer.class);
        } else {
            String SQL = "SELECT COUNT(*) FROM instrument";
            return jdbcTemplate.queryForObject(SQL, Integer.class);
        }
    }

    public List<Instrument> getInstrumentsByMusician(Musician musician) {
        String SQL = "SELECT instrument_id  FROM musician_instrument WHERE musician_id = ?";
        List<Integer> list = jdbcTemplate.queryForList(SQL, new Object[]{musician.getId()}, Integer.class);
        List<Instrument> instruments = new ArrayList<Instrument>();
        for(int i: list)
            instruments.add(get(i));
        return instruments;
    }
}
