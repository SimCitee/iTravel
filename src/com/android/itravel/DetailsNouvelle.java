package com.android.itravel;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DetailsNouvelle extends Activity {
	
	private TextView txtNomContact;
	private TextView txtCommentaire;
	private ImageView imvImage;
	
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

}
