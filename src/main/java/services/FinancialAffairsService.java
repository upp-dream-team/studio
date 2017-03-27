package services;

import java.util.Date;
import java.util.List;

import models.License;
import models.Record;
import models.Selling;

public interface FinancialAffairsService {
	
	public List<Selling> getSellings(String filterQuery, int start, int limit, Date dateFrom, Date dateTo);
	public List<Record> getRecords(String filterQuery, int start, int end, Date dateFrom, Date dateTo);
	public List<License> getLicenses(String filterQuery, int start, int end, Date dateFrom, Date dateTo);
	
	public int getNumOfSellings(String filterQuery, Date dateFrom, Date dateTo);
	public int getNumOfRecords(String currentFilterQuery, Date dateFrom, Date dateTo);
	public int getNumOfLicenses(String currentFilterQuery, Date dateFrom, Date dateTo);
	
	public Double getTotal(String query, Date dateFrom, Date dateTo);
	public Double getTotalForRecords(String query, Date dateFrom, Date dateTo);
	public Double getTotalForLicenses(String query, Date dateFrom, Date dateTo);
	
	public Date getDateOfTheOldestSelling();
	public Date getDateOfTheNewestSelling();
	
	public void createRecord(Record r);
	public void updateRecord(Record r);
	public void deleteRecord(int id);
	
	public void createLicense(License l);
	public void updateLicense(License l);
	public void deleteLicense(int id);
	
}
