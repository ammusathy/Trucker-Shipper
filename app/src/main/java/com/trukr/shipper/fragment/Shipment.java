package com.trukr.shipper.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.DetailedScreen;
import com.trukr.shipper.adapter.ShipmentAdapter;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.AllShipmentResponse;
import com.trukr.shipper.model.ResponseParams.GeneralResponseParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Shipment extends Fragment implements ShipmentAdapter.OnItemClickListener {

    private Context mContext;
    private ProgressDialog pd;
    RequestQueue queue;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    RecyclerView shipmentList;
    View rootView;
    TextView empty_view;
    ShipmentAdapter shipmentAdapter;
    private List<AllShipmentResponse> allShipment;

    public Shipment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.shipment, container, false);
        init();
        // Inflate the layout for this fragment
        if (Constant.isConnectingToInternet(mContext)) {
            getAllShipment();//Method declaration
        } else  {
            TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }

        return rootView;
    }

    private void init() {
        mContext = getActivity();
        shipmentList = (RecyclerView) rootView.findViewById(R.id.shipmentlist);
        empty_view=(TextView)rootView.findViewById(R.id.empty_view) ;
        shipmentList.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        shipmentList.setLayoutManager(llm);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getAllShipment() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        queue = Volley.newRequestQueue(mContext);
        //      String url = "http://softwaredevelopersusa.com/ws-all-shippment";
        JSONObject object = null;
        preferences = mContext.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        String authToken = preferences.getString("authToken", "");//getting a userid and authtoken in login screen
        String userId = preferences.getString("Userid", "");
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userId);
            object.put("AuthToken", authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ListAllShipment, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("ShippmentResponse-->", response.toString());
                try {
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    String Message = response.getString("Message");
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    allShipment = new ArrayList<>();
                    String isSuccess = response.getString("Message");
                    if (isSuccess.equals("Success")) {
                        JSONArray array = response.getJSONArray("Shipment");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            String Orderdate = jsonObject.getString("OrderDate");
                            String Orderstatus = jsonObject.getString("OrderStatus");
                            String Orderid = jsonObject.getString("OrderId");
                            AllShipmentResponse model = new AllShipmentResponse();
                            System.out.println("date-->" + Orderdate);
                            System.out.println("Status-->" + Orderstatus);
                            System.out.println("id-->" + Orderid);
                            model.setOrderDate(Orderdate);
                            model.setOrderStatus(Orderstatus);
                            model.setOrderId(Orderid);
                            if (Orderstatus.equals("Pending") || Orderstatus.equals("Delivered") || Orderstatus.equals("Cancelled") || Orderstatus.equals("In Transit") || Orderstatus.equals("Driver Accepted") || Orderstatus.equals("Approved") || Orderstatus.equals("Driver Arriving Now")) {
                                Log.d("Test", Orderstatus);
                                allShipment.add(model);
                                preferences = getActivity().getSharedPreferences("Fragment", Context.MODE_PRIVATE);//call shared Preference
                                editor = preferences.edit();//editor preferences
                                String userid = null;
                                try {
                                    userid = response.getString("UserId");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String authToken = null;
                                try {
                                    authToken = response.getString("AuthToken");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                editor.putString("OrderId", Orderid);
                                editor.putString("UserId", userid);
                                editor.putString("AuthToken", authToken);
                                editor.commit();
                            }
                        }
                      /*  if (allShipment.size() == 0) {
                            TrukrApplication.alertDialog(mContext, IConstant.alert, "No Orders Found");
                        }*/
                        if (allShipment.isEmpty()) {
                            shipmentList.setVisibility(View.GONE);
                            empty_view.setVisibility(View.VISIBLE);
                        } else {
                            shipmentList.setVisibility(View.VISIBLE);
                            empty_view.setVisibility(View.GONE);
                        }
                        System.out.println("ARRAY Size :" + allShipment.size());
                        createList();
                    }
                    closeprogress();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                GeneralResponseParams responseData = (GeneralResponseParams) TrukrApplication.getFromJSON(response.toString(), GeneralResponseParams.class);

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                closeprogress();
                Log.d("ErrorResponse-->", volleyError.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void createList() {
        shipmentAdapter = new ShipmentAdapter(mContext, allShipment);
        shipmentAdapter.setOnItemClickListener(this);
        shipmentList.setAdapter(shipmentAdapter);
        shipmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Log.d("Test", position + "");
        AllShipmentResponse response = allShipment.get(position);
        String orderId = response.getOrderId();
        String orderStatus = response.getOrderStatus();
        Log.d("Trucker", response.getOrderId());

        try {
            if (response.getOrderStatus().equals("Delivered")) {
                pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity(), DetailedScreen.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("Status", "Delivered");
                startActivity(intent);
                closeprogress();

            } else if (response.getOrderStatus().equals("Cancelled")) {
                pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity(), DetailedScreen.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("Status", "Cancelled");
                startActivity(intent);
                closeprogress();

            } else if (response.getOrderStatus().equals("Pending")) {
                pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity(), RequestReceived.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("Status", "Pending");
                intent.putExtra("OrderStatus", orderStatus);
                System.out.println("orderStatus = " + orderStatus);
                startActivity(intent);
                closeprogress();

            } else if (response.getOrderStatus().equals("In Transit")) {
                pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity(), RequestStatus.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("Status", "In Transit");
                intent.putExtra("OrderStatus", orderStatus);
                startActivity(intent);
                closeprogress();

            } else if (response.getOrderStatus().equals("Driver Accepted")) {
                pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity(), RequestReceived.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("Status", "Driver Accepted");
                intent.putExtra("OrderStatus", orderStatus);
                startActivity(intent);
                closeprogress();
            } else if (response.getOrderStatus().equals("Approved")) {
                pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity(), RequestStatus.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("Status", "Approved");
                intent.putExtra("OrderStatus", orderStatus);
                startActivity(intent);
                closeprogress();
            } else if (response.getOrderStatus().equals("Driver Arriving Now")) {
                pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
                Intent intent = new Intent(getActivity(), RequestStatus.class);
                intent.putExtra("OrderId", orderId);
                intent.putExtra("OrderStatus", orderStatus);
                intent.putExtra("Status", "Driver Arriving Now");
                startActivity(intent);
                closeprogress();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }
}
