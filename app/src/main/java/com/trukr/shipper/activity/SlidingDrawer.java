package com.trukr.shipper.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.fragment.AboutUs;
import com.trukr.shipper.fragment.FragmentDrawer;
import com.trukr.shipper.fragment.Help;
import com.trukr.shipper.fragment.Home;
import com.trukr.shipper.fragment.Payment;
import com.trukr.shipper.fragment.Settings;
import com.trukr.shipper.fragment.Shipment;
import com.trukr.shipper.model.ResponseParams.LoginResponseParams;
import com.trukr.shipper.model.ResponseParams.ProfileDetailsResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SlidingDrawer extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {


    public static TextView mTitle;
    private static String TAG = SlidingDrawer.class.getSimpleName();
    Fragment fragment = null;
    private Context mContext;
    Dialog dialog;
    int success = 1, invisible = 99, notification = 100, settings = 101, actionBar_Item_Flag, nothing, StatusCode;
    SharedPreferences preferences;
    String authToken, userId;
    RequestQueue queue;
    private Toolbar mToolbar;
    private ProgressDialog pd;
    private FragmentDrawer drawerFragment;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.toolbar_title);
        TrukrApplication.setHelveticaNeue_Bold(mTitle);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        mContext = SlidingDrawer.this;

        Bundle b = getIntent().getExtras();
        try {
            if (b != null) {
                Boolean status = (Boolean) b.get("PaymentList");
                status = true;
                displayView(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle orderstatus = getIntent().getExtras();
        if (orderstatus != null) {
            int value;
            try {
                value = (Integer) orderstatus.get("OrderStatus");
                if (value == 1) {
                    displayView(2);
                } else {
                    displayView(0);
                }
                System.out.println("value---->" + value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        /*MenuItem item = menu.findItem(R.id.action_notification);
        if (notification_Flag == 0)
            item.setVisible(true);
        else
            item.setVisible(false);*/
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem item = menu.findItem(R.id.action_notification);
        if (actionBar_Item_Flag == notification) {
            item.setTitle(getString(R.string.notification));
            item.setVisible(true);
            item.setIcon(R.drawable.bell);
        } else if (actionBar_Item_Flag == settings && Constant.settingEditFlag == 0) {
            item.setTitle(getString(R.string.edit));
            item.setVisible(true);
            item.setIcon(R.drawable.edit_icon);
        } else if (actionBar_Item_Flag == nothing) {
            item.setVisible(false);
        } else if (actionBar_Item_Flag == settings && Constant.settingEditFlag == 1) {
            item.setTitle(getString(R.string.save));
            item.setVisible(true);
            item.setIcon(R.drawable.save);
        } else if (actionBar_Item_Flag == invisible)
            item.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    public void getProfileUpdate() {

        pd = new ProgressDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        Settings.getValuesFromUI();
        ProfileDetailsResponseParams mProfileResponse = new ProfileDetailsResponseParams();
        preferences = getSharedPreferences("Trucker", Context.MODE_PRIVATE);
        authToken = preferences.getString("authToken", null);
        userId = preferences.getString("Userid", null);
        Log.d("ProfileResponse", authToken + ":" + userId + IConstant.UserType);
        queue = Volley.newRequestQueue(this);
        //     String url = "http://softwaredevelopersusa.com/ws-update-profile";
        JSONObject object = null;
        JSONObject personal = new JSONObject();
        try {
            object = new JSONObject();

            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
            personal.put("FirstName", Constant.firstName);
            personal.put("LastName", Constant.lastName);
            personal.put("Email", Constant.email);
            personal.put("Mobile", Constant.mobileNumber);
            personal.put("Company", Constant.companyName);
            personal.put("Street", Constant.streetName);
            personal.put("City", Constant.cityName);
            personal.put("State", Constant.stateName);
            personal.put("Zipcode", Constant.zipCode);
            personal.put("TaxId", Constant.taxId);
            personal.put("OfficeNumber", Constant.officeNumber);
            personal.put("ProfilePicture", Constant.profilePic);
            object.put("Personal", personal);

            System.out.println("json input: object = " + object);
            Log.d("response123", authToken + ":" + userId + IConstant.UserType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.UpdateProfileInfo, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ResponseOfProfileUpdate", response.toString());
                LoginResponseParams responseData = (LoginResponseParams) TrukrApplication.getFromJSON(response.toString(), LoginResponseParams.class);
                StatusCode = Integer.parseInt(responseData.getStatusCode());
                String message = responseData.getMessage();
                boolean mobileVerification = false;
                try {
                    mobileVerification = response.getBoolean("IsMobileChanged");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                closeprogress();
                showAlertDialog(message, success, mobileVerification);

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                Log.d("ErrorResponse", error.toString());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error password-->", json);
                        try {
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                               // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
            }
        }
        );
        // volley time out error
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_notification && actionBar_Item_Flag == notification) {
            Intent intent = new Intent(SlidingDrawer.this, Notification.class);
            startActivity(intent);
            //    displayView(7);
            return true;
        } else if (id == R.id.action_notification && actionBar_Item_Flag == settings) {
            if (Constant.settingEditFlag == 0) {
                item.setIcon(R.drawable.edit_icon);
                Constant.settingEditFlag = 1;
            } else {
                item.setIcon(R.drawable.save);
                Settings.getValuesFromUI();
                if ((Constant.mobileNumber).length() < 17) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Mobile number should be 10 characters");
                } else if ((Constant.firstName).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your firstname");
                } else if ((Constant.lastName).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your lastname");
                } else if ((Constant.companyName).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your company name");
                } else if ((Constant.streetName).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your street");
                } else if ((Constant.cityName).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your city");
                } else if ((Constant.stateName).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your state");
                } else if ((Constant.zipCode).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your zipcode");
                } else if ((Constant.officeNumber).length() < 17) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Office number should be 10 characters");
                } else if ((Constant.taxId).length() <= 0) {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, "Please enter your tax id");
                } else {
                    getProfileUpdate();
                    Constant.settingEditFlag = 0;
                }

            }

            displayView(3);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        Constant.settingEditFlag = 0;
        displayView(position);
    }

    private void displayView(int position) {
        String title = getString(R.string.app_name);

        switch (position) {

            case 0:
                actionBar_Item_Flag = notification;
                fragment = new Home();
                title = getString(R.string.title_trucker);
                break;
            case 1:
                actionBar_Item_Flag = notification;
                fragment = new Payment();
                title = getString(R.string.title_activity_payment);
                break;
            case 2:
                actionBar_Item_Flag = notification;
                fragment = new Shipment();
                title = getString(R.string.title_shipment);
                break;
            case 3:
                actionBar_Item_Flag = settings;
                fragment = new Settings();
                title = getString(R.string.title_settings);
                break;

            case 4:
                actionBar_Item_Flag = nothing;
                fragment = new AboutUs();
                title = getString(R.string.title_aboutus);
                break;
            case 5:
                actionBar_Item_Flag = nothing;
                fragment = new Help();
                title = getString(R.string.title_help);
                break;

            case 6:
                alertdialog(mContext, IConstant.logout, "Are you sure to logout?", StatusCode);
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle("");
            mTitle.setText(title);
        }
        supportInvalidateOptionsMenu();
    }

    public void showAlertDialog(final String content, final int statusCode, final boolean mobileVerification) {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(mContext.getResources().getString(R.string.alert));
        final TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
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
                if (mobileVerification == true) {
                    dialog.dismiss();
                    Intent i = new Intent(mContext, VerifyMobileNumber.class);
                    i.putExtra("MobileNumber", Constant.mobileNumber);
                    i.putExtra("UserId", userId);
                    System.out.println("userid and mobileno --------->" + Constant.mobileNumber + userId);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    finish();
                } else if (content.equals("Profile has been updated successfully")) {
                    dialog.dismiss();
                    displayView(3);
                } else if (content.equals("Unauthorized user token.")) {
                    dialog.dismiss();
                    alertDialog(mContext, IConstant.alert, content);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void alertdialog(final Context mContext, String Title, String Content, final int Status) {
        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
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
        // customize settext cancel as Ok
        btnDialogCancel.setText("OK");
        //  line.setVisibility(View.GONE);
        //  btnDialogCancel.setVisibility(View.GONE);
        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        // customize settext ok as cancel
        btnDialogOk.setText("Cancel");

        TrukrApplication.setHelveticaNeue_Bold(alertHead);
        TrukrApplication.setHelveticaNeue_Light(alertContent);
        TrukrApplication.setHelveticaNeue_Bold(btnDialogCancel);
        TrukrApplication.setHelveticaNeue_Bold(btnDialogOk);

        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                logOut();
            }
        });
        dialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void logOut() {
        pd = new ProgressDialog(mContext, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preferences = getSharedPreferences("Trucker", Context.MODE_PRIVATE);
        userId = preferences.getString("Userid", null);
        authToken = preferences.getString("authToken", null);
        Log.d("Test", userId + " : " + authToken);
        queue = Volley.newRequestQueue(this);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("AuthToken", authToken);
            object.put("UserId", userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.UserLogout, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response:", response.toString());
                LoginResponseParams responseData = (LoginResponseParams) TrukrApplication.getFromJSON(response.toString(), LoginResponseParams.class);
                StatusCode = Integer.parseInt(responseData.getStatusCode());
                String message = responseData.getMessage();
                closeprogress();
                if (StatusCode == 100) {
                    SharedPreferences preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor remEditor = preferences.edit();
                    remEditor.remove("userid");
                    remEditor.apply();
                    Intent intent = new Intent(mContext, Login.class);
                    startActivity(intent);
                } else if (StatusCode == 97) {
                    alertDialog(mContext, IConstant.alert, message);
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
                            //    Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(SlidingDrawer.this);
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
                            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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

    public void alertDialog(final Context ctx, String Title, String Content) {
        final Dialog dialog = new Dialog(ctx, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);
        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(Title);
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(Content);

        // To hide cancel and line separator
        View line = (View) dialog.findViewById(R.id.centerLineDialog);
        Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
        Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        line.setVisibility(View.GONE);
        btnDialogCancel.setVisibility(View.GONE);
        btnDialogOk.setBackgroundResource(R.drawable.dialogbtnbackground);

        // if button is clicked, close the custom dialog
        btnDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, Login.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}