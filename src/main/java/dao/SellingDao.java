package dao;

import java.util.Date;

public interface SellingDao {

	public Date getOldestDate();
	public Date getNewestDate();
	public Integer getStartForRecords(String filterQuery, int start, Date dateFrom, Date dateTo);
	public Integer getStartForLicenses(String filterQuery, int start, Date dateFrom, Date dateTo);
	public Integer getNumOfSellings(String filterQuery, Date dateFrom, Date dateTo);

}
