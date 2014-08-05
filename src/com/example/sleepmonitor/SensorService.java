package com.example.sleepmonitor;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

public class SensorService extends Service implements SensorEventListener{
	
	private SensorManager myManager;
	private Sensor accel;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "My Service Created", Toast.LENGTH_SHORT).show();
		myManager = (SensorManager)getSystemService(SENSOR_SERVICE);
		accel = myManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		myManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "My Service Stopped", Toast.LENGTH_SHORT).show();
		myManager.unregisterListener(this);
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Toast.makeText(this, "My Service Started", Toast.LENGTH_SHORT).show();
		//myManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
	}
}
