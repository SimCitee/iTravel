package model;

public class Commentaire {
	
	private int commentaireId;
	private String commentaireTexte;
	private String commenteur;
	private String dateEmission;
	
	public Commentaire() {}
	
	public Commentaire(int id, String comment, String commenteur, String date) {
		this.commentaireId = id;
		this.commentaireTexte = comment;
		this.commenteur = commenteur;
		this.dateEmission = date;
	}
	
	public int getCommentaireId() {
		return commentaireId;
	}
	
	public void setCommentaireId(int commentaireId) {
		this.commentaireId = commentaireId;
	}
	
	public String getCommentaireTexte() {
		return commentaireTexte;
	}
	
	public void setCommentaireTexte(String commentaireTexte) {
		this.commentaireTexte = commentaireTexte;
	}

	public String getCommenteur() {
		return commenteur;
	}

	public void setCommenteur(String commenteur) {
		this.commenteur = commenteur;
	}

	public String getDateEmission() {
		return dateEmission;
	}

	public void setDateEmission(String dateEmission) {
		this.dateEmission = dateEmission;
	}
	
}
