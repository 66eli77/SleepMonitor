package com.example.sleepmonitor;

import java.util.ArrayList;

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
		//paint.setShadowLayer(4, 2, 2, 0x80000000);
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
	
	private float[] datapoints = new float[] { 10, 12, 7, 14, 15, 19, 13, 0, 10, 13, 13, 30, 15, 14 };
	
	public void drawLineChart(Canvas canvas) {
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
	
/*
	int startX = 0;
	int startY = 0;
	int stopY = 0;
	ArrayList<Integer> list = new ArrayList<Integer>();

	protected void drawSomething(Canvas canvas){
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		startY = height;
		stopY = height;
		canvas.drawColor(Color.argb(255,r,g,b));
		if(startX > width){
			canvas.save();
			startX =0;
			return;
		}
			canvas.drawLine(startX, height - firstActivity.getY(), startX + 40, height - firstActivity.getY(), myPaint);
			startX++;
		
		
		firstActivity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				firstActivity.showTextView(startX + 100, stopY, startX, startY + firstActivity.getY());
			}
		});
	}
	
	
	int startX = 0;
	int startY = 0;
	int stopY = 0;
	protected void drawSomething(Canvas canvas){
		int height = canvas.getHeight();
		startY = height - firstActivity.getY();
		stopY = height - firstActivity.getY();
		canvas.drawColor(Color.argb(255,r,g,b));
		canvas.drawLine(startX, startY, startX + 100, stopY, myPaint);
		startX++;
		
		firstActivity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				firstActivity.showTextView(startX + 100, stopY, startX, startY);
			}
		});
	}
*/	
	/*
 	int startX = 0;
	int startY = 0;
	int stopY = 0;
	ArrayList<Integer> list = new ArrayList<Integer>();

	protected void drawSomething(Canvas canvas){
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		startY = height;
		stopY = height;
		list.add(height);
		list.add(height - firstActivity.getY());
		//startY = firstActivity.getY();
		//stopY = firstActivity.getX();
		canvas.drawColor(Color.argb(255,r,g,b));
		for(int i = 0; i < list.size() - 1; i++){
			canvas.drawLine(i, list.get(list.size() - i - 1), i + 10, list.get(list.size() - i - 2), myPaint);
			//startX++;
		}
		if(list.size() == width)list.clear();
		
		firstActivity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				firstActivity.showTextView(startX + 100, stopY, startX, startY + firstActivity.getY());
			}
		});
	}
	/*
	///////////////////
	int myX = 0;
	int myY = 0;
	protected void drawSomething(Canvas canvas){
		int startX = 0;
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		int scale = height / m_iScaler;
		canvas.drawColor(Color.argb(255,r,g,b));
		
		if(startX >= width){
			canvas.save();
	//		startX = 0;
			return;
		}
	//	if(stopBaseY >= height){
	//		stopBaseY = 0;
	//	}

		while(startX < width - 1){
			int startBaseY = firstActivity.getY()/scale;
			int stopBaseY = firstActivity.getX()/scale;
			if(startBaseY > height / 2){
				startBaseY = 1 + height / 2;
		        int checkSize = height / 2;
		        if (stopBaseY <= checkSize)
		            return;
		        stopBaseY = 2 + height / 2;
			}
			
			int startY = startBaseY + height / 2;
			int stopY = stopBaseY + height / 2;
			canvas.drawLine(startX, startY, startX + 1, stopY, myPaint);
			startX++;
			int checkSize_again = -1 * (height / 2);
			if (stopBaseY >= checkSize_again)
		          continue;
			 stopBaseY = -2 + -1 * (height / 2);
		}

		firstActivity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				firstActivity.showTextView(firstActivity.getX(), firstActivity.getY(), myX, myY);
			}
		});
	}
*/	
	public void onResume(){
		firstThread.setRunning(true);
	}

	public void onPause(){
		firstThread.setRunning(false);
	}

}
