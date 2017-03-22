package services;

import java.util.Date;
import java.util.List;

import models.Selling;

public interface FinancialAffairsService {
	
	public List<Selling> getSellings(String filterQuery, int start, int limit, Date dateFrom, Date dateTo);
	public int getNumOfSellings(String filterQuery, Date dateFrom, Date dateTo);
	public Double getTotal(String query, Date dateFrom, Date dateTo);
	public Date getDateOfTheOldestSelling();
	public Date getDateOfTheNewestSelling();

}
