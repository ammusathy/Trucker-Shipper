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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.helper.PreferenceConnector;
import com.trukr.shipper.model.ResponseParams.ChangePasswordResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CreateNewPassword extends Activity {


    EditText oldPassword, newPassword, confirmPassword;
    Button submit, btnBack;
    TextView createnewpasswordlabel, changePasswordTitle;
    String newPasswordValue, confirmPasswordValue, message;
    int type, StatusCode;
    Dialog dialog;
    int Success = 1, Error = 0;
    private Context mContext;
    private Intent intent;
    RequestQueue queue;
    SharedPreferences preferences;
    private ProgressDialog pd;
    SharedPreferences.Editor editor;
    Typeface Gibson_Light, HnBold, HnThin, HnLight, Gibson_Regular, GillSansStd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_password);
        oldPassword = (EditText) findViewById(R.id.et_editoldpassword);
        newPassword = (EditText) findViewById(R.id.et_editnewpassword);
        confirmPassword = (EditText) findViewById(R.id.et_editconfirmpassword);
        submit = (Button) findViewById(R.id.btn_submit_createpassword);
        btnBack = (Button) findViewById(R.id.btn_back_to_signin);
        createnewpasswordlabel = (TextView) findViewById(R.id.tv_createnewpasswordlabel);
        changePasswordTitle = (TextView) findViewById(R.id.tv_changepassword);
        init();
        process();
    }

    public void init() {
        mContext = CreateNewPassword.this;

        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        submit.setTypeface(Gibson_Light);
        btnBack.setTypeface(Gibson_Light);
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        newPassword.setTypeface(HnThin);
        confirmPassword.setTypeface(HnThin);
        GillSansStd = Typeface.createFromAsset(getAssets(), "GillSansStd.ttf");
        createnewpasswordlabel.setTypeface(GillSansStd);
    }

    public void process() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMethod();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type <= 0)
                    newPasswordValue = newPassword.getText().toString().trim();
                confirmPasswordValue = confirmPassword.getText().toString().trim();
                if (newPasswordValue.length() <= 0)
                    TrukrApplication.alertDialog(mContext, getResources().getString(R.string.alert), "Please enter your new password", 0);
                else if (newPasswordValue.length() < 8)
                    TrukrApplication.alertDialog(mContext, getResources().getString(R.string.alert), "Password must be between 8-20 characters", 0);
                else if (confirmPasswordValue.length() <= 0)
                    TrukrApplication.alertDialog(mContext, getResources().getString(R.string.alert), "Please enter confirm password", 0);
                else if (!newPasswordValue.equals(confirmPasswordValue))
                    TrukrApplication.alertDialog(mContext, getResources().getString(R.string.alert), "Passwords does not match", 0);
                else if (Constant.isConnectingToInternet(mContext)) {
                    changepassword();
                } else {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                }
            }
        });
    }

    public void changepassword() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preferences = getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        String authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        String userId = preferences.getString("Userid", null);
        String pass = PreferenceConnector.readString(mContext, PreferenceConnector._PREF_TEMP_PASSWORD, "");
        Log.d("Test", userId + " : " + authToken + ":" + pass + ":");//print both the result
        queue = Volley.newRequestQueue(this);
        //   String url = "http://softwaredevelopersusa.com/ws-update-password";
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("AuthToken", authToken);
            object.put("UserId", userId);
            object.put("OldPassword", pass);
            object.put("NewPassword", newPasswordValue);

            Log.d("Test", authToken + " : " + userId + ":" + pass + newPasswordValue + ":");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ChangePassword, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Response:", response.toString());

                    ChangePasswordResponseParams responseData = (ChangePasswordResponseParams) TrukrApplication.getFromJSON(response.toString(), ChangePasswordResponseParams.class);
                    //      LoginResponseParams responseData = (LoginResponseParams) TrukrApplication.getFromJSON(response.toString(),LoginResponseParams.class);
                    int StatusCode = Integer.parseInt(String.valueOf(responseData.getStatusCode()));
                    String Message = responseData.getMessage();
                    closeprogress();
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, message);
                    } else {
                        createalertdialog(mContext, IConstant.alert, Message, StatusCode);
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
                        Log.e("Error password-->", json);
                        try {
//                             Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                           //     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext);
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

    private void backMethod() {
        intent = new Intent(mContext, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backMethod();
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }


    public void createalertdialog(final Context mContext, String Title, String Content, final int Status) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        if (Status == 100) {
            alertHead.setText(getResources().getString(R.string.success));
        } else {
            alertHead.setText(Title);
        }

        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(Content);

        // To hide cancel and line separator
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
                    intent.putExtra("OrderStatus", 0);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(mContext, Login.class);
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }
}