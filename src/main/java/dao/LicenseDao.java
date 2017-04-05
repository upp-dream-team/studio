package dao;

import java.util.Date;
import java.util.List;

import models.License;

public interface LicenseDao {
	
	public List<License> get(int limit, int offset, String filterQuery, Date dateFrom, Date dateTo);
	public License getById(int id);
	public int insert(License license);
	public int update(License license);
	public int delete(int id);
	public int getNumOfLicenses(String filterQuery, Date dateFrom, Date dateTo);
	public Date getOldestDate();
	public Date getNewestDate();
	public Double getTotal(String query, Date dateFrom, Date dateTo);

	public int getNumOfSoldLicensesByAlbumId(int albumId);
}
