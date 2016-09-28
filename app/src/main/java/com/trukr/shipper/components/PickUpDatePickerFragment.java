package com.trukr.shipper.components;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PickUpDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

   public static  TextView source;
    int year, month, day;
    public static int selectedday, selectedmonth, selectedyear;

    public PickUpDatePickerFragment() {
        //Required no Constructor
    }

    @SuppressLint("ValidFragment")
    public PickUpDatePickerFragment(TextView textView) {
        this.source = textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd = null;

        try {
            dpd = new DatePickerDialog(getActivity(), this, year, month, day);
          /*  java.lang.reflect.Field mDatePickerField;
            try {
                mDatePickerField = dpd.getClass().getDeclaredField("DatePicker");
                mDatePickerField.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dpd.getDatePicker().updateDate(year, month, day);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        try {
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            selectedday = day;
            selectedmonth = month;
            selectedyear = year;
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = sdf.format(c.getTime());
            source.setText(formattedDate);
            //   source.setTextColor(Color.BLACK);
            Log.d("Date", formattedDate);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}