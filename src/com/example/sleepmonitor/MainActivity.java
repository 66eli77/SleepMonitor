package com.example.sleepmonitor;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;

public class MainActivity extends TabActivity {
private TabHost myTabHost;
private MediaPlayer mMediaPlayer = new MediaPlayer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		myTabHost = (TabHost)findViewById(android.R.id.tabhost);
		myTabHost.getTabWidget().setDividerDrawable(R.drawable.divider); // delete divider by using transparent png 
		//myTabHost.getTabWidget().setStripEnabled(false);  //not working !?

		TabSpec spec = myTabHost.newTabSpec("Tab1"); // tabhost object	
		spec.setIndicator("", getResources().getDrawable(R.drawable.tab1_selector)); //set title
		spec.setContent(new Intent(this, FirstActivity.class));
		myTabHost.addTab(spec);
		
		TabSpec spec2 = myTabHost.newTabSpec("Tab2"); // tabhost object
		spec2.setIndicator("", getResources().getDrawable(R.drawable.tab2_selector)); //set title
		spec2.setContent(new Intent(this, SecondActivity.class));
		myTabHost.addTab(spec2);
	
		TabSpec spec3 = myTabHost.newTabSpec("Tab3"); // tabhost object
		spec3.setIndicator("", getResources().getDrawable(R.drawable.tab3_selector)); //set title
		spec3.setContent(new Intent(this, ThirdActivity.class));
		myTabHost.addTab(spec3);
		
		int flags = getIntent().getFlags();     
		
		if(getIntent() != null && getIntent().getExtras() != null && (flags & Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY) == 0 ){
			if(getIntent().getStringExtra("methodName").equals("alarmDialog")){
				new AlertDialog.Builder(this)
					.setTitle("Wake Up")
						.setMessage("for now, both buttons will stop the alarm")
				 		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
				 		        public void onClick(DialogInterface dialog, int which) { 
				 		        	mMediaPlayer.stop();
				 		        }
				 		     })
				 		    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				 		        public void onClick(DialogInterface dialog, int which) { 
				 		        	mMediaPlayer.stop();
				 		        }
				 		     })
				 		    .setIcon(android.R.drawable.ic_dialog_alert)
				 		    .setCancelable(false)
				 		     .show();
				 		   
				 		   
				 		   playSound(this, getAlarmUri());
			}
		}
	}
	
	private void playSound(Context context, Uri alert) {
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }
 
    //Get an alarm sound. Try for an alarm. If none set, try notification, 
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

}
