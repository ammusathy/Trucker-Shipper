package com.trukr.shipper.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.Login;
import com.trukr.shipper.activity.ViewDetails;
import com.trukr.shipper.activity.WebViewDisplay;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.LoginResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.android.volley.toolbox.Volley.newRequestQueue;

/**
 * Created by nijamudhin on 6/6/2016.
 */
public class DeliverDetailsFragment extends Fragment {

    ImageView iv, arrow;
    View rootView;
    RatingBar alertContent;
    SharedPreferences preference, preferences;
    TextView txt_border, txt_nostraps, txt_stopintransit, txt_hazardousmaterial, txt_teamService, txt_redelivery, txt_tollCharge, txt_weightfine, txt_yardStorage, txt_reweighing, txt_lumperCharge, txt_powerusageHours, txt_weekDays, txt_solo, txt_image, txt_img1, txt_img2, txt_img3, txt_img4, txt_img5, price, et_source, et_destination, notes;
    TextView divider7, divider8, divider9, divider10, divider12, divider13, divider14, divider15, divider16, divider17, divider18, divider19, divider20, divider21, divider26, divider27;
    RequestQueue queue;
    Context context;
    private ProgressDialog pd;
    String userId, authToken, orderId, trucktype,rating, toAdd, fromAdd, borderCrossStatus, pickdate, picktime, deliverydate, deliverytime, borderCrossingFee, hazmat, teamService, redelivery, transit, straps, orderStatus, paymentOption;
    TextView hazadarous, stopintransit, nostraps, borders, teamservice, reDelivery, tollCharge, weightFine, yardStorage, reweighting, lumperCharge, powerusageHours, layOver, weekDays, solo, Images, txt_trucktype,accessories;
    Button btn_pickdate, btn_picktime, btn_deliverydate, btn_deliverytime, viewdetails, btn_close;
    Typeface Gibson_Light, HnBold, HnThin, HnLight, Gibson_Regular, GillSansStd, Hn;
    TextView total,includes;
    TextView div22,div23,div24,div25,div29,div28;

    public DeliverDetailsFragment() {
        //need empty constructorN
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.deliverfragment_details, container, false);
        btn_close = (Button) rootView.findViewById(R.id.fragment_btn_close);
        context=getActivity();
        preference = getActivity().getSharedPreferences("NewJobDetails", Context.MODE_PRIVATE);
        orderStatus = preference.getString("OrderStatus", "");
        System.out.println("orderStatus--->" + orderStatus);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (Constant.isConnectingToInternet(context)) {
            ReadShipment();//Method declaration
            if (orderStatus.equals("Delivered")) {
                btn_close.setText("Rate us");
            }
        } else {
            TrukrApplication.alertDialog(context, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }
        iv = (ImageView) rootView.findViewById(R.id.fragment_btn_currentjob);
        arrow = (ImageView) rootView.findViewById(R.id.fragment_image_arrow);
        et_source = (TextView) rootView.findViewById(R.id.fragment_et_source);
        et_destination = (TextView) rootView.findViewById(R.id.fragment_et_destination);
        accessories = (TextView) rootView.findViewById(R.id.txt_delivershipper);
        txt_trucktype = (TextView) rootView.findViewById(R.id.fragment_btn_trucktype);
        btn_pickdate = (Button) rootView.findViewById(R.id.fragment_btn_pickupdate);
        btn_picktime = (Button) rootView.findViewById(R.id.fragment_btn_pickuptime);
        btn_deliverydate = (Button) rootView.findViewById(R.id.fragment_btn_deliverydate);
        btn_deliverytime = (Button) rootView.findViewById(R.id.fragment_btn_deliverytime);
        borders = (TextView) rootView.findViewById(R.id.deliver_btn_border);
        nostraps = (TextView) rootView.findViewById(R.id.deliver_btn_nostraps);
        stopintransit = (TextView) rootView.findViewById(R.id.deliver_btn_stopintransit);
        hazadarous = (TextView) rootView.findViewById(R.id.deliver_btn_hazardousmaterial);
        teamservice = (TextView) rootView.findViewById(R.id.deliver_btn_teamservice);
        reDelivery = (TextView) rootView.findViewById(R.id.deliver_btn_redelivery);
        viewdetails = (Button) rootView.findViewById(R.id.fragment_btn_viewdetails);
        tollCharge = (TextView) rootView.findViewById(R.id.deliver_btn_tollcharge);
        weightFine = (TextView) rootView.findViewById(R.id.deliver_btn_weightfine);
        yardStorage = (TextView) rootView.findViewById(R.id.deliver_btn_yardstorage);
        reweighting = (TextView) rootView.findViewById(R.id.deliver_btn_reweighting);
        lumperCharge = (TextView) rootView.findViewById(R.id.deliver_btn_lumpercharge);
        powerusageHours = (TextView) rootView.findViewById(R.id.deliver_btn_powerusagehours);
        layOver = (TextView) rootView.findViewById(R.id.deliver_btn_layover);
        weekDays = (TextView) rootView.findViewById(R.id.deliver_btn_weekdays);
        solo = (TextView) rootView.findViewById(R.id.deliver_btn_solo);
        total = (TextView) rootView.findViewById(R.id.fragment_txt_total);
        includes = (TextView) rootView.findViewById(R.id.fragment_txt_includes);

        div22 = (TextView) rootView.findViewById(R.id.deliver_txt_divider22);
        div23 = (TextView) rootView.findViewById(R.id.deliver_txt_divider23);
        div24 = (TextView) rootView.findViewById(R.id.deliver_txt_divider24);
        div25 = (TextView) rootView.findViewById(R.id.deliver_txt_divider25);
        div29 = (TextView) rootView.findViewById(R.id.deliver_txt_divider29);
        div28 = (TextView) rootView.findViewById(R.id.deliver_txt_divider28);



        txt_hazardousmaterial = (TextView) rootView.findViewById(R.id.txt_hazardousmaterial);
        txt_border = (TextView) rootView.findViewById(R.id.txt_border);
        txt_nostraps = (TextView) rootView.findViewById(R.id.txt_nostraps);
        txt_stopintransit = (TextView) rootView.findViewById(R.id.txt_stopintransit);
        txt_teamService = (TextView) rootView.findViewById(R.id.txt_teamservice);
        txt_redelivery = (TextView) rootView.findViewById(R.id.txt_redelivery);
        txt_tollCharge = (TextView) rootView.findViewById(R.id.deliver_tollcharge);
        txt_weightfine = (TextView) rootView.findViewById(R.id.deliver_weightfine);
        txt_reweighing = (TextView) rootView.findViewById(R.id.deliver_reweighting);
        txt_yardStorage = (TextView) rootView.findViewById(R.id.deliver_yardstorage);
        txt_lumperCharge = (TextView) rootView.findViewById(R.id.deliver_lumpercharge);
        txt_powerusageHours = (TextView) rootView.findViewById(R.id.deliver_powerusagehours);
        txt_weekDays = (TextView) rootView.findViewById(R.id.deliver_weekdays);
        txt_solo = (TextView) rootView.findViewById(R.id.deliver_solo);
        txt_image = (TextView) rootView.findViewById(R.id.deliver_txt_image);
        txt_img1 = (TextView) rootView.findViewById(R.id.deliver_img1);
        txt_img2 = (TextView) rootView.findViewById(R.id.deliver_img2);
        txt_img3 = (TextView) rootView.findViewById(R.id.deliver_img3);
        txt_img4 = (TextView) rootView.findViewById(R.id.deliver_img4);
        txt_img5 = (TextView) rootView.findViewById(R.id.deliver_img5);
        price = (TextView) rootView.findViewById(R.id.fragment_txt_price);
        notes = (TextView) rootView.findViewById(R.id.fragment_et_notes);
        divider7 = (TextView) rootView.findViewById(R.id.deliver_txt_divider7);
        divider8 = (TextView) rootView.findViewById(R.id.deliver_txt_divider8);
        divider9 = (TextView) rootView.findViewById(R.id.deliver_txt_divider9);
        divider10 = (TextView) rootView.findViewById(R.id.deliver_txt_divider10);
        divider12 = (TextView) rootView.findViewById(R.id.deliver_txt_divider12);
        divider13 = (TextView) rootView.findViewById(R.id.deliver_txt_divider13);
        divider14 = (TextView) rootView.findViewById(R.id.deliver_txt_divider14);
        divider15 = (TextView) rootView.findViewById(R.id.deliver_txt_divider15);
        divider16 = (TextView) rootView.findViewById(R.id.deliver_txt_divider16);
        divider17 = (TextView) rootView.findViewById(R.id.deliver_txt_divider17);
        divider18 = (TextView) rootView.findViewById(R.id.deliver_txt_divider18);
        divider19 = (TextView) rootView.findViewById(R.id.deliver_txt_divider19);
        divider20 = (TextView) rootView.findViewById(R.id.deliver_txt_divider20);
        divider21 = (TextView) rootView.findViewById(R.id.deliver_txt_divider21);
      //  divider26 = (TextView) rootView.findViewById(R.id.deliver_txt_divider26);
        divider27 = (TextView) rootView.findViewById(R.id.deliver_txt_divider27);
        Gibson_Light = Typeface.createFromAsset(getActivity().getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        HnLight = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Light.ttf");
        et_source.setTypeface(HnLight);
        et_destination.setTypeface(HnLight);
        btn_pickdate.setTypeface(HnLight);
        btn_picktime.setTypeface(HnLight);
        btn_deliverydate.setTypeface(HnLight);
        btn_deliverytime.setTypeface(HnLight);
        accessories.setTypeface(HnLight);
        price.setTypeface(Gibson_Light);
        total.setTypeface(Gibson_Light);
        includes.setTypeface(HnLight);
        viewdetails.setTypeface(HnLight);

        super.onViewCreated(view, savedInstanceState);



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
    }

    private void ReadShipment() {
        pd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preferences = getActivity().getSharedPreferences("Fragment", Context.MODE_PRIVATE);
        userId = preferences.getString("UserId", "");
        authToken = preferences.getString("AuthToken", "");
        orderId = preference.getString("OrderId", null);
        Log.d("Test", orderId + ":" + userId + " : " + authToken);
        queue = Volley.newRequestQueue(getActivity());
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

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ReadShipment, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test", response.toString());
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
                        Float aFloat = Float.parseFloat(total);
                        float k = (float) Math.round(aFloat * 100) / 100;
                        System.out.println("float value-->" + k + aFloat);
                        price.setText("$" + k);
                        rating=response.getString("Rating");
                        System.out.println("rating = " + rating);
                        if ((orderStatus.equals("Delivered")&& (rating.length()>0))) {
                            btn_close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    rateDialog();
                                }
                            });
                        } else {
                            btn_close.setVisibility(View.GONE);
                        }                        trucktype = response.getString("TruckType");
                        if ((response.getString("TruckType")).equals("1")) {
                            txt_trucktype.setText("Vented Van");
                        } else if ((response.getString("TruckType")).equals("2")) {
                            txt_trucktype.setText("Van_Air Ride");
                        } else if ((response.getString("TruckType")).equals("3")) {
                            txt_trucktype.setText("Van W/Liftgate");
                        }
                        borderCrossStatus = response.getString("BorderCrossStatus");
                        pickdate = response.getString("PickupDate");
                        btn_pickdate.setText(pickdate.toString());
                        deliverydate = response.getString("DeliveryDate");
                        btn_deliverydate.setText(deliverydate.toString());
                        picktime = response.getString("PickupTime");
                        btn_picktime.setText(picktime.toString());
                        deliverytime = response.getString("DeliveryTime");
                        btn_deliverytime.setText(deliverytime.toString());
                        String etnotes = response.getString("Notes");
                        notes.setText(etnotes.toString());
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
                                txt_hazardousmaterial.setText("NO");
                            } else {
                                txt_hazardousmaterial.setText("YES");
                            }
                        }

                        teamService = accessories.getString("Team Service");
                        if ((accessories.getString("Team Service")).length() <= 0) {
                            teamservice.setVisibility(View.GONE);
                            txt_teamService.setVisibility(View.GONE);
                            divider12.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Team Service").equals("0")) {
                                txt_teamService.setText("NO");
                            } else {
                                txt_teamService.setText("YES");
                            }
                        }

                        redelivery = accessories.getString("Redelivery Charge");
                        if ((accessories.getString("Redelivery Charge")).length() <= 0) {
                            reDelivery.setVisibility(View.GONE);
                            txt_redelivery.setVisibility(View.GONE);
                            divider13.setVisibility(View.GONE);
                        } else {
                            if (accessories.getString("Redelivery Charge").equals("0")) {
                                txt_redelivery.setText("NO");
                            } else {
                                txt_redelivery.setText("YES");
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
                                    txt_weekDays.setText("No");
                                }
                            }
                            if ((layOvers.getString("Solo")).length() <= 0) {
                                solo.setVisibility(View.GONE);
                                txt_solo.setVisibility(View.GONE);
                                divider21.setVisibility(View.GONE);
                            } else {

                                if (soloDay.equals("1")) {
                                    txt_solo.setText("Yes");
                                } else {
                                    txt_solo.setText("No");
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
                         //   divider26.setVisibility(View.GONE);
                            txt_img1.setVisibility(View.GONE);
                            txt_img2.setVisibility(View.GONE);
                            txt_img3.setVisibility(View.GONE);
                            txt_img4.setVisibility(View.GONE);
                            txt_img5.setVisibility(View.GONE);
                            div23.setVisibility(View.GONE);
                            div22.setVisibility(View.GONE);
                            div24.setVisibility(View.GONE);
                            div25.setVisibility(View.GONE);
                            div29.setVisibility(View.GONE);
                            div28.setVisibility(View.GONE);
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
                                txt_img3.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(getActivity(), WebViewDisplay.class);
                                        i.putExtra("img1", image2);
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
                                        i.putExtra("img1", image3);
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
                                        i.putExtra("img1", image4);
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
                                        i.putExtra("img1", image5);
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
                            //    Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                              //  Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
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
                           // Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        //      String url = "http://softwaredevelopersusa.com/ws-calculate-distance";
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


                            //    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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

    public void rateDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.rating_alertdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.rating_alertdialog_tv_alerthead);
        alertHead.setText("Rate Us");
        alertContent = (RatingBar) dialog.findViewById(R.id.ratingBar);

        // To hide cancel and line separator
        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        Button btnRate = (Button) dialog.findViewById(R.id.rating_alertdialog_btn_cancel);
        btnRate.setText("Rate");
        Button btnCancel = (Button) dialog.findViewById(R.id.rating_alertdialog_btn_ok);
        btnCancel.setText("No Thanks");

        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                rateUs();
            }
        });
        dialog.show();
    }

    private void rateUs() {
        pd = new ProgressDialog(getActivity(), android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(getActivity());
        //       String url = "http://softwaredevelopersusa.com/ws-order-rating";
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            object.put("OrderId", orderId);
            object.put("Rating", String.valueOf(alertContent.getRating()));
            System.out.println("usertype---->" + IConstant.UserType);
            System.out.println("UserId---->" + userId);
            System.out.println("AuthToken---->" + authToken);
            System.out.println("OrderId---->" + orderId);
            System.out.println("Rating---->" + String.valueOf(alertContent.getRating()));

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.OrderRating, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response of RatingBar", response.toString());
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
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                Log.d("Response of RatingBar: ", error.toString());
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
                             //   Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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


    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }
}