package models;

import java.util.Date;

public abstract class Selling {
	
	private int id;

	protected String client;
	protected Date date;
	protected int albumId;
	protected Album album;
	protected int sellingId;
	
	protected double incomePerCent;

	protected boolean isLicense;
	
	// for records
	public abstract int getQuantity();
	
	// for licenses
	public abstract float getPrice();
	public abstract int getPeriod();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClient() {
		return client;
	}
	
	public void setClient(String client) {
		this.client = client;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getAlbumId() {
		return albumId;
	}
	
	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}
	
	public Album getAlbum() {
		return album;
	}
	
	public void setAlbum(Album album) {
		this.album = album;
	}
	
	public int getSellingId() {
		return sellingId;
	}
	
	public void setSellingId(int sellingId) {
		this.sellingId = sellingId;
	}
	
	public double getIncomePerCent() {
		return incomePerCent;
	}

	public void setIncomePerCent(double incomePerCent) {
		this.incomePerCent = incomePerCent;
	}
	
	public boolean isLicense() {
		return isLicense;
	}

	public void setLicense(boolean isLicense) {
		this.isLicense = isLicense;
	}

}
