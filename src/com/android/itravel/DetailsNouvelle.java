package com.android.itravel;

import java.util.ArrayList;
import java.util.List;

import model.Commentaire;
import model.Utilisateur;
import model.UtilisateurActif;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.itravel.constant.DataURL;
import com.android.itravel.util.ConnectionDetector;
import com.android.itravel.util.DataAccessController;
import com.android.itravel.util.Encryption;

public class DetailsNouvelle extends Activity {
	
	private long nouvelle_id;
	
	private LinearLayout lytCommentaires;
	private TextView txtNomContact;
	private TextView txtDateHeure;
	private TextView txtVille;
	private TextView txtPays;
	private TextView txtCommentaire;
	private EditText edtComment;
	private ImageView imvImage;
	private Button btnSend;
	private Bitmap	image;
	private ConnectionDetector cd;
	private ArrayList<Commentaire> liste;
	
	// JSON Node
	private static final String TAG_SUCCES = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_COMMENTAIRES = "commentaires";
	private static final String TAG_CID = "cid";
	private static final String TAG_DATEHEURE = "dateHeure";
	private static final String TAG_TEXTE = "texte";
	private static final String TAG_COMMENTEUR = "commenteur";
	
	// JSONArray de commentaires
	JSONArray commentaires = null;
	
	private void initViews() {
		lytCommentaires = (LinearLayout)findViewById(R.id.lytCommentaires);
		txtNomContact = (TextView)findViewById(R.id.txtDetailNomContact);
		txtCommentaire = (TextView)findViewById(R.id.txtDetailCommentaire);
		txtVille= (TextView)findViewById(R.id.txtDetailVille);
		txtPays = (TextView)findViewById(R.id.txtDetailPays);
		txtDateHeure = (TextView)findViewById(R.id.txtDetailDateHeure);
		edtComment = (EditText)findViewById(R.id.edtDetailAjoutCommentaire);
		imvImage = (ImageView)findViewById(R.id.imvDetailImage);
		btnSend = (Button)findViewById(R.id.btnDetailAjoutCommentaire);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_nouvelle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		initViews();
		
		liste = new ArrayList<Commentaire>();
		
		cd = new ConnectionDetector(getApplicationContext());
		
		boolean isInternetActive = cd.isConnectingToInternet();
		
		Intent intent = getIntent();
		
		nouvelle_id = intent.getLongExtra("nid", 0);
		
		String prenom = intent.getStringExtra("prenom");
		String nom = intent.getStringExtra("nom");
		String desc = intent.getStringExtra("commentaire");
		String date = intent.getStringExtra("date");
		String heure = intent.getStringExtra("heure");
		String image = intent.getStringExtra("image");
		String ville = intent.getStringExtra("ville");
		String pays = intent.getStringExtra("pays");
		
		txtNomContact.setText(prenom+" "+nom);
		txtCommentaire.setText(desc);
		txtDateHeure.setText(date+" "+heure);
		txtPays.setText(ville);
		txtVille.setText(pays);
		
		btnSend.setOnClickListener(onSend);
		
		// si internet et nouvelle contient image
		if (isInternetActive && !image.equalsIgnoreCase("")) {
			new LoadImage().execute(image);
		} 
		
		if (isInternetActive && nouvelle_id > 0) {
			new LoadCommentaires(this).execute();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details_nouvelle, menu);
		return true;
	}
	
	private Button.OnClickListener onSend=new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (nouvelle_id > 0) {
				new EnregistrerCommentaire().execute();
			}
		}
	};
	
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
	
	private void ajouterLigneCommentaire(Context ctx, String comment, String commenteur, String dateHeure) {
		
		LinearLayout layout = new LinearLayout(ctx); 
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 1);
		layout.setLayoutParams(params);
		
		layout.setBackgroundColor(Color.parseColor("#b4e0ff"));
		layout.setOrientation(LinearLayout.VERTICAL);
		
		TextView lblDateHeure = new TextView(ctx);
		lblDateHeure.setText(dateHeure);
		lblDateHeure.setTextSize(14);
		lblDateHeure.setTextColor(Color.parseColor("#0063a9"));
		
		TextView lblComment = new TextView(ctx);
		lblComment.setText(comment);
		lblComment.setTextSize(14);
		lblComment.setTextColor(Color.parseColor("#0063a9"));
		
		TextView lblCommenteur = new TextView(ctx);
		lblCommenteur.setText(commenteur);
		lblCommenteur.setTextSize(14);
		lblCommenteur.setTextColor(Color.parseColor("#0063a9"));
        
        layout.addView(lblDateHeure);
        layout.addView(lblComment);
        layout.addView(lblCommenteur);
                
		lytCommentaires.addView(layout);
		
	}
	
	public class LoadCommentaires extends AsyncTask<String, String, String> {

		private Context ctx;
		
		public LoadCommentaires(Context c) {
			ctx = c;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("nouvelle_id", String.valueOf(nouvelle_id)));
			
			JSONObject json = DataAccessController.getDataFromUrl(DataURL.NOUVELLE_COMMENTAIRES, "GET", params);

			try {
				int success = json.getInt(TAG_SUCCES);	// valider le chargement
				
				// si succes chargement
				if (success == 1) {
						
					// obtenir les nouvelles
					commentaires = json.getJSONArray(TAG_COMMENTAIRES);
					
					for(int i = 0; i < commentaires.length(); i++) {
						JSONObject node = commentaires.getJSONObject(i);
						
						int cid = node.getInt(TAG_CID);
						String comment = node.getString(TAG_TEXTE);
						String dateHeure = node.getString(TAG_DATEHEURE);
						String commenteur = node.getString(TAG_COMMENTEUR);
						
						Commentaire c = new Commentaire(cid, comment, commenteur, dateHeure);
						liste.add(c);
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
			
			/*
			 * Mettre a jour le UI
			 */
			runOnUiThread(new Runnable() {
				public void run() {
					
					for(Commentaire c : liste) 
						ajouterLigneCommentaire(ctx, c.getCommentaireTexte(), c.getCommenteur(), c.getDateEmission());
						
				}
			});
		}
		
	}
	
	/*
	 * Enregistrer le commentaire
	 */

	public class EnregistrerCommentaire extends AsyncTask<String, String, String> {
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... args) {
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			String message = "";
			String comment = edtComment.getText().toString();
			String nom = UtilisateurActif.getInstance().getUtilisateur().getFullName();
			int uid = UtilisateurActif.getInstance().getUtilisateur().getUtilisateurId();
			
			params.add(new BasicNameValuePair("comment", comment));
			params.add(new BasicNameValuePair("nouvelle_id", String.valueOf(nouvelle_id)));
			params.add(new BasicNameValuePair("utilisateur_id", String.valueOf(uid)));
			
			try {
				JSONObject json = DataAccessController.getDataFromUrl(DataURL.AJOUT_COMMENTAIRE, "GET", params);
				
				int success = json.getInt(TAG_SUCCES);	// valider le chargement
				
				// si succes chargement
				if (success == 1) {
					int cid = json.getInt(TAG_CID);
					String dateHeure = json.getString(TAG_DATEHEURE);
					Commentaire c = new Commentaire(cid, comment, nom, dateHeure);
					
					liste.add(0, c);
				}
				
				message = json.getString(TAG_MESSAGE);
				
			}
			catch (JSONException e) {
				e.printStackTrace();
			}
			
			return message;
		}
		
		@Override
		protected void onPostExecute(String url) {
			
			lytCommentaires.removeAllViews();
			
			for(Commentaire c : liste) 
				ajouterLigneCommentaire(DetailsNouvelle.this, c.getCommentaireTexte(), c.getCommenteur(), c.getDateEmission());
			
			Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();
		}
		
	}

}
