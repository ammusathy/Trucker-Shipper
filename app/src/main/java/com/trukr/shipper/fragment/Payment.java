package com.trukr.shipper.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.AddNewCard;
import com.trukr.shipper.activity.PayPal;
import com.trukr.shipper.activity.PaymentList;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.CardResponseParams;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

public class Payment extends Fragment {

    private RelativeLayout headerLayout = null;
    private Button btnEnterCardDetails = null, btnScanCard = null, btnPayPal = null;
    CheckBox imageview_Card, imageview_Paypal, imageview_Credits;
    CardResponseParams responseData;
    RelativeLayout card, credit, paypal;
    private View rootView = null;
    private String TAG = Payment.class.getName();
    private static final int DEFAULT_CARD_REQUEST_CODE = 103;
    private static final int MY_SCAN_REQUEST_CODE = 102;
    RequestQueue queue;
    Dialog dialog;
    private Context mContext;
    private ProgressDialog pd;
    SharedPreferences preferences, sharedPreferences;
    SharedPreferences.Editor editor;
    String authToken, userId, Message;
    int variable = 0;

    public Payment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.payment, container, false);
        headerLayout = (RelativeLayout) rootView.findViewById(R.id.headerLayout);
        headerLayout.setVisibility(View.GONE);
        init();
        if (Constant.isConnectingToInternet(mContext)) {
            paymentList();
            getPaymentOptions();
            getdefault();//Method declaration
        } else  {
            TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }

        process();
        return rootView;
    }

    public void init() {
        card = (RelativeLayout) rootView.findViewById(R.id.rl_card);
        paypal = (RelativeLayout) rootView.findViewById(R.id.rl_paypal);
        credit = (RelativeLayout) rootView.findViewById(R.id.rl_credit);
        btnEnterCardDetails = (Button) rootView.findViewById(R.id.cardno);
        btnScanCard = (Button) rootView.findViewById(R.id.btnscancard);
        btnPayPal = (Button) rootView.findViewById(R.id.btnpaypal);
        imageview_Card = (CheckBox) rootView.findViewById(R.id.imageView_Card);
        imageview_Paypal = (CheckBox) rootView.findViewById(R.id.imageView_Paypal);
        imageview_Credits = (CheckBox) rootView.findViewById(R.id.imageView_Credits);

        btnScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scanIntent = new Intent(getActivity(), CardIOActivity.class);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false);
                scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false);
                startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
            }
        });
    }

    public void process() {
        btnPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PayPal.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_left);
            }
        });
        imageview_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageview_Card.setChecked(true);
                imageview_Credits.setChecked(false);
                imageview_Paypal.setChecked(false);
                variable = 2;
                if (Constant.isConnectingToInternet(mContext)) {
                    paymentOptions();//Method declaration
                } else  {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                }

                try {
                    if ((responseData.CardNumber == null)) {
                        showAlertDialog("Please choose any card as default card", 0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        imageview_Paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageview_Paypal.setChecked(true);
                imageview_Credits.setChecked(false);
                imageview_Card.setChecked(false);
                variable = 1;
                paymentOptions();

            }
        });
        imageview_Credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageview_Credits.setChecked(true);
                imageview_Card.setChecked(false);
                imageview_Paypal.setChecked(false);
                variable = 3;
                paymentOptions();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getActivity();
        switch (requestCode) {
            case MY_SCAN_REQUEST_CODE:
                if (data == null || !data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                    String str = "Scan was canceled.";
                    return;
                }
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                String resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + StringUtils.LF;
                if (scanResult.isExpiryValid()) {
                    resultDisplayStr = resultDisplayStr + "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + StringUtils.LF;
                }
                if (scanResult.cvv != null) {
                    resultDisplayStr = resultDisplayStr + "CVV has " + scanResult.cvv.length() + " digits.\n";
                }
                if (scanResult.postalCode != null) {
                    resultDisplayStr = resultDisplayStr + "Postal Code: " + scanResult.postalCode + StringUtils.LF;
                }
                sharedPreferences = getActivity().getSharedPreferences("Card", Context.MODE_PRIVATE);
                Intent intent = new Intent(getActivity(), AddNewCard.class);
                editor = sharedPreferences.edit();
                intent.putExtra("CardNo", scanResult.cardNumber);
                editor.putString("ExpMonth", scanResult.expiryMonth + "");
                editor.putString("ExpYear", scanResult.expiryYear + "");
                editor.commit();
                startActivity(intent);
                Log.v(TAG, resultDisplayStr);
            case DEFAULT_CARD_REQUEST_CODE:
                CreditCard scanResults = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                Intent i = new Intent(getActivity(), AddNewCard.class);
                editor = sharedPreferences.edit();
                i.putExtra("CardNo", scanResults.cardNumber);
                editor.putString("ExpMonth", scanResults.expiryMonth + "");
                editor.putString("ExpYear", scanResults.expiryYear + "");
                editor.commit();
                startActivity(i);
            default:
        }
    }

    private void getdefault() {
        pd = new ProgressDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(getActivity());
        preferences = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);

        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserId", userId);
            object.put("UserType", IConstant.UserType);
            object.put("AuthToken", authToken);
            System.out.println("UserId-->" + userId);
            System.out.println("Usertype-->" + IConstant.UserType);
            System.out.println("AuthToken-->" + authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.GetDefaultCard, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                closeprogress();
                Log.d("response----->", response.toString());
                try {
                    final int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                responseData = (CardResponseParams) TrukrApplication.getFromJSON(response.toString(), CardResponseParams.class);
                try {
                    if (responseData.CardNumber != null) {
                        btnEnterCardDetails.setText(responseData.CardNumber);
                    } else {
                        btnEnterCardDetails.setText("Enter Card Details");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    closeprogress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void paymentOptions() {

        pd = new ProgressDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(getActivity());
        preferences = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);

        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserId", userId);
            object.put("UserType", IConstant.UserType);
            object.put("AuthToken", authToken);
            object.put("PaymentOption", variable);
            System.out.println("UserId-->" + userId);
            System.out.println("Usertype-->" + IConstant.UserType);
            System.out.println("AuthToken-->" + authToken);
            System.out.println("PaymentOptions-->" + variable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.SavePaymentOptions, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                closeprogress();
                Log.d("response----->", response.toString());
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                try {
                    closeprogress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getPaymentOptions() {
        queue = Volley.newRequestQueue(getActivity());
        preferences = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);

        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserId", userId);
            object.put("UserType", IConstant.UserType);
            object.put("AuthToken", authToken);
            System.out.println("UserId-->" + userId);
            System.out.println("Usertype-->" + IConstant.UserType);
            System.out.println("AuthToken-->" + authToken);

        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.PaymentOptions, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response----->", response.toString());
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    int paymentOptions = Integer.parseInt(response.getString("PaymentOption"));
                    Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                    }
                    if (paymentOptions == 1) {
                        imageview_Paypal.setChecked(true);
                        imageview_Credits.setChecked(false);
                        imageview_Card.setChecked(false);
                    } else if (paymentOptions == 2) {
                        imageview_Card.setChecked(true);
                        imageview_Credits.setChecked(false);
                        imageview_Paypal.setChecked(false);
                    } else if (paymentOptions == 3) {
                        imageview_Credits.setChecked(true);
                        imageview_Paypal.setChecked(false);
                        imageview_Card.setChecked(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
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

    private void paymentList() {
        queue = Volley.newRequestQueue(getActivity());
        preferences = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserId", userId);
            object.put("UserType", IConstant.UserType);
            object.put("AuthToken", authToken);
            System.out.println("UserId-->" + userId);
            System.out.println("Usertype-->" + IConstant.UserType);
            System.out.println("AuthToken-->" + authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ListCards, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response of paymentlist", response.toString());
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    final JSONArray jsonArray = response.getJSONArray("Cards");
                    System.out.println("length of listcard" + jsonArray.length());
                    Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                    }
                    btnEnterCardDetails.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (jsonArray.length() > 0) {
                                Intent intent = new Intent(getContext(), PaymentList.class);
                                startActivity(intent);
                            } else {
                                Intent i = new Intent(getContext(), AddNewCard.class);
                                startActivity(i);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}
