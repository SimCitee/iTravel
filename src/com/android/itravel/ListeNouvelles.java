package com.android.itravel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.itravel.constant.DataURL;
import com.android.itravel.database.ITravelDbHelper;
import com.android.itravel.database.ITravelContract.EntreeNouvelle;
import com.android.itravel.database.ITravelContract.EntreeUtilisateur;
import com.android.itravel.listadaptor.ListeNouvellesAdapteur;
import com.android.itravel.util.ConnectionDetector;
import com.android.itravel.util.DataAccessController;

import model.JSONParser;
import model.Nouvelle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Button;
import android.widget.Toast;

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
	private static final String TAG_NOUVELLES = "products";
	private static final String TAG_PID = "pid";
	private static final String TAG_CONTACT_NAME = "name";
	private static final String TAG_COMMENT = "comment";
	
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
        dbHelper = new ITravelDbHelper(this);
        db = dbHelper.getWritableDatabase();
        rdb = dbHelper.getReadableDatabase();
                   
        vListe.setAdapter(adapteur);
        vListe.setOnItemClickListener(onPostClick);
        
        chargerNouvelles();
        
        
		/*boolean isInternetActive = cd.isConnectingToInternet();
		
		if (isInternetActive) {
			new LoadNouvellesUtilisateur().execute();
			vListe.setOnItemClickListener(onPostClick);
		} 
		else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
		}*/
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
	
	private AdapterView.OnItemClickListener onPostClick=new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
			
			LinkedHashMap<String, Nouvelle> item = (LinkedHashMap<String, Nouvelle>) liste.get(position);
			Object key = item.keySet().iterator().next();
			Nouvelle n = item.get(key);
			
			Intent intent = new Intent(ListeNouvelles.this, DetailsNouvelle.class);
			
			intent.putExtra("nomContact", n.getPays());
			intent.putExtra("commentaire", n.getVille());
			
    		startActivity(intent);
			
		}
	};
	
	/*
	 * Charger toutes les nouvelles d'un utilisateur
	 */

	public class LoadNouvellesUtilisateur extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ListeNouvelles.this);
			//pDialog.setMessage(getResources().getString(R.string.loadingAllPosts));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
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
						String name = node.getString(TAG_CONTACT_NAME);
						String comment = node.getString(TAG_COMMENT);
												
						Nouvelle n = new Nouvelle();
						
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

	public static String getTagContactName() {
		return TAG_CONTACT_NAME;
	}

	public static String getTagComment() {
		return TAG_COMMENT;
	}
}
