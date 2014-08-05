package com.example.sleepmonitor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
	private TextView textView1;
	private FirstSurfaceView mySurfaceView;
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
		
		textView1 = (TextView) findViewById(R.id.textView1);
		View touchView = findViewById(R.id.textView2);
		touchView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				x = (int) event.getX();
				y = (int) event.getY();
				return true;
			}
		});
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
		textView1.setText(" X : "+x+" Y : "+y+" pX : "+pX+"  pY : "+pY);
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
}
