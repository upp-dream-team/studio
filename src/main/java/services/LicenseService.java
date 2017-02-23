package services;

import java.util.List;

import models.License;

public interface LicenseService {
	
	public List<License> getLicenses(String filterQuery, int start, int end);
	public int getNumOfLicenses(String filterQuery);
	public void createLicense(License r);
	public void updateLicense(License r);
	public void deleteLicense(int id);

}
