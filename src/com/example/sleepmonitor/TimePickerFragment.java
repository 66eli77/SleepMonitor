package com.example.sleepmonitor;

import java.util.Calendar;

import android.app.*;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {
	String timeString;
	public int hour;
	public int minutes;
	
	public interface EditDialogListener{
		void onFinishEditDialog(String inputText);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current time as the default values for the picker
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of TimePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute,false);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
		hour = hourOfDay;
		minutes = minute;
	
		if(hourOfDay > 12){
			if(minute == 0){
				timeString = (hourOfDay - 12) + ":" + "00" + " PM ";
			}else{
				timeString = (hourOfDay - 12) + ":" + minute + " PM ";
			}
			EditDialogListener passToActivity = (EditDialogListener)getActivity();
			passToActivity.onFinishEditDialog(timeString);
			this.dismiss();
		}else{
			if(minute == 0){
				timeString = hourOfDay + ":" + "00" + " AM ";
			}else{
				timeString = hourOfDay + ":" + minute + " AM ";
			}
			EditDialogListener passToActivity = (EditDialogListener)getActivity();
			passToActivity.onFinishEditDialog(timeString);
			this.dismiss();
		}
	}
	
}