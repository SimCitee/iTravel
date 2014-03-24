package model;

import java.util.Date;

public class Nouvelle {
	private Long nouvelleId;
	private String nouvelleTexte;
	private String nouvelleDate;
	private String nouvelleHeure;
	private String nouvelleDateMAJ;		//Date dernière mise à jour
	private String nouvelleHeureMAJ;	//Heure dernière mise à jour
	private double latitude;
	private double longitude;
	private String pays;
	private String ville;
	private String imageId;
	
	public Nouvelle(){}
	
	public Long getNouvelleId() {
		return nouvelleId;
	}
	
	public void setNouvelleId(Long nouvelleId) {
		this.nouvelleId = nouvelleId;
	}
	
	public String getNouvelleTexte() {
		return nouvelleTexte;
	}
	
	public void setNouvelleTexte(String nouvelleTexte) {
		this.nouvelleTexte = nouvelleTexte;
	}
	
	public String getNouvelleDate() {
		return nouvelleDate;
	}
	
	public void setNouvelleDate(String nouvelleDate) {
		this.nouvelleDate = nouvelleDate;
	}
	
	public String getNouvelleHeure() {
		return nouvelleHeure;
	}
	
	public void setNouvelleHeure(String nouvelleHeure) {
		this.nouvelleHeure = nouvelleHeure;
	}
	
	public String getNouvelleDateMAJ() {
		return nouvelleDateMAJ;
	}

	public void setNouvelleDateMAJ(String nouvelleDateMAJ) {
		this.nouvelleDateMAJ = nouvelleDateMAJ;
	}

	public String getNouvelleHeureMAJ() {
		return nouvelleHeureMAJ;
	}

	public void setNouvelleHeureMAJ(String nouvelleHeureMAJ) {
		this.nouvelleHeureMAJ = nouvelleHeureMAJ;
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
	
	public String getImageId() {
		return imageId;
	}
	
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	
	
}
