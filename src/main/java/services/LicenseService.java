package services;

import java.util.Date;
import java.util.List;

import models.License;

public interface LicenseService {
	
	public List<License> getLicenses(String filterQuery, int start, int end, Date dateFrom, Date dateTo);
	public int getNumOfLicenses(String filterQuery, Date dateFrom, Date dateTo);
	public void createLicense(License r);
	public void updateLicense(License r);
	public void deleteLicense(int id);
	public Date getDateOfTheOldestLicense();
	public Date getDateOfTheNewestLicense();
	public Double getTotal(String query, Date dateFrom, Date dateTo);
	public int getNumOfSoldLicensesByAlbumId(int albumId);
}
