package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.trukr.shipper.R;
import com.trukr.shipper.adapter.CardListAdapter;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.helper.TrukrWebServiceHelper;
import com.trukr.shipper.model.CardDetailsReqParams;
import com.trukr.shipper.model.CardResponseParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class PaymentList extends Activity {

    Button pay;
    TextView back;
    ArrayList<CardResponseParams> cardArrayList;
    private ListView listView;
    SharedPreferences preferences;
    int StatusCode;
    String authToken, userId, Message;
    RequestQueue queue;
    ImageView removeCard;
    private static final int PAYMENT_NEW_REQUEST_CODE = 101;
    public static boolean PAYMENT_DELETE = false;
    private Context mContext;
    private ProgressDialog pd;
    private CardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymentcard);
        mContext = PaymentList.this;
        listView = (ListView) findViewById(R.id.paymentcard_list);
        init();
        if (Constant.isConnectingToInternet(mContext)) {
            GetCardDetails();
        } else {
            TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }

        cardArrayList = new ArrayList<CardResponseParams>();
        adapter = new CardListAdapter(this, cardArrayList);
        listView.setAdapter(adapter);

    }

    private void init() {
        removeCard = (ImageView) findViewById(R.id.removecard);
        pay = (Button) findViewById(R.id.newpay);
        back = (TextView) findViewById(R.id.btnbackpay);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentList.this, SlidingDrawer.class);
                intent.putExtra("PaymentList", true);
                startActivity(intent);
            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentList.this, AddNewCard.class);
                startActivity(intent);
                PaymentList.this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        removeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  deleteCard();
                if (PAYMENT_DELETE == true) {
                    PAYMENT_DELETE = false;
                } else {
                    PAYMENT_DELETE = true;
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    public class GetCardDetailsAsync extends AsyncTask<Void, Void, Boolean> {
        boolean status;
        String responseParams = null;
        String inputParams = null;

        private GetCardDetailsAsync(String inputParams) {
            this.inputParams = inputParams;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            responseParams = TrukrWebServiceHelper.httpPostJsonData(IConstant.ListCards, inputParams);
            Log.i("CreateShippmentResponse", "" + responseParams);
            if (responseParams != null)
                status = true;
            else
                status = false;
            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            closeprogress();
            try {
                Log.i("Status", "" + aBoolean);
                if (aBoolean) {
                    JSONObject jsonObject = new JSONObject(responseParams);
                    if (jsonObject.getString("StatusCode").equals("97")) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                    }

                    if (jsonObject.getString("StatusCode").equals("100")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Cards");
                        CardResponseParams responseParams;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject cardObject = jsonArray.getJSONObject(i);
                            responseParams = new CardResponseParams();
                            responseParams.CardId = cardObject.optString("CardId");
                            responseParams.CardNumber = cardObject.optString("CardNumber");
                            responseParams.CardTypeId = cardObject.optString("CardTypeId");
                            responseParams.NameOnCard = cardObject.optString("NameOnCard");
                            responseParams.ExpiryMM = cardObject.optString("ExpiryMM");
                            responseParams.ExpiryYY = cardObject.optString("ExpiryYY");
                            responseParams.IsDefault = cardObject.optString("IsDefault");
                            cardArrayList.add(responseParams);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private void GetCardDetails() {
        CardDetailsReqParams reqParams = new CardDetailsReqParams();
        preferences = getSharedPreferences("Trucker", Context.MODE_PRIVATE);
        authToken = preferences.getString("authToken", null);
        userId = preferences.getString("Userid", null);
        reqParams.AuthToken = authToken;
        reqParams.UserId = userId;
        reqParams.UserType = IConstant.UserType;
        String inputParams = TrukrApplication.getToJSON(reqParams, null);
        Log.d("DistanceParams", inputParams);
        GetCardDetailsAsync GetCardDetailsAsync = new GetCardDetailsAsync(inputParams);
        GetCardDetailsAsync.execute();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PAYMENT_NEW_REQUEST_CODE:
                    this.cardArrayList.clear();
                    GetCardDetails();
                default:
            }
        }
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }
}