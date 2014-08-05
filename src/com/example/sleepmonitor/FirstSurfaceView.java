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
	int r = 240;
	int b = 240;
	int g = 240;
	
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
		canvas.drawColor(Color.argb(255,r,g,b));
		if(drawControl){
			Path path = new Path();
		
			list.add(toY(firstActivity.getX()));
     
			path.moveTo(0, list.peekLast());
			for (int i = 1; i < list.size() - 1; i++) {
				path.lineTo(i*5, list.get(list.size() - i));
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
	
/*
	private float[] datapoints = new float[] { 10, 12, 7, 14, 15, 19, 13, 0, 10, 13, 13, 30, 15, 14 };
	public void drawLineChart2(Canvas canvas) {
		canvas.drawColor(Color.argb(255,r,g,b));
        Path path = new Path();
        path.moveTo(getXPos(0), getYPos(datapoints[0]));
        for (int i = 1; i < datapoints.length; i++) {
            path.lineTo(getXPos(i), getYPos(datapoints[i]));
        }
        canvas.drawPath(path, paint);
    }
	
	private float getYPos(float value) {
	 	float height = getHeight() - getPaddingTop() - getPaddingBottom();
	   	float maxValue = getMax(datapoints);

	  	// scale it to the view size
		value = (value / maxValue) * height;

	   	// invert it so that higher values have lower y
	   	value = height - value;

	  	// offset it to adjust for padding
	    value += getPaddingTop();

		return value;
	}
	
	private float getXPos(float value) {
	    float width = getWidth() - getPaddingLeft() - getPaddingRight();
	    float maxValue = datapoints.length - 1;
	    
	    // scale it to the view size
	    value = (value / maxValue) * width;

	    // offset it to adjust for padding
	    value += getPaddingLeft();

	    return value;
	}
	
	private float getMax(float[] array) {
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
*/	
	
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
