package com.android.itravel;

import java.util.ArrayList;
import java.util.Calendar;

import com.android.itravel.R;
import com.android.itravel.database.ITravelDbHelper;
import com.android.itravel.database.ITravelContract.EntreeNouvelle;
import com.android.itravel.listadaptor.MesNouvellesListeAdapteur;

import model.Nouvelle;
import model.Utilisateur;
import model.UtilisateurActif;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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
import android.view.View.OnClickListener;

import android.widget.Toast;

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

		
		//FETCH la bd
		ITravelDbHelper mDbHelper = new ITravelDbHelper(this);
		SQLiteDatabase db = mDbHelper.getReadableDatabase(); 
	 
		
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
			EntreeNouvelle.TABLE, 	// The table to query 
			projection, 			// The columns to return 
			selection,			 	// The columns for the WHERE clause 
			selectionArgs, 			// The values for the WHERE clause 
			null, 					// don't group the rows 
			null, 					// don't filter by row groups 
			sortOrder 				// The sort order 
		); 
		
		if (cursor.moveToFirst()) {
			do {

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
		
		//Clique d'une nouvelle pour voir son détail
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
		        
				Nouvelle nouvelle = adapter.getItem(position);
				
				if(nouvelle != null)
				{
					Intent i = new Intent(MesNouvelles.this, MesNouvellesConsulter.class);
					i.putExtra("image_id", nouvelle.getImageId());
					i.putExtra("commentaire", nouvelle.getNouvelleTexte()); 
					i.putExtra("latitude", nouvelle.getLatitude());
					i.putExtra("longitude", nouvelle.getLongitude());
					startActivity(i);
				}
			}

		});

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
  
   
    @SuppressWarnings("unchecked")
	private void deleteSelectedItem(Nouvelle nouvelle) {
    	
    	//Crée un arraylist pour passer plusieurs paramètres au thread de modification
		ArrayList<Object> threadParams = new ArrayList<Object>();
		threadParams.add(0, nouvelle);
		threadParams.add(1, this);
    	
		//Thread de suppression
		new SupprimerDonneeNouvelleThread().execute(threadParams);
		
    	adapter.remove(nouvelle);
    	adapter.notifyDataSetChanged();
    }
  
   
    private void editSelectedItem(Nouvelle nouvelle, int position) {
 	
    	Intent intent = new Intent(this, MesNouvellesModifier.class);

		intent.putExtra("listPosition", position);
		intent.putExtra("nouvelle_id", nouvelle.getNouvelleId());
		intent.putExtra("id_image", nouvelle.getImageId());
		intent.putExtra("commentaire", nouvelle.getNouvelleTexte());
		intent.putExtra("latitude", nouvelle.getLatitude());
		intent.putExtra("longitude", nouvelle.getLongitude());
		
		startActivityForResult(intent, EDIT_ITEM_REQUEST);
		

    }
    
 
   
    @SuppressWarnings("unchecked")
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	
    	//Retour de modification
    	if(requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK)
    	{
 
    		Bundle dataMap = data.getExtras();
    	
    		Integer position = dataMap.getInt("id");
   
    		String commentaire = (String) dataMap.get("commentaire");
    		
    		//Récupère l'objet nouvelle dans la liste
    		Nouvelle nouvelle = adapter.getItem(position);
    		
    		nouvelle.setNouvelleTexte(commentaire);
    		
    		//Crée un arraylist pour passer plusieurs paramètres au thread de modification
    		ArrayList<Object> threadParams = new ArrayList<Object>();
    		threadParams.add(0, nouvelle);
    		threadParams.add(1, this);
    		
    		//Thread de modification
    		new ModifierDonneeNouvelleThread().execute(threadParams);
    		
    		adapter.notifyDataSetChanged();
    		
    		mActionMode.finish();
    		
    	}
    	
    	else{
    		
    		//Retour d'ajout
    		if(requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK)
    		{
    			Bundle dataMap = data.getExtras();
   		
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
        		
        		//Crée un arraylist pour passer plusieurs paramètres au thread d'ajout
        		ArrayList<Object> threadParams = new ArrayList<Object>();
        		threadParams.add(0, nouvelle);
        		threadParams.add(1, this);
        		
        		//Thread d'ajout
        		new AjouterDonneeNouvelleThread().execute(threadParams);
        		   		
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
            
        	startActivityForResult(new Intent(this, MesNouvellesAjouter.class), ADD_ITEM_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }
  
    
    /*
     * Ajout d'une nouvelle dans la base de données (asynchrone)
     */
    private class AjouterDonneeNouvelleThread extends AsyncTask<ArrayList<Object>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<Object>... params) {
  
        	Nouvelle nouvelle = (Nouvelle) params[0].get(0);
        	Context mesNouvelles = (Context) params[0].get(1);
        	
        	//Ajout dans la bd
    		ITravelDbHelper mDbHelper = new ITravelDbHelper(mesNouvelles);
    		
    		// Gets the data repository in write mode 
    		SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase(); 
    		 
    		// Create a new map of values, where column names are the keys 
    		ContentValues values = new ContentValues(); 
    		 
    		values.put(EntreeNouvelle._ID, nouvelle.getNouvelleId()); 
    		values.put(EntreeNouvelle._IMAGE, nouvelle.getImageId()); 
    		values.put(EntreeNouvelle._TEXTE, nouvelle.getNouvelleTexte());
    		values.put(EntreeNouvelle._LATITUDE, nouvelle.getLatitude());
    		values.put(EntreeNouvelle._LONGITUDE, nouvelle.getLongitude()); 
    		values.put(EntreeNouvelle._DATE, nouvelle.getNouvelleDate()); 
    		values.put(EntreeNouvelle._HEURE, nouvelle.getNouvelleHeure()); 
    		values.put(EntreeNouvelle._DATE_MAJ, nouvelle.getNouvelleDateMAJ()); 
    		values.put(EntreeNouvelle._HEURE_MAJ, nouvelle.getNouvelleHeureMAJ());
    		 
    		// Insert the new row, returning the primary key value of the new row 
    		long newRowId = dbWrite.insert(EntreeNouvelle.TABLE, null, values);
         		
    		return getResources().getString(com.android.itravel.R.string.element_added);
        }

        @Override
        protected void onPostExecute(String result) {
        	Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }

    }
    
    
    /*
     * Modification d'une nouvelle dans la base de données (asynchrone)
     */
    private class ModifierDonneeNouvelleThread extends AsyncTask<ArrayList<Object>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<Object>... params) {

        	Nouvelle nouvelle = (Nouvelle) params[0].get(0);
        	Context mesNouvelles = (Context) params[0].get(1);
        	
        	//Ajout dans la bd
    		ITravelDbHelper mDbHelper = new ITravelDbHelper(mesNouvelles);
    		
    		//Test DB
    		// Gets the data repository in write mode 
    		SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase(); 
    		 
    		// Create a new map of values, where column names are the keys 
    		ContentValues values = new ContentValues(); 
    		 
    		//values.put(EntreeNouvelle._ID, nouvelle.getNouvelleId()); 
    		//values.put(EntreeNouvelle._IMAGE, nouvelle.getImageId()); 
    		values.put(EntreeNouvelle._TEXTE, nouvelle.getNouvelleTexte());
		
    		int nbRowAffected = dbWrite.update(EntreeNouvelle.TABLE, values, EntreeNouvelle._ID + " = " + nouvelle.getNouvelleId(), null);		 
  
    		
    		return getResources().getString(com.android.itravel.R.string.element_modified);
        }

        @Override
        protected void onPostExecute(String result) {

        	Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
   
    }
    
    /*
     * Suppression d'une nouvelle dans la base de données (asynchrone)
     */
    private class SupprimerDonneeNouvelleThread extends AsyncTask<ArrayList<Object>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<Object>... params) {
    
   
    
        	Nouvelle nouvelle = (Nouvelle) params[0].get(0);
        	Context mesNouvelles = (Context) params[0].get(1);
        	
        	//Ajout dans la bd
    		ITravelDbHelper mDbHelper = new ITravelDbHelper(mesNouvelles);
 
    		// Gets the data repository in write mode 
    		SQLiteDatabase dbWrite = mDbHelper.getWritableDatabase(); 

    		int nbRowAffected = dbWrite.delete(EntreeNouvelle.TABLE, EntreeNouvelle._ID + " = " + 
    				nouvelle.getNouvelleId(), null);
    		
            return getResources().getString(com.android.itravel.R.string.element_deleted);
        }

        @Override
        protected void onPostExecute(String result) {
            //TextView txt = (TextView) findViewById(R.id.output);
            //txt.setText("Executed"); // txt.setText(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        	
        	Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
    
}
