package model;

import java.util.ArrayList;
import java.util.HashMap;

import com.android.itravel.ListeNouvelles;
import com.android.itravel.R;
import com.android.itravel.R.id;
import com.android.itravel.R.layout;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListeNouvellesAdapteur extends ArrayAdapter<HashMap<String, Nouvelle>> {
	
	public ListeNouvellesAdapteur(Context context, int resource, ArrayList<HashMap<String, Nouvelle>> data) {
		super(context, resource, data);
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = convertView;
		
		if (v == null) {
			LayoutInflater vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.list_post, null);
		}
		
		HashMap<String, Nouvelle> item = getItem(position);
		
		Nouvelle n = item.get(ListeNouvelles.getTagPid());
		
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
				image.setImageResource(n.getImageId());
			}
			
		}
		
		return v;
	}
}