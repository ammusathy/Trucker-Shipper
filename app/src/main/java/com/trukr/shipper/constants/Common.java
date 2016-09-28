package com.trukr.shipper.constants;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.trukr.shipper.R;


/**
 * Created by nijamudhin on 6/6/2016.
 */
public class Common {
    public static String GetTeleCountryCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        if (CountryZipCode.equals("")) {
            CountryZipCode = "+1";
        } else {
            CountryZipCode = "+" + CountryZipCode;
        }
        return CountryZipCode;
    }

    public static String getTeleCountryCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (int i = 0; i < rl.length; i++) {
            String[] g = rl[i].split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        if (CountryZipCode.equals("")) {
            CountryZipCode = "+1";
        } else {
            CountryZipCode = "+" + CountryZipCode;
        }
        return CountryZipCode;
    }

    // hide the soft keyboard
    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void formetText(String mLastBeforeText, Editable s, String countryCode, EditText txtPh) {

        String beforeValue = mLastBeforeText;
        String currentValue = s.toString();

        if (currentValue.length() > beforeValue.length()) {

            if (countryCode.length() == 2) {

                switch (s.length()) {
                    case 1:
                        txtPh.setText(countryCode + " (" + currentValue);
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 2:
                        String value2 = "" + currentValue.charAt(currentValue.length() - 1);
                        String country = "" + countryCode.charAt(currentValue.length() - 1);

                        if (!value2.equals(country)) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + country;
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 3:
                        String value3 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value3.equals(" ")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + " ";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 4:
                        String value4 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value4.equals("(")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + "(";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 7:
                        txtPh.setText(currentValue + ") ");
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 8:
                        String value8 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value8.equals(")")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + ")";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 9:
                        String value9 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value9.equals(" ")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + " ";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 12:
                        txtPh.setText(currentValue + "-");
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 13:
                        if (!currentValue.contains("-")) {

                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + "-";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                }
            } else if (countryCode.length() == 3) {

                switch (s.length()) {
                    case 1:
                        txtPh.setText(countryCode + " (" + currentValue);
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 2:
                        String value2 = "" + currentValue.charAt(currentValue.length() - 1);
                        String country = "" + countryCode.charAt(1);

                        if (!value2.equals(country)) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + country;
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 3:
                        String value3 = "" + currentValue.charAt(currentValue.length() - 1);
                        String country1 = "" + countryCode.charAt(2);

                        if (!value3.equals(country1)) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + country1;
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 4:
                        String value4 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value4.equals(" ")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + " ";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 5:
                        String value5 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value5.equals("(")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + "(";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 8:
                        txtPh.setText(currentValue + ") ");
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 9:
                        String value9 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value9.equals(")")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + ")";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 10:
                        String value10 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value10.equals(" ")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + " ";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 13:
                        txtPh.setText(currentValue + "-");
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 14:
                        if (!currentValue.contains("-")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + "-";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                }
            } else if (countryCode.length() == 3) {

                switch (s.length()) {
                    case 1:
                        txtPh.setText(countryCode + " (" + currentValue);
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 2:
                        String value2 = "" + currentValue.charAt(currentValue.length() - 1);
                        String country = "" + countryCode.charAt(1);

                        if (!value2.equals(country)) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + country;
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 3:
                        String value3 = "" + currentValue.charAt(currentValue.length() - 1);
                        String country1 = "" + countryCode.charAt(2);

                        if (!value3.equals(country1)) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + country1;
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 4:
                        String value4 = "" + currentValue.charAt(currentValue.length() - 1);
                        String country2 = "" + countryCode.charAt(3);

                        if (!value4.equals(country2)) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + country2;
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 5:
                        String value5 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value5.equals(" ")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + " ";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 6:
                        String value6 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value6.equals("(")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + "(";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 9:
                        txtPh.setText(currentValue + ") ");
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 10:
                        String value10 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value10.equals(")")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + ")";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 11:
                        String value11 = "" + currentValue.charAt(currentValue.length() - 1);
                        if (!value11.equals(" ")) {
                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + " ";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                    case 14:
                        txtPh.setText(currentValue + "-");
                        txtPh.setSelection(txtPh.getText().length());
                        break;
                    case 15:
                        if (!currentValue.contains("-")) {

                            String newText = currentValue.substring(0, currentValue.length() - 1);
                            newText = newText + "-";
                            String lastText = "" + currentValue.charAt(currentValue.length() - 1);
                            newText = newText + lastText;
                            txtPh.setText(newText);
                            txtPh.setSelection(txtPh.getText().length());
                        }
                        break;
                }
            }
        }


    }
}
