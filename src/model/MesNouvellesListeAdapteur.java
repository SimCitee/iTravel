package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MesNouvellesListeAdapteur  extends ArrayAdapter<Nouvelle>{
	//Red�finition du constructeur
		//Param�tre : 1. R�f�rence au contexte (le this sera "toujours" utilis�)
		//			  2. R�f�rence au layout qui sera utilis� pour placer les items
		//			  3. Liste d'objets qui contienne les donn�es � afficher dans la liste
		public MesNouvellesListeAdapteur(Context context, int resource, ArrayList<Nouvelle> objects) {
			super(context, resource, objects);
			
		}
		
		//Sera appel� � chaque cr�ation d'une ligne (cr�era la layout avec les donn�es pour un item de la liste)
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
					 iiv.setImageResource(p.getImageId());
				 }
			 }
			 
			 return v; 
		} 
		
		private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	/*	 
	    public SelectionAdapter(Context context, int resource,
	            int textViewResourceId, String[] objects) {
	        super(context, resource, textViewResourceId, objects);
	    }
	*/
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
	
}
