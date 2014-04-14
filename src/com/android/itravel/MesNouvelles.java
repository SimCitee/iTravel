package com.android.itravel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.itravel.R;
import com.android.itravel.constant.DataURL;
import com.android.itravel.constant.EnvironmentVariables;
import com.android.itravel.database.ITravelDbHelper;
import com.android.itravel.database.ITravelContract.EntreeNouvelle;
import com.android.itravel.listadaptor.ListeNouvellesAdapteur;
import com.android.itravel.listadaptor.MesNouvellesListeAdapteur;
import com.android.itravel.util.DataAccessController;
import com.android.itravel.util.Encryption;

import model.Nouvelle;
import model.Utilisateur;
import model.UtilisateurActif;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
	
	private Context context = this;
	//private ListView listView;
	//private ArrayList<Nouvelle> listeNouvelles = new ArrayList<Nouvelle>();
	
	private static final int EDIT_ITEM_REQUEST = 10;
	private static final int ADD_ITEM_REQUEST = 20;
	
	protected ActionMode mActionMode;
	private MesNouvellesListeAdapteur adapter = null;
	private JSONObject jsonServer;
	private ArrayList<Nouvelle> mesNouvelles;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles);

		getActionBar().setDisplayHomeAsUpEnabled(true);
/*
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
*/
		
		//1er param  : référence au contexte (toujours this)
		//2eme param : Activité qui définit le layout à utilisé
		//3eme param : Array ou liste d'objets utilisé pour créer la liste
		adapter = new MesNouvellesListeAdapteur(this, R.layout.liste_mes_nouvelles_layout, new ArrayList<Nouvelle>());

		//Cherche la liste à laquel on veut ajoute les données
		listView = (ListView) findViewById(R.id.mesNouvelleslistView);

		//Bind la liste avec l'adapter (qui contient le layout et les données)
		listView.setAdapter(adapter);

		
		new AfficherListeDonneeNouvelleThread().execute("");
		
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
    @SuppressWarnings("deprecation")
    private class AjouterDonneeNouvelleThread extends AsyncTask<ArrayList<Object>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<Object>... args) {
        	
        	//La nouvelle à ajouter
        	Nouvelle nouvelle = (Nouvelle) args[0].get(0);
        	
        	//Ajoute les information
        	try {
        		//Url of the server
                String url = DataURL.AJOUTER_NOUVELLE;
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
                
    			MultipartEntity mpEntity = new MultipartEntity();
                //Path of the file to be uploaded
                String filepath = Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER + "/" + nouvelle.getImageId();
    			//String filepath = Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER + "/" + "test.jpg";
                Log.i("", "Image path : " + filepath);
                File file = new File(filepath);
                
               if(file.exists())
               {
                Log.i("", "Image trouve");
               }
               else
               {
               Log.i("", "Image non trouve");
               }
                
                
                
                ContentBody cbFile = new FileBody(file, "image/jpeg");

                //Add the data to the multipart entity
                mpEntity.addPart("image", cbFile);
                //mpEntity.addPart("nouvelle_id", new StringBody(nouvelle.getNouvelleId().toString(), Charset.forName("UTF-8")));
                
    			mpEntity.addPart("nouvelle_texte", new StringBody(nouvelle.getNouvelleTexte(), Charset.forName("UTF-8")));
                mpEntity.addPart("latitude", new StringBody( nouvelle.getLatitude().toString(), Charset.forName("UTF-8")));
                mpEntity.addPart("longitude", new StringBody(nouvelle.getLongitude().toString(), Charset.forName("UTF-8")));
                
                String pays = nouvelle.getPays();
                if(pays == null)
                	pays = "";
                
                
                mpEntity.addPart("pays", new StringBody(pays, Charset.forName("UTF-8")));
                
                String ville = nouvelle.getVille();
                if(ville == null)
                	ville = "";
                
                mpEntity.addPart("ville", new StringBody(ville, Charset.forName("UTF-8")));
                mpEntity.addPart("utilisateur_id", new StringBody(UtilisateurActif.getInstance().getUtilisateur().getUtilisateurId().toString(), Charset.forName("UTF-8")));
               
    			
                //mpEntity.addPart("nouvelle_texte", new StringBody("aaa", Charset.forName("UTF-8")));
                //mpEntity.addPart("latitude", new StringBody( "200" , Charset.forName("UTF-8")));
                //mpEntity.addPart("longitude", new StringBody("220", Charset.forName("UTF-8")));
                //mpEntity.addPart("pays", new StringBody("pays", Charset.forName("UTF-8")));
                //mpEntity.addPart("ville", new StringBody("ville", Charset.forName("UTF-8")));
                //mpEntity.addPart("utilisateur_id", new StringBody("14", Charset.forName("UTF-8")));
               
                
                post.setEntity(mpEntity);
                //Execute the post request
                HttpResponse response1 = client.execute(post);
                //Get the response from the server
                HttpEntity resEntity = response1.getEntity();
                String Response=EntityUtils.toString(resEntity);
                Log.i("Response:", "Répnse du serveur : " + Response);
                //Generate the array from the response
                JSONArray jsonarray = new JSONArray("["+Response+"]");
                JSONObject jsonobject = jsonarray.getJSONObject(0);
                //Get the result variables from response 
                //String result = (jsonobject.getString("result"));
                //String msg = (jsonobject.getString("msg"));
                //Close the connection
                client.getConnectionManager().shutdown();
                
        		/*
        		//Url of the server
                String url = DataURL.AJOUTER_NOUVELLE;
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(url);
               
    			MultipartEntity mpEntity = new MultipartEntity();
                //Path of the file to be uploaded
                String filepath = Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER + "/201403251541330.jpg";
                File file = new File(filepath);
                ContentBody cbFile = new FileBody(file, "image/jpeg");         

                //Add the data to the multipart entity
                mpEntity.addPart("image", cbFile);
                mpEntity.addPart("name", new StringBody("Test", Charset.forName("UTF-8")));
                mpEntity.addPart("data", new StringBody("This is test report", Charset.forName("UTF-8")));
                post.setEntity(mpEntity);
                //Execute the post request
                HttpResponse response1 = client.execute(post);
                //Get the response from the server
                HttpEntity resEntity = response1.getEntity();
                String Response=EntityUtils.toString(resEntity);
                Log.d("Response:", Response);
                //Generate the array from the response
                JSONArray jsonarray = new JSONArray("["+Response+"]");
                JSONObject jsonobject = jsonarray.getJSONObject(0);
                //Get the result variables from response 
                String result = (jsonobject.getString("result"));
                String msg = (jsonobject.getString("msg"));
                //Close the connection
                client.getConnectionManager().shutdown();
                
                */
                
			} catch (Exception e) {
				Log.i("", "Message d'erreur : " +  e.getMessage());
				return getResources().getString(com.android.itravel.R.string.element_not_added);
			}
       /* 	
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
*/
    		return getResources().getString(com.android.itravel.R.string.element_added);
        }
/*
    	@Override
        protected String doInBackground(ArrayList<Object>... args) {
  
    		//Nouvelle à ajouter
        	Nouvelle nouvelle = (Nouvelle) args[0].get(0);
        	//Context mesNouvelles = (Context) params[0].get(1);
*/       	
        	
    		/* 
    		values.put(EntreeNouvelle._ID, nouvelle.getNouvelleId()); 
    		values.put(EntreeNouvelle._IMAGE, nouvelle.getImageId()); 
    		values.put(EntreeNouvelle._TEXTE, nouvelle.getNouvelleTexte());
    		values.put(EntreeNouvelle._LATITUDE, nouvelle.getLatitude());
    		values.put(EntreeNouvelle._LONGITUDE, nouvelle.getLongitude()); 
    		values.put(EntreeNouvelle._DATE, nouvelle.getNouvelleDate()); 
    		values.put(EntreeNouvelle._HEURE, nouvelle.getNouvelleHeure()); 
    		values.put(EntreeNouvelle._DATE_MAJ, nouvelle.getNouvelleDateMAJ()); 
    		values.put(EntreeNouvelle._HEURE_MAJ, nouvelle.getNouvelleHeureMAJ());
    		 */
/*
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			String nouvelleText = nouvelle.getNouvelleTexte();
			
			
			String imageCompleteName = Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER + "/" + nouvelle.getImageId(); 
			
			 File imageFile = new File(imageCompleteName);
			 
			 //Vérifie si l'image existe
			 if(imageFile.exists()) {
		 
				 //Bitmap myBitmap = BitmapFactory.decodeFile(imageCompleteName);
				 Log.i("", "Dans if image");
				 
				 //params.add(new BasicNameValuePair("image", myBitmap.toString()));
				
				 File file = new File(imageCompleteName);
				 
				 
				 try {
				     HttpClient httpclient = new DefaultHttpClient();

				     HttpPost httppost = new HttpPost(DataURL.AJOUTER_UTILISATEUR);
				     
				     InputStreamEntity reqEntity = new InputStreamEntity(
				             new FileInputStream(file), -1);
				     reqEntity.setContentType("binary/octet-stream");
				     reqEntity.setChunked(true); // Send in multiple parts if needed
				     httppost.setEntity(reqEntity);
				     HttpResponse response = httpclient.execute(httppost);
				     
				     Log.i("", "Reponse ajout image : " + response.toString());

				 } catch (Exception e) {
				     // show error
				 }
			 }
			
			// Send email and password	
			params.add(new BasicNameValuePair("nouvelle_texte", nouvelleText));
			
			
			
			JSONObject json = DataAccessController.getDataFromUrl(DataURL.AJOUTER_NOUVELLE, "POST", params);
			
			try {
				String success = json.getString("success");
				
				Log.i("", "Retour serveur : " + success);
				
				//Toast.makeText(getApplicationContext(), success, Toast.LENGTH_SHORT).show();
				
				
				int success = json.getInt(TAG_SUCCES);	// valider le chargement
				
				// si succes chargement
				if (success == 1) {
					savedAccount = true;
				} 
				else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.alertErrorSavingAccount), Toast.LENGTH_SHORT).show();
				}
				
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
    		
        	
        	
         		
    		return getResources().getString(com.android.itravel.R.string.element_added);
        }
  */  	
        @Override
        protected void onPostExecute(String result) {
        	Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
 /*       
        @SuppressWarnings("deprecation")
        private void upload() throws Exception {
            //Url of the server
            String url = DataURL.AJOUTER_NOUVELLE;
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
           
			MultipartEntity mpEntity = new MultipartEntity();
            //Path of the file to be uploaded
            String filepath = Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER + "/201403251541330.jpg";
            File file = new File(filepath);
            ContentBody cbFile = new FileBody(file, "image/jpeg");         

            //Add the data to the multipart entity
            mpEntity.addPart("image", cbFile);
            mpEntity.addPart("name", new StringBody("Test", Charset.forName("UTF-8")));
            mpEntity.addPart("data", new StringBody("This is test report", Charset.forName("UTF-8")));
            post.setEntity(mpEntity);
            //Execute the post request
            HttpResponse response1 = client.execute(post);
            //Get the response from the server
            HttpEntity resEntity = response1.getEntity();
            String Response=EntityUtils.toString(resEntity);
            Log.d("Response:", Response);
            //Generate the array from the response
            JSONArray jsonarray = new JSONArray("["+Response+"]");
            JSONObject jsonobject = jsonarray.getJSONObject(0);
            //Get the result variables from response 
            String result = (jsonobject.getString("result"));
            String msg = (jsonobject.getString("msg"));
            //Close the connection
            client.getConnectionManager().shutdown();
        }
 */       
    }
    
    
    /*
     * Modification d'une nouvelle dans la base de données (asynchrone)
     */
    private class ModifierDonneeNouvelleThread extends AsyncTask<ArrayList<Object>, Void, String> {
    	
    	@Override
        protected String doInBackground(ArrayList<Object>... args) {

        	Nouvelle nouvelle = (Nouvelle) args[0].get(0);
        	
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			
			// Send email and password	
			params.add(new BasicNameValuePair("nouvelle_id", nouvelle.getNouvelleId().toString()));
			params.add(new BasicNameValuePair("texte_nouvelle", nouvelle.getNouvelleTexte()));
			
			
			JSONObject json = DataAccessController.getDataFromUrl(DataURL.MODIFIER_NOUVELLE, "POST", params);
			
			try {
				int success = json.getInt("etat");	// valider le chargement
			
				if (success == 1) {
					return getResources().getString(com.android.itravel.R.string.element_modified);
				} 
				else {
					return getResources().getString(com.android.itravel.R.string.element_not_modified);
				}
				
				
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		return getResources().getString(com.android.itravel.R.string.element_not_modified);
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
        protected String doInBackground(ArrayList<Object>... args) {

        	Nouvelle nouvelle = (Nouvelle) args[0].get(0);
        	
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("nouvelle_id", nouvelle.getNouvelleId().toString()));

			JSONObject json = DataAccessController.getDataFromUrl(DataURL.SUPPRIMER_NOUVELLE, "POST", params);
			
			try {
				int success = json.getInt("etat");	// valider le chargement
			
				if (success == 1) {
					return getResources().getString(com.android.itravel.R.string.element_deleted);
				} 
				else {
					return getResources().getString(com.android.itravel.R.string.element_not_deleted);
				}
				
				
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
    		
    		return getResources().getString(com.android.itravel.R.string.element_not_deleted);
        }
    	
        @Override
        protected void onPostExecute(String result) {

        	Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
    
    /*
     * Affichage de la liste de "mes nouvelles" avec
     */
    private class AfficherListeDonneeNouvelleThread extends AsyncTask<String, Void, List<Nouvelle>> {
    	
    	private ProgressDialog dialog;
    	
        @Override
        protected ArrayList<Nouvelle> doInBackground(String... args) {
        	ArrayList<Nouvelle> listeNouvelles = new ArrayList<Nouvelle>();
        	List<NameValuePair> params = new ArrayList<NameValuePair>();
        	
        	//TODO Mettre le bon utilisateur
        	//Integer userId = 2; //UtilisateurActif.getInstance().getUtilisateur().getUtilisateurId();
        	Integer userId = UtilisateurActif.getInstance().getUtilisateur().getUtilisateurId();
        	
        	Log.i("", "Utilisateur : " + UtilisateurActif.getInstance().getUtilisateur().toString());
        	
        	//Identifiant de l'utilisateur actuel
    		params.add(new BasicNameValuePair("utilisateur_id", userId.toString()));
    		
    		
    		try {	
    			//Cherche le json sur le serveur
    			jsonServer = DataAccessController.getDataFromUrl(DataURL.CONSULTER_MES_NOUVELLES, "POST", params);
    			
    			if(jsonServer.length() > 0){
	    			//Tableau d'objets
	    		    JSONArray jsonRows= jsonServer.getJSONArray("data");   
	    		    
	    		    for(int i=0;i<jsonRows.length(); i++){
	    		        JSONObject jsonas = jsonRows.getJSONObject(i);
	    		        
	    		        Nouvelle nouvelle = new Nouvelle();
	    		        
	    		        nouvelle.setNouvelleId((jsonas.getLong("nouvelle_id")));
	    		        nouvelle.setNouvelleTexte(jsonas.getString("nouvelle_texte"));
	    		        nouvelle.setNouvelleDate(jsonas.getString("nouvelle_date"));
	    		        nouvelle.setNouvelleHeure(jsonas.getString("heure"));
	    		        nouvelle.setPays(jsonas.getString("pays"));
	    		        nouvelle.setVille(jsonas.getString("ville"));
	    		        nouvelle.setImageId(jsonas.getString("image_fichier"));
	    		        
	    		        listeNouvelles.add(nouvelle);
	
	    		    }
    			}
    			
    		   
    			
        	}
        	catch(Exception e)
    		{
        		e.printStackTrace();
    		   
    		}

        	return listeNouvelles;
			
        }
        
        @Override
            protected void onPreExecute() {       
                super.onPreExecute();
                dialog= new ProgressDialog(MesNouvelles.this);
                dialog.setMessage("Téléchargement des nouvelles...");
                dialog.show();           
            }

        
        @Override
        protected void onPostExecute(List<Nouvelle> result) {
        	
        	super.onPostExecute(result);
        	dialog.dismiss();
        	adapter.addAll(result);
			adapter.notifyDataSetChanged();
        	
        }
    }

}
