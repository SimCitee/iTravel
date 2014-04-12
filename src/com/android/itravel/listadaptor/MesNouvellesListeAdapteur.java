package com.android.itravel.listadaptor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.android.itravel.constant.EnvironmentVariables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import model.*;

public class MesNouvellesListeAdapteur  extends ArrayAdapter<Nouvelle>{
	
	private ArrayList<Nouvelle> itemList;
	//Redéfinition du constructeur
		//Paramètre : 1. Référence au contexte (le this sera "toujours" utilisé)
		//			  2. Référence au layout qui sera utilisé pour placer les items
		//			  3. Liste d'objets qui contienne les données à afficher dans la liste
		public MesNouvellesListeAdapteur(Context context, int resource, ArrayList<Nouvelle> objects) {
			super(context, resource, objects);
			
			itemList = objects;
			
		}
		
		public Nouvelle getItem(int position) {
			
			if (itemList != null)
				return itemList.get(position);
			
			return null;
			
		}

		
		//Sera appelé à chaque création d'une ligne (créera la layout avec les données pour un item de la liste)
		public View getView(int position, View convertView, ViewGroup parent) {
			 View v = convertView;
			 if (v == null) { 
				 LayoutInflater vi = LayoutInflater.from(getContext());
				 
				 
				 
				 //NB : Le layout se trouve dans com.example.epicerie.R.layout.activity_epicerie
				 v = vi.inflate(com.android.itravel.R.layout.liste_mes_nouvelles_layout, null);
			 } 
 
			 Nouvelle p = getItem(position);

			 if (p != null) { 

				 TextView ittv = (TextView) v.findViewById(com.android.itravel.R.id.mesNouvellesListeText);  
				 ImageView iiv = (ImageView) v.findViewById(com.android.itravel.R.id.mesNouvellesListeImage); 
				 
				 if (ittv != null) {

					 ittv.setText(p.getNouvelleTexte()); 
				 }
				 
				 if (iiv != null) {
				 
					 //S'il y a une image à charger
					 if(p.getImageId() != null)
					 {
						 
						 //Image avec le path
						 String imageCompleteName = Environment.getExternalStorageDirectory() + EnvironmentVariables.IMAGE_FOLDER + "/" + p.getImageId(); 
				
						 File imageFile = new File(imageCompleteName);
						 
						 //Vérifie si l'image existe
						 if(imageFile.exists()) {
		 			 
							 Bitmap myBitmap = BitmapFactory.decodeFile(imageCompleteName);
							 iiv.setImageBitmap(myBitmap);
						 }
						 else
						 {
							//iiv.setImageResource(p.getImageId());
						 }
					 }
					 else
					 {
						 iiv.setImageResource(com.android.itravel.R.drawable.thumbnail_default);
					 }
					 
				 }
			 }
			 
			 return v; 
		} 
		
		private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	
	    public void setNewSelection(int position, boolean value) {
	        mSelection.put(position, value);
	        notifyDataSetChanged();
	    }

	    public boolean isPositionChecked(int position) {
	        Boolean result = mSelection.get(position);
	        return result == null ? false : result;
	    }

	    public Set<Integer> getCurrentCheckedPosition() {
	        return mSelection.keySet();
	    }

	    public void removeSelection(int position) {
	        mSelection.remove(position);
	        notifyDataSetChanged();
	    }

	    public void clearSelection() {
	        mSelection = new HashMap<Integer, Boolean>();
	        notifyDataSetChanged();
	    }
	    
	    public void setItemList(List<Nouvelle> itemList) {
	    	
	    	this.itemList = (ArrayList<Nouvelle>) itemList;
	    	
	    }
	
}
