package com.android.itravel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.ConnectionDetector;
import model.DataURL;
import model.JSONParser;
import model.ListeNouvellesAdapteur;
import model.Nouvelle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
	private JSONParser jParser;				// JSON Parser
	private ConnectionDetector cd;
	
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
		
		jParser = new JSONParser();
		liste  = new ArrayList<LinkedHashMap<String, Nouvelle>>();
		cd = new ConnectionDetector(getApplicationContext());
		
		boolean isInternetActive = cd.isConnectingToInternet();
		
		if (isInternetActive) {
			new LoadNouvellesUtilisateur().execute();
			vListe.setOnItemClickListener(onPostClick);
		} 
		else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
		}
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
			// TODO Auto-generated method stub
			Intent intent = new Intent(ListeNouvelles.this, DetailsNouvelle.class);
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
			pDialog.setMessage(getResources().getString(R.string.loadingAllPosts));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(DataURL.getNouvellesVoyageur(), "GET", params);
						
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
												
						Nouvelle n = new Nouvelle(name, comment, 1);
						
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
