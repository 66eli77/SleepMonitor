package com.example.sleepmonitor;

import java.util.Calendar;

import android.app.*;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
implements TimePickerDialog.OnTimeSetListener {
	String timeString;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	// Use the current time as the default values for the picker
	final Calendar c = Calendar.getInstance();
	int hour = c.get(Calendar.HOUR_OF_DAY);
	int minute = c.get(Calendar.MINUTE);

	// Create a new instance of TimePickerDialog and return it
	return new TimePickerDialog(getActivity(), this, hour, minute,
		DateFormat.is24HourFormat(getActivity()));
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// Do something with the time chosen by the user
		if(hourOfDay > 12){
			timeString = hourOfDay + ":" + minute + " PM ";
		}else{
			timeString = hourOfDay + ":" + minute + " AM ";
		}
	}
	
	public String getTimeString(){
		return timeString;
	}
}