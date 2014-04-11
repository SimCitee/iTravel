package model;

public class Utilisateur {
	private int utilisateurId;
	private String courriel;
	private String nom;
	private String prenom;
	
	
	public Utilisateur() {	
	}
	
	public Utilisateur(int id, String courriel, String nom, String prenom) {
		this.utilisateurId = id;
		this.courriel = courriel;
		this.nom = nom;
		this.prenom = prenom;
	}
	
	public int getUtilisateurId() {
		return utilisateurId;
	}
	
	public void setUtilisateurId(int utilisateurId) {
		this.utilisateurId = utilisateurId;
	}
	
	public String getCourriel() {
		return courriel;
	}
	
	public void setCourriel(String courriel) {
		this.courriel = courriel;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	
}
