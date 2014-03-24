package com.android.itravel;

import java.util.ArrayList;
import java.util.Calendar;

import com.android.itravel.R;
import model.ITravelDbHelper;
import model.MesNouvellesListeAdapteur;
import model.Nouvelle;
import model.Utilisateur;
import model.UtilisateurActif;
import model.ITravelContract.EntreeNouvelle;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class MesNouvelles extends Activity {
	
	private static final int EDIT_ITEM_REQUEST = 10;
	private static final int ADD_ITEM_REQUEST = 20;
	
	
	
	protected ActionMode mActionMode;
	private MesNouvellesListeAdapteur adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles);
		
		
		//Set l'utilisateur actif
		Utilisateur u = new Utilisateur();
		u.setUtilisateurId(1000);
		u.setCourriel("pl@gmail.com");
		u.setNom("Handfield");
		u.setPrenom("Pierre-Luc");
		UtilisateurActif.getInstance().setUtilisateur(u);
		
		
		ArrayList<Nouvelle> myList = new ArrayList<Nouvelle>();
		
		
		///////////////////////
		//FETCH
		ITravelDbHelper mDbHelper = new ITravelDbHelper(this);
		SQLiteDatabase db = mDbHelper.getReadableDatabase(); 
	 
		// Define a projection that specifies which columns from the database 
		// you will actually use after this query. 
		String[] projection={EntreeNouvelle._ID, 
			EntreeNouvelle._IMAGE,
			EntreeNouvelle._TEXTE,
			EntreeNouvelle._LATITUDE,
			EntreeNouvelle._LONGITUDE,
			EntreeNouvelle._DATE,
			EntreeNouvelle._HEURE,
			EntreeNouvelle._DATE_MAJ,
			EntreeNouvelle._HEURE_MAJ}; 
	 
		// How you want the results sorted in the resulting Cursor 
		String sortOrder = EntreeNouvelle._DATE_MAJ + " DESC"; 
	 
		//Champs de la clause WHERE
		String selection = "";
	
		//Valeur des champs de la clause WHERE
		String[] selectionArgs = {};
	       		
		Cursor cursor = db.query( 
			EntreeNouvelle.TABLE, // The table to query 
			projection, // The columns to return 
			selection, // The columns for the WHERE clause 
			selectionArgs, // The values for the WHERE clause 
			null, // don't group the rows 
			null, // don't filter by row groups 
			sortOrder // The sort order 
		); 
		
		
		
		if (cursor.moveToFirst()) {
Log.i("", "Cursor not empty");
			do {
				Log.i("", "Fetch data");
				Long idNouvelle = cursor.getLong(cursor.getColumnIndex(EntreeNouvelle._ID)); 
				
				String imageId = cursor.getString(cursor.getColumnIndex(EntreeNouvelle._IMAGE));

	    		String commentaire = cursor.getString(cursor.getColumnIndex(EntreeNouvelle._TEXTE)); 
	    		
	    		Double latitude = cursor.getDouble(cursor.getColumnIndex(EntreeNouvelle._LATITUDE)); 
	    		Double longitude = cursor.getDouble(cursor.getColumnIndex(EntreeNouvelle._LONGITUDE)); 
	    		String date = cursor.getString(cursor.getColumnIndex(EntreeNouvelle._DATE)); 
	    		String heure = cursor.getString(cursor.getColumnIndex(EntreeNouvelle._HEURE)); 
	    		String dateMAJ = cursor.getString(cursor.getColumnIndex(EntreeNouvelle._DATE_MAJ)); 
	    		String heureMAJ = cursor.getString(cursor.getColumnIndex(EntreeNouvelle._HEURE_MAJ)); 
			
				
			
				Nouvelle nouvelle = new Nouvelle();
		
				nouvelle.setNouvelleId(idNouvelle);
				nouvelle.setImageId(imageId);
				nouvelle.setNouvelleTexte(commentaire);
		
				nouvelle.setLatitude(latitude);
				nouvelle.setLongitude(longitude);
				nouvelle.setNouvelleDate(date);
				nouvelle.setNouvelleHeure(heure);
				nouvelle.setNouvelleDateMAJ(dateMAJ);
				nouvelle.setNouvelleHeureMAJ(heureMAJ);
				
				//Liste d'items
				myList.add(nouvelle);
	        	   
			} while (cursor.moveToNext());
		}
		else
		{
			Log.i("", "Cursor IS empty");
		}
		
		
		
			
			
		
	
		//1er param  : référence au contexte (toujours this)
		//2eme param : Activité qui définit le layout à utilisé
		//3eme param : Array ou liste d'objets utilisé pour créer la liste
		adapter = new MesNouvellesListeAdapteur(this, R.layout.liste_mes_nouvelles_layout, myList);
		
		//Cherche la liste à laquel on veut ajoute les données
		ListView listView = (ListView) findViewById(R.id.mesNouvelleslistView);
		 
		//Bind la liste avec l'adapter (qui contient le layout et les données)
		listView.setAdapter(adapter);
				
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
 
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long arg3) {     	
            	if (mActionMode != null) { 
		        	 return false; 
	        	 } 
				
	        	 mActionMode = startActionMode(mActionModeCallback); 
	        	 mActionMode.setTag(adapter.getItem(position));
	        	 view.setSelected(true); 
	        	 return true;
            }

        });

		
		///
		//getFragmentManager().beginTransaction()
        //.replace(android.R.id.content, new SettingsFragment())
        //.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mes_nouvelles_action_bar_no_action, menu);
		return true;
	}
	
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.mes_nouvelles_action_bar, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }


        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
 
            switch (item.getItemId()) {
            
                case R.id.mes_nouvelles_modifier:             
                	editSelectedItem((Nouvelle)mode.getTag(), adapter.getPosition((Nouvelle) mode.getTag()));
                    return true;
                
                case R.id.mes_nouvelle_supprimer :

                	deleteSelectedItem((Nouvelle)mode.getTag());
                	mode.finish();
                    return true;
             
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
  
   
    private void deleteSelectedItem(Nouvelle itemSelected) {
    	adapter.remove(itemSelected);
    	adapter.notifyDataSetChanged();
    }
  
   
    private void editSelectedItem(Nouvelle nouvelle, int position) {
 	
    	Intent intent = new Intent(this, MesNouvellesModifier.class);

		intent.putExtra("id", String.valueOf(position));
		intent.putExtra("image_id", nouvelle.getImageId());
		intent.putExtra("commentaire", nouvelle.getNouvelleTexte());
		intent.putExtra("position_pays", nouvelle.getPays());
		intent.putExtra("position_ville", nouvelle.getVille());
		
		startActivityForResult(intent, EDIT_ITEM_REQUEST);
		

    }
    
 
   
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	
    	//Retour de modification
    	if(requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK)
    	{
 
    		Bundle dataMap = data.getExtras();
    	
    		Integer position = Integer.parseInt((String) dataMap.get("id"));
    		
    		//Integer imageId = Integer.parseInt((String) dataMap.get("image_id"));
    		
    		//TODO modifier pour vrai id de l'image
    		Integer imageId = 0;
    		
    		
    		String commentaire = (String) dataMap.get("commentaire");
    		
    		Nouvelle nouvelle = adapter.getItem(position);
    		//nouvelle.setImageId(imageId);
    		nouvelle.setNouvelleTexte(commentaire);

    		adapter.notifyDataSetChanged();
    		
    		mActionMode.finish();
    		
    	}
    	
    	else{
    		
    		//Retour d'ajout
    		if(requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK)
    		{
    			Bundle dataMap = data.getExtras();
   	    	
        		//Integer position = Integer.parseInt((String) dataMap.get("id"));
        		
        		
       		
        		//Integer imageId = Integer.parseInt((String) dataMap.get("image_id"));
        		
       		
        		
        		Long idNouvelle = dataMap.getLong("nouvelle_id");
        		
        		String idImage = (String) dataMap.get("id_image");
        		
        		String commentaire = (String) dataMap.get("commentaire");
        		
        		Double latitude = dataMap.getDouble ("latitude", 2);
        		
        		Double longitude = (Double) dataMap.get("longitude");
        		String date = (String) dataMap.get("date");
        		String heure = (String) dataMap.get("heure");
        		String dateMAJ = date;
        		String heureMAJ = heure;
        		    		
        		Nouvelle nouvelle = new Nouvelle();
        		nouvelle.setNouvelleId(idNouvelle);
        		nouvelle.setImageId(idImage); //Sera changé pour un id coté serveur
        		nouvelle.setNouvelleTexte(commentaire);
        		nouvelle.setLatitude(latitude);
        		nouvelle.setLongitude(longitude);
        		nouvelle.setNouvelleDate(date);
        		nouvelle.setNouvelleHeure(heure);
        		nouvelle.setNouvelleDateMAJ(date);
        		nouvelle.setNouvelleHeureMAJ(heureMAJ);
        		   		
        		//Ajout dans la bd
        		ITravelDbHelper mDbHelper = new ITravelDbHelper(this);
        		
        		//Test DB
        		// Gets the data repository in write mode 
        		SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase(); 
        		 
        		// Create a new map of values, where column names are the keys 
        		ContentValues values = new ContentValues(); 
        		 
        		values.put(EntreeNouvelle._ID, idNouvelle); 
        		values.put(EntreeNouvelle._IMAGE, idImage); 
        		values.put(EntreeNouvelle._TEXTE, commentaire);
        		values.put(EntreeNouvelle._LATITUDE, latitude); 
        		values.put(EntreeNouvelle._LONGITUDE, longitude); 
        		values.put(EntreeNouvelle._DATE, date); 
        		values.put(EntreeNouvelle._HEURE, heure); 
        		values.put(EntreeNouvelle._DATE_MAJ, dateMAJ); 
        		values.put(EntreeNouvelle._HEURE_MAJ, heureMAJ); 
        		 
        		// Insert the new row, returning the primary key value of the new row 
        		long newRowId = dbWrite.insert(EntreeNouvelle.TABLE, null, values);
             		
        		Log.i("", "NewRowId : " + newRowId);
        		
        		//Ajout à la liste
        		adapter.add(nouvelle);
        		adapter.notifyDataSetChanged();
 	
    		}
    	}
    	
    }
    
   
   
    public boolean onPrepareOptionsMenu(Menu menu) {
        //  preparation code here
        return super.onPrepareOptionsMenu(menu);
    }
    
   
    //Sélection dans la barre de menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	//Ajout
        if (item.getItemId() == R.id.mes_nouvelles_actionbar_add_id) {
            //startActivity(new Intent(this, CoursesActivity.class));
        	
        	
        	startActivityForResult(new Intent(this, MesNouvellesAjouter.class), ADD_ITEM_REQUEST);
        	
        }
        
        
       
        
        return super.onOptionsItemSelected(item);
    }
  
}
