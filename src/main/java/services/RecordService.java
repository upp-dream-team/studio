package services;

import java.util.Date;
import java.util.List;

import models.Musician;
import models.Record;
import models.Selling;

public interface RecordService {
	
	public List<Record> getRecords(String filterQuery, int start, int end, Date dateFrom, Date dateTo);
	public int getNumOfRecords(String filterQuery, Date dateFrom, Date dateTo);
	public void createRecord(Record r);
	public void updateRecord(Record r);
	public void deleteRecord(int id);
	public Date getDateOfTheOldestRecord();
	public Date getDateOfTheNewestRecord();
	public Double getTotal(String query, Date dateFrom, Date dateTo);
	public int getNumOfSoldRecordsByAlbumId(int albumId);
}
