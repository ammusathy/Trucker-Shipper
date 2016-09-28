package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.trukr.shipper.constants.AppController;
import com.trukr.shipper.constants.Common;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.helper.PreferenceConnector;
import com.trukr.shipper.model.ResponseParams.GeneralResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by kalaivani on 3/23/2016.
 */
public class ChangeMobileNumber extends Activity {
    TextView changeBack;
    EditText changeNumber;
    Button submit;
    SharedPreferences preferences;
    private Context mContext;
    String message, countryCode, changenumbervalue, userId, mLastBeforeText;
    int StatusCode;
    int Success = 1, Alert = 0;
    RequestQueue queue;
    private ProgressDialog pd;
    public static String Tag = ChangeMobileNumber.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_mobile_number);
        init();
        process();
    }

    private void init() {
        mContext = ChangeMobileNumber.this;
        countryCode = Common.GetTeleCountryCode(mContext);
        changeBack = (TextView) findViewById(R.id.changenumberback);
        changeNumber = (EditText) findViewById(R.id.change_Mobile_number);
        submit = (Button) findViewById(R.id.btnsubmit);
    }

    public void process() {
        int maxLength;
        if (countryCode.length() == 2) {
            maxLength = 17;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            changeNumber.setFilters(fArray);
        } else if (countryCode.length() == 3) {
            maxLength = 19;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            changeNumber.setFilters(fArray);
        } else {
            maxLength = 20;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            changeNumber.setFilters(fArray);
        }
        changeNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mLastBeforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Common.formetText(mLastBeforeText, s, countryCode, changeNumber);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    changenumbervalue = changeNumber.getText().toString().trim();
                    if (changenumbervalue.length() <= 0) {
                        showAlertDialog("Please enter your mobile number", Alert);
                    } else if (changenumbervalue.length() <= 16) {
                        showAlertDialog("Please enter valid mobile number", Alert);
                    } else if (Constant.isConnectingToInternet(ChangeMobileNumber.this)) {
                        changemobilenumber();
                    } else {
                        TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        changeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backMethod();
            }
        });
    }

    public void changemobilenumber() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        try {
            userId = getIntent().getExtras().getString("UserId");
        } catch (Exception e) {
            e.printStackTrace();
        }
        queue = Volley.newRequestQueue(this);
        //   String url = "http://softwaredevelopersusa.com/ws-change-mobile";
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("AuthToken", "");
            object.put("UserId", userId);
            object.put("Mobile", changenumbervalue);
            System.out.println("AuthToken--->" + "");
            System.out.println("UserId--->" + userId);
            System.out.println("Mobile--->" + changenumbervalue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                IConstant.ChangeMobileNumber, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(Tag, response.toString());

                        Log.d("Response:", response.toString());
                        GeneralResponseParams responseData = (GeneralResponseParams) TrukrApplication.getFromJSON(response.toString(), GeneralResponseParams.class);
                        StatusCode = Integer.parseInt(String.valueOf(responseData.getStatusCode()));
                        message = responseData.getMessage();
                        closeprogress();
                        if (StatusCode == 97) {
                            TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, message);
                        } else {
                            forgotalertdialog(ChangeMobileNumber.this, IConstant.alert, message, StatusCode);
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
                        Log.e("Error password-->", json);
                        try {
                            // Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String messages = jsonObject.getString("message");
                            //    Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ChangeMobileNumber.this);
                                dlgAlert.setMessage(messages);
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
                          //  Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backMethod();
    }

    private void backMethod() {
        Intent intent = new Intent(getApplicationContext(), VerifyMobileNumber.class);
        SharedPreferences preferences = getSharedPreferences("Mobile", MODE_PRIVATE);
        String mobileNoValue = preferences.getString("MobileNo", "");
        String userid = preferences.getString("UserId", "");
        intent.putExtra("MobileNumber", mobileNoValue);
        intent.putExtra("UserId", userid);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    public void showAlertDialog(String content, final int statusCode) {
        final Dialog dialog = new Dialog(ChangeMobileNumber.this, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        if (statusCode == Success)
            alertHead.setText(getResources().getString(R.string.app_name));
        else
            alertHead.setText(getResources().getString(R.string.alert));
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(content);
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

    public void forgotalertdialog(final Context mContext, String Title, String Content, final int StatusCode) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);
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

        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StatusCode == 100) {
                    dialog.dismiss();
                    Intent i = new Intent(ChangeMobileNumber.this, VerifyMobileNumber.class);
                    i.putExtra("MobileNumber", changenumbervalue);
                    i.putExtra("UserId", userId);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    finish();
                } else
                    dialog.dismiss();
            }
        });
        dialog.show();
    }
}




