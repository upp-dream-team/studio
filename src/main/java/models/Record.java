package models;

import java.util.Date;

public class Record {

	private int id;
	private String client;
	private Date date;
	private int quantity;
	private int albumId; //костыль
	private Album album;
	private int sellingId;

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Album getAlbum() {
		return album;
	}

	public void setAlbum(Album album) {
		this.album = album;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public int getSellingId() {
		return sellingId;
	}

	public void setSellingId(int sellingId) {
		this.sellingId = sellingId;
	}

}
