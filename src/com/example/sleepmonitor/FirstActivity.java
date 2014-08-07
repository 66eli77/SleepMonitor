package com.example.sleepmonitor;

import java.util.Calendar;

import com.example.sleepmonitor.TimePickerFragment.EditDialogListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends FragmentActivity 
implements CompoundButton.OnCheckedChangeListener, View.OnTouchListener, EditDialogListener{
	private FirstSurfaceView mySurfaceView;
	private TextView tv1;
	private TextView tv3;
	private TextView tv4;
	private Switch switch1;
	TimePickerFragment newFragment1;
	TimePickerFragment newFragment2;
	TimePickerFragment newFragment3;
	int x;
	int y;
	private MySettings mySetting;
	TextView button1;
	TextView button2;
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		mySurfaceView = (FirstSurfaceView) findViewById(R.id.surfaceView);
		
		Switch mSwitch = (Switch)findViewById(R.id.switch1);
		if(mSwitch != null){
			mSwitch.setOnCheckedChangeListener(this);
		}
		
		button1 = (TextView) findViewById(R.id.button1);
		button2 = (TextView) findViewById(R.id.button2);
		
		switch1 = (Switch) findViewById(R.id.switch1);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);
		View touchView = findViewById(R.id.textView2);
		touchView.setOnTouchListener(this);
		tv1.setOnTouchListener(this);
		tv3.setOnTouchListener(this);
		tv4.setOnTouchListener(this);
		
		button1.setOnTouchListener(this);
		button2.setOnTouchListener(this);
		
		mySetting = new MySettings(this);
		//routine for loading the settings
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean switch_state = sharedPreferences.getBoolean("Switch_State", false);
		switch1.setChecked(switch_state);
		String Earliest_Alarm = sharedPreferences.getString("Earliest_Alarm", "0:00");
		tv1.setText("Earliest Alarm : " + Earliest_Alarm);
		String nap_interval = sharedPreferences.getString("nap_interval", "0:00");
		tv3.setText("No Nap Interval : " + nap_interval);
		String Must_Wakeup_Alarm = sharedPreferences.getString("Must_Wakeup_Alarm", "0:00");
		tv4.setText("No Must Wakeup Alarm : " + Must_Wakeup_Alarm);	
		
//		if(alarmMgr != null){
	//		alarmMgr.cancel(alarmIntent);
		//}
	}
	
	//return touch x y coordinates
	public int getY(){
		return y;
	}
	public int getX(){
		return x;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_MOVE:	//get touch x y coordinates
			x = (int) event.getX();
			y = (int) event.getY();
			
		case MotionEvent.ACTION_DOWN:
			if(v.equals(tv1)){
				//Toast.makeText(this, "tv1", Toast.LENGTH_SHORT).show();
				newFragment1 = new TimePickerFragment();
				newFragment1.show(getFragmentManager(), "timePicker");
				switchState = 1;
			}
			if(v.equals(tv3)){
				newFragment2 = new TimePickerFragment();
				newFragment2.show(getFragmentManager(), "timePicker");
				switchState = 2;
			}
			if(v.equals(tv4)){
				newFragment3 = new TimePickerFragment();
				newFragment3.show(getFragmentManager(), "timePicker");
				switchState = 3;
			}
			if(v.equals(button1)){
				accel = true;
			}
			if(v.equals(button2)){
				accel = false;
			}
		}
		return true;
	}
	
	int switchState = 3;
	@Override
	public void onFinishEditDialog(String inputText){
		switch(switchState){
		case 1:	mySetting.savePreferences("Earliest_Alarm", inputText);
				tv1.setText("Earliest Alarm : " + inputText);
				
				//set the alarm
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, newFragment1.hour);
				calendar.set(Calendar.MINUTE, newFragment1.minutes);
				Intent intent = new Intent(this, AlarmReceiver.class);
				alarmIntent = PendingIntent.getActivity(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);
				alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
				//Toast.makeText(this, "hour " + newFragment1.hour + " minute " + newFragment1.minutes, Toast.LENGTH_SHORT).show();
				break;
				
		case 2: mySetting.savePreferences("nap_interval", inputText);
				tv3.setText("nap interval : " + inputText);
/*				
				// setRepeating() lets you specify a precise custom interval--in this case,
				// 20 minutes.
				alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				        1000 * 60 * 20, alarmIntent);
*/				
				break;
				
		case 3: mySetting.savePreferences("Must_Wakeup_Alarm", inputText);
				tv4.setText("Must Wakeup Alarm : " + inputText);
				break;
		}
	}

/*	
	@Override
	protected void onResume() {
        super.onResume();
	}
	
	@Override
	protected void onPause() {
        super.onPause();
	}
*/	
	
	/*
	 * this function handle the switch widget
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
//uncomment the next line of code will enable accelerometer service
//			Toast.makeText(this, "eli", Toast.LENGTH_SHORT).show();
			startService(new Intent(this, SensorService.class));
			mySurfaceView.onResume();
			mySetting.savePreferences("Switch_State", true);
		}else{
//uncomment the next line of code will enable accelerometer service
			stopService(new Intent(this, SensorService.class));
			mySurfaceView.onPause();
			mySetting.savePreferences("Switch_State", false);
			
			// If the alarm has been set, cancel it.
			if (alarmMgr!= null) {
			    alarmMgr.cancel(alarmIntent);
			}
		}
	}
	
	int Sensordata = 0;
	boolean accel;
	public int getSensorData(){
		Sensordata = 0;
		if(accel){
		Sensordata = myReceiver.getDataPassed();
		}
		Sensordata += x/3;
		x = 0;
		return Sensordata;
	}
	
	private MyReceiver myReceiver;
	
	@Override
	protected void onStart() {	 
		//Register BroadcastReceiver
		//to receive event from our service
	    myReceiver = new MyReceiver();
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(SensorService.MY_ACTION);
	    registerReceiver(myReceiver, intentFilter);
	     
	    //Start our own service
	    Intent intent = new Intent(FirstActivity.this, SensorService.class);
	    startService(intent);
	 
	    super.onStart();
	}
	
	@Override
	protected void onStop() {
		unregisterReceiver(myReceiver);
		super.onStop();
	}
	
	private class MyReceiver extends BroadcastReceiver{
		int datapassed = 0;
		
		 @Override
		 public void onReceive(Context arg0, Intent arg1) {		  
			 datapassed = arg1.getIntExtra("DATAPASSED", 0);
		 }
		 
		 public int getDataPassed(){
			 return datapassed;
		 }
	}
	
}
