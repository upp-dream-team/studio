package dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import models.License;
import rowmappers.LicenseRowMapper;

@Repository
public class LicenseDaoImpl implements LicenseDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<License> get(int limit, int offset, String filterQuery, Date dateFrom, Date dateTo) {
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM license INNER JOIN sellings ON license.selling_id=sellings.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ? ORDER BY sell_date LIMIT ? OFFSET ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			List<License> records = jdbcTemplate.query(SQL, new Object[] { wildcard, dateFrom, dateTo, limit, offset },
					new LicenseRowMapper());
			return records;
		} else {
			String SQL = "SELECT * FROM license INNER JOIN sellings ON license.selling_id=sellings.id WHERE sell_date BETWEEN ? AND ? ORDER BY sell_date LIMIT ? OFFSET ?";
			List<License> records = jdbcTemplate.query(SQL, new Object[] { dateFrom, dateTo, limit, offset }, new LicenseRowMapper());
			return records;
		}
	}

	public License getById(int id) {
		String query = "select * from license INNER JOIN sellings ON license.selling_id=sellings.id where license.id = ?";
		License record = (License) jdbcTemplate.queryForObject(query, new Object[] { id }, new LicenseRowMapper());
		return record;
	}

	public int insert(License license) {
		String query = "INSERT INTO sellings (client, sell_date, album_id) VALUES (?, ?, ?)";
		jdbcTemplate.update(query,
				new Object[] { license.getClient(), license.getDate(), license.getAlbum().getId() });
		int sellings_id = getLastNotBindedId();
		if (sellings_id == -1) {
			return -1;
		}

		query = "INSERT INTO license (price, period, selling_id) VALUES (?, ?, ?)";
		return jdbcTemplate.update(query, new Object[] { license.getPrice(), license.getPeriod(), sellings_id });
	}

	public int update(License license) {
		String query = "UPDATE sellings SET client = ?, sell_date = ?, album_id = ? WHERE id = ?";
		jdbcTemplate.update(query, new Object[] { license.getClient(), license.getDate(), license.getAlbum().getId(),
				license.getSellingId() });
		query = "UPDATE license SET price = ?, period = ?, selling_id = ? WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] {license.getPrice(), license.getPeriod(),  license.getSellingId(), license.getId() });
	}

	public int delete(int id) {
		License license = getById(id);
		String query = "DELETE FROM license WHERE id = ?";
		jdbcTemplate.update(query, new Object[] { id });
		query = "DELETE FROM sellings WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] { license.getSellingId() });
	}
	
	public int getNumOfLicenses(String filterQuery, Date dateFrom, Date dateTo) {
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT COUNT(*) FROM license RIGHT JOIN sellings ON license.selling_id=sellings.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[] { wildcard, dateFrom, dateTo }, Integer.class);
		} else {
			String SQL = "SELECT COUNT(*) FROM license INNER JOIN sellings ON license.selling_id=sellings.id WHERE sell_date BETWEEN ? AND ?";
			return jdbcTemplate.queryForObject(SQL, new Object[] { dateFrom, dateTo }, Integer.class);
		}
	}

	public Date getOldestDate() {
		String SQL = "SELECT MIN(sell_date) FROM license INNER JOIN sellings ON license.selling_id=sellings.id";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

	public Date getNewestDate() {
		String SQL = "SELECT MAX(sell_date) FROM license INNER JOIN sellings ON license.selling_id=sellings.id";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

	public Double getTotal(String filterQuery, Date dateFrom, Date dateTo) {
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT SUM(period*price) FROM license INNER JOIN sellings ON license.selling_id=sellings.id WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[] { wildcard, dateFrom, dateTo }, Double.class);
		} else {
			String SQL = "SELECT SUM(period*price) FROM license INNER JOIN sellings ON license.selling_id=sellings.id WHERE sell_date BETWEEN ? AND ?";
			return jdbcTemplate.queryForObject(SQL, new Object[] { dateFrom, dateTo }, Double.class);
		}
	}
	
	public int getNumOfSoldLicensesByAlbumId(int albumId) {
		String SQL = "SELECT COUNT(*) FROM license INNER JOIN sellings AS s ON selling_id = s.id WHERE s.album_id = ?";
		Integer res =  jdbcTemplate.queryForObject(SQL, new Object[] { albumId }, Integer.class);
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
