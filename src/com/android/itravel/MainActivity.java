package com.android.itravel;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Nouvelle;
import model.Utilisateur;
import model.UtilisateurActif;

import com.android.itravel.NouveauCompte.EnregistrerUtilisateur;
import com.android.itravel.constant.DataURL;
import com.android.itravel.database.ITravelDbHelper;
import com.android.itravel.listadaptor.ListeNouvellesAdapteur;
import com.android.itravel.util.ConnectionDetector;
import com.android.itravel.util.DataAccessController;
import com.android.itravel.util.Encryption;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText edtEmail;
	private EditText edtPassword;
	private Button btnLogin;
	private Button btnNewAccount;
	private ListeNouvellesAdapteur adapteur;
	private ProgressDialog pDialog;			// Progress dialog
	private ConnectionDetector cd;
	
	//JSON node
	private static final String TAG_SUCCES = "success";
	private static final String TAG_USER = "user";
	private static final String TAG_UID = "utilisateur_id";
	private static final String TAG_EMAIL = "courriel";
	private static final String TAG_PRENOM = "prenom";
	private static final String TAG_NOM = "nom";
	
	private void initViews() {
		edtEmail = (EditText)findViewById(R.id.edtMainEmail);
		edtPassword = (EditText)findViewById(R.id.edtMainPassword);
		btnLogin = (Button)findViewById(R.id.btnMainLogin);
		btnNewAccount = (Button)findViewById(R.id.btnMainNewAccount);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initViews();
		cd = new ConnectionDetector(getApplicationContext());
		
		// Set listeners
		btnLogin.setOnClickListener(onLoginClick);
		btnNewAccount.setOnClickListener(onNewAccount);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private Button.OnClickListener onLoginClick=new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			
			if (cd.isConnectingToInternet()) {
				new AuthentifierUtilisateur().execute();
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private Button.OnClickListener onNewAccount=new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, NouveauCompte.class);
			
    		startActivity(intent);
			
		}
	};
	
	/*
	 * Authentifier l'utilisateur
	 */

	public class AuthentifierUtilisateur extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage(getResources().getString(R.string.txtAuthenticating));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			String email = edtEmail.getText().toString();
			String password = edtPassword.getText().toString();
			
			// hash password
			password = Encryption.hashSHA256(password);
			
			JSONObject json = DataAccessController.securePost(DataURL.GET_UTILISATEUR, "GET", params, email, password);
			
			if (json != null) {
				try {
					int success = json.getInt(TAG_SUCCES);	// valider le chargement
									
					// si succes chargement
					if (success == 1) {
						
						int uid = json.getInt(TAG_UID);
						String courriel = json.getString(TAG_EMAIL);
						String prenom = json.getString(TAG_PRENOM);
						String nom = json.getString(TAG_NOM);
							
						Utilisateur u = new Utilisateur(uid, courriel, nom, prenom);
						UtilisateurActif.getInstance().setUtilisateur(u);
						
					} 
					else {
						UtilisateurActif.getInstance().setUtilisateur(null);
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.alertFailedLogin), Toast.LENGTH_SHORT).show();
					}
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String url) {
			pDialog.dismiss();
			
			//si authentifier, rediriger vers la liste des nouvelles
			if (UtilisateurActif.getInstance().getUtilisateur() != null) {
				Intent intent = new Intent(MainActivity.this, ListeNouvelles.class);
	    		startActivity(intent);
			}
		}
		
	}

}
