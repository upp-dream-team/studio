package dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import models.Musician;
import models.Record;
import rowmappers.MusicianRowMapper;
import rowmappers.RecordRowMapper;

@Repository
public class RecordDaoImpl implements RecordDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Record> get(int limit, int offset, String filterQuery) {
		System.out.println("in RecordDaoImpl.get()");
		if(filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE LOWER(client) LIKE ? || LOWER(title) LIKE ? LIMIT ? OFFSET ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			List<Record> records = jdbcTemplate.query(SQL, new Object[]{wildcard, wildcard, limit, offset},  new RecordRowMapper());
			return records;
		} else {
			String SQL = "SELECT * FROM record INNER JOIN sellings ON record.selling_id=sellings.id LIMIT ? OFFSET ?";
			List<Record> records = jdbcTemplate.query(SQL, new Object[]{ limit, offset},  new RecordRowMapper());
			return records;
		}
	}

	public Record getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int insert(Record record) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int update(Record record) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumOfRecords(String filterQuery) {
		System.out.println("in RecordDaoImpl.getNumOfRecords()");
		if(filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT COUNT(*) FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE LOWER(client) LIKE ? || LOWER(title) LIKE ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			return jdbcTemplate.queryForObject(SQL, new Object[]{wildcard, wildcard},  Integer.class);
		} else {
			String SQL = "SELECT COUNT(*) FROM record INNER JOIN sellings ON record.selling_id=sellings.id";
			return jdbcTemplate.queryForObject(SQL, Integer.class);
		}
	}

}
