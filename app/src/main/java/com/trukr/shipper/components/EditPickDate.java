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

import com.trukr.shipper.fragment.CurrentJobDetails;
import com.trukr.shipper.fragment.NewJobScreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by androidusr1 on 21/9/16.
 */
public class EditPickDate extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    TextView source;
    int year, month, day;
    public static int s_date, s_month, s_year;

    public EditPickDate() {
        //Required no Constructor
    }

    @SuppressLint("ValidFragment")
    public EditPickDate(TextView textView) {
        this.source = textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = CurrentJobDetails.s_year;
        month = CurrentJobDetails.s_month;
        day = CurrentJobDetails.s_date;
        System.out.println("year = " + year +month +day);
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.YEAR, year);
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd = null;
        try {
            dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            c.set(s_year, s_month, s_date);
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
            dpd.getDatePicker().updateDate(CurrentJobDetails.s_year, CurrentJobDetails.s_month-1, CurrentJobDetails.s_date);
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
            s_date = day;
            s_month = month;
            s_year = year;
            SimpleDateFormat sdf;
            sdf = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = sdf.format(c.getTime());
            source.setText(formattedDate);
            Log.d("Date", formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}