package com.android.itravel;

import com.android.itravel.R;

import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;


import model.EnvironmentVariables;
import model.ExifPositionUtil;
import model.BitmapRotator;
import model.PositionUtil;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.widget.ImageButton;


public class MesNouvellesAjouter extends Activity {

	  
	private File photoFile = null;		//Fichier d'image physique
	private Timestamp photoTimestamp = null;
	
	private static final int CAMERA_REQUEST_CODE = 1;
	private ImageView i_image = null;
	private EditText e_comment = null;
	private TextView t_position = null;
	private Double photoLatitude = null;
	private Double photoLongitude = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ImageButton takePicture;
		Button save;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mes_nouvelles_ajouter);

		i_image = (ImageView)findViewById(R.id.mes_nouvelles_ajouter_image);
		e_comment  = (EditText)findViewById(R.id.mes_nouvelles_ajouter_commentaire_text);
		t_position = (TextView)findViewById(R.id.mes_nouvelles_ajouter_position_textview);

		takePicture = (ImageButton)findViewById(R.id.mes_nouvelles_ajouter_photo_button);
		takePicture.setOnClickListener(onPictureTaken);
		
		save = (Button)findViewById(R.id.mes_nouvelles_ajouter_sauvegarder_button);
		save.setOnClickListener(onSave);

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;

	}
	
	private View.OnClickListener onPictureTaken = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
				
			//Vérifie si le folder de l'application existe
			File imageDirectory = new File(Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER);
			if(!imageDirectory.isDirectory()) {

				//Crée le dossier d'image de l'application
				imageDirectory.mkdir();
			}
			
			//Chemin où sont stocké les images
			String path=Environment.getExternalStorageDirectory().getPath() + EnvironmentVariables.IMAGE_FOLDER;
			
			//Timestamp de la photo (utilisé seulement comme id, voir plus bas)
			Calendar calendar = Calendar.getInstance();
			photoTimestamp = new Timestamp(calendar.getTime().getTime());
			
			//Formatage pour donner un identifiant à l'image
			//NB : quand le serveur sera implanté, ce sera lui qui définira l'identifiant de l'image
			//Champ incrémental de la base de données
			String strTimestamp = photoTimestamp.toString();
			strTimestamp = strTimestamp.replaceAll("[\\s\\-.:]", "");
			
			//Nommage du fichier d'image
			photoFile = new File(path, strTimestamp + ".jpg");
			
			//Crée le fichier d'image (vide) sur le disque 
			try {
				photoFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			Uri outputFileUri = Uri.fromFile(photoFile);
			Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

			startActivityForResult(intent, CAMERA_REQUEST_CODE);
		}

	};
	
	
	private View.OnClickListener onSave = new View.OnClickListener() {

		@Override
		public void onClick(View view) {
	
			Intent intent = getIntent();
			
			Calendar calendar = Calendar.getInstance();
			photoTimestamp = new Timestamp(calendar.getTime().getTime());

			
			String strTimestamp = photoTimestamp.toString();
			strTimestamp = strTimestamp.replaceAll("[\\s\\-.:]", "");

			//Identifiant de la nouvelle (il provindra éventuellement du serveur)
			intent.putExtra("nouvelle_id", Long.parseLong(strTimestamp));
			
			//Si aucune photo n'a été prise
			if(photoFile == null)
			{
				Log.i("", "Pas image");
				intent.putExtra("id_image", "0"); //Pas d'image de fournit
			}
			else
			{
				Log.i("", "a une image");
				intent.putExtra("id_image", (String) photoFile.getName());
				
			}
			
			//Commentaire
			if(e_comment.getText().length() > 0)
			{
	
				intent.putExtra("commentaire", e_comment.getText().toString());
			}
			else
			{
				Log.i("", "Pas de texte");
				intent.putExtra("commentaire", "");
			}
			
			//Si les coordonnées ne peuvent être prise dans la photo
			if(photoLatitude == null || photoLongitude == null)
			{
				
				//Classe utilitaire qui ne récupère la position qu'une seule foisd
				//PositionUtil positionUtil = new PositionUtil();
				
				
				Double d = 22.22;
				//TODO implanter la recherche des coordonnées du GPS
				intent.putExtra("latitude", d);
				intent.putExtra("longitude", d);
			}
			else
			{
				//Coordonnée prise sur la photo
				intent.putExtra("latitude", photoLatitude);
				intent.putExtra("longitude", photoLongitude);
			}
			
			
			intent.putExtra("date",  String.valueOf(calendar.get(Calendar.DATE)));
			intent.putExtra("heure",  String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
		
			setResult(RESULT_OK, intent);
						
			finish();
		}

	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		 if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK)
		 {

			 ////////////////////////////////////////////////////////////
			 BitmapRotator photoRotator = new BitmapRotator(photoFile.getAbsolutePath());
			 photoRotator.doRotaionIfNeeded();
			 Bitmap myBitmap = photoRotator.getBitmap();
			 photoRotator.save();
	          
			 ///////////////////////////////////////////////////////////
			 
			 i_image.setImageBitmap(myBitmap);
			 
			 
			 ExifInterface exif;
			try {
				exif = new ExifInterface(photoFile.getAbsolutePath());
				
				ExifPositionUtil exifPositionUtil = new ExifPositionUtil(exif);
				
				//Garde en mémoire la latitude/longitude de l'image (si activé)
				photoLatitude =  exifPositionUtil.getLatitude();
				photoLongitude = exifPositionUtil.getLongitude();
				
				if(photoLatitude != null && photoLongitude != null)
				{
					//Écrit la position dans le textview
					t_position.setText(photoLatitude.toString() + ", " + photoLongitude.toString());
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		 }
		 
		 //Opération annulée
		 else if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED)
		 {
			 //Supprime le fichier d'image si l'opération de prise de photo a été annulée
			 
			 photoFile.delete();
			 photoFile = null;
			 
		 }
	  }

}
