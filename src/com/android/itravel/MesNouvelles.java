package com.android.itravel;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
	
	private static final int EDIT_ITEM_REQUEST = 10;
	private static final int ADD_ITEM_REQUEST = 20;
	
	protected ActionMode mActionMode;
	private MesNouvellesListeAdapteur adapter = null;
	private JSONObject jsonServer;
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
					i.putExtra("ville", nouvelle.getVille());
					i.putExtra("pays", nouvelle.getPays());
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
		intent.putExtra("ville", nouvelle.getVille());
		intent.putExtra("pays", nouvelle.getPays());
		
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
        		Double latitude = dataMap.getDouble ("latitude");
        		Double longitude = (Double) dataMap.get("longitude");
        		String ville = dataMap.getString("ville");
        		String pays = dataMap.getString("pays");
        		String date = (String) dataMap.get("date");
        		String heure = (String) dataMap.get("heure");
        		String heureMAJ = heure;
        		    		
        		Nouvelle nouvelle = new Nouvelle();
        		nouvelle.setNouvelleId(idNouvelle);
        		nouvelle.setImageId(idImage);
        		nouvelle.setNouvelleTexte(commentaire);
        		nouvelle.setLatitude(latitude);
        		nouvelle.setLongitude(longitude);
        		nouvelle.setVille(ville);
        		nouvelle.setPays(pays);
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
                mpEntity.addPart("image_id", new StringBody(nouvelle.getImageId(), Charset.forName("UTF-8")));

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
                
                
			} catch (Exception e) {
				Log.i("", "Message d'erreur : " +  e.getMessage());
				return getResources().getString(com.android.itravel.R.string.element_not_added);
			}
       
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
