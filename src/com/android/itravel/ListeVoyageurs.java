package com.android.itravel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import model.Utilisateur;
import model.UtilisateurActif;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.itravel.constant.DataURL;
import com.android.itravel.listadaptor.ListeVoyageursAdapteur;
import com.android.itravel.util.ConnectionDetector;
import com.android.itravel.util.DataAccessController;

public class ListeVoyageurs extends Activity {

	private ListView vListe;
	private Button btnSaveSubscription;
	private ListeVoyageursAdapteur adapteur;
	private ConnectionDetector cd;
	private ProgressDialog pDialog;	
	private ArrayList<LinkedHashMap<String, Utilisateur>> liste;
	
	// JSON Node
	private static final String TAG_SUCCES = "success";
	private static final String TAG_UTILISATEURS = "utilisateurs";
	private static final String TAG_NOM = "nom";
	private static final String TAG_PRENOM = "prenom";
	private static final String TAG_UID = "uid";
	private static final String TAG_COURRIEL = "courriel";
	private static final String TAG_MESSAGE = "message";
	
	// JSONArray de nouvelles
	JSONArray utilisateurs = null;
	
	private void initViews() {
		vListe = (ListView)findViewById(R.id.lvVoyageurs);
		btnSaveSubscription = (Button)findViewById(R.id.btnSaveSubscription);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_voyageurs);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		cd = new ConnectionDetector(getApplicationContext());
		liste  = new ArrayList<LinkedHashMap<String, Utilisateur>>();
		initViews();
		
		if (cd.isConnectingToInternet()) {
			new LoadUtilisateurs().execute();
		} 
		else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
		}
	
		btnSaveSubscription.setOnClickListener(onSaveSubscription);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liste_voyageurs, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private Button.OnClickListener onSaveSubscription=new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if (cd.isConnectingToInternet()) {
				new EnregistrerAbonnement().execute();
			} 
		}
	};
	
	private class LoadUtilisateurs extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListeVoyageurs.this);
			pDialog.setMessage(getResources().getString(R.string.alertLoadingNews));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String utilisateur_id = String.valueOf(UtilisateurActif.getInstance().getUtilisateur().getUtilisateurId());
			params.add(new BasicNameValuePair("utilisateur_id", utilisateur_id));
			
			JSONObject json = DataAccessController.getDataFromUrl(DataURL.GET_UTILISATEURS, "GET", params);

			try {
				int success = json.getInt(TAG_SUCCES);	// valider le chargement
				
				// si succes chargement
				if (success == 1) {
						
					// obtenir les nouvelles
					utilisateurs = json.getJSONArray(TAG_UTILISATEURS);
					
					for(int i = 0; i < utilisateurs.length(); i++) {
						JSONObject node = utilisateurs.getJSONObject(i);
						
						
						int uid = node.getInt(TAG_UID);
						String nom = node.getString(TAG_PRENOM);
						String prenom = node.getString(TAG_NOM);
						String courriel = node.getString(TAG_COURRIEL);
								
						Utilisateur u = new Utilisateur(uid, courriel, nom, prenom);

						LinkedHashMap<String, Utilisateur> map = new LinkedHashMap<String, Utilisateur>();
						
						map.put(String.valueOf(uid), u);
						
						liste.add(map);
					}
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String url) {
			pDialog.dismiss();
			
			/*
			 * Mettre a jour le UI
			 */
			runOnUiThread(new Runnable() {
				public void run() {
					adapteur = new ListeVoyageursAdapteur(ListeVoyageurs.this, R.layout.list_post, liste);	
					vListe.setAdapter(adapteur);
					vListe.refreshDrawableState();
				}
			});
		}
		
	}
	
	public class EnregistrerAbonnement extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			String message = "";
			
			if (adapteur != null && adapteur.getSelectedTags().size() > 0) {
				
				ArrayList<String> listeUids = adapteur.getSelectedTags();
				String uid = String.valueOf(UtilisateurActif.getInstance().getUtilisateur().getUtilisateurId());
				String uids = "";
								
				for(String id : listeUids) {
					Log.d("tags", id);
					uids += id + ".";
				}
				
				uids = uids.substring(0, uids.length()-1);
				
				params.add(new BasicNameValuePair("uid", uid));
				params.add(new BasicNameValuePair("uids", uids));
							
				try {
					JSONObject json = DataAccessController.getDataFromUrl(DataURL.AJOUT_ABONNEMENT, "GET", params);
				
					int success = json.getInt(TAG_SUCCES);	// valider le chargement
	
					message = json.getString(TAG_MESSAGE);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			return message;
		}
		
		@Override
		protected void onPostExecute(String result) {
			
			//si authentifier, rediriger vers la liste des nouvelles
			if (UtilisateurActif.getInstance().getUtilisateur() != null) {
				Intent intent = new Intent(ListeVoyageurs.this, ListeNouvelles.class);
	    		startActivity(intent);
			}
		}
		
	}
	
}
