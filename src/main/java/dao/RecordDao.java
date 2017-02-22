package dao;

import java.util.List;

import models.Record;

public interface RecordDao {
	
	public List<Record> get(int limit, int offset, String filterQuery);
	public Record getById(int id);
	public int insert(Record record);
	public int update(Record record);
	public int delete(int id);
	public int getNumOfRecords(String filterQuery);

}
