package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.LoginResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.android.volley.toolbox.Volley.newRequestQueue;

public class ForgotPassword extends Activity {


    EditText emailAddress;
    Button submit, backToSignIn;
    TextView forgot;
    String emailAddressValue;
    private Context mContext;
    public Typeface Gibson_Light, HnThin;
    private Intent intent;
    private ProgressDialog pd;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        emailAddress = (EditText) findViewById(R.id.forgot_password_et_emailaddress);
        submit = (Button) findViewById(R.id.forgot_password_btn_submit);
        backToSignIn = (Button) findViewById(R.id.forgot_password_btn_backto_signin);
        forgot = (TextView) findViewById(R.id.forgot_password_tv_label);
        init();
        process();
    }

    public void init() {
        mContext = ForgotPassword.this;
        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        submit.setTypeface(Gibson_Light);
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        emailAddress.setTypeface(HnThin);
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        forgot.setTypeface(HnThin);
        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        backToSignIn.setTypeface(Gibson_Light);
    }

    public void process() {
        backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  backMethod();*/
                intent = new Intent(mContext, Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailAddressValue = emailAddress.getText().toString().trim();
                try {
                    if (emailAddressValue.length() <= 0)
                        TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your email id.");
                    else if (Constant.isConnectingToInternet(mContext)) {
                        ForgotPassword();
                    } else {
                        TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                    }

                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void ForgotPassword() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = newRequestQueue(this);
        //    String url = "http://softwaredevelopersusa.com/ws-reset-password";
        JSONObject object = null;

        try {
            object = new JSONObject();
            object.put("Email", emailAddressValue);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ForgotPassword, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response:", response.toString());
                LoginResponseParams responseData = (LoginResponseParams) TrukrApplication.getFromJSON(response.toString(), LoginResponseParams.class);
                int StatusCode = Integer.parseInt(String.valueOf(responseData.getStatusCode()));
                String Message = responseData.getMessage();
                closeprogress();
                if (StatusCode == 97) {
                    TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                } else {
                    forgotalertdialog(mContext, IConstant.alert, Message, StatusCode);
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
                        Log.e("Error login-->", json);
                        try {
                            //   Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                            //    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ForgotPassword.this);
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

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    public void forgotalertdialog(final Context ctx, String Title, String Content, final int Status) {
        final Dialog dialog = new Dialog(ctx, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        // set the custom dialog components - title and content
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(Title);
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
                    Intent intent = new Intent(ForgotPassword.this, Login.class);
                    startActivity(intent);
                } else if (Status == 101)
                    dialog.dismiss();

            }
        });
        dialog.show();
    }
}

