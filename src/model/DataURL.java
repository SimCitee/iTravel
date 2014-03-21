package model;

public final class DataURL {
	
	
	
	public final static String SERVER_URL = "http://simond.byethost7.com";
	
	
	
	/*
	 * 
	 * 
	 * 
	 * Tu peux déclarer tes constantes (en majuscule) public final static , pas besoin de getters
	 * 
	 * 
	 */
	
	private final static String nouvellesVoyageur = SERVER_URL + "/getNouvelle.php?pid=1";

	public static String getNouvellesVoyageur() {
		return nouvellesVoyageur;
	}
	
	
	//Pas d'instance possible
	private DataURL(){}
} 
