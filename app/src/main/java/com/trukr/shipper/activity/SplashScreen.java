package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;

public class SplashScreen extends Activity {

    Context ctx;
    Handler handlerSplash;
    Dialog dialog;
    Intent intent;
    SharedPreferences preferences;
    Boolean rememberme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        init();
    }

    public void init() {
        ctx = SplashScreen.this;
        handlerSplash = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("isConnected***", Constant.isConnectingToInternet(ctx) + "");
        if (Constant.isConnectingToInternet(ctx)) {
            process();
        } else {
            dialog = new Dialog(ctx, R.style.Dialog);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_alertdialog);

            TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
            alertHead.setText(ctx.getResources().getString(R.string.alert));
            TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
            alertContent.setText(IConstant._ERR_NO_INTERNET_CONNECTION);

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
                    finish();
                }
            });
            dialog.show();
        }
    }

    public void process() {
        GCMRegistrar.checkDevice(ctx);
        GCMRegistrar.checkManifest(ctx);
        String regId = TrukrApplication.getGCMRegistrationId(ctx);
        Log.d("GCM_REG_ID_S ", regId);

        handlerSplash.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    preferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
                    int userId = preferences.getInt("userid", 0);
                    rememberme = preferences.getBoolean("saveLogin", false);
                    System.out.println("userid-->" + userId);
                    int orderStatus = preferences.getInt("orderStatus", 0);
                    System.out.println("orderstatus--->" + orderStatus);

                    System.out.println("useridvalue--->" + rememberme + userId);
                    if (userId == 0) {
                        intent = new Intent(ctx, Login.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        finish();
                    } else {
                        intent = new Intent(ctx, SlidingDrawer.class);
                        intent.putExtra("OrderStatus", orderStatus);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                        finish();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
    }
}