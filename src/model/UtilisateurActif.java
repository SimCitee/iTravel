package model;

/*
 * Singleton qui représente l'utilisateur qui utilise l'application
 */
public class UtilisateurActif {
	
	private Utilisateur utilisateur;
	
	
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}


	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}


	private UtilisateurActif()
	{}
 
	
	private static class SingletonHolder
	{		
		
		private final static UtilisateurActif instance = new UtilisateurActif();
	}
 
	
	public static UtilisateurActif getInstance()
	{
		return SingletonHolder.instance;
	}
}
