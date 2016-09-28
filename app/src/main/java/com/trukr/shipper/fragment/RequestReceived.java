package com.trukr.shipper.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.DirectionsParser;
import com.trukr.shipper.activity.NewJobDetails;
import com.trukr.shipper.activity.Notification;
import com.trukr.shipper.activity.SlidingDrawer;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.GeneralResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nijamudhin on 6/21/2016.
 */
public class RequestReceived extends FragmentActivity {
    private GoogleMap map;
    Double latstart = null, latEnd = null, longStart = null, longEnd = null;
    ImageView jobStatus, back, notification;
    LinearLayout more_Icon;
    TextView label_status;
    RequestQueue queue;
    private Context mContext;
    private ProgressDialog pd;
    SharedPreferences preference, preferences;
    SharedPreferences.Editor editor;
    Dialog dialogCamera;
    String authToken, userId, orderId, fromAdd, toAdd, label, approve;
    int StatusCode;
    int approval = 0;
    String orderstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_screen);
        mContext = RequestReceived.this;
        more_Icon = (LinearLayout) findViewById(R.id.ll_moreicon);
        label_status = (TextView) findViewById(R.id.received_label);
        jobStatus = (ImageView) findViewById(R.id.iv_currentjob);
        back = (ImageView) findViewById(R.id.iv_back);
        notification = (ImageView) findViewById(R.id.iv_notification);
        Intent intent = getIntent();
        orderId = intent.getStringExtra("OrderId");
        orderstatus = intent.getStringExtra("OrderStatus");
        System.out.println("orderstatus = " + orderstatus);
        preference = getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        authToken = preference.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preference.getString("Userid", null);
        if (Constant.isConnectingToInternet(mContext)) {
            readOrder();//Method declaration
        } else  {
            TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }

        process();
    }

    private void process() {
        more_Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageAlert();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SlidingDrawer.class);
                intent.putExtra("OrderStatus", 1);
                startActivity(intent);
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestReceived.this, Notification.class);
                startActivity(intent);
            }
        });
    }

    private void showImageAlert() {
        dialogCamera = new Dialog(mContext);
        dialogCamera.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCamera.setContentView(R.layout.dialog);
        dialogCamera.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogCamera.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogCamera.show();
        TextView btnApproval = (TextView) dialogCamera.findViewById(R.id.btn_Approval);
        TextView btnDisapproval = (TextView) dialogCamera.findViewById(R.id.btn_Disapproval);
        TextView btnJobDetail = (TextView) dialogCamera.findViewById(R.id.btn_Job_Detail);
        TextView btnCancelOrder = (TextView) dialogCamera.findViewById(R.id.btn_Cancel_Order);
        TextView btnCancel = (TextView) dialogCamera.findViewById(R.id.btn_Cancel);

        if (label.equals("Pending")) {
            btnApproval.setVisibility(View.GONE);
            btnDisapproval.setVisibility(View.GONE);
        }

        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCamera.dismiss();
                approval = 1;
                approvealertdialog(RequestReceived.this, IConstant.alert, getResources().getString(R.string.approveorder), StatusCode);
            }
        });

        btnDisapproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogCamera.dismiss();
                approval = 0;
                approvealertdialog(RequestReceived.this, IConstant.alert, getResources().getString(R.string.disapproveorder), StatusCode);
            }
        });



        /* JobButton button click Method */
        btnJobDetail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogCamera.dismiss();
                Intent intent = new Intent(RequestReceived.this, NewJobDetails.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("OrderStatus", label);
                startActivity(intent);
            }
        });

        /* CancelOrder button click Method */
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogCamera.dismiss();
                cancelalertdialog(RequestReceived.this, IConstant.alert, getResources().getString(R.string.cancelorder), StatusCode);
            }

        });
        /* Cancel button click Method */
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialogCamera.cancel();
            }
        });
    }

    private void approvealertdialog(final Context mContext, String Title, String Content, final int Status) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(Title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(Content);

        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        final Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        btnDialogOk.setText("Yes");
        btnDialogCancel.setText("No");

        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                approve = String.valueOf(approval);
                if (Constant.isConnectingToInternet(mContext)) {
                    approveJob();//Method declaration
                } else  {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                }


            }
        });
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    private void approveJob() {
        pd = new ProgressDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(this);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("OrderId", orderId);
            object.put("ApproveStatus", approve);
            System.out.println("print the request of approval" + approve);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ApproveJob, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("approval response", response.toString());
                if (StatusCode ==100){
                    Intent intent = new Intent(mContext, SlidingDrawer.class);
                    intent.putExtra("OrderStatus", 1);
                    startActivity(intent);
                }
                String Message = null;
                try {
                    StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    Message = response.getString("Message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                closeprogress();
                if (StatusCode == 97) {
                    TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                } else {
                    cancelAlertDialog(RequestReceived.this, IConstant.alert, Message, StatusCode);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
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
                               // Toast.makeText(RequestReceived.this, message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RequestReceived.this);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //dismiss the dialog
                                                dialog.dismiss();
                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                          //  Toast.makeText(RequestReceived.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void disapproveJob() {
        pd = new ProgressDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(this);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("OrderId", orderId);
            object.put("ApproveStatus", approve);
            System.out.println("print the request of approval" + approve);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ApproveJob, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("approval response", response.toString());
                String Message = null;
                try {
                    StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    Message = response.getString("Message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                closeprogress();
                if (StatusCode == 97) {
                    TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                } else {
                    cancelAlertDialog(RequestReceived.this, IConstant.alert, Message, StatusCode);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
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
                              //  Toast.makeText(RequestReceived.this, message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RequestReceived.this);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //dismiss the dialog
                                                dialog.dismiss();
                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                          //  Toast.makeText(RequestReceived.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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


    private void readOrder() {
        pd = new ProgressDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        // Get the value of orderId from intent get
        try {
            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            if (b != null) {
                orderId = (String) b.get("OrderId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("Test", orderId + ":" + userId + " : " + authToken);
        queue = Volley.newRequestQueue(this);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("OrderId", orderId);
            System.out.println("readputparams-------->" + userId + authToken + orderId);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ReadShipment, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Testofreadorder", response.toString());
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
                    if (response.getString("Message").equals("Success")) {
                        label = response.getString("OrderStatus");
                        label_status.setText(label);
                        JSONObject shipment = response.getJSONObject("Shipment");
                        JSONObject source = shipment.getJSONObject("Source");
                        JSONObject destination = shipment.getJSONObject("Destination");
                        fromAdd = source.getString("FromAddress");
                        toAdd = destination.getString("ToAddress");
                        String fromlat = source.getString("FromLatitude");
                        String fromlon = source.getString("FromLongitude");
                        String tolat = destination.getString("ToLatitude");
                        String tolon = destination.getString("ToLongitude");

                        preferences = getSharedPreferences("Details", MODE_PRIVATE);
                        editor = preferences.edit();
                        editor.putString("FromAddress", fromAdd);
                        editor.putString("FromLatitude", fromlat);
                        editor.putString("FromLongitude", fromlon);
                        editor.putString("ToAddress", toAdd);
                        editor.putString("ToLatitude", tolat);
                        editor.putString("ToLongitude", tolon);
                        editor.commit();

                        System.out.println(fromAdd.toString() + toAdd.toString());
                        System.out.println(fromlat.toString() + fromlon.toString());
                        System.out.println(tolat.toString() + tolon.toString());
                        latstart = Double.parseDouble(fromlat);
                        latEnd = Double.parseDouble(tolat);
                        longStart = Double.parseDouble(fromlon);
                        longEnd = Double.parseDouble(tolon);
                        System.out.println("Directions----->" + latstart + latEnd + longStart + longEnd);
                        if (map == null) {
                            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapid)).getMap();
                            // check if map is created successfully or not
                            if (map == null) {

                              //  Toast.makeText(RequestReceived.this,
                                        //"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                            }
                        }
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latstart, longStart), 7));
                        map.animateCamera(CameraUpdateFactory.zoomTo(7));
                        String url = getDirectionsUrl();
                        // Start downloading json data from Google Directions API
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
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
                            //    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RequestReceived.this);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //dismiss the dialog
                                                dialog.dismiss();
                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private String getDirectionsUrl() {
        // Origin of route
        String str_origin = "origin=" + latstart + "," + longStart;
        // Destination of route
        String str_dest = "destination=" + latEnd + "," + longEnd;
        // Sensor enabled
        String sensor = "sensor=false";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null)
                sb.append(line);
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception in url", e.toString());
        } finally {

            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";
            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                System.out.println("data = " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsParser parser = new DirectionsParser();
                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            MarkerOptions options = new MarkerOptions();

            /**
             * Starting point
             */
            LatLng start = new LatLng(latstart, longStart);
            map.addMarker(new MarkerOptions().position(start)/*.title("Marker in Pondy")*/.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon)).title(fromAdd));

            map.moveCamera(CameraUpdateFactory.newLatLng(start));
            //options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            /**
             * Ending point
             */
            LatLng end = new LatLng(latEnd, longEnd);
            map.addMarker(new MarkerOptions().position(end)/*.title("Marker in Goa")*/.icon(BitmapDescriptorFactory.fromResource(R.drawable.imgpsh_fullsize)).title(toAdd));
           /* map.moveCamera(CameraUpdateFactory.newLatLng(end));
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));*/


            try {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();
                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);
                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);
                        points.add(position);
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.parseColor("#00b3fd"));
                }

                // Drawing polyline in the Google Map for the i-th route
                map.addPolyline(lineOptions);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void cancelJob() {
        pd = new ProgressDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(this);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("OrderId", orderId);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.CancelJob, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test", response.toString());
                String Message = null;
                try {
                    StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    Message = response.getString("Message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                closeprogress();
                if (StatusCode == 97) {
                    TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                } else {
                    cancelAlertDialog(RequestReceived.this, IConstant.alert, Message, StatusCode);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
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
                                //Toast.makeText(RequestReceived.this, message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(RequestReceived.this);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //dismiss the dialog
                                                dialog.dismiss();
                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                          //  Toast.makeText(RequestReceived.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    public void cancelalertdialog(final Context mContext, String Title, String Content, final int Status) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(Title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(Content);

        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);

        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Constant.isConnectingToInternet(mContext)) {
                    cancelJob();//Method declaration
                } else  {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                }

                Intent intent = new Intent(mContext, SlidingDrawer.class);
                intent.putExtra("OrderStatus", 1);
                startActivity(intent);
            }
        });
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void cancelAlertDialog(final Context mContext, String Title, String Content, final int Status) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(Title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(Content);

        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        line.setVisibility(View.GONE);
        btnDialogCancel.setVisibility(View.GONE);
        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        btnDialogOk.setBackgroundResource(R.drawable.dialogbtnbackground);

        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status == 100) {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, SlidingDrawer.class);
                    intent.putExtra("OrderStatus", 1);
                    startActivity(intent);
                } else
                    dialog.dismiss();
            }
        });
        dialog.show();
    }

}
