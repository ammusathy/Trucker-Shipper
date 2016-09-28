package com.trukr.shipper.components;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.trukr.shipper.fragment.Home;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by nijamudhin on 8/26/2016.
 */
public class DeliveryDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

 public  static TextView destination;
    int year, month, day;
    public static int selectedday, selectedmonth, selectedyear;
    Context context;

    public DeliveryDatePickerFragment() {
        //Required no Constructor
    }

    @SuppressLint("ValidFragment")
    public DeliveryDatePickerFragment(TextView textView) {
        this.destination = textView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        year = PickUpDatePickerFragment.selectedyear;
        month = PickUpDatePickerFragment.selectedmonth;
        day = PickUpDatePickerFragment.selectedday;
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dpd = null;
        try {
            dpd = new DatePickerDialog(getActivity(), this, year, month, day);
            c.set(selectedyear,selectedmonth,selectedday);
            dpd.getDatePicker().setMinDate(calendar.getTimeInMillis());
            dpd.getDatePicker().updateDate(PickUpDatePickerFragment.selectedyear, PickUpDatePickerFragment.selectedmonth, PickUpDatePickerFragment.selectedday);
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
            destination.setText(formattedDate);
            Log.d("Date", formattedDate);

            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}