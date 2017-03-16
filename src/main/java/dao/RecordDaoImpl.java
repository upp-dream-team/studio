package dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import models.Album;
import models.Musician;
import models.Record;
import rowmappers.AlbumRowMapper;
import rowmappers.MusicianRowMapper;
import rowmappers.RecordRowMapper;

@Repository
public class RecordDaoImpl implements RecordDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Record> get(int limit, int offset, String filterQuery, Date dateFrom, Date dateTo) {
		System.out.println("in RecordDaoImpl.get()");
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			List<Record> records = jdbcTemplate.query(SQL, new Object[] { wildcard, dateFrom, dateTo, limit, offset },
					new RecordRowMapper());
			return records;
		} else {
			String SQL = "SELECT * FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE sell_date BETWEEN ? AND ? LIMIT ? OFFSET ?";
			List<Record> records = jdbcTemplate.query(SQL, new Object[] { dateFrom, dateTo, limit, offset }, new RecordRowMapper());
			return records;
		}
	}

	public Record getById(int id) {
		String query = "select * from record INNER JOIN sellings ON record.selling_id=sellings.id where record.id = ?";
		Record record = (Record) jdbcTemplate.queryForObject(query, new Object[] { id }, new RecordRowMapper());
		return record;
	}

	public int insert(Record record) {
		System.out.println("in RecordDaoImpl.insert()");
		String query = "INSERT INTO sellings (client, sell_date, album_id) VALUES (?, ?, ?)";
		jdbcTemplate.update(query,
				new Object[] { record.getClient(), record.getDate(), record.getAlbum().getId() });
		int sellings_id = getLastSellingsId();
		if (sellings_id == -1) {
			return -1;
		}

		query = "INSERT INTO record (quantity, selling_id) VALUES (?, ?)";
		return jdbcTemplate.update(query, new Object[] { record.getQuantity(), sellings_id });
	}

	public int update(Record record) {
		String query = "UPDATE sellings SET client = ?, sell_date = ?, album_id = ? WHERE id = ?";
		jdbcTemplate.update(query, new Object[] { record.getClient(), record.getDate(), record.getAlbum().getId(),
				record.getSellingId() });
		query = "UPDATE record SET quantity = ?, selling_id = ? WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] { record.getQuantity(), record.getSellingId(), record.getId() });
	}

	public int delete(int id) {
		Record r = getById(id);
		String query = "DELETE FROM record WHERE id = ?";
		jdbcTemplate.update(query, new Object[] { id });
		query = "DELETE FROM sellings WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] { r.getSellingId() });
	}

	public int getNumOfRecords(String filterQuery, Date dateFrom, Date dateTo) {
		System.out.println("in RecordDaoImpl.getNumOfRecords()");
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT COUNT(*) FROM record RIGHT JOIN sellings ON record.selling_id=sellings.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[] { wildcard, dateFrom, dateTo }, Integer.class);
		} else {
			String SQL = "SELECT COUNT(*) FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE sell_date BETWEEN ? AND ?";
			return jdbcTemplate.queryForObject(SQL, new Object[] { dateFrom, dateTo }, Integer.class);
		}
	}

	private int getLastSellingsId() {
		System.out.println("in RecordDaoImpl.getLastSellingsId()");
		String SQL = "SELECT MAX(id) FROM sellings";
		return jdbcTemplate.queryForObject(SQL, new Object[] {}, Integer.class);
	}
	
	public Date getOldestDate() {
		System.out.println("in RecordDaoImpl.getOldestRecord()");
		String SQL = "SELECT MIN(sell_date) FROM record INNER JOIN sellings ON record.selling_id=sellings.id";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

	public Date getNewestDate() {
		System.out.println("in RecordDaoImpl.getNewestRecord()");
		String SQL = "SELECT MAX(sell_date) FROM record INNER JOIN sellings ON record.selling_id=sellings.id";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

}
