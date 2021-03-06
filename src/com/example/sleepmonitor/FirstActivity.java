package com.example.sleepmonitor;

import java.util.Calendar;

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
import android.support.v4.app.FragmentManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
	private ImageView imageView1;
	private HourMinuteTimePicker hourMinutePicker_1;
	private HourMinuteTimePicker hourMinutePicker_3;
	private NumPicker myNumPicker;
	int x;
	int y;
	private MySettings mySetting;
	TextView disable_accel;
	TextView enable_accel;
	private AlarmManager alarmMgr;
	private PendingIntent alarmIntent;
	private SharedPreferences sharedPreferences;
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		mySurfaceView = (FirstSurfaceView) findViewById(R.id.surfaceView);
		
		Switch mSwitch = (Switch)findViewById(R.id.switch1);
		if(mSwitch != null){
			mSwitch.setOnCheckedChangeListener(this);
		}
		
		enable_accel = (TextView) findViewById(R.id.button1);
		disable_accel = (TextView) findViewById(R.id.button2);
		
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		switch1 = (Switch) findViewById(R.id.switch1);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView4);
		//View touchView = findViewById(R.id.textView2);
		//touchView.setOnTouchListener(this);
		tv1.setOnTouchListener(this);
		tv3.setOnTouchListener(this);
		tv4.setOnTouchListener(this);
		imageView1.setOnTouchListener(this);
		mySurfaceView.setOnTouchListener(this);
		
		enable_accel.setOnTouchListener(this);
		disable_accel.setOnTouchListener(this);
		
		mySetting = new MySettings(this);
		//routine for loading the settings
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
		boolean switch_state = sharedPreferences.getBoolean("Switch_State", false);
		switch1.setChecked(switch_state);
		String Earliest_Alarm = sharedPreferences.getString("Earliest_Alarm", "0:00");
		//make "am/pm" half as smaller
		Spannable span = new SpannableString(Earliest_Alarm);
		span.setSpan(new RelativeSizeSpan(0.5f), Earliest_Alarm.length()-3, 
				Earliest_Alarm.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv1.setText(span);
		String nap_interval = sharedPreferences.getString("nap_interval", "0");
		tv3.setText("Nap Interval : " + nap_interval + " minutes");
		String Must_Wakeup_Alarm = sharedPreferences.getString("Must_Wakeup_Alarm", "0:00");
		tv4.setText("No Must Wakeup Alarm : " + Must_Wakeup_Alarm);	
		
		hourMinutePicker_1 = new HourMinuteTimePicker();
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
				//hourMinutePicker_1 = new HourMinuteTimePicker();
				hourMinutePicker_1.show(getFragmentManager(), "timePicker");
				switchState = 1;
				break;
			}
			if(v.equals(tv3)){
				myNumPicker = new NumPicker();
				FragmentManager fm = getSupportFragmentManager();
				myNumPicker.show(fm, "numberPicker");
				switchState = 2;
				break;
			}
			if(v.equals(tv4)){
				hourMinutePicker_3 = new HourMinuteTimePicker();
				hourMinutePicker_3.show(getFragmentManager(), "timePicker");
				switchState = 3;
				break;
			}
			if(v.equals(enable_accel)){
				accel = true;
				break;
			}
			if(v.equals(disable_accel)){
				accel = false;
				break;
			}
			if(v.equals(imageView1)){
				imageView1.setImageResource(R.drawable.button_stainless_start_on);
				mySurfaceView.onResume();
				break;
			}
			if(v.equals(mySurfaceView)){
				//Toast.makeText(this, "surface", Toast.LENGTH_SHORT).show();
				imageView1.setVisibility (View.VISIBLE);
				mySurfaceView.onPause();
				switch1.setChecked(false);
				break;
			}
			
		case MotionEvent.ACTION_UP:
			if(v.equals(imageView1)){
				imageView1.setImageResource(R.drawable.button_stainless_start_off);
				//Toast.makeText(this, "invis", Toast.LENGTH_SHORT).show();
				imageView1.setVisibility (View.INVISIBLE);
				break;
			}
		}
		return true;
	}
	
	//final Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
	int switchState = 3;
	@Override
	public void onFinishEditDialog(String inputText){
		switch(switchState){
		case 1:	mySetting.savePreferences("Earliest_Alarm", inputText);
				//make "am/pm" half as smaller
				Spannable span = new SpannableString(inputText);
				span.setSpan(new RelativeSizeSpan(0.5f), inputText.length()-3, 
						inputText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tv1.setText(span);
				//set the alarm
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.set(Calendar.HOUR_OF_DAY, hourMinutePicker_1.hour);
				calendar.set(Calendar.MINUTE, hourMinutePicker_1.minutes);
				if(calendar.getTimeInMillis() < System.currentTimeMillis()){
					calendar.add(Calendar.DAY_OF_MONTH, 1);
				}
				Intent intent = new Intent(this, MainActivity.class);   //define alarm callback which activity
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("methodName","alarmDialog");
				alarmIntent = PendingIntent.getActivity(this, 12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
				alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);
				
				boolean toggle_state = sharedPreferences.getBoolean("Toggle_State", false);
				if(toggle_state){
					int repeatMinutes = Integer.parseInt(sharedPreferences.getString("nap_interval", "5"));
					alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
					        1000 * 60 * repeatMinutes, alarmIntent);
				}else{
					alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
				}
				//Toast.makeText(this, "hour " + newFragment1.hour + " minute " + newFragment1.minutes, Toast.LENGTH_SHORT).show();
				break;
				
		case 2: mySetting.savePreferences("nap_interval", inputText);
				//tv3.setText("nap interval : " + inputText + " minutes");
				Calendar calendar_1 = Calendar.getInstance();
				calendar_1.setTimeInMillis(System.currentTimeMillis());
				calendar_1.set(Calendar.HOUR_OF_DAY, hourMinutePicker_1.hour);
				calendar_1.set(Calendar.MINUTE, hourMinutePicker_1.minutes);
				if(calendar_1.getTimeInMillis() < System.currentTimeMillis()){
					calendar_1.add(Calendar.DAY_OF_MONTH, 1);
				}
				Intent intent_1 = new Intent(this, MainActivity.class);   //define alarm callback which activity
				intent_1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent_1.putExtra("methodName","alarmDialog");
				alarmIntent = PendingIntent.getActivity(this, 12345, intent_1, PendingIntent.FLAG_CANCEL_CURRENT);
				alarmMgr = (AlarmManager)this.getSystemService(Activity.ALARM_SERVICE);
				boolean toggle_state_1 = sharedPreferences.getBoolean("Toggle_State", false);
				if(toggle_state_1){
					int repeatMinutes = Integer.parseInt(sharedPreferences.getString("nap_interval", "5"));
					alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar_1.getTimeInMillis(),
					        1000 * 60 * repeatMinutes, alarmIntent);
					
					tv3.setText("nap interval : " + inputText + " minutes");
				}else{
					alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar_1.getTimeInMillis(), alarmIntent);
					tv3.setText("nap interval disabled");
				}			
				break;
				
		case 3: mySetting.savePreferences("Must_Wakeup_Alarm", inputText);
				tv4.setText("Must Wakeup Alarm : " + inputText);
				break;
		}
	}
	
	@Override
	protected void onResume() {
        super.onResume();
	}
/*
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
			imageView1.setVisibility (View.INVISIBLE);
		}else{
//uncomment the next line of code will enable accelerometer service
			stopService(new Intent(this, SensorService.class));
			mySurfaceView.onPause();
			mySetting.savePreferences("Switch_State", false);
			imageView1.setVisibility (View.VISIBLE);
			// If the alarm has been set, cancel it.   Don's know if this works???
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
		Sensordata = mySensorReceiver.getDataPassed();
		}
		Sensordata += x/3;
		x = 0;
		return Sensordata;
	}
	
	private SensorReceiver mySensorReceiver;
	
	@Override
	protected void onStart() {	 
		//Register BroadcastReceiver
		//to receive event from our service
		mySensorReceiver = new SensorReceiver();
	    IntentFilter intentFilter = new IntentFilter();
	    intentFilter.addAction(SensorService.MY_ACTION);
	    registerReceiver(mySensorReceiver, intentFilter);
	     
	    //Start our own service
	    Intent intent = new Intent(FirstActivity.this, SensorService.class);
	    startService(intent);
	 
	    super.onStart();
	}
	
	@Override
	protected void onStop() {
		unregisterReceiver(mySensorReceiver);
		super.onStop();
	}
	
	private class SensorReceiver extends BroadcastReceiver{
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
