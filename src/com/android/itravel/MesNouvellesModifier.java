package com.android.itravel;

import java.io.File;

import com.android.itravel.constant.EnvironmentVariables;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MesNouvellesModifier extends Activity {

	
		private ImageView i_image = null;
		private EditText e_comment = null;
		private TextView t_position = null;
		
		private int itemListPosition;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_mes_nouvelles_modifier);
			
			i_image = (ImageView)findViewById(R.id.mes_nouvelles_modifier_image);
			e_comment  = (EditText)findViewById(R.id.mes_nouvelles_modifier_commentaire_text);
			t_position = (TextView)findViewById(R.id.mes_nouvelles_modifier_position_textview);
			
			Button save;
			
			Intent intent = getIntent();
			
			getActionBar().setDisplayHomeAsUpEnabled(true);
			
			itemListPosition = intent.getIntExtra("listPosition", 0);
			Long nouvelleId = intent.getLongExtra("nouvelle_id", 0);
			String imageId = intent.getStringExtra("id_image");
			String commentaire = intent.getStringExtra("commentaire");
			Double positionPays = intent.getDoubleExtra("latitude", 0);
			Double positionVille = intent.getDoubleExtra("longitude", 0);
			
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
			 t_position.setText(positionPays + ", " + positionVille);
			
			 save = (Button)findViewById(R.id.mes_nouvelles_modifier_sauvegarder_button);
			 save.setOnClickListener(onSave);
			
			 getActionBar().setDisplayHomeAsUpEnabled(true);
			
			
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}
		
		private View.OnClickListener onSave = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
		
				Intent intentResult = new Intent();
			
				intentResult.putExtra("id", itemListPosition);
				
				//Commentaire
				if(e_comment.getText().length() > 0)
				{
					intentResult.putExtra("commentaire", e_comment.getText().toString());
				}
				else
				{
					Log.i("", "Pas de texte");
					intentResult.putExtra("commentaire", "");
				}

				setResult(RESULT_OK, intentResult);
							
				finish();
			}

		};

}
