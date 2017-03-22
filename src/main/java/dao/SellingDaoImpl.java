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

}
