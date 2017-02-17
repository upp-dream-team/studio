package models;

import java.util.Date;
import java.util.List;

public class Album {
	
	private int id;
	private String title;
	private Date recordDate;
	private double price;
	private double royaltiesPercentage;
	private double chiefRoyaltiesPercentage;
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

	public double getRoyaltiesPercentage() {
		return royaltiesPercentage;
	}

	public void setRoyaltiesPercentage(double royaltiesPercentage) {
		this.royaltiesPercentage = royaltiesPercentage;
	}

	public double getChiefRoyaltiesPercentage() {
		return chiefRoyaltiesPercentage;
	}

	public void setChiefRoyaltiesPercentage(double chiefRoyaltiesPercentage) {
		this.chiefRoyaltiesPercentage = chiefRoyaltiesPercentage;
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
}
