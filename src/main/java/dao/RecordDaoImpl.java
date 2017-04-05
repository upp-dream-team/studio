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
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ? ORDER BY sell_date LIMIT ? OFFSET ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			List<Record> records = jdbcTemplate.query(SQL, new Object[] { wildcard, dateFrom, dateTo, limit, offset },
					new RecordRowMapper());
			return records;
		} else {
			String SQL = "SELECT * FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE sell_date BETWEEN ? AND ? ORDER BY sell_date LIMIT ? OFFSET ?";
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
		String query = "INSERT INTO sellings (client, sell_date, album_id) VALUES (?, ?, ?)";
		jdbcTemplate.update(query,
				new Object[] { record.getClient(), record.getDate(), record.getAlbum().getId() });
		int sellings_id = getLastNotBindedId();
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
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT COUNT(*) FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[] { wildcard, dateFrom, dateTo }, Integer.class);
		} else {
			String SQL = "SELECT COUNT(*) FROM record INNER JOIN sellings ON record.selling_id=sellings.id WHERE sell_date BETWEEN ? AND ?";
			return jdbcTemplate.queryForObject(SQL, new Object[] { dateFrom, dateTo }, Integer.class);
		}
	}
	
	public Date getOldestDate() {
		String SQL = "SELECT MIN(sell_date) FROM record INNER JOIN sellings ON record.selling_id=sellings.id";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

	public Date getNewestDate() {
		String SQL = "SELECT MAX(sell_date) FROM record INNER JOIN sellings ON record.selling_id=sellings.id";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

	public Double getTotal(String filterQuery, Date dateFrom, Date dateTo) {
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT SUM(quantity*price) FROM (record INNER JOIN sellings ON record.selling_id=sellings.id) INNER JOIN album ON sellings.album_id=album.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[] { wildcard, dateFrom, dateTo }, Double.class);
		} else {
			String SQL = "SELECT SUM(quantity*price) FROM (record INNER JOIN sellings ON record.selling_id=sellings.id) INNER JOIN album ON sellings.album_id=album.id WHERE sell_date BETWEEN ? AND ?";
			return jdbcTemplate.queryForObject(SQL, new Object[] { dateFrom, dateTo }, Double.class);
		}
	}
	
	public int getNumOfSoldRecordsByAlbumId(int albumId) {
		String SQL = "SELECT SUM(quantity) FROM record INNER JOIN sellings AS s ON record.selling_id=s.id WHERE s.album_id = ?";
		Integer res = jdbcTemplate.queryForObject(SQL, new Object[] { albumId }, Integer.class);
		if(res == null)
			return 0;
		else
			return res;
	}
	
	private int getLastNotBindedId() {
		String SQL = "SELECT MAX(id) FROM sellings WHERE NOT EXISTS " +
						"(SELECT * FROM record WHERE record.selling_id=sellings.id) AND NOT EXISTS " +
						"(SELECT * FROM license WHERE license.selling_id=sellings.id)";
		return jdbcTemplate.queryForObject(SQL, new Object[] {}, Integer.class);
	}

}
