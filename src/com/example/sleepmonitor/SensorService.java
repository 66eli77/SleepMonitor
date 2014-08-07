package com.example.sleepmonitor;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;
import android.util.Log;

public class SensorService extends Service implements SensorEventListener{	
	private SensorManager myManager;
	private Sensor accel;
	private int vibration = 0;
	final static String MY_ACTION = "MY_ACTION";
	
	@Override
	  public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();		 		 
		return Service.START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Sensor Service Created", Toast.LENGTH_SHORT).show();
		myManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accel = myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		myManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Sensor Service Stopped", Toast.LENGTH_SHORT).show();
		myManager.unregisterListener(this);
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		//Toast.makeText(this, "Sensor Service Started", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// check sensor type
		if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){		
			// assign directions
			float x=event.values[0];
			float y=event.values[1];
			float z=event.values[2];
					
			vibration = (int) (x + y + z)*10;
			Intent intent = new Intent();
			intent.setAction(MY_ACTION);      
			intent.putExtra("DATAPASSED", vibration);
			      
			sendBroadcast(intent);
					
//			Log.d("eli", "x " + x + " y " + y + " z " + z);
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}

}
