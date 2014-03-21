package model;

import java.util.Date;

public class Nouvelle {
	private int nouvelleId;
	private String nouvelleTexte;
	private Date nouvelleDate;
	private String nouvelleHeure;
	private double latitude;
	private double longitude;
	private String pays;
	private String ville;
	private int imageId;
	
	public Nouvelle(){}
	
	public int getNouvelleId() {
		return nouvelleId;
	}
	
	public void setNouvelleId(int nouvelleId) {
		this.nouvelleId = nouvelleId;
	}
	
	public String getNouvelleTexte() {
		return nouvelleTexte;
	}
	
	public void setNouvelleTexte(String nouvelleTexte) {
		this.nouvelleTexte = nouvelleTexte;
	}
	
	public Date getNouvelleDate() {
		return nouvelleDate;
	}
	
	public void setNouvelleDate(Date nouvelleDate) {
		this.nouvelleDate = nouvelleDate;
	}
	
	public String getNouvelleHeure() {
		return nouvelleHeure;
	}
	
	public void setNouvelleHeure(String nouvelleHeure) {
		this.nouvelleHeure = nouvelleHeure;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public String getPays() {
		return pays;
	}
	
	public void setPays(String pays) {
		this.pays = pays;
	}
	
	public String getVille() {
		return ville;
	}
	
	public void setVille(String ville) {
		this.ville = ville;
	}
	
	public int getImageId() {
		return imageId;
	}
	
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
	
	
	
}
