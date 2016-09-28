package com.trukr.shipper.fragment;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.Login;
import com.trukr.shipper.activity.ViewDetails;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.components.DeliveryDatePickerFragment;
import com.trukr.shipper.components.EditPickDate;
import com.trukr.shipper.components.EditshipmentDate;
import com.trukr.shipper.components.PickUpDatePickerFragment;
import com.trukr.shipper.components.TimePickerFragment;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.ReadShipmentResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.android.volley.toolbox.Volley.newRequestQueue;

/**
 * Created by nijamudhin on 5/23/2016.
 */
public class NewJobScreen extends Fragment {
    SharedPreferences preference, preferences;
    RequestQueue queue;
    Context context;
    TextView txt_border, txt_nostraps, txt_stopintransit, txt_hazardousmaterial, txt_teamservice, txt_redelivery, price, imagedocuments, et_source, et_destination, notes, divider7, divider8, divider9, divider10, divider12, divider13;
    TextView hazadarous, stopintransit, nostraps, borders, teamServie, reDelivery, txt_trucktype,total,includes;
    Button btn_pickdate, btn_picktime, btn_deliverydate, btn_deliverytime, viewdetails;
    SharedPreferences.Editor editor;
    String userId, authToken, orderId, trucktype, toAdd, fromAdd, borderCrossStatus, pickdate, picktime, deliverydate, deliverytime, borderCrossingFee, hazmat, teamService, redelivery, transit, straps;
    int Success = 100;
    View rootView;
    Button  edit,save;
    String deliveryDateValue="", deliveryTimevalue="";
    int statuscode = 100;
    private ProgressDialog pd;
    String s,s1,s2;
    public  static int s_date,s_month,s_year ;
    Typeface Gibson_Light, HnBold, HnThin, HnLight, Gibson_Regular, GillSansStd, Hn;

    public NewJobScreen() {
        //need empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.newjob_screen, container, false);
        context = getContext();
        init();
        if (Constant.isConnectingToInternet(context)) {
            ReadShipment();//Method declaration
        } else {
            TrukrApplication.alertDialog(context, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private void init() {

        et_source = (TextView) rootView.findViewById(R.id.newjob_et_source);
        et_destination = (TextView) rootView.findViewById(R.id.newjob_et_destination);
        txt_trucktype = (TextView) rootView.findViewById(R.id.newjob_btn_trucktype);
        btn_pickdate = (Button) rootView.findViewById(R.id.newjob_btn_pickupdate);
        btn_picktime = (Button) rootView.findViewById(R.id.newjob_btn_pickuptime);
        btn_deliverydate = (Button) rootView.findViewById(R.id.newjob_btn_deliverydate);
        btn_deliverytime = (Button) rootView.findViewById(R.id.newjob_btn_deliverytime);
        borders = (TextView) rootView.findViewById(R.id.newjob_btn_border);
        nostraps = (TextView) rootView.findViewById(R.id.newjob_btn_nostraps);
        stopintransit = (TextView) rootView.findViewById(R.id.newjob_btn_stopintransit);
        hazadarous = (TextView) rootView.findViewById(R.id.newjob_btn_hazardousmaterial);
        teamServie = (TextView) rootView.findViewById(R.id.newjob_btn_teamservice);
        reDelivery = (TextView) rootView.findViewById(R.id.newjob_btn_redelivery);
        viewdetails = (Button) rootView.findViewById(R.id.newjob_btn_viewdetails);
        ///  save = (Button) rootView.findViewById(R.id.btn_save);
        edit = (Button) rootView.findViewById(R.id.btn_edit);

        txt_hazardousmaterial = (TextView) rootView.findViewById(R.id.newjob_hazardousmaterial);
        txt_border = (TextView) rootView.findViewById(R.id.newjob_txt_border);
        txt_nostraps = (TextView) rootView.findViewById(R.id.newjob_txt_nostraps);
        txt_stopintransit = (TextView) rootView.findViewById(R.id.newjob_txt_stopintransit);
        txt_teamservice = (TextView) rootView.findViewById(R.id.newjob_teamservice);
        txt_redelivery = (TextView) rootView.findViewById(R.id.newjob_redelivery);
        price = (TextView) rootView.findViewById(R.id.newjob_txt_price);
        total = (TextView) rootView.findViewById(R.id.newjob_txt_total);
        notes = (TextView) rootView.findViewById(R.id.newjob_et_notes);
        includes = (TextView) rootView.findViewById(R.id.newjob_txt_includes);

        divider7 = (TextView) rootView.findViewById(R.id.newjob_txt_divider7);
        divider8 = (TextView) rootView.findViewById(R.id.newjob_txt_divider8);
        divider9 = (TextView) rootView.findViewById(R.id.newjob_txt_divider9);
        divider10 = (TextView) rootView.findViewById(R.id.newjob_txt_divider10);
        divider12 = (TextView) rootView.findViewById(R.id.newjob_txt_divider12);
        divider13 = (TextView) rootView.findViewById(R.id.newjob_txt_divider13);

        Gibson_Light = Typeface.createFromAsset(getActivity().getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        HnLight = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Light.ttf");
      //  Hn = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue.ttf");
        et_source.setTypeface(HnLight);
        et_destination.setTypeface(HnLight);
        btn_pickdate.setTypeface(HnLight);
        btn_picktime.setTypeface(HnLight);
        btn_deliverydate.setTypeface(HnLight);
        btn_deliverytime.setTypeface(HnLight);
        price.setTypeface(Gibson_Light);
        total.setTypeface(Gibson_Light);
        includes.setTypeface(HnLight);
        viewdetails.setTypeface(HnLight);
        edit.setTypeface(Gibson_Light);

        viewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant.isConnectingToInternet(context)) {
                    viewdetails();//Method declaration
                } else {
                    TrukrApplication.alertDialog(context, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                }


            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  edit.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);*/

                if (edit.getText().equals("Edit delivery date and time")) {
                    edit.setText("Save");
                    btn_deliverydate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment picker = new EditshipmentDate(btn_deliverydate);
                            picker.show(getFragmentManager(), "DatePicker");
                        }
                    });


                    btn_deliverytime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment picker = new TimePickerFragment(btn_deliverytime);
                            picker.show(getFragmentManager(), "TimePicker");
                        }
                    });

                } else if (edit.getText().equals("Save")) {
                    EditShipment();
                    edit.setText("Edit delivery date and time");
                    btn_deliverytime.setEnabled(false);
                    btn_deliverydate.setEnabled(false);
                }
            }
        });
    }

  /*  String ButtonText = button.getText().toString();
    if(ButtonText.equals("Save"){
        //code for save
        button.setText("Edit");
    }
    else{
        //code for edit
        button.setText("Save");
    }
}

});*/

    private void EditShipment() {

        //* {"UserType":"2","UserId":"496","AuthToken":"805fc7b121","OrderId":"4","DeliveryDateTime":"2016-01-31 11:00"}*//*
        pd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preferences = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        preference = getActivity().getSharedPreferences("pending", Context.MODE_PRIVATE);
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);
        orderId = preference.getString("OrderId", null);
        deliveryDateValue = btn_deliverydate.getText().toString().trim();
        deliveryTimevalue = btn_deliverytime.getText().toString().trim();
        SimpleDateFormat sdfSource = new SimpleDateFormat("MM/dd/yyyy");
        String pickupDateValues = null;
        String deliveryDateValues = null;
        try {
            //parse the string into Date object
            Date date1 = sdfSource.parse(deliveryDateValue);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
            //parse the date into another format
            deliveryDateValue = sdfDestination.format(date1);
            System.out.println("simple format value--->" + deliveryDateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.d("Test", orderId + ":" + userId + " : " + authToken);
        queue = Volley.newRequestQueue(getActivity());
        //  String url = "http://softwaredevelopersusa.com/ws-read-order";
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("OrderId", orderId);
            object.put("DeliveryDateTime", deliveryDateValue + deliveryTimevalue);
            System.out.println("object = " + object);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.EditShipment1, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test", response.toString());

                ReadShipmentResponse responseData = (ReadShipmentResponse) TrukrApplication.getFromJSON(response.toString(), ReadShipmentResponse.class);
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    String Message = response.getString("Message");
                    closeprogress();
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(getActivity(), IConstant.alert, Message);
                    }
                    if (StatusCode == 100) {
                        editalertdialog(context, IConstant.alert, Message, StatusCode);
                    }
                } catch (JSONException e) {
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
//                             Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                                //  Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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
                            // Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void editalertdialog(final Context ctx, String Title, String Content, final int Status) {

        final Dialog dialog = new Dialog(ctx, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.customdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_dialog_tv_alerthead);

        alertHead.setText(Title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_dialog_tv_alertcontent);
        alertContent.setText("Updated Successfully");
        dialog.show();

        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_dialog_btn_ok);
        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statuscode == 100) {
                    dialog.dismiss();
                  // edit.setVisibility(View.VISIBLE);
                   // save.setVisibility(View.GONE);
                } else if (statuscode == 97) {
                    dialog.dismiss();
                    Intent intent = new Intent(ctx, Login.class);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                }
            }


        });
    }

   /* public void editalertdialog(final Context ctx, String Title, String Content, final int Status) {

        final Dialog dialog = new Dialog(ctx, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.customdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_dialog_tv_alerthead);

        alertHead.setText(Title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_dialog_tv_alertcontent);
        alertContent.setText("Updated Successfully");
        dialog.show();

        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_dialog_btn_ok);
        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (statuscode == 100) {
                    dialog.dismiss();
                } else if (statuscode == 97) {
                    dialog.dismiss();
                    Intent intent = new Intent(ctx, Login.class);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                }
            }


        });
    }
*/

    private void ReadShipment() {
        pd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preferences = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        preference = getActivity().getSharedPreferences("pending", Context.MODE_PRIVATE);
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);
        orderId = preference.getString("OrderId", null);
        Log.d("Test", orderId + ":" + userId + " : " + authToken);
        queue = Volley.newRequestQueue(getActivity());
        //  String url = "http://softwaredevelopersusa.com/ws-read-order";
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("OrderId", orderId);
            System.out.println("UserType-->" + IConstant.UserType);
            System.out.println("UserId-->" + userId);
            System.out.println("AuthToken-->" + authToken);
            System.out.println("OrderId-->" + orderId);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ReadShipment, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test", response.toString());
                ReadShipmentResponse responseData = (ReadShipmentResponse) TrukrApplication.getFromJSON(response.toString(), ReadShipmentResponse.class);
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    String Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(getActivity(), IConstant.alert, Message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (response.getString("Message").equals("Success")) {
                        JSONObject shipment = response.getJSONObject("Shipment");
                        JSONObject source = shipment.getJSONObject("Source");
                        JSONObject destination = shipment.getJSONObject("Destination");
                        toAdd = destination.getString("ToAddress");
                        fromAdd = source.getString("FromAddress");
                        et_destination.setText(toAdd);
                        et_source.setText(fromAdd);
                        Log.d("Test", fromAdd);
                        JSONObject payment = response.getJSONObject("Payment");
                        String total = payment.getString("TotalPayable");
                        Float f1 = Float.parseFloat(total);
                        float k = (float) Math.round(f1 * 100) / 100;
                        price.setText("$" + k);
                        trucktype = response.getString("TruckType");
                        if ((response.getString("TruckType").equals("1"))) {
                            txt_trucktype.setText("Vented Van");
                        } else if ((response.getString("TruckType")).equals("2")) {
                            txt_trucktype.setText("Van_Air Ride");
                        } else if ((response.getString("TruckType")).equals("3")) {
                            txt_trucktype.setText("Van W/Liftgate");
                        }
                        borderCrossStatus = response.getString("BorderCrossStatus");
                        pickdate = response.getString("PickupDate");
                        btn_pickdate.setText(pickdate.toString());

                        System.out.println("btn_pickdate = " + pickdate);
                        String[] separated = pickdate.split("/");
                         s = separated[0];
                        s_month=Integer.parseInt(s);
                         s1 = separated[1];
                        s_date=Integer.parseInt(s1);
                         s2 = separated[2];
                        s_year=Integer.parseInt(s2);
                        System.out.println("date = " + s_date);//year
                        System.out.println("month = " + s_month);//day
                        System.out.println("year = " + s_year);//month

                        deliverydate = response.getString("DeliveryDate");
                        btn_deliverydate.setText(deliverydate.toString());
                        picktime = response.getString("PickupTime");
                        btn_picktime.setText(picktime.toString());
                        deliverytime = response.getString("DeliveryTime");
                        btn_deliverytime.setText(deliverytime.toString());
                        String notescontent = response.getString("Notes");
                        notes.setText(notescontent.toString());
                        JSONObject accessories = response.getJSONObject("Accessories");

                        borderCrossingFee = accessories.getString("Border Crossing fee");
                        if ((accessories.getString("Border Crossing fee")).length() <= 0) {
                            txt_border.setVisibility(View.GONE);
                            borders.setVisibility(View.GONE);
                            divider7.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Border Crossing fee").equals("1")) {
                                txt_border.setText("Canada");
                            } else {
                                txt_border.setText("Mexico");
                            }
                        }
                        straps = accessories.getString("No Of Straps");
                        if ((accessories.getString("No Of Straps")).length() <= 0) {
                            nostraps.setVisibility(View.GONE);
                            txt_nostraps.setVisibility(View.GONE);
                            divider8.setVisibility(View.GONE);
                        } else {
                            txt_nostraps.setText(straps);
                        }

                        transit = accessories.getString("Stops-in-transit");
                        if ((accessories.getString("Stops-in-transit")).length() <= 0) {
                            stopintransit.setVisibility(View.GONE);
                            txt_stopintransit.setVisibility(View.GONE);
                            divider9.setVisibility(View.GONE);
                        } else {
                            txt_stopintransit.setText(transit);
                        }
                        hazmat = accessories.getString("Hazmat charge");
                        if ((accessories.getString("Hazmat charge")).length() <= 0) {
                            txt_hazardousmaterial.setVisibility(View.GONE);
                            hazadarous.setVisibility(View.GONE);
                            divider10.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Hazmat charge").equals("0")) {
                                txt_hazardousmaterial.setText("No");
                            } else {
                                txt_hazardousmaterial.setText("Yes");
                            }
                        }
                        teamService = accessories.getString("Team Service");
                        if ((accessories.getString("Team Service")).length() <= 0) {
                            teamServie.setVisibility(View.GONE);
                            txt_teamservice.setVisibility(View.GONE);
                            divider12.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Team Service").equals("0")) {
                                txt_teamservice.setText("No");
                            } else {
                                txt_teamservice.setText("Yes");
                            }
                        }
                        redelivery = accessories.getString("Redelivery Charge");
                        if ((accessories.getString("Redelivery Charge")).length() <= 0) {
                            reDelivery.setVisibility(View.GONE);
                            txt_redelivery.setVisibility(View.GONE);
                            divider13.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Redelivery Charge").equals("0")) {
                                txt_redelivery.setText("No");
                            } else {
                                txt_redelivery.setText("Yes");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                closeprogress();

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
//                             Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                                //  Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
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
                            // Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    private void viewdetails() {
        pd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = newRequestQueue(getActivity());
        //     String url = "http://softwaredevelopersusa.com/ws-calculate-distance";
        JSONObject object = null;
        JSONObject accessories = new JSONObject();
        try {
            object = new JSONObject();

            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("FromLat", "");
            object.put("FromLong", "");
            object.put("ToLat", "");
            object.put("ToLong", "");
            object.put("TruckType", trucktype);
            object.put("FromAddress", fromAdd);
            object.put("ToAddress", toAdd);
            object.put("BorderCrossStatus", borderCrossStatus);
            object.put("OrderId", orderId);
            object.put("PickupDate", pickdate);
            object.put("PickupTime", picktime);
            object.put("DeliveryDate", deliverydate);
            object.put("DeliveryTime", deliverytime);

            accessories.put("BorderCrossing", borderCrossingFee);
            accessories.put("NoOfStraps", straps);
            accessories.put("StopsinTransit", transit);
            accessories.put("HazardousMaterial", hazmat);
            accessories.put("TeamService", teamService);
            accessories.put("RedeliveryCharge", redelivery);

            object.put("Accessories", accessories);
            System.out.println("object" + object);
        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.GetCalculateDistance, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResponseOfViewDeatails:", response.toString());
                closeprogress();
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    String Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(getActivity(), IConstant.alert, Message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity(), ViewDetails.class);
                intent.putExtra("jsonResponse", response.toString());
                startActivity(intent);

            }

            //forgotalertdialog(ctx, IConstant.title, message, StatusCode);
            //

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                Log.d("Response: ", error.toString());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);
                        try {
                            //   Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                                //   Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                android.app.AlertDialog.Builder dlgAlert = new android.app.AlertDialog.Builder(getActivity());
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
                            // Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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





}
