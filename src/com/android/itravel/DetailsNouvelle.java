package com.android.itravel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import model.ConnectionDetector;
import model.DataAccessController;
import model.DataURL;
import model.ListeNouvellesAdapteur;
import model.Nouvelle;

import com.android.itravel.ListeNouvelles.LoadNouvellesUtilisateur;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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
		
		if (isInternetActive) {
			new LoadImage().execute();
		} 
		else {
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.noInternetConnection), Toast.LENGTH_SHORT).show();
		}
		
		Intent intent = getIntent();
		
		String nom = intent.getStringExtra("nomContact");
		String desc = intent.getStringExtra("commentaire");
		
		txtNomContact.setText(nom);
		txtCommentaire.setText(desc);
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
						
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			

			try {
				image = DataAccessController.getBitmapFromURL("http://simond.byethost7.com/download.jpg");	
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
	
	/*
	 * 
	 * public class ResizableImageView extends ImageView {

    public ResizableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
         Drawable d = getDrawable();

         if(d!=null){
                 // ceil not round - avoid thin vertical gaps along the left/right edges
                 int width = MeasureSpec.getSize(widthMeasureSpec);
                 int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
                 setMeasuredDimension(width, height);
         }else{
                 super.onMeasure(widthMeasureSpec, heightMeasureSpec);
         }
    }

}
	 */


}
