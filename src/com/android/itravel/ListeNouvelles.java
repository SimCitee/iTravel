package com.android.itravel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import model.Nouvelle;
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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.itravel.constant.DataURL;
import com.android.itravel.database.ITravelContract.EntreeNouvelle;
import com.android.itravel.database.ITravelDbHelper;
import com.android.itravel.listadaptor.ListeNouvellesAdapteur;
import com.android.itravel.util.ConnectionDetector;
import com.android.itravel.util.DataAccessController;

public class ListeNouvelles extends Activity {

	private ListView vListe;
	private Button btnMyPost;
	private ListeNouvellesAdapteur adapteur;
	private ArrayList<LinkedHashMap<String, Nouvelle>> liste;
	private ProgressDialog pDialog;			// Progress dialog
	private ConnectionDetector cd;
	private ITravelDbHelper dbHelper;
	private SQLiteDatabase db, rdb;
	
	// JSON Node
	private static final String TAG_SUCCES = "success";
	private static final String TAG_NOUVELLES = "nouvelles";
	private static final String TAG_PID = "id";
	private static final String TAG_COMMENT = "comment";
	private static final String TAG_IMAGE = "image";
	private static final String TAG_DATE = "date";
	private static final String TAG_HEURE = "heure";
	private static final String TAG_INTERVALLE = "minute";
	private static final String TAG_VILLE = "ville";
	private static final String TAG_PAYS = "pays";
	
	private static final String TAG_NOM = "nom";
	private static final String TAG_PRENOM = "prenom";
	private static final String TAG_UID = "uid";
	private static final String TAG_COURRIEL = "courriel";
	
	// JSONArray de nouvelles
	JSONArray nouvelles = null;
	
	private void initViews() {
		vListe = (ListView)findViewById(R.id.lvDetailListeCommentaire);
		btnMyPost = (Button)findViewById(R.id.btnMyPosts);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_liste_nouvelles);
		initViews();

		liste  = new ArrayList<LinkedHashMap<String, Nouvelle>>();
		cd = new ConnectionDetector(getApplicationContext());
		adapteur = new ListeNouvellesAdapteur(this, R.layout.list_post, liste);
        /*dbHelper = new ITravelDbHelper(this);
        db = dbHelper.getWritableDatabase();
        rdb = dbHelper.getReadableDatabase();*/
                   
        vListe.setAdapter(adapteur);
        vListe.setOnItemClickListener(onPostClick);
        btnMyPost.setOnClickListener(onMyPost);
        
        //chargerNouvelles();
		
		if (cd.isConnectingToInternet()) {
			new LoadNouvellesUtilisateur().execute();
			vListe.setOnItemClickListener(onPostClick);
		} 
		else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void chargerNouvelles() {
		
		Cursor c = dbHelper.getNouvellesUtilisateur("1");
    	
    	if (c.moveToFirst()){
    		
    		liste.clear();
    		
    		do {
			   
    			String nouvelle_id = c.getString(c.getColumnIndex(EntreeNouvelle._ID));
    			String txt = c.getString(c.getColumnIndex(EntreeNouvelle._TEXTE));
    			String date = c.getString(c.getColumnIndex(EntreeNouvelle._DATE));
    			LinkedHashMap<String, Nouvelle> map = new LinkedHashMap<String, Nouvelle>();
				Nouvelle n = new Nouvelle();
				
				n.setNouvelleTexte(txt);
				n.setNouvelleDate(date);
    			
				map.put(nouvelle_id, n);
		      
    			liste.add(map);
		      
		      
		   } while(c.moveToNext());
		}
		c.close();	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.liste_nouvelles, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	//Ajout
        if (item.getItemId() == R.id.liste_nouvelles_actionbar_addsubscription) {
        	Intent intent = new Intent(ListeNouvelles.this, ListeVoyageurs.class);
        	startActivity(intent);
        }

        return true;
    }
	
	private Button.OnClickListener onMyPost=new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ListeNouvelles.this, MesNouvelles.class);
    		startActivity(intent);
		}
	};
	
	private AdapterView.OnItemClickListener onPostClick=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
			
			LinkedHashMap<String, Nouvelle> item = (LinkedHashMap<String, Nouvelle>) liste.get(position);
			Object key = item.keySet().iterator().next();
			Nouvelle n = item.get(key);
			
			Intent intent = new Intent(ListeNouvelles.this, DetailsNouvelle.class);
			
			intent.putExtra("nid", n.getNouvelleId());
			intent.putExtra("prenom", n.getUtilisateur().getPrenom());
			intent.putExtra("nom", n.getUtilisateur().getNom());
			intent.putExtra("commentaire", n.getNouvelleTexte());
			intent.putExtra("date", n.getNouvelleDate());
			intent.putExtra("heure", n.getNouvelleHeure());
			intent.putExtra("image", n.getImageId());
			intent.putExtra("ville", n.getVille());
			intent.putExtra("pays", n.getPays());
			
    		startActivity(intent);
			
		}
	};
	
	/*
	 * Charger toutes les nouvelles d'un utilisateur
	 */

	private class LoadNouvellesUtilisateur extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListeNouvelles.this);
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
			JSONObject json = DataAccessController.getDataFromUrl(DataURL.NOUVELLES_VOYAGEURS, "GET", params);

			try {
				int success = json.getInt(TAG_SUCCES);	// valider le chargement
				
				// si succes chargement
				if (success == 1) {
						
					// obtenir les nouvelles
					nouvelles = json.getJSONArray(TAG_NOUVELLES);
					
					for(int i = 0; i < nouvelles.length(); i++) {
						JSONObject node = nouvelles.getJSONObject(i);
						
						String id = node.getString(TAG_PID);
						String comment = node.getString(TAG_COMMENT);
						String image = node.getString(TAG_IMAGE);
						String dt = node.getString(TAG_DATE);
						String hr = node.getString(TAG_HEURE);
						String pays = node.getString(TAG_PAYS);
						String ville = node.getString(TAG_VILLE);
						int minute = node.getInt(TAG_INTERVALLE);
						
						int uid = node.getInt(TAG_UID);
						String nom = node.getString(TAG_PRENOM);
						String prenom = node.getString(TAG_NOM);
						String courriel = node.getString(TAG_COURRIEL);
								
						Utilisateur u = new Utilisateur(uid, courriel, nom, prenom);
						Nouvelle n = new Nouvelle(Long.parseLong(id), comment, image, pays, ville, dt, hr, u, minute);
						
						// Nouvelle Hashmap de nouvelles
						LinkedHashMap<String, Nouvelle> map = new LinkedHashMap<String, Nouvelle>();
						
						map.put(id, n);
						
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
					ListeNouvellesAdapteur adapt = new ListeNouvellesAdapteur(ListeNouvelles.this, R.layout.list_post, liste);	
					vListe.setAdapter(adapt);
					vListe.refreshDrawableState();
				}
			});
		}
		
	}

	public static String getTagSucces() {
		return TAG_SUCCES;
	}

	public static String getTagNouvelles() {
		return TAG_NOUVELLES;
	}

	public static String getTagPid() {
		return TAG_PID;
	}

	public static String getTagComment() {
		return TAG_COMMENT;
	}
}
