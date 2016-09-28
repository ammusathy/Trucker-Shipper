package com.trukr.shipper.constants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Constant {

    public static int DialCountService = 0;
    public static int DialCount = IConstant.DialCount;
    public static int gpsChecker = 0;
    public static String Img = "";
    public static int settingEditFlag = 0;
    public static Bitmap _UserImage = null;

    /* To check the internet connection */
    public static Boolean isConnectingToInternet(Context ctx) {
        Boolean isConnected = false;
        try {
            ConnectivityManager connectivity = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            isConnected = info != null && info.isAvailable() && info.isConnected();
            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("isConnected: ", "" + isConnected);
        return isConnected;
    }

    /* Get Device Id Value*/
    public static String getDeviceID(Context ctx) {
        String AndroidUDID = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.e("Android UDID: ", AndroidUDID);
        return AndroidUDID;
    }

    /* Get Device Information*/
    public static String getDeviceInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        String NetworkType = "";
        StringBuilder sb = new StringBuilder();
        if (info == null || !info.isConnected())
            NetworkType = "Not Connected"; //return "-"; //not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            NetworkType = "Wifi";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    NetworkType = "2G";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    NetworkType = "3G";
                    break;
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    NetworkType = "4G";
                    break;
                default:
                    NetworkType = "Unknown";
                    break;
            }
        }

        try {
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String carrierName = manager.getNetworkOperatorName();
            sb.append("<b><font color=green>Network Type: " + NetworkType + "</font></b>");
            sb.append(", Manufacturer: " + android.os.Build.MANUFACTURER);
            sb.append(", Model: " + android.os.Build.MODEL);
            sb.append(", OS Version: " + android.os.Build.VERSION.RELEASE);
            sb.append(", API Level: " + android.os.Build.VERSION.SDK_INT);
            if (carrierName.length() < 1)
                sb.append(", Carrier Name: Nil");
            else
                sb.append(", Carrier Name: " + carrierName);
            sb.append(", App Version: " + pinfo.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Device : ", "" + sb.toString());
        return sb.toString();
    }

    /*Check Response JSON|JSONARRAY valid|not*/
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    //Encodes the byte array into base64 string
    public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, Base64.NO_WRAP);
    }

    //Decodes the base64 string into byte array
    public static byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString, Base64.DEFAULT);
    }

    /**
     * For Settings page
     * Edit profile
     */
    public static String email = "";
    public static String mobileNumber = "";
    public static String firstName = "";
    public static String lastName = "";
    public static String companyName = "";
    public static String streetName = "";
    public static String cityName = "";
    public static String stateName = "";
    public static String zipCode = "";
    public static String officeNumber = "";
    public static String taxId = "";
    public static String countryCode = "";
    public static String profilePic = "";

}
