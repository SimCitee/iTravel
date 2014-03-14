package com.android.itravel;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DetailsNouvelle extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_nouvelle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details_nouvelle, menu);
		return true;
	}

}
