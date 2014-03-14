package model;

import java.sql.Date;

public class Nouvelle {
	
	private String nomContact;
	private String commmentaire;
	private Date date;
	private String postTime;
	private int imageId;
	
	public String getNomContact() {
		return nomContact;
	}
	public void setNomContact(String nomContact) {
		this.nomContact = nomContact;
	}
	public String getCommmentaire() {
		return commmentaire;
	}
	public void setCommmentaire(String commmentaire) {
		this.commmentaire = commmentaire;
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
