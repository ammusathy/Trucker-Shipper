package com.trukr.shipper.application;

import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.support.multidex.MultiDex;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.crittercism.app.Crittercism;
import com.google.android.gcm.GCMRegistrar;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.trukr.shipper.BuildConfig;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.Login;
import com.trukr.shipper.model.GeneralParams.RequestKeyParams;

import java.util.ArrayList;


public class TrukrApplication extends Application {

    public static Context mContext;
    public Context context;
    public static ProgressDialog progressDialog;
    public static TrukrApplication instance;
    public static ArrayList<String> array_additional_services = new ArrayList<String>();
    public static Typeface Gibson_Light;
    public static Typeface HelveticaNeue_Light;
    public static Typeface HelveticaNeue_Thin;
    public static Typeface HelveticaNeue_Bold;
    public static Typeface Monteserrat_Bold;
    public static String AuthToken = null;
    public static String userId = null;
    public static int mSelectedSource = -1;
    public static int mSelectedDestination = -1;
    public static final String TAG = TrukrApplication.class.getSimpleName();
    public static String mSelAdditionallabel = null;
    public static String mSelAdditionalValue = null;
    //  public static Dialog customProgressDialog = null;
    public static AnimationDrawable frameAnimation;
    public static RequestQueue mRequestQueue;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);


    }

    public static synchronized TrukrApplication getInstance() {
        return instance;
    }

    public static void addAdditionalServices() {
        array_additional_services.add("Border Crossing fee");
        array_additional_services.add("Hazmat charge");
        array_additional_services.add("No Of Straps");
        array_additional_services.add("Redelivery Charge");
        array_additional_services.add("Stops-in-transit");
        array_additional_services.add("Team Service");
    }

    public static void removeAdditionalService(String removeString) {
        array_additional_services.remove(removeString);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public static ArrayList<String> getArrayList() {
        return array_additional_services;
    }

    public static Typeface getMonteserrat_Bold() {
        return Monteserrat_Bold;
    }

    public static void setMonteserrat_Bold(TextView tx) {
        tx.setTypeface(Monteserrat_Bold);
    }

    public static Typeface getHelveticaNeue_Bold() {
        return HelveticaNeue_Bold;
    }

    public static void setHelveticaNeue_Bold(TextView tx) {
        tx.setTypeface(HelveticaNeue_Bold);
    }

    public static Typeface getHelveticaNeue_Light() {
        return HelveticaNeue_Light;
    }

    public static void setHelveticaNeue_Light(TextView tx) {
        tx.setTypeface(HelveticaNeue_Light);
    }

    public static Typeface getHelveticaNeue_Thin() {
        return HelveticaNeue_Thin;
    }

    public static void setHelveticaNeue_Thin(TextView tx) {
        tx.setTypeface(HelveticaNeue_Thin);
    }

    public static Typeface getGibson_Light() {
        return Gibson_Light;
    }

    public static void setGibson_Light(TextView tx) {
        tx.setTypeface(Gibson_Light);
    }

    public static void alertDialog(final Context ctx, String Title, String Content, final int Status) {
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
        Button btnDialog = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
        TrukrApplication.setHelveticaNeue_Bold(alertHead);
        TrukrApplication.setHelveticaNeue_Light(alertContent);
        TrukrApplication.setHelveticaNeue_Bold(btnDialogCancel);
        TrukrApplication.setHelveticaNeue_Bold(btnDialog);

        line.setVisibility(View.GONE);
        btnDialogCancel.setVisibility(View.GONE);
        btnDialog.setBackgroundResource(R.drawable.dialogbtnbackground);

        // if button is clicked, close the custom dialog
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        dialog.show();
    }

    public static void alertDialog(final Context ctx, String Title, String Content) {
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
            }
        });
        dialog.show();
    }


    public static void unauthorisedAlertDialog(final Context ctx, String Title, String Content) {
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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        dialog.show();
    }


    /**
     * Register and Get the GCM RegistrationId
     */
    public static String getGCMRegistrationId(Context ctx) {
        String regId = GCMRegistrar.getRegistrationId(ctx);

        //Log.d("RegID",""+regId);
        if (regId.equals("")) {
            // Registration is not present, register now with GCM
            GCMRegistrar.register(ctx, BuildConfig.GCM_ID);
            GCMRegistrar.setRegisteredOnServer(ctx, true);
            regId = GCMRegistrar.getRegistrationId(ctx);
        } else {
            // Device is already registered on GCM
            if (GCMRegistrar.isRegisteredOnServer(ctx)) {
                // Skips registration.
                regId = GCMRegistrar.getRegistrationId(ctx);
                //Toast.makeText(ctx, "Already registered with GCM", Toast.LENGTH_LONG).show();
            } else {
                GCMRegistrar.setRegisteredOnServer(ctx, true);
                regId = GCMRegistrar.getRegistrationId(ctx);
            }
        }
        //Log.d("MyRedId","RegID: "+regId);
        return regId;
    }

    /**
     * Default android progress bar
     */
    public static void showProgressDialog(Context ctx) {
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(ctx.getString(R.string.loading));
        progressDialog.setCancelable(false);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

   /* public static void progressdialogpopup(Context ctx, String content) {
        if (customProgressDialog == null) {
            //Log.d("newDialog", "newDialog");
            customProgressDialog = new Dialog(ctx, R.style.Dialog);
            customProgressDialog.setCancelable(false);
            customProgressDialog.setContentView(R.layout.custom_progressdialog_iphone);
            TextView msg = (TextView) customProgressDialog.findViewById(R.id.progress_message);
            final ImageView progress = (ImageView) customProgressDialog.findViewById(R.id.spinnerImageView);
            //progress.setBackground(R.anim.customprogressspin);
            progress.post(new Runnable() {
                @Override
                public void run() {
                    frameAnimation = (AnimationDrawable) progress.getBackground();
                    frameAnimation.start();
                }
            });
            setMonteserrat_Bold(msg);
            msg.setText(content);
        }
    }*/

    /**
     * Form the request params with key value
     */
    public static String getToJSON(Object src, ArrayList<RequestKeyParams> additionalKeyValue) {
        Gson gDataBean = new Gson();
        String responseObj = null;
        responseObj = gDataBean.toJson(src);
        if (additionalKeyValue != null) {
            JsonElement jsonElement = gDataBean.toJsonTree(src);
            for (int i = 0; i < additionalKeyValue.size(); i++)
                jsonElement.getAsJsonObject()
                        .addProperty(additionalKeyValue.get(i).getRequestKey(), additionalKeyValue.get(i).getRequestValue());

            responseObj = gDataBean.toJson(jsonElement);
        }
        return responseObj;
    }

    public static Object getFromJSON(String responseValue, Class<?> classname) {
        Gson gDataBean = new Gson();
        return gDataBean.fromJson(responseValue, classname);
    }

    public static Bitmap getRoundedCornerImage(Bitmap bitmap) {
        int targetWidth = 300;
        int targetHeight = 300;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2, (Math.min(((float) targetWidth),
                ((float) targetHeight)) / 2), Path.Direction.CCW);
        canvas.clipPath(path);
        Bitmap sourceBitmap = bitmap;
        canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()), new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        instance = this;
        Crittercism.initialize(mContext, "14ef2b7c6da2438292f5a24b6850cafd00555300");
        // Initializing Typeface
        Gibson_Light = Typeface.createFromAsset(mContext.getResources().getAssets(), "Gibson_Light.otf");
        HelveticaNeue_Light = Typeface.createFromAsset(mContext.getResources().getAssets(), "HelveticaNeue-Light.ttf");
        HelveticaNeue_Thin = Typeface.createFromAsset(mContext.getResources().getAssets(), "HelveticaNeue-Thin.otf");
        HelveticaNeue_Bold = Typeface.createFromAsset(mContext.getResources().getAssets(), "HelveticaNeue-Bold.ttf");
        Monteserrat_Bold = Typeface.createFromAsset(mContext.getResources().getAssets(), "Montserrat-Bold.ttf");
    }

}
