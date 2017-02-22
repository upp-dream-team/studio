package models;

import java.util.Date;
import java.util.List;

public class Album {
	
	private int id;
	private String title;
	private Date recordDate;
	private double price;
	private double musicianRoyalties;
	private double producerRoyalties;
	private int producerFk;
	
	private Musician producer;
	private List<Song> songs;
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getProducerFk() {
		return producerFk;
	}

	public void setProducerFk(int producerFk) {
		this.producerFk = producerFk;
	}

	public Musician getProducer() {
		return producer;
	}

	public void setProducer(Musician producer) {
		this.producer = producer;
	}

	public double getMusicianRoyalties() {
		return musicianRoyalties;
	}

	public void setMusicianRoyalties(double musicianRoyalties) {
		this.musicianRoyalties = musicianRoyalties;
	}

	public double getProducerRoyalties() {
		return producerRoyalties;
	}

	public void setProducerRoyalties(double producerRoyalties) {
		this.producerRoyalties = producerRoyalties;
	}
	
	 @Override 
	 public boolean equals(Object aThat) {
	    if ( this == aThat ) return true;
	    Album that = (Album)aThat;

	    //now a proper field-by-field evaluation can be made
	    return that.getId() == this.id;
	 }
}
