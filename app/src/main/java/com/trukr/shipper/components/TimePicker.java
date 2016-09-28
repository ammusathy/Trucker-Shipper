package com.trukr.shipper.components;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TextView;

import com.trukr.shipper.RangeTimePickerDialog;

import java.util.Calendar;

/**
 * Created by androidusr1 on 26/9/16.
 */
public class TimePicker extends DialogFragment {
    TextView source;
    public static int hour,minute;

    public TimePicker() {
    }

    @SuppressLint("ValidFragment")
    public TimePicker(TextView textView) {
        this.source = textView;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int month = c.get(Calendar.MONTH);
        final int year = c.get(Calendar.YEAR);
        hour=TimePickerFragment.hour;
        minute=TimePickerFragment.minute;

     //   final int hour = c.get(Calendar.HOUR_OF_DAY);
       // final int minute = c.get(Calendar.MINUTE);
        final RangeTimePickerDialog timePickerDialog;

        // have to work
        timePickerDialog = new RangeTimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minuteOfHour) {

                if (minuteOfHour < 10) {
                    source.setText(hourOfDay + ":" + "0" + minuteOfHour);
                } else {
                    source.setText(hourOfDay + ":" + minuteOfHour);
                }
                System.out.println(minuteOfHour);

            }

        }, hour, minute, true);
        if (day == PickUpDatePickerFragment.selectedday && month == PickUpDatePickerFragment.selectedmonth && year == PickUpDatePickerFragment.selectedyear) {
            timePickerDialog.setMin(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
            System.out.println("selected date" + PickUpDatePickerFragment.selectedday);
            timePickerDialog.hide();
        }
        timePickerDialog.show();

        // Create a new instance of DatePickerDialog and return it
        return timePickerDialog;
    }
}

