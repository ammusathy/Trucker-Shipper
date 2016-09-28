package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.adapter.NotificationListAdapter;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.GeneralParams.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Notification extends AppCompatActivity implements NotificationListAdapter.OnItemClickListener {

    private List<NotificationModel> notificationModels;
    Context mContext;
    ImageView back;
    SharedPreferences preferences;
    RequestQueue queue;
    RecyclerView notify_view;
    private ProgressDialog pd;
    TextView notification,head_notify;
    NotificationListAdapter notificationListAdapter;
    Typeface Gibson_Light, HnThin, HnLight,HnRegular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        HnRegular = Typeface.createFromAsset(getAssets(), "HelveticaNeue.ttf");

        mContext = Notification.this;
        notify_view = (RecyclerView) findViewById(R.id.notificationlist);
        notification = (TextView) findViewById(R.id.emptynotify_view);
        head_notify = (TextView) findViewById(R.id.header_notification);
        back = (ImageView) findViewById(R.id.notification_back);
        head_notify.setTypeface(HnRegular);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        final LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        notify_view.setLayoutManager(llm);
        if (Constant.isConnectingToInternet(mContext)) {
            notification();
        } else {
            TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }
    }

    private void notification() {
        pd = new ProgressDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preferences = getSharedPreferences("Trucker", Context.MODE_PRIVATE);
        String userId = preferences.getString("Userid", null);
        String authToken = preferences.getString("authToken", null);
        Log.d("Test", userId + " : " + authToken);

        queue = Volley.newRequestQueue(this);
        //  String url = "http://softwaredevelopersusa.com/ws-notification";
        JSONObject object = null;
        try {
            if (userId != null && authToken != null) {
                object = new JSONObject();
                object.put("UserType", IConstant.UserType);
                object.put("UserId", userId);
                object.put("AuthToken", authToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.Notification, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test", response.toString());
                closeprogress();
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    String Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    notificationModels = new ArrayList<>();
                    String isSuccess = response.getString("Message");
                    if (isSuccess.equals("Success")) {
                        JSONArray array = response.getJSONArray("Notifications");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            String id = object1.getString("Id");
                            String msg = object1.getString("message");
                            String date = object1.getString("Date");
                            NotificationModel model = new NotificationModel();
                            model.setDate(date);
                            model.setId(id);
                            model.setMessage(msg);
                            notificationModels.add(model);
                            System.out.println("notification-->" + date + id + msg);
                        }
                        if (notificationModels.size() == 0) {
                            TrukrApplication.alertDialog(mContext, IConstant.alert, "Notifications not available");
                        }
                        System.out.println("ARRAY Size :" + notificationModels.size());
                        createList();
                    }
                    if (notificationModels.isEmpty()) {
                        notify_view.setVisibility(View.GONE);
                        notification.setVisibility(View.VISIBLE);
                    } else {
                        notify_view.setVisibility(View.VISIBLE);
                        notification.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                }

        }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
               // Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
                Log.d("Response: ", error.toString());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error list-->", json);
                        try {
                            //   Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                             //   Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(mContext, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
            }
        });
        // volley time out error
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void createList() {
        notificationListAdapter = new NotificationListAdapter(mContext, notificationModels);
        notificationListAdapter.setOnItemClickListener(this);
        notify_view.setAdapter(notificationListAdapter);
        notificationListAdapter.notifyDataSetChanged();
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    @Override
    public void onItemClick(View itemView, int position) {

    }

}
