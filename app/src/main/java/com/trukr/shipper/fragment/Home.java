package com.trukr.shipper.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.trukr.shipper.ActionCallBack;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.SlidingDrawer;
import com.trukr.shipper.activity.ViewDetails;
import com.trukr.shipper.adapter.ShipmentAdapter;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.components.DeliveryDatePickerFragment;
import com.trukr.shipper.components.PickUpDatePickerFragment;
import com.trukr.shipper.components.TimePicker;
import com.trukr.shipper.components.TimePickerFragment;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.helper.TrukrWebServiceHelper;
import com.trukr.shipper.model.CalculateDistanceParams;
import com.trukr.shipper.model.GetBookNowRequest;
import com.trukr.shipper.model.TruckTypeModel;
import com.trukr.shipper.model.ViewDetailModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Home extends Fragment implements ShipmentAdapter.OnItemClickListener, ActionCallBack {

    public static ArrayList<ViewDetailModel> ViewList;
    public static int selectedday, selectedmonth, selectedyear;
    public Activity act;
    public ArrayList<TruckTypeModel> additionalService = new ArrayList<>();
    EditText notes, edittextsource, edittextdestination;
    CheckBox borderCrossStatus;
    ImageView ivAdditionalServices;
    int StatusCode;
    ScrollView views;
    Button btnChangeOrder, btnViewDetails, btnBookNow;
    View rootView;
    TextView btnAdditionalServices, btnTrucktype, btnPickUpDate, btnPickUpTime, btnDeliveryDate,
            btnDeliveryTime, textprice;
    SharedPreferences preferences;
    String edittextsourcevalue, edittextdestinationvalue, userId, authToken;
    LayoutInflater vi;
    String FromLat = "", FromLon = "", ToLat = "", ToLon = "", TruckID = "", orderId, borderStatus, orderstatus;
    String btntrucktypevalue = "", BorderCrossing = "", HazardousMaterial = "", Loadstraps = "", RedeliveryCharge = "", StopsinTransit = "", TeamService = "", OrderId = "", pickupDateValue = "", pickupTimeValue = "", deliveryDateValue = "", deliveryTimeValue = "";
    String pick, delivery;
    private Context mContext;
    private BroadcastReceiver localBroadcastReceiver;
    private ArrayList<TruckTypeModel> TruckList;
    private LinearLayout add_service_values;
    private ProgressDialog pd;
    private boolean btnPickUpDateBoolean = false;
    private boolean btnPickUpTimeBoolean = false;
    private boolean btnDeliveryDateBoolean = false;
    private boolean btnDeliveryTimeBoolean = false;
    private boolean btnTrucktypeBoolean = false;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TrukrApplication.getArrayList().clear();
        TrukrApplication.addAdditionalServices();
        localBroadcastReceiver = new LocalBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home, container, false);
        init();
        paymentalert();
        getTruckTypeAsync();
        process();
        Log.i("onCreate", "Called");
        return rootView;
    }

    private void paymentalert() {
        //  TrukrApplication.alertDialog1(mContext, IConstant.alert, IConstant.PAYMENT_ALERT);
    }

    private void getTruckTypeAsync() {
        GetTruckTypeAsync truckTypeAsync = new GetTruckTypeAsync();
        truckTypeAsync.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        SlidingDrawer.mTitle.setText(getString(R.string.title_trucker));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                localBroadcastReceiver,
                new IntentFilter("SOME_ACTION"));
        Log.i("OnResume", "Called!!");
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                localBroadcastReceiver);
    }

    private void init() {
        mContext = getContext();
        act = getActivity();
        if (getActivity() != null) {
            vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } else {
            vi = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        views = (ScrollView) rootView.findViewById(R.id.scrollview);
        borderCrossStatus = (CheckBox) rootView.findViewById(R.id.home_chk_rememberme);
        btnTrucktype = (TextView) rootView.findViewById(R.id.btntrucktype);
        btnPickUpDate = (TextView) rootView.findViewById(R.id.btnpickupdate);
        btnPickUpTime = (TextView) rootView.findViewById(R.id.btnpickuptime);
        btnDeliveryDate = (TextView) rootView.findViewById(R.id.btndeliverydate);
        btnDeliveryTime = (TextView) rootView.findViewById(R.id.btndeliverytime);
        btnAdditionalServices = (TextView) rootView.findViewById(R.id.btnadditionalservices);
        ivAdditionalServices = (ImageView) rootView.findViewById(R.id.iv_addicon);
        add_service_values = (LinearLayout) rootView.findViewById(R.id.add_service_values);
        btnChangeOrder = (Button) rootView.findViewById(R.id.btnchangeorder);
        btnViewDetails = (Button) rootView.findViewById(R.id.btnviewdetails);
        btnBookNow = (Button) rootView.findViewById(R.id.btnbooknow);
        textprice = (TextView) rootView.findViewById(R.id.textprice);
        edittextsource = (EditText) rootView.findViewById(R.id.edittextsource);
        edittextdestination = (EditText) rootView.findViewById(R.id.edittextdestination);
        notes = (EditText) rootView.findViewById(R.id.editnotes);
        edittextsource.setText("106 Harris Dr Austin,TX 73301");
        edittextdestination.setText("1020 Musser St, Laredo, TX 78040");
        TrukrApplication.setHelveticaNeue_Thin((TextView) rootView.findViewById(R.id.textincludes));
        TrukrApplication.setGibson_Light(btnBookNow);
        TrukrApplication.setHelveticaNeue_Thin(edittextsource);
        TrukrApplication.setHelveticaNeue_Thin(edittextdestination);
        TrukrApplication.setHelveticaNeue_Thin(borderCrossStatus);
        TrukrApplication.setHelveticaNeue_Thin(btnAdditionalServices);
        TrukrApplication.setHelveticaNeue_Thin(btnTrucktype);
        TrukrApplication.setHelveticaNeue_Thin(btnPickUpDate);
        TrukrApplication.setHelveticaNeue_Thin(btnPickUpTime);
        TrukrApplication.setHelveticaNeue_Thin(btnDeliveryDate);
        TrukrApplication.setHelveticaNeue_Thin(btnDeliveryTime);
        TrukrApplication.setHelveticaNeue_Thin(btnViewDetails);
        TrukrApplication.setHelveticaNeue_Thin(btnBookNow);
        TrukrApplication.setHelveticaNeue_Thin(edittextdestination);
        TrukrApplication.setHelveticaNeue_Thin(notes);
        TruckList = new ArrayList<TruckTypeModel>();
        ViewList = new ArrayList<ViewDetailModel>();

    }

    public void showDialog(ArrayList<String> mArrayList, String mSource, TextView txt_view) {
        MyDialogFragment dialog = new MyDialogFragment(txt_view);
        Bundle b = new Bundle();
        b.putStringArrayList("mSource", mArrayList);
        b.putString("value", mSource);
        dialog.setArguments(b);
        dialog.show(getActivity().getFragmentManager(), "Dialog");
    }

    private void process() {

        views.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (edittextsource.isFocused()) {
                        Rect outRect = new Rect();
                        edittextsource.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                            edittextsource.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                } else if (edittextdestination.isFocused()) {
                    Rect outRect = new Rect();
                    edittextdestination.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        edittextdestination.clearFocus();
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                } else if (notes.isFocused()) {
                    Rect outRect = new Rect();
                    notes.getGlobalVisibleRect(outRect);
                    if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        notes.clearFocus();
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        btnChangeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edittextsourcevalue = edittextsource.getText().toString().trim();
                edittextdestinationvalue = edittextdestination.getText().toString().trim();
                edittextsource.setText(edittextdestinationvalue);
                edittextdestination.setText(edittextsourcevalue);
                getDistanceCalc();
            }
        });

        btnTrucktype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> dialogArray = new ArrayList<String>();
                for (int i = 0; i < TruckList.size(); i++) {
                    dialogArray.add(TruckList.get(i).Name);
                }
                showDialog(dialogArray, "TruckType", btnTrucktype);
            }
        });

        /**
         * Start Date
         */


        btnPickUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeliveryDate.setText("Date");
                DialogFragment picker = new PickUpDatePickerFragment(btnPickUpDate);
                picker.show(getFragmentManager(), "DatePicker");


                btnPickUpDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        btnPickUpTime.setEnabled(true);

                    }
                });
            }
        });


        /**
         * End Date
         */
        btnDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new DeliveryDatePickerFragment(btnDeliveryDate);
                picker.show(getFragmentManager(), "DatePicker");

                btnDeliveryDate.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        btnDeliveryTime.setEnabled(true);

                    }
                });
            }
        });
        if (PickUpDatePickerFragment.selectedday > DeliveryDatePickerFragment.selectedday) {
            PickUpDatePickerFragment.selectedday = Integer.parseInt("");
            PickUpDatePickerFragment.selectedyear = Integer.parseInt("");
            PickUpDatePickerFragment.selectedmonth = Integer.parseInt("");
        }


         /*   btnDeliveryDate.setText(null);
        }*/
        btnPickUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new TimePickerFragment(btnPickUpTime);
                picker.show(getFragmentManager(), "TimePicker");
                btnPickUpTime.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        btnDeliveryDate.setEnabled(true);
                    }
                });
            }
        });

        btnDeliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment picker = new TimePicker(btnDeliveryTime);
                picker.show(getFragmentManager(), "TimePicker");
            }
        });


        ivAdditionalServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TrukrApplication.getArrayList().size() > 0) {
                    AddAdditionalServices fragment = new AddAdditionalServices();
                    fragment.setAdditionalServiceListener(new AdditionalServiceListener() {
                        @Override
                        public void refereshAditionalServiceView() {
                            Log.v("MyCallback", "Called");
                            if (TrukrApplication.mSelAdditionallabel != null &&
                                    TrukrApplication.mSelAdditionalValue != null) {
                                if (add_service_values != null) {
                                    TruckTypeModel model = new TruckTypeModel();
                                    model.Id = TrukrApplication.mSelAdditionallabel;
                                    model.Name = TrukrApplication.mSelAdditionalValue;
                                    additionalService.add(model);
                                    System.out.println("size of the list" + additionalService.size());
                                    addDynamicView(TrukrApplication.mSelAdditionallabel,
                                            TrukrApplication.mSelAdditionalValue);
                                }
                            }
                        }
                    });
                    replaceFragment(fragment);
                } else {
                    //  Toast.makeText(getActivity(), "No Additional Services to added", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.isConnectingToInternet(mContext)) {
                    Booknow();//Method declaration
                } else {
                    TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                }

            }
        });

        btnTrucktype.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!pickupDateValue.equals("Date") &&
                        !deliveryDateValue.equals("Date") &&
                        !pickupTimeValue.equals("Time") &&
                        !deliveryTimeValue.equals("Time") &&
                        !btntrucktypevalue.equals("Truck type")) {

                    btnTrucktypeBoolean = true;
                    if (btnPickUpDateBoolean == true &&
                            btnPickUpTimeBoolean == true &&
                            btnDeliveryDateBoolean == true &&
                            btnDeliveryTimeBoolean == true &&
                            btnTrucktypeBoolean == true) {
                        if (Constant.isConnectingToInternet(mContext)) {
                            getDistanceCalc();//Method declaration
                        } else {
                            TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                        }


                        btnViewDetails.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnPickUpDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!pickupDateValue.equals("Date") &&
                        !deliveryDateValue.equals("Date") &&
                        !pickupTimeValue.equals("Time") &&
                        !deliveryTimeValue.equals("Time") &&
                        !btntrucktypevalue.equals("Truck type")) {

                    btnPickUpDateBoolean = true;
                    if (btnPickUpDateBoolean == true &&
                            btnPickUpTimeBoolean == true &&
                            btnDeliveryDateBoolean == true &&
                            btnDeliveryTimeBoolean == true &&
                            btnTrucktypeBoolean == true) {
                        getDistanceCalc();
                        btnViewDetails.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnPickUpTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!pickupDateValue.equals("Date") &&
                        !deliveryDateValue.equals("Date") &&
                        !pickupTimeValue.equals("Time") &&
                        !deliveryTimeValue.equals("Time") &&
                        !btntrucktypevalue.equals("Truck type")) {

                    btnPickUpTimeBoolean = true;
                    if (btnPickUpDateBoolean == true &&
                            btnPickUpTimeBoolean == true &&
                            btnDeliveryDateBoolean == true &&
                            btnDeliveryTimeBoolean == true &&
                            btnTrucktypeBoolean == true) {
                        getDistanceCalc();
                        btnViewDetails.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnDeliveryDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!pickupDateValue.equals("Date") &&
                        !deliveryDateValue.equals("Date") &&
                        !pickupTimeValue.equals("Time") &&
                        !deliveryTimeValue.equals("Time") &&
                        !btntrucktypevalue.equals("Truck type")) {

                    btnDeliveryDateBoolean = true;
                    if (btnPickUpDateBoolean == true &&
                            btnPickUpTimeBoolean == true &&
                            btnDeliveryDateBoolean == true &&
                            btnDeliveryTimeBoolean == true &&
                            btnTrucktypeBoolean == true) {
                        getDistanceCalc();
                        btnViewDetails.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        btnDeliveryTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!pickupDateValue.equals("Date") &&
                        !deliveryDateValue.equals("Date") &&
                        !pickupTimeValue.equals("Time") &&
                        !deliveryTimeValue.equals("Time") &&
                        !btntrucktypevalue.equals("Truck type")) {

                    btnDeliveryTimeBoolean = true;
                    if (btnPickUpDateBoolean == true &&
                            btnPickUpTimeBoolean == true &&
                            btnDeliveryDateBoolean == true &&
                            btnDeliveryTimeBoolean == true &&
                            btnTrucktypeBoolean == true) {
                        getDistanceCalc();
                        btnViewDetails.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void Booknow() {
        edittextsourcevalue = edittextsource.getText().toString().trim();
        edittextdestinationvalue = edittextdestination.getText().toString().trim();
        btntrucktypevalue = btnTrucktype.getText().toString().trim();
        pickupDateValue = btnPickUpDate.getText().toString().trim();
        deliveryDateValue = btnDeliveryDate.getText().toString().trim();

        System.out.println("pickupDateValue = " + pickupDateValue);
        System.out.println("deliveryDateValue = " + deliveryDateValue);


        SimpleDateFormat sdfSource = new SimpleDateFormat("MM/dd/yyyy");
        String pickupDateValues = null;
        String deliveryDateValues = null;
        try {
            //parse the string into Date object
            Date date1 = sdfSource.parse(pickupDateValue);
            Date date2 = sdfSource.parse(deliveryDateValue);
            //create SimpleDateFormat object with desired date format
            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfDestination1 = new SimpleDateFormat("yyyy-MM-dd");
            //parse the date into another format
            pickupDateValue = sdfDestination.format(date1);
            deliveryDateValue = sdfDestination1.format(date2);
            System.out.println("simple format value--->" + pickupDateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        pickupTimeValue = btnPickUpTime.getText().toString().trim();
        deliveryTimeValue = btnDeliveryTime.getText().toString().trim();
        if (edittextsourcevalue.length() <= 0)
            TrukrApplication.alertDialog(getActivity(), IConstant.alert, "Please enter your source", 0);
        else if (edittextdestinationvalue.length() <= 0)
            TrukrApplication.alertDialog(getActivity(), IConstant.alert, "Please enter your destination", 0);
        else if (btntrucktypevalue.equalsIgnoreCase("Truck type"))
            TrukrApplication.alertDialog(getActivity(), IConstant.alert, "Please enter your truck type", 0);
        else if (pickupDateValue.equalsIgnoreCase("Date")) {
            TrukrApplication.alertDialog(getActivity(), IConstant.alert, "Please choose pickup date", 0);
        } else if (pickupTimeValue.equalsIgnoreCase("Time")) {
            TrukrApplication.alertDialog(getActivity(), IConstant.alert, "Please choose pickup time", 0);
        } else if (deliveryDateValue.equalsIgnoreCase("Date")) {
            TrukrApplication.alertDialog(getActivity(), IConstant.alert, "Please choose delivery date", 0);
        } else if (deliveryTimeValue.equalsIgnoreCase("Time")) {
            TrukrApplication.alertDialog(getActivity(), IConstant.alert, "Please choose delivery time", 0);
        } else
            getBookNow();
    }

    @Override
    public void onItemClick(View itemView, int position) {
    }

    @Override
    public void onSelectedItem(String value, String subvalue) {
        Log.i("OnSelected", "Called!!!");
    }

    public void getDistanceCalc() {
        TruckID = getTruckType();
        CalculateDistanceParams reqDisParams = new CalculateDistanceParams();
        preferences = mContext.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);
        reqDisParams.AuthToken = authToken;
        reqDisParams.UserId = userId;
        reqDisParams.UserType = IConstant.UserType;
        reqDisParams.FromLat = FromLat;
        reqDisParams.FromLong = FromLon;
        reqDisParams.ToLat = ToLat;
        reqDisParams.ToLong = ToLon;
        reqDisParams.TruckType = TruckID;
        reqDisParams.FromAddress = edittextsource.getText().toString().trim();
        reqDisParams.ToAddress = edittextdestination.getText().toString().trim();
        reqDisParams.PickupDate = btnPickUpDate.getText().toString().trim();
        reqDisParams.PickupTime = btnPickUpTime.getText().toString().trim();
        reqDisParams.DeliveryDate = btnDeliveryDate.getText().toString().trim();
        reqDisParams.DeliveryTime = btnDeliveryTime.getText().toString().trim();
        reqDisParams.OrderId = OrderId;

        if (additionalService.size() > 0) {
            for (int i = 0; i < additionalService.size(); i++) {
                if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                    //TODO
                    if (additionalService.get(i).Name.equals("Canada")) {
                        BorderCrossing = "1";
                    } else {
                        BorderCrossing = "2";
                    }
                } else if (additionalService.get(i).Id.equals("Hazmat charge")) {
                    if (additionalService.get(i).Name.equals("YES")) {
                        HazardousMaterial = "1";
                    } else {
                        HazardousMaterial = "0";
                    }
                } else if (additionalService.get(i).Id.equals("No Of Straps")) {
                    Loadstraps = additionalService.get(i).Name;
                } else if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                    if (additionalService.get(i).Name.equals("YES")) {
                        RedeliveryCharge = "1";
                    } else {
                        RedeliveryCharge = "0";
                    }
                } else if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                    StopsinTransit = additionalService.get(i).Name;
                } else if (additionalService.get(i).Id.equals("Team Service")) {
                    if (additionalService.get(i).Name.equals("YES")) {
                        TeamService = "1";
                    } else {
                        TeamService = "0";
                    }
                }
            }
        }
        CalculateDistanceParams.Accessories Accessories = reqDisParams.new Accessories();
        Accessories.BorderCrossing = BorderCrossing;
        Accessories.NoOfStraps = Loadstraps;
        Accessories.StopsinTransit = StopsinTransit;
        Accessories.HazardousMaterial = HazardousMaterial;
        Accessories.TeamService = TeamService;
        Accessories.RedeliveryCharge = RedeliveryCharge;
        reqDisParams.Accessories = Accessories;

        String inputParams = TrukrApplication.getToJSON(reqDisParams, null);
        Log.d("DistanceParams", inputParams);
        GetDistanceAmountAsync distanceAmountAsync = new GetDistanceAmountAsync(inputParams);
        distanceAmountAsync.execute();
        System.out.println("Authtoken" + reqDisParams.AuthToken);
        System.out.println("UserId" + reqDisParams.UserId);

    }

    private void getBookNow() {
        GetBookNowRequest reqParams = new GetBookNowRequest();
        if (borderCrossStatus.isChecked()) {
            borderStatus = "1";
        } else {
            borderStatus = "0";
        }
        preferences = mContext.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
        authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
        userId = preferences.getString("Userid", null);
        reqParams.AuthToken = authToken;
        reqParams.UserId = userId;
        reqParams.UserType = IConstant.UserType;

        GetBookNowRequest.Shipment shipment = reqParams.new Shipment();
        shipment.FromLatitude = FromLat;
        shipment.FromLongitude = FromLon;
        shipment.BorderCrossStatus = borderStatus;
        System.out.println("borderStatusValue---->" + borderStatus);
        shipment.ToLatitude = ToLat;
        shipment.ToLongitude = ToLon;
        shipment.TruckType = TruckID;
        shipment.DeliveryTime = deliveryDateValue + " " + btnDeliveryTime.getText().toString();
        shipment.PickupTime = pickupDateValue + " " + btnPickUpTime.getText().toString();
        shipment.FromAddress = edittextsource.getText().toString();
        shipment.Notes = notes.getText().toString();
        shipment.ToAddress = edittextdestination.getText().toString();
        reqParams.Shipment = shipment;

        if (additionalService.size() > 0) {
            for (int i = 0; i < additionalService.size(); i++) {
                if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                    //TODO
                    if (additionalService.get(i).Name.equals("Canada")) {
                        BorderCrossing = "1";
                    } else {
                        BorderCrossing = "2";
                    }
                } else if (additionalService.get(i).Id.equals("Hazmat charge")) {
                    if (additionalService.get(i).Name.equals("YES")) {
                        HazardousMaterial = "1";
                    } else {
                        HazardousMaterial = "0";
                    }
                } else if (additionalService.get(i).Id.equals("No Of Straps")) {
                    Loadstraps = additionalService.get(i).Name;
                } else if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                    if (additionalService.get(i).Name.equals("YES")) {
                        RedeliveryCharge = "1";
                    } else {
                        RedeliveryCharge = "0";
                    }
                } else if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                    StopsinTransit = additionalService.get(i).Name;
                } else if (additionalService.get(i).Id.equals("Team Service")) {
                    if (additionalService.get(i).Name.equals("YES")) {
                        TeamService = "1";
                    } else {
                        TeamService = "0";
                    }
                }
            }
        }
        GetBookNowRequest.Shipment.Accessories Accessories = reqParams.Shipment.new Accessories();
        Accessories.BorderCrossing = BorderCrossing;
        Accessories.HazardousMaterial = HazardousMaterial;
        Accessories.Loadstraps = Loadstraps;
        Accessories.RedeliveryCharge = RedeliveryCharge;
        Accessories.StopsinTransit = StopsinTransit;
        Accessories.TeamService = TeamService;
        reqParams.Shipment.Accessories = Accessories;

        String inputParams = TrukrApplication.getToJSON(reqParams, null);
        Log.d("CreateShipmentParams", inputParams);
        GetCreateShippmentAsync getCreateShippmentAsync = new GetCreateShippmentAsync(inputParams);
        getCreateShippmentAsync.execute();
    }

    public void addDynamicView(final String mAddLabel, final String mAddValue) {
        final View v = vi.inflate(R.layout.added_additional_item, null);
        // fill in any details dynamically here
        final TextView txtLabel = (TextView) v.findViewById(R.id.additionalitem_tv_servicelabel);
        TextView txtValue = (TextView) v.findViewById(R.id.additionalitem_tv_servicevalue);
        ImageView imageView = (ImageView) v.findViewById(R.id.additionalitem_iv_removeservice);
        txtLabel.setText(mAddLabel);
        txtValue.setText(mAddValue);
        add_service_values.addView(v);
        getDistanceCalc();

        // remove additional service value
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_service_values.removeView(v);
                TrukrApplication.getArrayList().add(mAddLabel);
                System.out.println("Remove Service" + mAddLabel);
                try {
                    for (int i = 0; i < additionalService.size(); i++) {
                        System.out.println("remove service" + additionalService.size());
                        if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Hazmat charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("No Of Straps")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Team Service")) {
                            additionalService.remove(i);
                        }

                        if (additionalService.get(i).Id.equals("Hazmat charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("No Of Straps")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Team Service")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                            additionalService.remove(i);
                        }

                        if (additionalService.get(i).Id.equals("No Of Straps")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Team Service")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Hazmat charge")) {
                            additionalService.remove(i);
                        }

                        if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Team Service")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Hazmat charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("No Of Straps")) {
                            additionalService.remove(i);
                        }

                        if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Team Service")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Hazmat charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("No Of Straps")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                            additionalService.remove(i);
                        }

                        if (additionalService.get(i).Id.equals("Team Service")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Border Crossing fee")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Hazmat charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("No Of Straps")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Redelivery Charge")) {
                            additionalService.remove(i);
                        } else if (additionalService.get(i).Id.equals("Stops-in-transit")) {
                            additionalService.remove(i);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                TruckID = getTruckType();
                CalculateDistanceParams reqDisParams = new CalculateDistanceParams();
                preferences = mContext.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
                authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
                userId = preferences.getString("Userid", null);
                reqDisParams.AuthToken = authToken;
                reqDisParams.UserId = userId;
                reqDisParams.UserType = IConstant.UserType;
                reqDisParams.FromLat = FromLat;
                reqDisParams.FromLong = FromLon;
                reqDisParams.ToLat = ToLat;
                reqDisParams.ToLong = ToLon;
                reqDisParams.TruckType = TruckID;
                reqDisParams.FromAddress = edittextsource.getText().toString().trim();
                reqDisParams.ToAddress = edittextdestination.getText().toString().trim();
                reqDisParams.PickupDate = btnPickUpDate.getText().toString().trim();
                reqDisParams.PickupTime = btnPickUpTime.getText().toString().trim();
                reqDisParams.DeliveryDate = btnDeliveryDate.getText().toString().trim();
                reqDisParams.DeliveryTime = btnDeliveryTime.getText().toString().trim();
                reqDisParams.OrderId = OrderId;

                if (mAddLabel.equals("Border Crossing fee")) {
                    BorderCrossing = "";
                } else if (mAddLabel.equals("Hazmat charge")) {
                    HazardousMaterial = "";
                } else if (mAddLabel.equals("No Of Straps")) {
                    Loadstraps = "";
                } else if (mAddLabel.equals("Redelivery Charge")) {
                    RedeliveryCharge = "";
                } else if (mAddLabel.equals("Stops-in-transit")) {
                    StopsinTransit = "";
                } else if (mAddLabel.equals("Team Service")) {
                    TeamService = "";
                }
                CalculateDistanceParams.Accessories Accessories = reqDisParams.new Accessories();
                Accessories.BorderCrossing = BorderCrossing;
                Accessories.NoOfStraps = Loadstraps;
                Accessories.StopsinTransit = StopsinTransit;
                Accessories.HazardousMaterial = HazardousMaterial;
                Accessories.TeamService = TeamService;
                Accessories.RedeliveryCharge = RedeliveryCharge;
                reqDisParams.Accessories = Accessories;

                String inputParams = TrukrApplication.getToJSON(reqDisParams, null);
                Log.d("DistanceParams", inputParams);
                GetDistanceAmountAsync distanceAmountAsync = new GetDistanceAmountAsync(inputParams);
                distanceAmountAsync.execute();

            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(R.id.container_body, fragment);
        ft.addToBackStack(backStateName);
        ft.commit();
    }

    public String getTruckType() {
        for (int i = 0; i < TruckList.size(); i++) {
            if (TruckList.get(i).Name.equals(btnTrucktype.getText().toString())) {
                Log.i("Truck Position : ", "" + TruckList.get(i).Id);
                return TruckList.get(i).Id;
            }
        }
        Log.i("TruckList Size::: ", "" + TruckList.size());
        return null;
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    public void alertDialog(final Context ctx, String Title, String Content, final int Status) {
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
        line.setVisibility(View.GONE);
        btnDialogCancel.setVisibility(View.GONE);
        btnDialog.setBackgroundResource(R.drawable.dialogbtnbackground);
        final Context context = getActivity();
        // if button is clicked, close the custom dialog
        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status == 100) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, RequestReceived.class);
                    intent.putExtra("OrderId", orderId);
                    context.startActivity(intent);
                } else if (Status == 104) {
                    dialog.dismiss();
                    Intent intent = new Intent(context, SlidingDrawer.class);
                    intent.putExtra("PaymentList", true);
                    context.startActivity(intent);
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public interface AdditionalServiceListener {
        void refereshAditionalServiceView();
    }

    public class GetTruckTypeAsync extends AsyncTask<Void, Void, Boolean> {
        boolean status;
        String responseParams = null;

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
            responseParams = TrukrWebServiceHelper.httpGetJsonData(IConstant.GetTruckType);
            Log.i("TruckTypeResponse", "" + responseParams);
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
                    JSONObject jsonRootObject = new JSONObject(responseParams);
                    JSONArray jsonArray = jsonRootObject.getJSONArray("Andriod");
                    TruckTypeModel truckTypeModel;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        truckTypeModel = new TruckTypeModel();
                        truckTypeModel.Id = jsonObject.getString("Id");
                        truckTypeModel.Name = jsonObject.getString("Name");
                        TruckList.add(truckTypeModel);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class GetDistanceAmountAsync extends AsyncTask<Void, Void, Boolean> {
        boolean status;
        String inputParams, responseParam = null;

        private GetDistanceAmountAsync(String inputParams) {
            this.inputParams = inputParams;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            responseParam = TrukrWebServiceHelper.httpPostJsonData(IConstant.GetCalculateDistance, inputParams);
            Log.i("GetDistanceResponse", "" + responseParam);
            if (responseParam != null)
                status = true;
            else
                status = false;
            return status;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            try {
                Log.i("Status", "" + aBoolean);
                if (aBoolean) {
                    JSONObject jsonObject = new JSONObject(responseParam);
                    System.out.println("Home class : GetDistanceAmountAsync : jsonObject = " + jsonObject);
                    String total = jsonObject.getString("CalculateAmount");
                    Float aFloat = Float.parseFloat(total);
                    float k = (float) Math.round(aFloat * 100) / 100;
                    textprice.setText("$" + k);
                    JSONArray shipment = jsonObject.getJSONArray("Accessories");
                    System.out.println("Home class : GetDistanceAmountAsync : shipment = " + shipment);
                }
            } catch (Exception e) {
            }

            btnViewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getDistanceCalc();
                    Intent intent = new Intent(mContext, ViewDetails.class);
                    intent.putExtra("jsonResponse", responseParam);
                    startActivity(intent);
                }
            });
        }
    }

    public class GetCreateShippmentAsync extends AsyncTask<Void, Void, Boolean> {
        boolean status;
        String responseParams = null;

        private GetCreateShippmentAsync(String responseParams) {
            this.responseParams = responseParams;
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
            responseParams = TrukrWebServiceHelper.httpPostJsonData(IConstant.CreateNewOrder, responseParams);
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
                    String message = jsonObject.getString("Message");
                    StatusCode = Integer.parseInt(String.valueOf(jsonObject.getString("StatusCode")));
                    if (StatusCode == 100) {
                        orderId = jsonObject.getString("OrderId");
                        alertDialog(getActivity(), IConstant.alert, "" + message, StatusCode);
                    } else if (StatusCode == 104 || StatusCode == 98) {
                        alertDialog(getActivity(), IConstant.alert, "" + message, StatusCode);
                    } else if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, message);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    private class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // safety check
            if (intent == null || intent.getAction() == null) {
                return;
            }
            if (intent.getAction().equals("SOME_ACTION")) {
                // doSomeAction();
                Log.i("Home", "Called!!!");
                getDistanceCalc();

            }
        }
    }


}