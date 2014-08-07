package com.example.sleepmonitor;

import android.graphics.Canvas;
import android.util.Log;

public class FirstThread extends Thread{
	FirstSurfaceView firstView;
	private boolean running = false;
	
	public FirstThread(FirstSurfaceView view){
		firstView = view;
	}

	public void setRunning(boolean run){
		running = run;
	}
	
	@Override
	 public void run() {
		while(running){
			//Log.d("eli ", "in");
			Canvas canvas = firstView.getHolder().lockCanvas();
			if(canvas != null){
				synchronized(firstView.getHolder()){
					firstView.drawLineChart(canvas);
				}
				firstView.getHolder().unlockCanvasAndPost(canvas);
			}
			try {
				sleep(100); //this define the frame rate of the chart
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
