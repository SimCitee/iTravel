package com.android.itravel;

import com.android.itravel.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MesNouvellesAjouter extends Activity {

	ImageView i_image = null;
	EditText e_comment = null;
	TextView t_position = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles_ajouter);
		

		
		i_image = (ImageView)findViewById(R.id.mes_nouvelles_ajouter_image);
		e_comment  = (EditText)findViewById(R.id.mes_nouvelles_ajouter_commentaire_text);
		t_position = (TextView)findViewById(R.id.mes_nouvelles_ajouter_position_textview);
		
		Button save;
		
		Intent intent = getIntent();
			
		//String title = intent.getStringExtra("title");
		//String description = intent.getStringExtra("description");
		//String icon_id = intent.getStringExtra("icon_id");
		
		//e_title.setText(title);
		//e_desc.setText(description);
		
		save = (Button)findViewById(R.id.mes_nouvelles_ajouter_sauvegarder_button);
		save.setOnClickListener(onSave);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
/*
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
*/
		return true;

	}
	
	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
	
			Intent intent = getIntent();
		
			//TODO Modifier pour ajouter l'id de la db
			intent.putExtra("id", 2);
			//intentResult.putExtra("image_id", i_image.getId());
			intent.putExtra("commentaire", e_comment.getText().toString());	
			
			setResult(RESULT_OK, intent);
						
			finish();
		}

	};

}
