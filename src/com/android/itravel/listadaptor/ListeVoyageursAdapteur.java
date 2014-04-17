package com.android.itravel.listadaptor;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import model.Commentaire;
import model.Utilisateur;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.itravel.R;

public class ListeVoyageursAdapteur extends ArrayAdapter<LinkedHashMap<String, Utilisateur>> {
	
	private final ArrayList data;
	private ArrayList<String> selectedIds = new ArrayList<String>();
	
	public ListeVoyageursAdapteur(Context context, int resource, ArrayList<LinkedHashMap<String, Utilisateur>> data) {
		super(context, resource, data);
	
		this.data = data;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.liste_voyageur, null);
		}
		
		LinkedHashMap<String, Utilisateur> item = (LinkedHashMap<String, Utilisateur>) data.get(position);
		Object key = item.keySet().iterator().next();
		Utilisateur u = item.get(key);
		
		if (u != null) {
			TextView voyageur = (TextView) v.findViewById(R.id.txtListeVoyageurNom);
			CheckBox chk = (CheckBox) v.findViewById(R.id.chkVoyageur);
			
			if (voyageur != null) {
				voyageur.setText(u.getFullName());
			}
			
			if (chk != null) {
				
				chk.setTag(u.getUtilisateurId());
				chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						CheckBox chk = (CheckBox) buttonView;
												
						if (isChecked) {
							selectedIds.add(chk.getTag().toString());
		                }else{
		                	selectedIds.remove(chk.getTag().toString());
		                }
					}
		        });
				
			}
		
		}
		
		return v;
	}

	public ArrayList<String> getSelectedTags() {
		return selectedIds;
	}
	
}