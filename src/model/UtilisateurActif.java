package model;

/*
 * Singleton qui représente l'utilisateur qui utilise l'application
 */
public class UtilisateurActif {
	
	private Utilisateur utilisateur = null;
	private static UtilisateurActif _instance = null;
	
	
	public Utilisateur getUtilisateur() {
		return utilisateur;
	}


	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	private UtilisateurActif()
	{}
 
	
	public static UtilisateurActif getInstance()
	{
		if (_instance == null) {
			_instance = new UtilisateurActif();
		}
		return _instance;
	}
}
