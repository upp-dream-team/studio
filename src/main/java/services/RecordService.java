package services;

import java.util.List;

import models.Musician;
import models.Record;

public interface RecordService {
	
	public List<Record> getRecords(String filterQuery, int start, int end);
	public int getNumOfRecords(String filterQuery);
	public void createRecord(Record r);
	public void updateRecord(Record r);
	public void deleteRecord(int id);

}
