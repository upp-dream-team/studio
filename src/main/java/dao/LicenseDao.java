package dao;

import java.util.List;

import models.License;

public interface LicenseDao {
	
	public List<License> get(int limit, int offset, String filterQuery);
	public License getById(int id);
	public int insert(License license);
	public int update(License license);
	public int delete(int id);
	public int getNumOfLicenses(String filterQuery);

}
