package dao;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import models.Album;
import models.Musician;
import models.Song;
import rowmappers.AlbumRowMapper;
import rowmappers.MusicianRowMapper;
import rowmappers.SongRowMapper;

@Repository
public class AlbumDaoImpl implements AlbumDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	  
	public List<Album> get(int limit, int offset, String filterQuery) {
		List <Album> albums = new ArrayList<Album>();
		
		if(filterQuery != null && !filterQuery.trim().isEmpty()) {
			String SQL = "SELECT * FROM album WHERE LOWER(album.title) LIKE ? LIMIT ? OFFSET ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			albums = jdbcTemplate.query(SQL, new Object[]{wildcard, limit, offset},  new AlbumRowMapper());
		} else {
			String SQL = "SELECT * FROM album LIMIT ? OFFSET ?";
			albums = jdbcTemplate.query(SQL, new Object[]{ limit, offset},  new AlbumRowMapper());
		}
		
		for(int i = 0; i < albums.size(); ++i) {
			String songsQuery = "SELECT * FROM song WHERE album = ?";
			List<Song> songs = jdbcTemplate.query(songsQuery, new Object[]{ albums.get(i).getId()}, new SongRowMapper());
			System.out.println("there are "+songs.size()+" in album "+albums.get(i).getId());
			albums.get(i).setSongs(songs);
		}
		return albums;
	}

	public Album getById(int id) {
		String query = "select * from album where id = ?";
		Album album = (Album)jdbcTemplate.queryForObject(query, new Object[] { id }, new AlbumRowMapper());
		
		String songsQuery = "SELECT * FROM song WHERE album = ?";
		album.setSongs(jdbcTemplate.query(songsQuery, new Object[]{ id}, new SongRowMapper()));
		
		return album;
	}

	public int insert(Album a) {
		String query = "INSERT INTO album (title, record_date, price, gonorar_percent, chief_part, chief) VALUES (?, ?, ?, ?, ?, ?)";
		int r = jdbcTemplate.update(query, new Object[] {a.getTitle(), a.getRecordDate(), a.getPrice(), a.getMusicianRoyalties(), a.getProducerRoyalties(), a.getProducerFk()});
		
		String query2 = "SELECT * from album ORDER BY id DESC LIMIT 1";
		Album album = (Album)jdbcTemplate.queryForObject(query2,  new AlbumRowMapper());
		
		String updateSongQuery = "UPDATE song SET album = ? WHERE id = ?";
		
		if(a.getSongs() != null) {
			for(Song s : a.getSongs()) {
				jdbcTemplate.update(updateSongQuery, new Object[] {album.getId(), s.getId()});
			}
		}
		
		return r;
	}

	public int update(Album a) {
		String query = "UPDATE album SET title = ?, record_date = ?, price = ?, gonorar_percent = ?, chief_part = ?, chief = ? WHERE id = ?";
		
		String queryForOldSongIds = "SELECT id FROM song WHERE album = ?";
		List <Integer> oldSongIds = jdbcTemplate.queryForList(queryForOldSongIds, new Object[]{ a.getId()}, Integer.class);
		List<Integer> currentSongIds = new ArrayList<Integer>();
		if(a.getSongs() != null) {
			for(Song s : a.getSongs()) {
				currentSongIds.add(s.getId());
			}
		}
		List<Integer> currentSongIds2 = currentSongIds;
		currentSongIds.removeAll(oldSongIds);
		List<Integer> toAdd = currentSongIds;
		oldSongIds.removeAll(currentSongIds2);
		List<Integer> toDelete = oldSongIds;
		
		String updateSongQuery = "UPDATE song SET album = ? WHERE id = ?";
		if(toAdd != null) {
			for(int sId : toAdd) {
				jdbcTemplate.update(updateSongQuery, new Object[] {a.getId(), sId});
			}
		}
		
		if(toDelete != null) {
			for(int sId : toDelete) {
				jdbcTemplate.update(updateSongQuery, new Object[] {null, sId});
			}
		}
		
		return jdbcTemplate.update(query, new Object[] {a.getTitle(), a.getRecordDate(), a.getPrice(), a.getMusicianRoyalties(), a.getProducerRoyalties(), a.getProducer()});
	}

	public int delete(int id) {
		String query = "DELETE FROM album WHERE id = ?";
		return jdbcTemplate.update(query, new Object[] {id});
	}

	public int getNumOfAlbums(String filterQuery) {
		if(filterQuery != null && !filterQuery.isEmpty()) {
			String query = "SELECT COUNT(*) FROM album WHERE LOWER(album.title) LIKE ?";
			String wildcard = "%"+filterQuery.toLowerCase()+"%";
			return jdbcTemplate.queryForObject(query, new Object[] {wildcard}, Integer.class);
		} else {
			String query = "SELECT COUNT(*) FROM album";
			return jdbcTemplate.queryForObject(query, Integer.class);
		}
	}

	public List<String> getAlbumTitles() {
		String query = "SELECT title FROM album";
		return jdbcTemplate.queryForList(query, String.class);
	}
}
