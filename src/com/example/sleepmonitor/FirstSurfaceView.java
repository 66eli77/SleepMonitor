package com.example.sleepmonitor;

import java.util.LinkedList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class FirstSurfaceView extends SurfaceView{
	private SurfaceHolder surfaceHolder;
	private FirstThread firstThread;
	private Paint paint;
	FirstActivity firstActivity;
	
	public FirstSurfaceView(Context context){
		super(context);
		init(context);
	}
	
	public FirstSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FirstSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context c){
		paint = new Paint();
		paint.setStrokeWidth(4);
		paint.setShadowLayer(4, -4, 4, 0x80000000);
		paint.setStyle(Style.STROKE);
		//paint.setARGB(255, 100, 100, 200);
		paint.setColor(0xFF33B5E5);
	    paint.setAntiAlias(true);
		firstActivity = (FirstActivity) c;
		firstThread = new FirstThread(this);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(new SurfaceHolder.Callback(){
			@Override
			public void surfaceCreated(SurfaceHolder holder){
				//if it is the first time the thread starts
				if(firstThread.getState() == Thread.State.NEW){
				firstThread.setRunning(true);
			//	Log.d("eli", "in");
				firstThread.start();
				}else if(firstThread.getState() == Thread.State.TERMINATED){
					firstThread = new FirstThread(FirstSurfaceView.this);
					firstThread.setRunning(true);
					firstThread.start();
				}
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				boolean retry = true;
				firstThread.setRunning(false);
				while(retry){
					try{
						firstThread.join();
						retry = false;
					}catch(InterruptedException e) {
                    }
				}
			}
		});
	}
	
	
	LinkedList<Float> list = new LinkedList<Float>();
	boolean drawControl = false;
	
	public void drawLineChart(Canvas canvas) {
		canvas.drawColor(Color.argb(255,240,240,240));
		if(drawControl){
			Path path = new Path();
		
			list.add(toY(firstActivity.getSensorData()));
     
			path.moveTo(0, list.peekLast());
			for (int i = 1; i < list.size() - 1; i++) {
				path.lineTo(i*30, list.get(list.size() - i));
						// i*5 defines the resolution of the chart
			}
				canvas.drawPath(path, paint);
        	
				if(list.size() >= 180){ //here define the size of the list, therefore the length of the chart
				list.removeFirst();  
			}
		}
    }
	
	private float toY(int f){
		if(getHeight() - f - 4 < 0) return 0;
		if(getHeight() - f - 4 > getHeight()) return getHeight();
		return (float)getHeight() - f - 4;
	}
	
	public void onResume(){
		//firstThread.setRunning(true);
		drawControl = true;
	}

	public void onPause(){
		//firstThread.setRunning(false);
		drawControl = false;
		list.clear();
	}

}
