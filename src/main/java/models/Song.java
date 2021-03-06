package models;

import java.util.List;

public class Song {

	private int id;
	private String title;
	private String author;
	private Integer albumFk;
	private Album album;
	private List<Musician> musicians;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Musician> getMusicians() {
		return musicians;
	}
	public void setMusicians(List<Musician> musicians) {
		this.musicians = musicians;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Integer getAlbumFk() {
		return albumFk;
	}
	public void setAlbumFk(Integer albumFk) {
		this.albumFk = albumFk;
	}
	
	@Override 
	 public boolean equals(Object aThat) {
		    if ( this == aThat ) return true;
		    Song that = (Song)aThat;

		    //now a proper field-by-field evaluation can be made
		    return that.getId() == this.id;
		  }
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
}
