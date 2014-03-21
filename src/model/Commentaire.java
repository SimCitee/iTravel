package model;

public class Commentaire {
	
	private int commentaireId;
	private String commentaireTexte;
	private Utilisateur utilisateur;
	private Nouvelle nouvelle;
	
	
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
	
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}
	
	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
	
	public Nouvelle getNouvelle() {
		return nouvelle;
	}
	
	public void setNouvelle(Nouvelle nouvelle) {
		this.nouvelle = nouvelle;
	}
	
	
}
