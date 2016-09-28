package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.helper.PreferenceConnector;
import com.trukr.shipper.model.ResponseParams.LoginResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Login extends Activity {

    EditText emailAddress, password;
    Button submit, signUp, forgotPassword;
    CheckBox rememberMe;
    String emailAddressValue, passwordValue, deviceUDIDValue, userIdAct, mobileAct, authTokenAct;
    String regIDValue = "";
    Dialog dialog;
    int Success = 100, Error = 99, Verifymobilenumber = 103, ChangePassword = 104, LoginFailed = 105, orderStatus;
    private Context mContext;
    SharedPreferences preferencese, sharedPreferences;
    SharedPreferences.Editor editor, remEditor;
    RequestQueue queue;
    private Intent intent;
    Boolean saveLogin;
    private ProgressDialog pd;
    String authToken = null, userId = null, mobile = null;
    Typeface Gibson_Light, HnThin, HnLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        emailAddress = (EditText) findViewById(R.id.login_et_emailaddress);
        password = (EditText) findViewById(R.id.login_et_password);
        submit = (Button) findViewById(R.id.login_btn_submit);
        rememberMe = (CheckBox) findViewById(R.id.login_chk_rememberme);
        forgotPassword = (Button) findViewById(R.id.login_btn_forgotpassword);
        signUp = (Button) findViewById(R.id.login_btn_signup);
        init();
        process();
    }

    public void init() {
        mContext = Login.this;
        deviceUDIDValue = Constant.getDeviceID(mContext);
        System.out.println("deviceUDIDValue = " + deviceUDIDValue);
        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        submit.setTypeface(Gibson_Light);
        emailAddress.setTypeface(HnThin);
        password.setTypeface(HnThin);
        HnLight = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Light.ttf");
        rememberMe.setTypeface(HnLight);
        forgotPassword.setTypeface(HnLight);
        signUp.setTypeface(Gibson_Light);
        deviceUDIDValue = Constant.getDeviceID(mContext);
        getGCM_ID();
        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        remEditor = sharedPreferences.edit();
        saveLogin = sharedPreferences.getBoolean("saveLogin", false);
        if (saveLogin) {
            emailAddress.setText(sharedPreferences.getString("username", ""));
            password.setText(sharedPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }
    }

    public void process() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
            }
        });

        try {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    emailAddressValue = emailAddress.getText().toString().trim();
                    passwordValue = password.getText().toString().trim();
                    if (emailAddressValue.length() <= 0)
                        TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your email id.", 0);
                    else if (!emailAddressValue.matches(IConstant._EMAIL_PATTERN))
                        TrukrApplication.alertDialog(mContext, IConstant.alert, mContext.getResources().getString(R.string.invalidemailid), 0);
                    else if (passwordValue.length() <= 0)
                        TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your password.", 0);
                    else if (passwordValue.length() < 8)
                        TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter valid Password", Error);
                    else if (Constant.isConnectingToInternet(mContext)) {
                        Loginrequest();
                    } else {
                        TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* emailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    emailAddress.setHintTextColor(mContext.getResources().getColor(R.color.darkgray));
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    password.setHintTextColor(mContext.getResources().getColor(R.color.darkgray));
            }
        });*/
    }

    /*REGISTER AND GET THE GCM ID*/
    public void getGCM_ID() {
        if (regIDValue.length() <= 0)
            regIDValue = TrukrApplication.getGCMRegistrationId(mContext);
        Log.d("GCM_REG_ID_L ", regIDValue);
    }

    public void Loginrequest() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(this);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            JSONObject loginDetails = new JSONObject();
            loginDetails.put("Username", emailAddressValue);
            loginDetails.put("Password", passwordValue);
            loginDetails.put("DeviceUDID", "");
            loginDetails.put("DeviceType", IConstant.DeviceType);
            loginDetails.put("RegId", regIDValue);
            object.put("LoginDetails", loginDetails);
            PreferenceConnector.writeString(getApplicationContext(), PreferenceConnector._PREF_TEMP_PASSWORD, passwordValue);
            Log.d("Test", passwordValue + " : ");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.UserLogin, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response:", response.toString());
                preferencese = getSharedPreferences("Trucker", MODE_PRIVATE);
                editor = preferencese.edit();
                try {
                    userId = response.getString("UserId");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    authToken = response.getString("AuthToken");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Test1", userId + " : " + authToken);
                editor.putString("Userid", userId);
                editor.putString("authToken", authToken);
                editor.commit();
                int StatusCode = 0;
                String message = null;
                try {
                    LoginResponseParams responseData = (LoginResponseParams) TrukrApplication.getFromJSON(response.toString(), LoginResponseParams.class);//Loginresponse model class
                    StatusCode = Integer.parseInt(responseData.getStatusCode());
                    message = responseData.getMessage();
                    orderStatus = Integer.parseInt(responseData.getOrderStatus());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (StatusCode == Verifymobilenumber) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject user = jsonObject.getJSONObject("User");
                        userIdAct = user.getString("UserId");
                        mobileAct = user.getString("Mobile");
                        authTokenAct = user.getString("AuthToken");
                        preferencese = getSharedPreferences("Trucker", MODE_PRIVATE);
                        editor = preferencese.edit();
                        editor.putString("Userid", userIdAct);
                        editor.putString("authToken", authTokenAct);
                        editor.commit();
                        Log.d("Testing", userIdAct + ":" + mobileAct + ":" + authTokenAct);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (rememberMe.isChecked()) {
                        remEditor.putBoolean("saveLogin", true);
                        remEditor.putString("username", emailAddressValue);
                        remEditor.putString("password", passwordValue);
                        int userValue = Integer.valueOf(String.valueOf(userId));
                        remEditor.putInt("userid", userValue);
                        System.out.println("useridlogin--->" + userId);
                        remEditor.putInt("orderStatus", orderStatus);
                        remEditor.commit();
                    } else {
                        remEditor.clear();
                        remEditor.commit();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                closeprogress();
                alertdialog(mContext, IConstant.alert, message, StatusCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response: ", error.toString());//print if it is error response
                closeprogress();
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);
                        try {
                            //   Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);//creating a new json object
                                String message = jsonObject.getString("message");//print message in json
                              //  Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();//toast message
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(Login.this);
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
                    } catch (UnsupportedEncodingException e) {//when a stack trace and detail message filled in this msg will occur
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

    public void alertdialog(final Context ctx, String Title, final String Content, final int Status) {
        final Dialog dialog = new Dialog(ctx, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);
        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        if (Status == 105) {
            alertHead.setText(R.string.activationrequired);
        } else
            alertHead.setText(Title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(Content);
        // To hide cancel and line separator
        View line = (View) dialog.findViewById(R.id.centerLineDialog);

        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        if (Status == Verifymobilenumber) {
            btnDialogOk.setText(R.string.verifyalert);
        }
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        if (Status == 105 || Status == 102 || Status == 101) {
            line.setVisibility(View.GONE);
            btnDialogCancel.setVisibility(View.GONE);
            btnDialogOk.setBackgroundResource(R.drawable.dialogbtnbackground);
        }

        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status == Verifymobilenumber) {
                    dialog.dismiss();
                    Intent intent = new Intent(Login.this, VerifyMobileNumber.class);
                    intent.putExtra("UserId", userIdAct);
                    intent.putExtra("MobileNumber", mobileAct);
                    System.out.println("userid and mobile--->" + mobile + userId);
                    startActivity(intent);
                } else if (Status == ChangePassword) {
                    dialog.dismiss();
                    Intent intent = new Intent(Login.this, CreateNewPassword.class);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
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
        if (Status == Success) {
            dialog.dismiss();
            Intent intent = new Intent(Login.this, SlidingDrawer.class);
            intent.putExtra("OrderStatus", orderStatus);
            System.out.println("orderstatus----->" + orderStatus);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishAffinity();
        }
        return super.onKeyDown(keyCode, event);
    }
}