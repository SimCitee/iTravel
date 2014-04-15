package com.android.itravel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import model.Nouvelle;

import com.android.itravel.ListeNouvelles.LoadNouvellesUtilisateur;
import com.android.itravel.constant.DataURL;
import com.android.itravel.listadaptor.ListeNouvellesAdapteur;
import com.android.itravel.util.ConnectionDetector;
import com.android.itravel.util.DataAccessController;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsNouvelle extends Activity {
	
	private TextView txtNomContact;
	private TextView txtCommentaire;
	private ImageView imvImage;
	private Bitmap	image;
	private ConnectionDetector cd;
	
	private void initViews() {
		txtNomContact = (TextView)findViewById(R.id.txtDetailNomContact);
		txtCommentaire = (TextView)findViewById(R.id.txtDetailCommentaire);
		imvImage = (ImageView)findViewById(R.id.imvDetailImage);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_nouvelle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		initViews();
		
		cd = new ConnectionDetector(getApplicationContext());
		
		boolean isInternetActive = cd.isConnectingToInternet();
		
		Intent intent = getIntent();
		
		String prenom = intent.getStringExtra("prenom");
		String nom = intent.getStringExtra("nom");
		String desc = intent.getStringExtra("commentaire");
		String date = intent.getStringExtra("date");
		String heure = intent.getStringExtra("heure");
		String image = intent.getStringExtra("image");
		
		Log.d("detail image", image);
		
		txtNomContact.setText(prenom+" "+nom);
		txtCommentaire.setText(desc);
		
		// si internet et nouvelle contient image
		if (isInternetActive && !image.equalsIgnoreCase("")) {
			new LoadImage().execute(image);
		} 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details_nouvelle, menu);
		return true;
	}
	
	/*
	 * Charger image
	 */

	public class LoadImage extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... args) {
			String imageStr = args[0];

			try {
				image = DataAccessController.getBitmapFromURL(DataURL.SERVER_URL+imageStr);	
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(String url) {
			
			/*
			 * Mettre a jour le UI
			 */
			runOnUiThread(new Runnable() {
				public void run() {
					imvImage.setImageBitmap(image);				
				}
			});
		}
		
	}

}
