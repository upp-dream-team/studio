package services;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.SellingDao;
import models.License;
import models.Record;
import models.Selling;

@Component
public class FinancialAffairsServiceImpl implements FinancialAffairsService {
	
	@Autowired
	private RecordService recordService;
	
	@Autowired
	private LicenseService licenseService;
	
	@Autowired
	private SellingDao sellingDao;

	public List<Selling> getSellings(String filterQuery, int startForRecords, int startForLicenses, int limit, Date dateFrom, Date dateTo) {
		List<Record> records = recordService.getRecords(filterQuery, startForRecords, startForRecords+limit, dateFrom, dateTo);
		List<License> licenses = licenseService.getLicenses(filterQuery, startForLicenses, startForLicenses+limit, dateFrom, dateTo);
		List<Selling> res = new LinkedList<Selling>();
		res.addAll(records);
		res.addAll(licenses);
		Collections.sort(res, new Comparator<Selling>(){
			public int compare(Selling s1, Selling s2) {
		        return s1.getDate().compareTo(s2.getDate());
		    }
		});
		return res;
	}

	public int getNumOfSellings(String filterQuery, Date dateFrom, Date dateTo) {
		return recordService.getNumOfRecords(filterQuery, dateFrom, dateTo) + licenseService.getNumOfLicenses(filterQuery, dateFrom, dateTo);
	}

	public Double getTotal(String query, Date dateFrom, Date dateTo) {
		System.out.println(recordService.getTotal(query, dateFrom, dateTo));
		System.out.println(licenseService.getTotal(query, dateFrom, dateTo));
		return recordService.getTotal(query, dateFrom, dateTo) + licenseService.getTotal(query, dateFrom, dateTo);
	}

	public Date getDateOfTheOldestSelling() {
		return sellingDao.getOldestDate();
	}

	public Date getDateOfTheNewestSelling() {
		return sellingDao.getNewestDate();
	}

	// govnokod
	public List<Selling> getAll(String filterQuery, Date dateFrom, Date dateTo) {
		return getSellings(filterQuery, 0, 0, getNumOfSellings(filterQuery, dateFrom, dateTo), dateFrom, dateTo);
	}

}
