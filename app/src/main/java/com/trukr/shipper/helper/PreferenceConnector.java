package com.trukr.shipper.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConnector {

    public static final String PREF_NAME = "com.trukr.shipper";
    public static final String PREF_NAME1 = "com.trukr.shipper1";
    public static final int MODE = Context.MODE_PRIVATE;

    public static final String _PREF_TEMP_PASSWORD = "PASSWORD";

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static SharedPreferences getPreferencesRemember(Context context) {
        return context.getSharedPreferences(PREF_NAME1, MODE);
    }

    public static SharedPreferences.Editor getEditorRemember(Context context) {
        return getPreferencesRemember(context).edit();
    }


    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeStringRemember(Context context, String key, String value) {
        getEditorRemember(context).putString(key, value).commit();
    }

    public static String readStringRemember(Context context, String key, String defValue) {
        return getPreferencesRemember(context).getString(key, defValue);
    }

    public static void clearAllPreferences(Context context) {
        getPreferences(context).edit().clear().commit();
    }

    public static void clearAllPreferencesRemember(Context context) {
        getPreferencesRemember(context).edit().clear().commit();
    }
}
