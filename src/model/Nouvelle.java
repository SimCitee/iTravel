package model;

import java.sql.Date;

public class Nouvelle {
	
	private String nomContact;
	private String commentaire;
	private Date date;
	private String postTime;
	private int imageId;
	
	public Nouvelle(String nom, String comment, int id) {
		nomContact = nom;
		commentaire = comment;
		imageId = id;
	}
	
	public String getNomContact() {
		return nomContact;
	}
	public void setNomContact(String nomContact) {
		this.nomContact = nomContact;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPostTime() {
		return postTime;
	}
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

}
