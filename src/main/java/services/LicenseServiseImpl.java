package services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.AlbumDao;
import dao.LicenseDao;
import models.License;

@Component
public class LicenseServiseImpl implements LicenseService{
	
	@Autowired
	private LicenseDao licenseDao;
	
	@Autowired
	private AlbumDao albumDao;

	public List<License> getLicenses(String filterQuery, int start, int end, Date dateFrom, Date dateTo) {
		List<License> records = licenseDao.get(end-start, start, filterQuery, dateFrom, dateTo);
		for (License r: records){
			r.setAlbum(albumDao.getById(r.getAlbumId()));
		}
		return records;
	}
	
	public int getNumOfLicenses(String filterQuery, Date dateFrom, Date dateTo) {
		return licenseDao.getNumOfLicenses(filterQuery, dateFrom, dateTo);
	}

	public void createLicense(License license) {
		licenseDao.insert(license);
	}

	public void updateLicense(License license) {
		licenseDao.update(license);
	}

	public void deleteLicense(int id) {
		licenseDao.delete(id);
	}

	public Date getDateOfTheOldestLicense() {
		return licenseDao.getOldestDate();
	}

	public Date getDateOfTheNewestLicense() {
		return licenseDao.getNewestDate();
	}

}
