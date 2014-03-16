package com.android.itravel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.DataURL;
import model.JSONParser;
import model.ListeNouvellesAdapteur;
import model.Nouvelle;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Button;
import android.widget.AdapterView.OnItemLongClickListener;

public class ListeNouvelles extends Activity {

	private ListView vListe;
	private Button btnMyPost;
	private ListeNouvellesAdapteur adapteur;
	private ArrayList<HashMap<String, Nouvelle>> liste;
	private ProgressDialog pDialog;			// Progress dialog
	private JSONParser jParser;				// JSON Parser
	
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
		new LoadNouvellesUtilisateur().execute();
		
		liste  = new ArrayList<HashMap<String, Nouvelle>>();
		
		/*adapteur = new ListeNouvellesAdapteur(this, R.layout.list_post, liste);
              
        vListe.setAdapter(adapteur);*/
        vListe.setOnItemClickListener(onPostClick);
         
        /*Nouvelle Nouvelle1 = new Nouvelle("Tomates", "Acheter 5 tomates", R.drawable.ic_launcher);
        Nouvelle Nouvelle2 = new Nouvelle("Carottes", "Bio", R.drawable.ic_launcher);
        Nouvelle Nouvelle3 = new Nouvelle("Cereales", "Corn Flake", R.drawable.ic_launcher);
        Nouvelle Nouvelle4 = new Nouvelle("Lait", "Quebon", R.drawable.ic_launcher);
        Nouvelle Nouvelle5 = new Nouvelle("Pain", "Gadoua", R.drawable.ic_launcher);
        Nouvelle Nouvelle6 = new Nouvelle("Tomates", "Acheter 5 tomates", R.drawable.ic_launcher);
        Nouvelle Nouvelle7 = new Nouvelle("Carottes", "Bio", R.drawable.ic_launcher);
        Nouvelle Nouvelle8 = new Nouvelle("Cereales", "Corn Flake", R.drawable.ic_launcher);
        Nouvelle Nouvelle9 = new Nouvelle("Lait", "Quebon", R.drawable.ic_launcher);
        Nouvelle Nouvelle10 = new Nouvelle("Pain", "Gadoua", R.drawable.ic_launcher);
        Nouvelle Nouvelle11 = new Nouvelle("Tomates", "Acheter 5 tomates", R.drawable.ic_launcher);
        Nouvelle Nouvelle12 = new Nouvelle("Carottes", "Bio", R.drawable.ic_launcher);
        Nouvelle Nouvelle13 = new Nouvelle("Cereales", "Corn Flake", R.drawable.ic_launcher);
        Nouvelle Nouvelle14 = new Nouvelle("Lait", "Quebon", R.drawable.ic_launcher);
        Nouvelle Nouvelle15 = new Nouvelle("Pain", "Gadoua", R.drawable.ic_launcher);
		
        liste.add(Nouvelle1);
        liste.add(Nouvelle2);
        liste.add(Nouvelle3);
        liste.add(Nouvelle4);
        liste.add(Nouvelle5);
        liste.add(Nouvelle6);
        liste.add(Nouvelle7);
        liste.add(Nouvelle8);
        liste.add(Nouvelle9);
        liste.add(Nouvelle10);
        liste.add(Nouvelle11);*/
        
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
			pDialog.setMessage("Chargement des nouvelles. Veuillez patienter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(DataURL.getNouvellesVoyageur(), "GET", params);
			
			Log.d("Nouvelles: ", json.toString());
			
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
												
						Nouvelle n = new Nouvelle(name, "test", 1);
						
						// Nouvelle Hashmap de nouvelles
						HashMap<String, Nouvelle> map = new HashMap<String, Nouvelle>();
						
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
