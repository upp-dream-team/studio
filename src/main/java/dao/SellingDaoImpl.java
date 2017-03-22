package dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SellingDaoImpl implements SellingDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Date getOldestDate() {
		String SQL = "SELECT MIN(sell_date) FROM sellings";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

	public Date getNewestDate() {
		String SQL = "SELECT MAX(sell_date) FROM sellings";
		return jdbcTemplate.queryForObject(SQL, Date.class);
	}

	public Integer getStartForRecords(String filterQuery, int start, Date dateFrom, Date dateTo) {
		Date lastDate = getLastSellDateInBatch(filterQuery, dateFrom, dateTo, start);
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT COUNT(*) FROM record INNER JOIN sellings ON record.selling_id=sellings.id " + 
					"WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ? ORDER BY sell_date";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[]{ wildcard, dateFrom, lastDate }, Integer.class);
		} else {
			String SQL = "SELECT COUNT(*) FROM record INNER JOIN sellings ON record.selling_id=sellings.id " + 
					"WHERE sell_date BETWEEN ? AND ? ORDER BY sell_date";
			return jdbcTemplate.queryForObject(SQL, new Object[]{ dateFrom, lastDate }, Integer.class);
		}
	}

	public Integer getStartForLicenses(String filterQuery, int start, Date dateFrom, Date dateTo) {
		Date lastDate = getLastSellDateInBatch(filterQuery, dateFrom, dateTo, start);
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT COUNT(*) FROM license INNER JOIN sellings ON license.selling_id=sellings.id " + 
					"WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ? ORDER BY sell_date";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[]{ wildcard, dateFrom, lastDate }, Integer.class);
		} else {
			String SQL = "SELECT COUNT(*) FROM license INNER JOIN sellings ON license.selling_id=sellings.id " + 
					"WHERE sell_date BETWEEN ? AND ? ORDER BY sell_date";
			return jdbcTemplate.queryForObject(SQL, new Object[]{ dateFrom, lastDate }, Integer.class);
		}
	}

	public Integer getNumOfSellings(String filterQuery, Date dateFrom, Date dateTo) {
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT COUNT(*) FROM sellings WHERE LOWER(client) LIKE ? AND sell_date BETWEEN ? AND ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[] { wildcard, dateFrom, dateTo }, Integer.class);
		} else {
			String SQL = "SELECT COUNT(*) FROM sellings WHERE sell_date BETWEEN ? AND ?";
			return jdbcTemplate.queryForObject(SQL, new Object[] { dateFrom, dateTo }, Integer.class);
		}
	}
	
	private Date getLastSellDateInBatch(String filterQuery, Date dateFrom, Date dateTo, int batchSize){
		if (filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT sell_date FROM sellings WHERE LOWER(client) LIKE ? " +
				"AND sell_date BETWEEN ? AND ? ORDER BY sell_date LIMIT ? OFFSET ?";
			String wildcard = "%" + filterQuery.toLowerCase() + "%";
			return jdbcTemplate.queryForObject(SQL, new Object[]{ wildcard, dateFrom, dateTo, 1, batchSize-1 }, Date.class);
		} else {
			String SQL = "SELECT sell_date FROM sellings WHERE " +
					"sell_date BETWEEN ? AND ? ORDER BY sell_date LIMIT ? OFFSET ?";
			return jdbcTemplate.queryForObject(SQL, new Object[]{ dateFrom, dateTo, 1, batchSize-1 }, Date.class);
		}
	}

}
