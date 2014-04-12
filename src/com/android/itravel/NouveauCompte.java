package com.android.itravel;

import java.util.ArrayList;
import java.util.List;

import model.Utilisateur;
import model.UtilisateurActif;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.itravel.ListeNouvelles.LoadNouvellesUtilisateur;
import com.android.itravel.constant.DataURL;
import com.android.itravel.util.ConnectionDetector;
import com.android.itravel.util.DataAccessController;
import com.android.itravel.util.Encryption;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class NouveauCompte extends Activity {

	private EditText edtFirstname;
	private EditText edtLastname;
	private EditText edtEmail;
	private EditText edtPassword;
	private EditText edtRepeatPassword;
	private Button btnSave;
	private Button btnCancel;
	private ProgressDialog pDialog;
	private ConnectionDetector cd;
	
	//JSON node
	private static final String TAG_SUCCES = "success";
	private static final String TAG_ERREUR = "erreur";
	private static final String TAG_UID = "utilisateur_id";

	private void initViews() {
		edtFirstname = (EditText)findViewById(R.id.edtNouveauCompteFirstname);
		edtLastname = (EditText)findViewById(R.id.edtNouveauCompteLastname);
		edtEmail = (EditText)findViewById(R.id.edtNouveauCompteEmail);
		edtPassword = (EditText)findViewById(R.id.edtNouveauComptePassword);
		edtRepeatPassword = (EditText)findViewById(R.id.edtNouveauCompteRPassword);
		btnSave = (Button)findViewById(R.id.btnNouveauComtpeSave);
		btnCancel = (Button)findViewById(R.id.btnNouveauComtpeCancel);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nouveau_compte);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		initViews();
		cd = new ConnectionDetector(getApplicationContext());
		
		btnSave.setOnClickListener(onSave);
		btnCancel.setOnClickListener(onCancel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nouveau_compte, menu);
		return true;
	}
	
	private boolean validerChamps() {
		
		if (edtFirstname.getText().toString().matches("") ||
			edtLastname.getText().toString().matches("") ||
			edtEmail.getText().toString().matches("") ||
			edtPassword.getText().toString().matches("") ||
			edtRepeatPassword.getText().toString().matches("")
			) {
		    Toast.makeText(this, getResources().getString(R.string.alertMissingField), Toast.LENGTH_SHORT).show();
		    return false;
		}
		
		return true;
	}
	
	private boolean validerMDP() {
		boolean isValid = (edtPassword.getText().toString().matches(edtRepeatPassword.getText().toString()));
		
		if(!isValid)
			Toast.makeText(this, getResources().getString(R.string.alertPasswordsDiffer), Toast.LENGTH_SHORT).show();
		
		return isValid;
		
	}
	
	private Button.OnClickListener onSave=new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (validerChamps() && validerMDP()) {
				if (cd.isConnectingToInternet()) {
					new EnregistrerUtilisateur().execute();
				} else {
					Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
				}
			}
		}
	};
	
	private Button.OnClickListener onCancel=new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			finish();
		}
	};
	
	/*
	 * Enregistrer l'utilisateur
	 */

	public class EnregistrerUtilisateur extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(NouveauCompte.this);
			pDialog.setMessage(getResources().getString(R.string.alertSavingAccount));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			String firstname = edtFirstname.getText().toString();
			String lastname = edtLastname.getText().toString();
			String email = edtEmail.getText().toString();
			String password = edtPassword.getText().toString();
			
			// hash password
			password = Encryption.hashSHA256(password);
			
			// Send email and password	
			params.add(new BasicNameValuePair("prenom", firstname));
			params.add(new BasicNameValuePair("nom", lastname));
			params.add(new BasicNameValuePair("courriel", email));
			params.add(new BasicNameValuePair("ljn_mot_passe", password));
			
			try {
				JSONObject json = DataAccessController.getDataFromUrl(DataURL.AJOUT_UTILISATEUR, "GET", params);
			
				int success = json.getInt(TAG_SUCCES);	// valider le chargement

				// si succes chargement
				if (success == 1) {
					int uid = json.getInt(TAG_UID);
					
					Utilisateur u = new Utilisateur(uid, email, lastname, firstname);
					UtilisateurActif.getInstance().setUtilisateur(u);
				} 
				else {
					UtilisateurActif.getInstance().setUtilisateur(null);
					String error = json.getString(TAG_ERREUR);
					Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
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
			if (UtilisateurActif.getInstance().getUtilisateur() != null) {
				Intent intent = new Intent(NouveauCompte.this, ListeNouvelles.class);
	    		startActivity(intent);
			}
		}
		
	}

}
