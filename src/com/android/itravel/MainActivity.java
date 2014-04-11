package com.android.itravel;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import model.Nouvelle;

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
	private boolean authenticated = false;
	
	//JSON node
	private static final String TAG_SUCCES = "success";
	
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
			
			Intent intent = new Intent(MainActivity.this, ListeNouvelles.class);
			
    		startActivity(intent);
			
			/*if (cd.isConnectingToInternet()) {
				new AuthentifierUtilisateur().execute();
			} else {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
			}*/
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
			
			// Send email and password	
			params.add(new BasicNameValuePair("email", email));
			params.add(new BasicNameValuePair("password", password));
			
			JSONObject json = DataAccessController.getDataFromUrl(DataURL.NOUVELLES_VOYAGEURS, "POST", params);
			
			try {
				int success = json.getInt(TAG_SUCCES);	// valider le chargement
				
				// si succes chargement
				if (success == 1) {
					authenticated = true;
				} 
				else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.alertFailedLogin), Toast.LENGTH_SHORT).show();
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
			
			//si authentifier, rediriger vers la liste des nouvelles
			if (authenticated) {
				Intent intent = new Intent(MainActivity.this, ListeNouvelles.class);
				
	    		startActivity(intent);
				
			}
		}
		
	}

}
