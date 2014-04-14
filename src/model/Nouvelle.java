package model;

import java.util.Date;

public class Nouvelle {
	private Long nouvelleId;
	private String nouvelleTexte;
	private String nouvelleDate;
	private String nouvelleHeure;
	private String nouvelleDateMAJ;		//Date dernière mise à jour
	private String nouvelleHeureMAJ;	//Heure dernière mise à jour
	private Double latitude;
	private Double longitude;
	private String pays;
	private String ville;
	private String imageId;
	private Utilisateur utilisateur;
	private String voyageur;
	private int intervalleMinute;
	
	public Nouvelle(){}
	
	public Nouvelle(Long id, String texte, String image, Utilisateur u, int minute) {
		this.nouvelleId = id;
		this.nouvelleTexte = texte;
		this.utilisateur = u;
		this.intervalleMinute = minute;
		this.imageId = image;
	}
	
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
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
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

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	
	public String getVoyageur() {
		return voyageur;
	}

	public void setVoyageur(String voyageur) {
		this.voyageur = voyageur;
	}

	public int getIntervalleMinute() {
		return intervalleMinute;
	}

	public void setIntervalleMinute(int intervalleMinute) {
		this.intervalleMinute = intervalleMinute;
	}
	
	public String getAffichageTemps() {
		// si une journee et plus
		if (intervalleMinute >= 1440) {
			int day = (intervalleMinute / 24) / 60;
			return String.valueOf(day) + " jours";
		} 
		else if (intervalleMinute >= 60) {
			int hour = intervalleMinute / 60;
			return String.valueOf(hour) + " heures";
		}
		else
			return String.valueOf(intervalleMinute) + " minutes";
	}

	@Override
	public String toString() {
		return "Nouvelle [nouvelleId=" + nouvelleId + ", nouvelleTexte="
				+ nouvelleTexte + ", nouvelleDate=" + nouvelleDate
				+ ", nouvelleHeure=" + nouvelleHeure + ", nouvelleDateMAJ="
				+ nouvelleDateMAJ + ", nouvelleHeureMAJ=" + nouvelleHeureMAJ
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", pays=" + pays + ", ville=" + ville + ", imageId="
				+ imageId + ", utilisateur=" + utilisateur + "]";
	}
	
	
	
}
