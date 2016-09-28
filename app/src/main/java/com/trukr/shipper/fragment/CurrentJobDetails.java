package com.trukr.shipper.fragment;

import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.trukr.shipper.activity.Login;
import com.trukr.shipper.activity.WebViewDisplay;
import com.trukr.shipper.activity.ViewDetails;
import com.trukr.shipper.application.TrukrApplication;
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
 * Created by nijamudhin on 5/27/2016.
 */
public class CurrentJobDetails extends Fragment {

    TextView txt_border, txt_nostraps, txt_stopintransit, txt_hazardousmaterial, txt_teamService, txt_redelivery, txt_tollCharge, txt_weightfine, txt_yardStorage, txt_reweighing, txt_lumperCharge, txt_powerusageHours, txt_weekDays, txt_solo, txt_image, txt_img1, txt_img2, txt_img3, txt_img4, txt_img5, price, txt_source, txt_destination, notes,total,includes;
    TextView hazadarous, stopintransit, nostraps, borders, txt_trucktype, teamService, redeliveryCharge, tollCharge, weightFine, yardStorage, reweighting, lumperCharge, powerusageHours, layOver, weekDays, solo, viewDetails;
    Button btn_pickdate, btn_picktime, btn_deliverydate, btn_deliverytime;
    TextView divider7, divider8, divider9, divider10, divider11, divider12, divider13, divider14, divider15, divider16, divider17, divider18, divider19, divider20, divider21, divider26, divider27;
    SharedPreferences preferences, preference;
    RequestQueue queue;
    Context context;
    private ProgressDialog pd;
    String userId, authToken, orderId, trucktype, toAdd, fromAdd, borderCrossStatus, pickdate, picktime, deliverydate, deliverytime, borderCrossingFee, hazmat, teamservice, redelivery, transit, straps;
    View rootView;
    Activity activity;
    LayoutInflater vi;
    Typeface Gibson_Light, HnThin, HnLight;
    Button edit, save;
    int statuscode = 100;
    String deliveryDateValue, deliveryTimevalue;
    public  static int s_date,s_month,s_year ;
    String s,s1,s2;
    TextView div22,div23,div24,div25,div29,div28;

    public CurrentJobDetails() {
        //Need empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.details_fragment, container, false);
        init();
        if (Constant.isConnectingToInternet(context)) {
            ReadShipment();//Method declaration
        } else {
            TrukrApplication.alertDialog(context, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }

        return rootView;
    }

    public void init() {
        context = getContext();
        activity = getActivity();

        if (getActivity() != null) {
            vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } else {
            vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        txt_source = (TextView) rootView.findViewById(R.id.details_et_source);
        txt_destination = (TextView) rootView.findViewById(R.id.details_et_destination);
        txt_trucktype = (TextView) rootView.findViewById(R.id.details_btn_trucktype);
        btn_pickdate = (Button) rootView.findViewById(R.id.details_btn_pickupdate);
        btn_picktime = (Button) rootView.findViewById(R.id.details_btn_pickuptime);
        btn_deliverydate = (Button) rootView.findViewById(R.id.details_btn_deliverydate);
        btn_deliverytime = (Button) rootView.findViewById(R.id.details_btn_deliverytime);
        borders = (TextView) rootView.findViewById(R.id.details_btn_border);
        nostraps = (TextView) rootView.findViewById(R.id.details_btn_nostraps);
        stopintransit = (TextView) rootView.findViewById(R.id.details_btn_stopintransit);
        hazadarous = (TextView) rootView.findViewById(R.id.details_btn_hazardousmaterial);
        txt_hazardousmaterial = (TextView) rootView.findViewById(R.id.details_hazardousmaterial);
        txt_border = (TextView) rootView.findViewById(R.id.details_txt_border);
        txt_nostraps = (TextView) rootView.findViewById(R.id.details_txt_nostraps);
        txt_stopintransit = (TextView) rootView.findViewById(R.id.details_txt_stopintransit);
        teamService = (TextView) rootView.findViewById(R.id.details_btn_teamservice);
        redeliveryCharge = (TextView) rootView.findViewById(R.id.details_btn_redeliverycharge);
        tollCharge = (TextView) rootView.findViewById(R.id.details_btn_tollcharge);
        weightFine = (TextView) rootView.findViewById(R.id.details_btn_weightfine);
        yardStorage = (TextView) rootView.findViewById(R.id.details_btn_yardstorage);
        reweighting = (TextView) rootView.findViewById(R.id.details_btn_reweighting);
        lumperCharge = (TextView) rootView.findViewById(R.id.details_btn_lumpercharge);
        powerusageHours = (TextView) rootView.findViewById(R.id.details_btn_powerusagehours);
        layOver = (TextView) rootView.findViewById(R.id.details_btn_layover);
        weekDays = (TextView) rootView.findViewById(R.id.details_btn_weekdays);
        solo = (TextView) rootView.findViewById(R.id.details_btn_solo);

        edit = (Button) rootView.findViewById(R.id.btn_edit);
        //save = (Button) rootView.findViewById(R.id.btn_save);
        if (RequestStatus.orderStatus.equals("In Transit")) {
            edit.setVisibility(View.GONE);
           // save.setVisibility(View.GONE);
        }
        txt_hazardousmaterial = (TextView) rootView.findViewById(R.id.details_hazardousmaterial);
        txt_border = (TextView) rootView.findViewById(R.id.details_txt_border);
        txt_nostraps = (TextView) rootView.findViewById(R.id.details_txt_nostraps);
        txt_stopintransit = (TextView) rootView.findViewById(R.id.details_txt_stopintransit);
        txt_teamService = (TextView) rootView.findViewById(R.id.details_teamservice);
        txt_redelivery = (TextView) rootView.findViewById(R.id.details_redeliverycharge);
        txt_tollCharge = (TextView) rootView.findViewById(R.id.details_tollcharge);
        txt_weightfine = (TextView) rootView.findViewById(R.id.details_weightfine);
        txt_reweighing = (TextView) rootView.findViewById(R.id.details_reweighting);
        txt_yardStorage = (TextView) rootView.findViewById(R.id.details_yardstorage);
        txt_lumperCharge = (TextView) rootView.findViewById(R.id.details_lumpercharge);
        txt_powerusageHours = (TextView) rootView.findViewById(R.id.details_powerusagehours);
        txt_weekDays = (TextView) rootView.findViewById(R.id.details_weekdays);
        txt_solo = (TextView) rootView.findViewById(R.id.details_solo);
        txt_image = (TextView) rootView.findViewById(R.id.details_txt_image);
        txt_img1 = (TextView) rootView.findViewById(R.id.details_img1);
        txt_img2 = (TextView) rootView.findViewById(R.id.details_img2);
        txt_img3 = (TextView) rootView.findViewById(R.id.details_img3);
        txt_img4 = (TextView) rootView.findViewById(R.id.details_img4);
        txt_img5 = (TextView) rootView.findViewById(R.id.details_img5);


        div22=(TextView)rootView.findViewById(R.id.details_txt_divider22) ;
        div23=(TextView)rootView.findViewById(R.id.details_txt_divider23) ;
        div24=(TextView)rootView.findViewById(R.id.details_txt_divider24) ;
        div25=(TextView)rootView.findViewById(R.id.details_txt_divider25) ;
        div29=(TextView)rootView.findViewById(R.id.details_txt_divider29) ;
        div28=(TextView)rootView.findViewById(R.id.details_txt_divider28) ;

        price = (TextView) rootView.findViewById(R.id.details_txt_price);
        includes = (TextView) rootView.findViewById(R.id.details_txt_includes);
        total = (TextView) rootView.findViewById(R.id.details_txt_total);
        notes = (TextView) rootView.findViewById(R.id.details_et_notes);
        viewDetails = (TextView) rootView.findViewById(R.id.details_btn_viewdetails);
        divider7 = (TextView) rootView.findViewById(R.id.details_txt_divider7);
        divider8 = (TextView) rootView.findViewById(R.id.details_txt_divider8);
        divider9 = (TextView) rootView.findViewById(R.id.details_txt_divider9);
        divider10 = (TextView) rootView.findViewById(R.id.details_txt_divider10);
        divider11 = (TextView) rootView.findViewById(R.id.details_txt_divider11);
        divider12 = (TextView) rootView.findViewById(R.id.details_txt_divider12);
        divider13 = (TextView) rootView.findViewById(R.id.details_txt_divider13);
        divider14 = (TextView) rootView.findViewById(R.id.details_txt_divider14);
        divider15 = (TextView) rootView.findViewById(R.id.details_txt_divider15);
        divider16 = (TextView) rootView.findViewById(R.id.details_txt_divider16);
        divider17 = (TextView) rootView.findViewById(R.id.details_txt_divider17);
        divider18 = (TextView) rootView.findViewById(R.id.details_txt_divider18);
        divider19 = (TextView) rootView.findViewById(R.id.details_txt_divider19);
        divider20 = (TextView) rootView.findViewById(R.id.details_txt_divider20);
        divider21 = (TextView) rootView.findViewById(R.id.details_txt_divider21);
       // divider26 = (TextView) rootView.findViewById(R.id.details_txt_divider26);
        divider27 = (TextView) rootView.findViewById(R.id.details_txt_divider27);

        Gibson_Light = Typeface.createFromAsset(getActivity().getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        HnLight = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Light.ttf");

        txt_source.setTypeface(HnLight);
        txt_destination.setTypeface(HnLight);
        txt_trucktype.setTypeface(HnLight);
        btn_pickdate.setTypeface(HnLight);
        btn_picktime.setTypeface(HnLight);
        btn_deliverydate.setTypeface(HnLight);
        btn_deliverytime.setTypeface(HnLight);

        txt_hazardousmaterial.setTypeface(HnLight);
        txt_border.setTypeface(HnLight);
        txt_nostraps.setTypeface(HnLight);
        txt_stopintransit.setTypeface(HnLight);
        txt_teamService.setTypeface(HnLight);
        txt_redelivery.setTypeface(HnLight);
        txt_powerusageHours.setTypeface(HnLight);
        txt_reweighing.setTypeface(HnLight);
        txt_tollCharge.setTypeface(HnLight);
        txt_yardStorage.setTypeface(HnLight);
        txt_lumperCharge.setTypeface(HnLight);
        txt_solo.setTypeface(HnLight);
        txt_weekDays.setTypeface(HnLight);
        txt_image.setTypeface(HnLight);
        price.setTypeface(Gibson_Light);
        total.setTypeface(Gibson_Light);
        includes.setTypeface(Gibson_Light);
        notes.setTypeface(HnLight);
        edit.setTypeface(Gibson_Light);
        viewDetails.setTypeface(HnLight);

        viewDetails.setOnClickListener(new View.OnClickListener() {
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
                if (edit.getText().equals("Edit delivery date and time")) {
                    edit.setText("Save");
                    btn_deliverydate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogFragment picker = new EditPickDate(btn_deliverydate);
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
                }
            }
        });
    }

    private void EditShipment() {

        //* {"UserType":"2","UserId":"496","AuthToken":"805fc7b121","OrderId":"4","DeliveryDateTime":"2016-01-31 11:00"}*//*
        pd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();

        preferences = getActivity().getSharedPreferences("Fragment", Context.MODE_PRIVATE);
        preference = getActivity().getSharedPreferences("CurrentJobTabLayout", Context.MODE_PRIVATE);
        userId = preferences.getString("UserId", "");
        authToken = preferences.getString("AuthToken", "");
        orderId = preference.getString("Order", "");
        Log.d("Test", orderId + ":" + userId + " : " + authToken);
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
                    //save.setVisibility(View.GONE);

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


    private void ReadShipment() {
        pd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preferences = getActivity().getSharedPreferences("Fragment", Context.MODE_PRIVATE);
        preference = getActivity().getSharedPreferences("CurrentJobTabLayout", Context.MODE_PRIVATE);
        userId = preferences.getString("UserId", "");
        authToken = preferences.getString("AuthToken", "");
        orderId = preference.getString("Order", "");
        Log.d("Test", orderId + ":" + userId + " : " + authToken);
        queue = Volley.newRequestQueue(getActivity());
        //    String url = "http://softwaredevelopersusa.com/ws-read-order";
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
                closeprogress();
                ReadShipmentResponse responseData = (ReadShipmentResponse) TrukrApplication.getFromJSON(response.toString(), ReadShipmentResponse.class);
                int StatusCode = Integer.parseInt(String.valueOf(responseData.getStatusCode()));
                String Message = responseData.getMessage();
                try {
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(getActivity(), IConstant.alert, Message);
                    } else if (response.getString("Message").equals("Success")) {
                        JSONObject shipment = response.getJSONObject("Shipment");
                        JSONObject source = shipment.getJSONObject("Source");
                        JSONObject destination = shipment.getJSONObject("Destination");
                        toAdd = destination.getString("ToAddress");
                        fromAdd = source.getString("FromAddress");
                        txt_destination.setText(toAdd);
                        txt_source.setText(fromAdd);
                        Log.d("Test", fromAdd);
                        JSONObject payment = response.getJSONObject("Payment");
                        String total = payment.getString("TotalPayable");
                        Float aFloat = Float.parseFloat(total);
                        float k = (float) Math.round(aFloat * 100) / 100;
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
                        teamservice = accessories.getString("Team Service");
                        if ((accessories.getString("Team Service")).length() <= 0) {
                            teamService.setVisibility(View.GONE);
                            txt_teamService.setVisibility(View.GONE);
                            divider12.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Team Service").equals("0")) {
                                txt_teamService.setText("No");
                            } else {
                                txt_teamService.setText("Yes");
                            }
                        }
                        redelivery = accessories.getString("Redelivery Charge");
                        if ((accessories.getString("Redelivery Charge")).length() <= 0) {
                            redeliveryCharge.setVisibility(View.GONE);
                            txt_redelivery.setVisibility(View.GONE);
                            divider13.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Redelivery Charge").equals("0")) {
                                txt_redelivery.setText("No");
                            } else {
                                txt_redelivery.setText("Yes");
                            }
                        }
                        String toll = accessories.getString("Toll Charge");
                        if ((accessories.getString("Toll Charge")).length() <= 0) {
                            tollCharge.setVisibility(View.GONE);
                            txt_tollCharge.setVisibility(View.GONE);
                            divider14.setVisibility(View.GONE);
                        } else {
                            txt_tollCharge.setText(toll);
                        }

                        String weightCharge = accessories.getString("Weight Fine");
                        if ((accessories.getString("Weight Fine")).length() <= 0) {
                            weightFine.setVisibility(View.GONE);
                            txt_weightfine.setVisibility(View.GONE);
                            divider15.setVisibility(View.GONE);
                        } else {
                            txt_weightfine.setText(weightCharge);
                        }

                        String yard = accessories.getString("Yard Storage");
                        if ((accessories.getString("Yard Storage")).length() <= 0) {
                            yardStorage.setVisibility(View.GONE);
                            txt_yardStorage.setVisibility(View.GONE);
                            divider16.setVisibility(View.GONE);
                        } else {
                            txt_yardStorage.setText(yard);
                        }

                        String reweighingCharge = accessories.getString("Reweighing");
                        if ((accessories.getString("Reweighing")).length() <= 0) {
                            reweighting.setVisibility(View.GONE);
                            txt_reweighing.setVisibility(View.GONE);
                            divider17.setVisibility(View.GONE);
                        } else {
                            txt_reweighing.setText(reweighingCharge);
                        }

                        String lumperCharges = accessories.getString("Lumper Charge");
                        if ((accessories.getString("Lumper Charge")).length() <= 0) {
                            lumperCharge.setVisibility(View.GONE);
                            txt_lumperCharge.setVisibility(View.GONE);
                            divider18.setVisibility(View.GONE);
                        } else {
                            txt_lumperCharge.setText(lumperCharges);
                        }

                        String power = accessories.getString("PowerUsage Hours");
                        if ((accessories.getString("PowerUsage Hours")).length() <= 0) {
                            powerusageHours.setVisibility(View.GONE);
                            txt_powerusageHours.setVisibility(View.GONE);
                            divider19.setVisibility(View.GONE);
                        } else {
                            txt_powerusageHours.setText(power);
                        }

                        JSONObject layOvers = accessories.getJSONObject("LayOver");
                        String weekdays = layOvers.getString("WeekDays");
                        String soloDay = layOvers.getString("Solo");
                        if (layOvers.isNull("LayOver") && (weekdays.isEmpty()) && (soloDay.isEmpty())) {
                            layOver.setVisibility(View.GONE);
                            divider27.setVisibility(View.GONE);
                            weekDays.setVisibility(View.GONE);
                            txt_weekDays.setVisibility(View.GONE);
                            divider20.setVisibility(View.GONE);
                            solo.setVisibility(View.GONE);
                            txt_solo.setVisibility(View.GONE);
                            divider21.setVisibility(View.GONE);
                        } else {
                            if ((layOvers.getString("WeekDays")).length() <= 0) {
                                weekDays.setVisibility(View.GONE);
                                txt_weekDays.setVisibility(View.GONE);
                                divider20.setVisibility(View.GONE);
                            } else {
                                layOver.setVisibility(View.GONE);
                                divider27.setVisibility(View.GONE);
                                if (weekdays.equals("1")) {
                                    txt_weekDays.setText("Yes");
                                } else {
                                    txt_weekDays.setText(weekdays);
                                }

                            }
                            if ((layOvers.getString("Solo")).length() <= 0) {
                                solo.setVisibility(View.GONE);
                                txt_solo.setVisibility(View.GONE);
                                divider21.setVisibility(View.GONE);
                            } else {
                                layOver.setVisibility(View.GONE);
                                divider27.setVisibility(View.GONE);
                                if (soloDay.equals("1")) {
                                    txt_solo.setText("Yes");
                                } else {
                                    txt_solo.setText(soloDay);
                                }
                            }
                        }
                        JSONObject Images = accessories.getJSONObject("Images");
                        final String image1 = Images.getString("img1");
                        final String image2 = Images.getString("img2");
                        final String image3 = Images.getString("img3");
                        final String image4 = Images.getString("img4");
                        final String image5 = Images.getString("img5");

                        if (Images.isNull("Images") && (image1.isEmpty()) && (image2.isEmpty()) && (image3.isEmpty()) && (image4.isEmpty() && (image5.isEmpty()))) {
                            txt_image.setVisibility(View.GONE);
                          //  divider26.setVisibility(View.GONE);
                            txt_img1.setVisibility(View.GONE);
                            txt_img2.setVisibility(View.GONE);
                            txt_img3.setVisibility(View.GONE);
                            txt_img4.setVisibility(View.GONE);
                            txt_img5.setVisibility(View.GONE);
                        } else {

                            if ((Images.getString("img1")).length() <= 0) {
                                txt_img1.setVisibility(View.GONE);
                            } else {
                                txt_img1.setText(image1);
                                div23.setVisibility(View.VISIBLE);
                                div22.setVisibility(View.VISIBLE);
                                txt_img1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(), WebViewDisplay.class);
                                        i.putExtra("img1", image1);
                                        startActivity(i);
                                    }
                                });
                            }

                            if ((Images.getString("img2")).length() <= 0) {
                                txt_img2.setVisibility(View.GONE);
                            } else {
                                txt_img2.setText(image2);
                                div24.setVisibility(View.VISIBLE);
                                txt_img2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(), WebViewDisplay.class);
                                        i.putExtra("img2", image2);
                                        startActivity(i);
                                    }
                                });
                            }

                            if ((Images.getString("img3")).length() <= 0) {
                                txt_img3.setVisibility(View.GONE);

                            } else {
                                txt_img3.setText(image3);
                                div25.setVisibility(View.VISIBLE);
                                txt_img3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(), WebViewDisplay.class);
                                        i.putExtra("img3", image3);
                                        startActivity(i);
                                    }
                                });
                            }

                            if ((Images.getString("img4")).length() <= 0) {

                                txt_img4.setVisibility(View.GONE);

                            } else {
                                txt_img4.setText(image4);
                                div29.setVisibility(View.VISIBLE);
                                txt_img4.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(), WebViewDisplay.class);
                                        i.putExtra("img4", image4);
                                        startActivity(i);
                                    }
                                });
                            }

                            if ((Images.getString("img5")).length() <= 0) {

                                txt_img5.setVisibility(View.GONE);

                            } else {
                                txt_img5.setText(image5);
                                div28.setVisibility(View.VISIBLE);
                                txt_img5.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(), WebViewDisplay.class);
                                        i.putExtra("img5", image5);
                                        startActivity(i);
                                    }
                                });
                            }
                        }
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
                            //  Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                                //    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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
                            //  Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            accessories.put("TeamService", teamservice);
            accessories.put("RedeliveryCharge", redelivery);

            object.put("Accessories", accessories);
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
                                //  Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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
