package models;

public class Rozpodil {

	private int musicianId;
	private Musician musician;
	private int songId;
	private double chastka;
	public double getChastka() {
		return chastka;
	}
	public void setChastka(double chastka) {
		this.chastka = chastka;
	}
	public int getMusicianId() {
		return musicianId;
	}
	public void setMusicianId(int musicianId) {
		this.musicianId = musicianId;
	}
	public int getSongId() {
		return songId;
	}
	public void setSongId(int songId) {
		this.songId = songId;
	}
	public Musician getMusician() {
		return musician;
	}
	public void setMusician(Musician musician) {
		this.musician = musician;
	}
}
