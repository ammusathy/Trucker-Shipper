package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.BuildConfig;
import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class VerifyMobileNumber extends Activity implements View.OnClickListener {

    TextView mobileNumberTitle, numberTitle, verifyOne, verifyTwo, verifyThree, verifyFour;
    TextView back, header, number, submit;
    Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnZero, btnResend, btnChangeNumber, btnDel;
    String verificationNumber = "";
    private Context mContext;
    private ProgressDialog pd;
    Dialog dialog;
    String mobile, userId, message;
    public Typeface HelveticaNeuebold, HnThin, HnLight;
    RequestQueue queue;
    int StatusCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_mobile_number);
        init();
        process();
    }

    public void init() {
        HelveticaNeuebold = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Bold.ttf");
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        HnLight = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Light.ttf");
        mContext = VerifyMobileNumber.this;
        mobileNumberTitle = (TextView) findViewById(R.id.verify_mobile_number_tv_verifytitle);
        back = (TextView) findViewById(R.id.verify_mobile_number_tv_back);
        header = (TextView) findViewById(R.id.verify_mobile_number_tv_header);
        numberTitle = (TextView) findViewById(R.id.tv_verifytitle);
        number = (TextView) findViewById(R.id.verify_mobile_number);
        submit = (TextView) findViewById(R.id.verify_mobile_number_tv_verifysubmit);
        verifyOne = (TextView) findViewById(R.id.tv_verifyone);
        verifyTwo = (TextView) findViewById(R.id.tv_verifytwo);
        verifyThree = (TextView) findViewById(R.id.tv_verifythree);
        verifyFour = (TextView) findViewById(R.id.tv_verifyfour);
        btnOne = (Button) findViewById(R.id.btn_one);
        btnTwo = (Button) findViewById(R.id.btn_two);
        btnThree = (Button) findViewById(R.id.btn_three);
        btnFour = (Button) findViewById(R.id.btn_four);
        btnFive = (Button) findViewById(R.id.btn_five);
        btnSix = (Button) findViewById(R.id.btn_six);
        btnSeven = (Button) findViewById(R.id.btn_seven);
        btnEight = (Button) findViewById(R.id.btn_eight);
        btnNine = (Button) findViewById(R.id.btn_nine);
        btnZero = (Button) findViewById(R.id.btn_zero);
        btnDel = (Button) findViewById(R.id.btn_del);
        btnResend = (Button) findViewById(R.id.btn_resend);
        btnChangeNumber = (Button) findViewById(R.id.btn_changenumber);
        btnOne.setOnClickListener(this);
        btnTwo.setOnClickListener(this);
        btnThree.setOnClickListener(this);
        btnFour.setOnClickListener(this);
        btnFive.setOnClickListener(this);
        btnSix.setOnClickListener(this);
        btnSeven.setOnClickListener(this);
        btnEight.setOnClickListener(this);
        btnNine.setOnClickListener(this);
        btnZero.setOnClickListener(this);
        btnDel.setOnClickListener(this);

        header.setTypeface(HnLight);
        submit.setTypeface(HnLight);
        btnResend.setTypeface(HnLight);
        btnChangeNumber.setTypeface(HnLight);
        try {
            mobile = getIntent().getExtras().getString("MobileNumber");
            userId = getIntent().getExtras().getString("UserId");
            //  number.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            number.setText(Html.fromHtml("<u>" + mobile + "</u>"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMethod();
            }
        });

        btnChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VerifyMobileNumber.this, ChangeMobileNumber.class);
                i.putExtra("UserId", userId);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (verificationNumber.length() <= 0) {
                        showAlertDialog("Please enter your verification code", 0);
                    } else if (Constant.isConnectingToInternet(mContext)) {

                        makeJsonObjectRequest();//Method declaration

                    } else {
                        TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ResendObjRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ResendObjRequest() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(this);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserId", userId);
            object.put("Mobile", mobile);
            System.out.println("ResendObjRequest >" + userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ResendText, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("resend code", response.toString());
                try {
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
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        response.getString("Message");
                        System.out.println("parant josn object id -->" + response.getString("Message"));
                        resendalertDialog(mContext, IConstant.alert, message, StatusCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                VolleyLog.d("verify page", "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error password-->", json);
                        try {
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                              //  String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                           // Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void resendalertDialog(final Context mContext, String title, String content, final int statusCode) {

        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(this.mContext.getResources().getString(R.string.alert));
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(this.mContext.getResources().getString(R.string.resendalert));

        // To hide cancel and line separator
        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        line.setVisibility(View.GONE);
        btnDialogCancel.setVisibility(View.GONE);

        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        btnDialogOk.setBackgroundResource(R.drawable.dialogbtnbackground);
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    private void makeJsonObjectRequest() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(this);
        //   String url = "http://softwaredevelopersusa.com/ws-mobile-verify";
        JSONObject object = null;

        try {
            object = new JSONObject();
            object.put("UserId", userId);
            object.put("Mobile", mobile);
            object.put("ActivationCode", verificationNumber);
            System.out.println("makeJsonObjectRequest >" + userId + mobile);
            System.out.println("activation code-->" + verificationNumber);
            System.out.println("request-->" + object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.Mobile, object, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("verify page", response.toString());
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
                    // Parsing json object response response will be a json object
                    if (response != null) {
                        response.getString("Message");
                        StatusCode = Integer.parseInt(response.getString("StatusCode"));
                        System.out.println("parant josn object id -->" + response.getString("Message"));
                        verifyalertDialog(mContext, IConstant.alert, response.getString("Message"), StatusCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                  //  Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                VolleyLog.d("verify page", "Error: " + error.getMessage());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error password-->", json);
                        try {
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                             //   Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
                           // Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
            }
        });
        // Adding request to request queue
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void verifyalertDialog(final Context mContext, String title, String content, final int statusCode) {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(content);

        // To hide cancel and line separator
        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        line.setVisibility(View.GONE);
        btnDialogCancel.setVisibility(View.GONE);

        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        btnDialogOk.setBackgroundResource(R.drawable.dialogbtnbackground);
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StatusCode == 100) {
                    Intent intent = new Intent(VerifyMobileNumber.this, SlidingDrawer.class);
                    intent.putExtra("OrderStatus", 0);
                    startActivity(intent);
                } else
                    dialog.dismiss();
            }

        });
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backMethod();
    }

    public void backMethod() {
        Intent intent = new Intent(VerifyMobileNumber.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_one:
                setNumber("1");
                break;
            case R.id.btn_two:
                setNumber("2");
                break;
            case R.id.btn_three:
                setNumber("3");
                break;
            case R.id.btn_four:
                setNumber("4");
                break;
            case R.id.btn_five:
                setNumber("5");
                break;
            case R.id.btn_six:
                setNumber("6");
                break;
            case R.id.btn_seven:
                setNumber("7");
                break;
            case R.id.btn_eight:
                setNumber("8");
                break;
            case R.id.btn_nine:
                setNumber("9");
                break;
            case R.id.btn_zero:
                setNumber("0");
                break;
            case R.id.btn_del:
                int size = verificationNumber.length();
                if (size > 0) {
                    String temp = verificationNumber;
                    verificationNumber = "";
                    for (int i = 0; i < size - 1; i++) {
                        verificationNumber = verificationNumber + temp.charAt(i);
                    }
                }
                setVerifyBackground();
                break;
        }
        setVerifyBackground();
    }

    public void setNumber(String value) {
        if (verificationNumber.length() < 4) {
            verificationNumber = verificationNumber + value;
        }
    }

    private void setVerifyBackground() {
        verifyOne.setBackgroundResource(R.drawable.whitecirclebackground);
        verifyTwo.setBackgroundResource(R.drawable.whitecirclebackground);
        verifyThree.setBackgroundResource(R.drawable.whitecirclebackground);
        verifyFour.setBackgroundResource(R.drawable.whitecirclebackground);
        int size = verificationNumber.length();
        switch (size) {
            case 1:
                verifyOne.setBackgroundResource(R.drawable.redcirclebackground);
                break;
            case 2:
                verifyOne.setBackgroundResource(R.drawable.redcirclebackground);
                verifyTwo.setBackgroundResource(R.drawable.redcirclebackground);
                break;
            case 3:
                verifyOne.setBackgroundResource(R.drawable.redcirclebackground);
                verifyTwo.setBackgroundResource(R.drawable.redcirclebackground);
                verifyThree.setBackgroundResource(R.drawable.redcirclebackground);
                break;
            case 4:
                verifyOne.setBackgroundResource(R.drawable.redcirclebackground);
                verifyTwo.setBackgroundResource(R.drawable.redcirclebackground);
                verifyThree.setBackgroundResource(R.drawable.redcirclebackground);
                verifyFour.setBackgroundResource(R.drawable.redcirclebackground);
                break;
        }
    }

    public void showAlertDialog(String content, final int statusCode) {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(mContext.getResources().getString(R.string.alert));
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(content);
        // To hide cancel and line separator
        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        line.setVisibility(View.GONE);
        btnDialogCancel.setVisibility(View.GONE);
        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        btnDialogOk.setBackgroundResource(R.drawable.dialogbtnbackground);

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }


}
