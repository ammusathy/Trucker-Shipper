package com.trukr.shipper.helper;

import android.os.StrictMode;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TrukrWebServiceHelper {
    public static String TAG = "TrukrWebServiceHelper";


    public static String httpPostData(String sUrl, Headers headers, RequestBody requestBody) {
        String responseString = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(sUrl)
                    .headers(headers)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
            Log.i(TAG, "Response String " + responseString);
        } catch (Exception e5) {
            Log.e(TAG + " Exception", e5.toString());
            e5.printStackTrace();
        }
        return responseString;
    }

    public static String httpPostJsonData(String sUrl, String json) {
        String responseString = null;
        try {
            OkHttpClient client = new OkHttpClient();
            //client.setConnectTimeout(60, TimeUnit.SECONDS);
            final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(sUrl)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
            Log.i(TAG, "Response String " + responseString);
        } catch (Exception e5) {
            Log.e(TAG + " Exception", e5.toString());
            e5.printStackTrace();

        }
        return responseString;
    }

    public static String httpGetJsonData(String sUrl) {
        String responseString = null;
        try {
            OkHttpClient client = new OkHttpClient();
            //client.setConnectTimeout(60, TimeUnit.SECONDS);
            Request request = new Request.Builder()
                    .url(sUrl)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            responseString = response.body().string();
            Log.i(TAG, "Response String " + responseString);
        } catch (Exception e5) {
            Log.e(TAG + " Exception", e5.toString());
            e5.printStackTrace();
        }
        return responseString;
    }
}
