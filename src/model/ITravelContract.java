package model;

import android.provider.BaseColumns;

public class ITravelContract {
	
	public ITravelContract() {}
	
	public static abstract class EntreeNouvelle implements BaseColumns {
		public static final String TABLE = "nouvelle";
		public static final String _ID = "nouvelle_id";
		public static final String _TEXTE  = "nouvelle_texte";
		public static final String _DATE = "nouvelle_date";
		public static final String _HEURE = "heure";
		public static final String _LATITUDE = "latitude";
		public static final String _LONGITUDE = "longitude";
		public static final String _PAYS = "pays";
		public static final String _VILLE = "ville";
		public static final String _IMAGE = "image_fichier";
		public static final String _UTILISATEUR_ID = "utilisateur_id";
		public static final String _DATE_CREER = "date_creer";
		public static final String _DATE_MAJ = "date_maj";
	}
	
	public static abstract class EntreeUtilisateur implements BaseColumns {
		public static final String TABLE = "utilisateur";
		public static final String _ID = "utilisateur_id";
		public static final String _COURRIEL  = "courriel";
		public static final String _PRENOM = "prenom";
		public static final String _NOM = "nom";
		public static final String _MOT_PASSE = "mot_passe";
		public static final String _DATE_CREER = "date_creer";
		public static final String _DATE_MAJ = "date_maj";
	}
	
	public static abstract class EntreeCommentaire implements BaseColumns {
		public static final String TABLE = "commentaire";
		public static final String _ID = "commentaire_id";
		public static final String _TEXTE  = "commentaire_texte";
		public static final String _UTILISATEUR_ID = "utilisateur_id";
		public static final String _NOUVELLE_ID = "nouvelle_id";
		public static final String _DATE_CREER = "date_creer";
		public static final String _DATE_MAJ = "date_maj";
	}
	
}
