package com.example.sleepmonitor;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends FragmentActivity 
implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener{
	private FirstSurfaceView mySurfaceView;
	private TextView tv1;
	private TextView tv3;
	private TextView tv4;
	TimePickerFragment newFragment1;
	TimePickerFragment newFragment2;
	TimePickerFragment newFragment3;
	int x;
	int y;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		mySurfaceView = (FirstSurfaceView) findViewById(R.id.surfaceView);
		
		
		Switch mSwitch = (Switch)findViewById(R.id.switch1);
		if(mSwitch != null){
			mSwitch.setOnCheckedChangeListener(this);
		}
		
		tv1 = (TextView) findViewById(R.id.textView1);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);
		View touchView = findViewById(R.id.textView2);
		touchView.setOnTouchListener(this);
		tv1.setOnTouchListener(this);
		tv3.setOnTouchListener(this);
		tv4.setOnTouchListener(this);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
			x = (int) event.getX();
			y = (int) event.getY();
			
		case MotionEvent.ACTION_DOWN:
			if(v.equals(tv1)){
				//Toast.makeText(this, "tv1", Toast.LENGTH_SHORT).show();
				newFragment1 = new TimePickerFragment();
				newFragment1.show(getFragmentManager(), "timePicker");
				tv1.setText("Earliest Alarm : " + newFragment1.getTimeString());
			}
			if(v.equals(tv3)){
				newFragment2 = new TimePickerFragment();
				newFragment2.show(getFragmentManager(), "timePicker");
			}
			if(v.equals(tv4)){
				newFragment3 = new TimePickerFragment();
				newFragment3.show(getFragmentManager(), "timePicker");
			}
		}
		return true;
	}
/*	
	@Override
	protected void onResume() {
        super.onResume();
        mySurfaceView.onResume();
	}
	
	@Override
	protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
	}
*/
	/*
	 * this function handle the switch widget
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			startService(new Intent(this, SensorService.class));
			mySurfaceView.onResume();
			//Toast.makeText(this, "Monitored switch is ON", Toast.LENGTH_SHORT).show();
		}else{
			stopService(new Intent(this, SensorService.class));
			mySurfaceView.onPause();
			//Toast.makeText(this, "Monitored switch is OFF", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void showTextView(int x, int y, int pX, int pY){
		tv1.setText(" X : "+x+" Y : "+y+" pX : "+pX+"  pY : "+pY);
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
