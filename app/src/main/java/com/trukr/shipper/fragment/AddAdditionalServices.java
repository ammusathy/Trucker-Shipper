package com.trukr.shipper.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.Login;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.IConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by kalaivani on 3/27/2016.
 */
public class AddAdditionalServices extends Fragment implements View.OnClickListener {
    private ArrayList<String> stringList;
    private ArrayList<String> sublist;
    TextView add_additional_text1, add_additional_text2;
    Button btn_ok, btn_cancel;
    //  View rootView;
    Dialog additionalalert;
    private ProgressDialog pd;
    private BroadcastReceiver AdditionalBroadcastReceiver;
    private EditText edit_additional_text2;
    public Home.AdditionalServiceListener additionalServiceListener;

    public void setAdditionalServiceListener(Home.AdditionalServiceListener additionalServiceListener) {
        this.additionalServiceListener = additionalServiceListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sublist = new ArrayList<String>();
        stringList = TrukrApplication.getArrayList();
        AdditionalBroadcastReceiver = new LocalAdditionalServiceReceiver();
        additionalalert = new Dialog(getActivity());
        additionalalert.setCanceledOnTouchOutside(false);
        additionalalert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        additionalalert.setContentView(R.layout.add_additional_services);
        additionalalert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        additionalalert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        additionalalert.show();
        add_additional_text1 = (TextView) additionalalert.findViewById(R.id.add_additional_text1);
        add_additional_text2 = (TextView) additionalalert.findViewById(R.id.add_additional_text2);
        edit_additional_text2 = (EditText) additionalalert.findViewById(R.id.edit_additional_text2);
        btn_ok = (Button) additionalalert.findViewById(R.id.add_additional_btn_ok);
        btn_cancel = (Button) additionalalert.findViewById(R.id.add_additional_btn_cancel);
        add_additional_text1.setOnClickListener(this);
        add_additional_text2.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_additional_text1:
                if (stringList.size() > 0) {
                    showDialog(stringList, "AdditionalLabel", add_additional_text1);
                    add_additional_text2.setText("");
                    edit_additional_text2.setText("");
                }
                break;
            case R.id.add_additional_text2:
                if (!TextUtils.isEmpty(add_additional_text1.getText().toString())) {
                    if (add_additional_text1.getText().toString().equals("Load straps") ||
                            add_additional_text1.getText().toString().equals("Stops-in-transit")) {
                    } else {
                        if (sublist.size() > 0) {
                            showDialog(sublist, "SubAdditionalLabel", add_additional_text2);
                        }
                    }
                }
                break;
            case R.id.add_additional_btn_ok:
                if (!TextUtils.isEmpty(add_additional_text1.getText().toString()) && (!TextUtils.isEmpty(add_additional_text2.getText().toString()) || !TextUtils.isEmpty(edit_additional_text2.getText().toString()))) {
                    TrukrApplication.removeAdditionalService(add_additional_text1.getText().toString());
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                    TrukrApplication.mSelAdditionallabel = add_additional_text1.getText().toString();
                    if (!TextUtils.isEmpty(add_additional_text2.getText().toString())) {
                        TrukrApplication.mSelAdditionalValue = add_additional_text2.getText().toString();
                    }
                    if (!TextUtils.isEmpty(edit_additional_text2.getText().toString())) {
                        TrukrApplication.mSelAdditionalValue = edit_additional_text2.getText().toString();
                    }
                    additionalServiceListener.refereshAditionalServiceView();
                    additionalalert.dismiss();
                   /* InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);*/
                } else if (TextUtils.isEmpty(add_additional_text1.getText().toString())) {
                    alertDialog(getActivity(), IConstant.alert, "Please select service");
                } else if (!TextUtils.isEmpty(add_additional_text1.getText().toString()) && TextUtils.isEmpty(add_additional_text2.getText().toString()) || TextUtils.isEmpty(edit_additional_text2.getText().toString())) {
                    alertDialog(getActivity(), IConstant.alert, "Please select value");
                }
                break;
            case R.id.add_additional_btn_cancel:
                additionalalert.dismiss();
                break;
        }
    }

    public void showDialog(ArrayList<String> mArrayList, String mSource, TextView txt_view) {
        MyDialogFragment dialog = new MyDialogFragment(txt_view);
        Bundle b = new Bundle();
        b.putStringArrayList("mSource", mArrayList);
        b.putString("value", mSource);
        dialog.setArguments(b);
        dialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                AdditionalBroadcastReceiver,
                new IntentFilter("ADDLABEL"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                AdditionalBroadcastReceiver);
    }

    private class LocalAdditionalServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals("ADDLABEL")) {
                // doSomeAction();
                Log.i("AdditionalServices", "Called!!!");
                if (add_additional_text1.getText().toString().equals("Border Crossing fee")) {
                    Bordercross();
                } else {
                    sublist.clear();
                    sublist.add("YES");
                    sublist.add("NO");
                }
                if (add_additional_text1.getText().toString().equals("No Of Straps") ||
                        add_additional_text1.getText().toString().equals("Stops-in-transit")) {
                    add_additional_text2.setVisibility(View.GONE);
                    edit_additional_text2.setVisibility(View.VISIBLE);
                } else {
                    add_additional_text2.setVisibility(View.VISIBLE);
                    edit_additional_text2.setVisibility(View.GONE);
                }
            }
        }
    }

    private void Bordercross() {
        pd = new ProgressDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
    //    String url = "http://softwaredevelopersusa.com/ws-border-cross";
        // Request a string response
        StringRequest stringRequest = new StringRequest(Request.Method.GET, IConstant.BorderCrossing,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sublist.clear();
                        // Result handling
                        System.out.println("Response Bordercross :::" + response);
                        JSONObject jsonRootObject = null;
                        try {
                            jsonRootObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONObject jsonobj = null;
                        try {
                            jsonobj = jsonRootObject.getJSONObject("Data");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Iterator x = jsonobj.keys();

                        while (x.hasNext()) {
                            String key = (String) x.next();
                            try {
                                Log.i("Value : ", "" + jsonobj.get(key));
                                sublist.add(jsonobj.get(key).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        closeprogress();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                // Error handling
                System.out.println("Something went wrong!");
                error.printStackTrace();
            }
        });

        // Add the request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
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

            }
        });
        dialog.show();
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

}
