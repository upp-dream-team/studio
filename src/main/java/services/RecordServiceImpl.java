package services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dao.AlbumDao;
import dao.RecordDao;
import models.Record;

@Component
public class RecordServiceImpl implements RecordService{
	
	@Autowired
	private RecordDao recordDao;
	
	@Autowired
	private AlbumDao albumDao;

	public List<Record> getRecords(String filterQuery, int start, int end) {
		List<Record> records = recordDao.get(end-start, start, filterQuery);
		for (Record r: records){
			r.setAlbum(albumDao.getById(r.getAlbumId()));
		}
		return records;
	}

	public int getNumOfRecords(String filterQuery) {
		return recordDao.getNumOfRecords(filterQuery);
	}

	public void createRecord(Record r) {
		recordDao.insert(r);
	}

	public void updateRecord(Record r) {
		recordDao.update(r);
	}

	public void deleteRecord(int id) {
		recordDao.delete(id);
	}

}
