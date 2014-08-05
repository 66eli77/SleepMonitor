package com.example.sleepmonitor;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
private TabHost myTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		myTabHost = (TabHost)findViewById(android.R.id.tabhost);
		myTabHost.getTabWidget().setDividerDrawable(R.drawable.divider); // delete divider by using transparent png 
		//myTabHost.getTabWidget().setStripEnabled(false);  //not working !?

		TabSpec spec = myTabHost.newTabSpec("Tab1"); // tabhost object	
		spec.setIndicator("", getResources().getDrawable(R.drawable.tab1_selector)); //set title
		spec.setContent(new Intent(this, FirstActivity.class));
		myTabHost.addTab(spec);
		
		TabSpec spec2 = myTabHost.newTabSpec("Tab2"); // tabhost object
		spec2.setIndicator("", getResources().getDrawable(R.drawable.tab2_selector)); //set title
		spec2.setContent(new Intent(this, SecondActivity.class));
		myTabHost.addTab(spec2);
	
		TabSpec spec3 = myTabHost.newTabSpec("Tab3"); // tabhost object
		spec3.setIndicator("", getResources().getDrawable(R.drawable.tab3_selector)); //set title
		spec3.setContent(new Intent(this, ThirdActivity.class));
		myTabHost.addTab(spec3);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
