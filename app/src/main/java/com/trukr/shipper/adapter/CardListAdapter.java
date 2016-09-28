package com.trukr.shipper.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.PaymentList;
import com.trukr.shipper.activity.SlidingDrawer;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.CardResponseParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CardListAdapter extends BaseAdapter {

    Context context;
    private ArrayList<CardResponseParams> cardList;
    private LayoutInflater inflater = null;
    SharedPreferences preferences;
    String authToken, userId;
    RequestQueue queue;
    private ProgressDialog pd;

    public CardListAdapter(Context mContext, ArrayList<CardResponseParams> cardList) {
        // TODO Auto-generated constructor stub
        this.cardList = cardList;
        this.context = mContext;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv_cardNo, tv_cardName, tv_cardExpiry, tv_default;
        ImageView imgTick, imgDelete;
        CheckBox default_Choose;
    }

    Holder holder = null;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.card_item, null);
            holder.tv_cardName = (TextView) convertView.findViewById(R.id.carditem_tv_cardName);
            holder.tv_cardNo = (TextView) convertView.findViewById(R.id.carditem_tv_cardNo);
            holder.tv_cardExpiry = (TextView) convertView.findViewById(R.id.carditem_tv_cardExpiry);
            holder.imgTick = (ImageView) convertView.findViewById(R.id.default_icon);
            holder.imgDelete = (ImageView) convertView.findViewById(R.id.default_delete);
            holder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            holder.default_Choose = (CheckBox) convertView.findViewById(R.id.default_choose);
            holder.tv_cardName.setText(cardList.get(position).NameOnCard);
            holder.tv_cardNo.setText(cardList.get(position).CardNumber);
            holder.tv_cardExpiry.setText(cardList.get(position).ExpiryMM + "/" +
                    cardList.get(position).ExpiryYY);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_cardNo.setText(cardList.get(position).CardNumber);
        if (PaymentList.PAYMENT_DELETE == true) {
            holder.imgTick.setVisibility(View.INVISIBLE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.default_Choose.setVisibility(View.INVISIBLE);
        }
        if (cardList.get(position).IsDefault.equals("1")) {
            holder.tv_default.setVisibility(View.VISIBLE);
            holder.imgTick.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.INVISIBLE);
            holder.default_Choose.setVisibility(View.INVISIBLE);
            System.out.println("position-->" + position);
        } else {
            holder.tv_default.setVisibility(View.GONE);
            holder.imgTick.setVisibility(View.GONE);
            holder.default_Choose.setVisibility(View.VISIBLE);
        }
        if (PaymentList.PAYMENT_DELETE == false) {
            holder.imgDelete.setVisibility(View.INVISIBLE);
        }

        if (PaymentList.PAYMENT_DELETE == true && cardList.get(position).IsDefault.equals("1")) {
            holder.imgTick.setVisibility(View.INVISIBLE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.default_Choose.setVisibility(View.INVISIBLE);
        }

        if (PaymentList.PAYMENT_DELETE == true && cardList.get(position).IsDefault.equals("0")) {
            holder.imgTick.setVisibility(View.INVISIBLE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.default_Choose.setVisibility(View.INVISIBLE);
        }

        holder.default_Choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CardResponseParams cardResponseParams = (CardResponseParams) CardListAdapter.this.cardList.get(position);
                Log.d("cardResponse----->", cardResponseParams.CardId.toString());
                pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                queue = Volley.newRequestQueue(context);
                preferences = context.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
                authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
                userId = preferences.getString("Userid", null);
                JSONObject object = null;
                try {
                    object = new JSONObject();
                    object.put("UserId", userId);
                    object.put("UserType", IConstant.UserType);
                    object.put("AuthToken", authToken);
                    object.put("CardId", cardResponseParams.CardId);
                    object.put("IsDefault", IConstant.IsDefault);
                    System.out.println("UserId-->" + userId);
                    System.out.println("Usertype-->" + IConstant.UserType);
                    System.out.println("AuthToken-->" + authToken);
                    System.out.println("CardId-->" + cardResponseParams.CardId);
                    System.out.println("IsDefault-->" + IConstant.IsDefault);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.SetDefaultCard, object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response----->", response.toString());
                        closeprogress();
                        try {
                            int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                            String Message = response.getString("Message");
                            if (StatusCode == 97) {
                                TrukrApplication.unauthorisedAlertDialog(context, IConstant.alert, Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        closeprogress();
                    }
                });

                request.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);

                Intent intent = new Intent(context, SlidingDrawer.class);
                intent.putExtra("PaymentList", true);
                context.startActivity(intent);

            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCard();
            }

            private void deleteCard() {
                queue = Volley.newRequestQueue(context);
                preferences = context.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
                authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
                userId = preferences.getString("Userid", null);
                JSONObject object = null;
                JSONArray jsonArray = new JSONArray();
                try {
                    object = new JSONObject();
                    object.put("UserId", userId);
                    object.put("UserType", IConstant.UserType);
                    object.put("AuthToken", authToken);
                    System.out.println("request-->" + object);
                    JSONObject obj = null;
                    System.out.println("cardsize--->" + cardList.size());

                    obj = new JSONObject();
                    try {
                        obj.put("CardId", CardListAdapter.this.cardList.get(position).CardId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(obj);
                    object.put("Card", jsonArray);
                    System.out.println("result-->" + object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.RemoveCard, object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response----->", response.toString());
                        try {
                            int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                            String Message = response.getString("Message");
                            if (StatusCode == 97) {
                                TrukrApplication.unauthorisedAlertDialog(context, IConstant.alert, Message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (response.getString("StatusCode").equals("100")) {
                                Intent i = new Intent(context, PaymentList.class);
                                context.startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                      //  Toast.makeText(context, volleyError.toString(), Toast.LENGTH_LONG).show();

                    }
                });
                request.setRetryPolicy(new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(request);
            }
        });
        return convertView;
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }
}