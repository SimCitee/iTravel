package com.android.itravel;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MesNouvellesModifier extends Activity {

	
		ImageView i_image = null;
		EditText e_comment = null;
		TextView t_position = null;
		
		//Button save;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_mes_nouvelles_modifier);
			
			i_image = (ImageView)findViewById(R.id.mes_nouvelles_modifier_image);
			e_comment  = (EditText)findViewById(R.id.mes_nouvelles_modifier_commentaire_text);
			t_position = (TextView)findViewById(R.id.mes_nouvelles_modifier_position_textview);
			
			Button save;
			
			Intent intent = getIntent();
				
			//String title = intent.getStringExtra("title");
			//String description = intent.getStringExtra("description");
			
			String id = intent.getStringExtra("id");
			String imageId = intent.getStringExtra("image_id");
			String commentaire = intent.getStringExtra("commentaire");
			String positionPays = intent.getStringExtra("position_pays");
			String positionVille = intent.getStringExtra("position_ville");
			
			
			//String icon_id = intent.getStringExtra("icon_id");
			
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
			
				intentResult.putExtra("id", getIntent().getStringExtra("id"));
				//intentResult.putExtra("image_id", i_image.getId());
				intentResult.putExtra("commentaire", e_comment.getText().toString());		

				setResult(RESULT_OK, intentResult);
							
				finish();
			}

		};

	


}
