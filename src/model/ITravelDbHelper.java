package model;

import model.ITravelContract.EntreeCommentaire;
import model.ITravelContract.EntreeNouvelle;
import model.ITravelContract.EntreeUtilisateur;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ITravelDbHelper extends SQLiteOpenHelper {
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "ListeEpicerie.db";
	
	public static final String SQL_CREATE_TABLE_UTILISATEUR = 
			"CREATE TABLE " + EntreeUtilisateur.TABLE + " (" +
			EntreeUtilisateur._ID + " INTEGER PRIMARY KEY, " +
			EntreeUtilisateur._COURRIEL + " TEXT, " + 
			EntreeUtilisateur._PRENOM + " TEXT, " +
			EntreeUtilisateur._NOM + " TEXT, " +
			EntreeUtilisateur._DATE_CREER + " TEXT, " +
			EntreeUtilisateur._DATE_MAJ + " TEXT)";
	
	public static final String SQL_CREATE_TABLE_NOUVELLE = 
			"CREATE TABLE " + EntreeNouvelle.TABLE + " (" +
			EntreeNouvelle._ID + " INTEGER PRIMARY KEY, " +
			EntreeNouvelle._TEXTE + " TEXT, " + 
			EntreeNouvelle._DATE + " TEXT, " +
			EntreeNouvelle._HEURE + " TEXT, " +
			EntreeNouvelle._LATITUDE + " TEXT, " +
			EntreeNouvelle._LONGITUDE + " TEXT, " +
			EntreeNouvelle._PAYS + " TEXT, " +
			EntreeNouvelle._VILLE + " TEXT, " +
			EntreeNouvelle._IMAGE + " TEXT, " +
			EntreeNouvelle._UTILISATEUR_ID + " INTEGER, " +
			"FOREIGN KEY("+ EntreeNouvelle._UTILISATEUR_ID +") REFERENCES "+ EntreeUtilisateur.TABLE +"("+ EntreeUtilisateur._ID +"))";
	
	public static final String SQL_CREATE_TABLE_COMMENTAIRE = 
			"CREATE TABLE " + EntreeCommentaire.TABLE + " (" +
			EntreeCommentaire._ID + " INTEGER PRIMARY KEY, " +
			EntreeCommentaire._TEXTE + " TEXT, " + 
			EntreeCommentaire._UTILISATEUR_ID + " INTEGER, " +
			EntreeCommentaire._NOUVELLE_ID + " TEXT, " +
			"FOREIGN KEY("+ EntreeCommentaire._UTILISATEUR_ID +") REFERENCES "+ EntreeUtilisateur.TABLE +"("+ EntreeUtilisateur._ID +"), " +
			"FOREIGN KEY("+ EntreeCommentaire._NOUVELLE_ID +") REFERENCES "+ EntreeNouvelle.TABLE +"("+ EntreeNouvelle._ID +"))";
			
	
	public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + EntreeCommentaire.TABLE + ";" +
													"DROP TABLE IF EXISTS " + EntreeNouvelle.TABLE + ";" +
													"DROP TABLE IF EXISTS " + EntreeUtilisateur.TABLE + ";";
	
	private static String qryNouvellesUtilisateur = 
			"SELECT " + EntreeNouvelle._ID + " ID, " +
			EntreeNouvelle._TEXTE + " NOUVELLE, " +
			EntreeNouvelle._DATE + " DATE, " +
			EntreeUtilisateur._PRENOM + " PRENOM, " +
			EntreeUtilisateur._NOM + " NOM " +	
			"FROM " + EntreeNouvelle.TABLE + " n JOIN " +
			EntreeUtilisateur.TABLE + " u ON " +
			"n." + EntreeNouvelle._ID + " = u." + EntreeUtilisateur._ID +
			"WHERE u." + EntreeUtilisateur._ID + " = ?"; 
	
	public ITravelDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}
	
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_TABLE_UTILISATEUR);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
	
	public Cursor getNouvellesUtilisateur(String utilisateur_id) {
		String[] args = {utilisateur_id};
		
		return (getReadableDatabase()
				.rawQuery(this.qryNouvellesUtilisateur, args));
	}
}
