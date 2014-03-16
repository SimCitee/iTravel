package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.android.itravel.ListeNouvelles;
import com.android.itravel.R;
import com.android.itravel.R.id;
import com.android.itravel.R.layout;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListeNouvellesAdapteur extends ArrayAdapter<LinkedHashMap<String, Nouvelle>> {
	
	private final ArrayList mData;
	
	public ListeNouvellesAdapteur(Context context, int resource, ArrayList<LinkedHashMap<String, Nouvelle>> data) {
		super(context, resource, data);
	
		mData = data;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.list_post, null);
		}
		
		LinkedHashMap<String, Nouvelle> item = (LinkedHashMap<String, Nouvelle>) mData.get(position);
		Object key = item.keySet().iterator().next();
		Nouvelle n = item.get(key);
		
		if (n != null) {
			TextView nomContact = (TextView) v.findViewById(R.id.txtListeNomContact);
			TextView commentaire= (TextView) v.findViewById(R.id.txtListeCommentaire);
			ImageView image		= (ImageView) v.findViewById(R.id.imvListeImage);
			
			if (nomContact != null) {
				nomContact.setText(n.getNomContact());
			}
			
			if (commentaire != null) {
				commentaire.setText(n.getCommentaire());
			}
			
			if (image != null) {
				//image.setImageResource(n.getImageId());
			}
		
		}
		
		return v;
	}
}
