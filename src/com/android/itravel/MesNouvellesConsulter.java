package com.android.itravel;

import java.io.File;

import com.android.itravel.constant.EnvironmentVariables;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MesNouvellesConsulter extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles_consulter);
		
		ImageView i_image = null;
		TextView e_comment = null;
		TextView t_position = null;
		
		i_image = (ImageView)findViewById(R.id.mes_nouvelles_consulter_image);
		e_comment  = (TextView)findViewById(R.id.mes_nouvelles_consulter_commentaire_textview);
		t_position = (TextView)findViewById(R.id.mes_nouvelles_consulter_position_textview);
		
		Intent intent = getIntent();
		
		String imageId = intent.getStringExtra("image_id");
		String commentaire = intent.getStringExtra("commentaire");
		String ville = intent.getStringExtra("ville");
		String pays = intent.getStringExtra("pays");
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		//Image avec le path
		 String imageCompleteName = Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER + "/" + imageId; 
		 	
		 File imageFile = new File(imageCompleteName);
		 
		 //Vérifie si l'image existe
		 if(imageFile.exists()) {
			 
			 //Affiche l'image
			 Bitmap myBitmap = BitmapFactory.decodeFile(imageCompleteName);
			 i_image.setImageBitmap(myBitmap);
		 }
		
		 e_comment.setText(commentaire);
		 t_position.setText(ville + ", " + pays);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mes_nouvelles_consulter, menu);
		return true;
	}

}
