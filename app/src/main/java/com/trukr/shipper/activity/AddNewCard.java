package com.trukr.shipper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.helper.TrukrWebServiceHelper;
import com.trukr.shipper.model.AddCardModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewCard extends Activity {


    private Context mContext;
    Button add;
    TextView btnBack, month;
    EditText cardnumber, editnameoncard;
    ProgressDialog pDialog;
    EditText cvv;
    String authToken, userId, monthvalue, cardnumbervalue, namecardvalue, cvvvalue;
    SharedPreferences preferences;
    int Success = 1, Alert = 0;
    Dialog dialog;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card);
        init();
        process();
    }

    public void init() {
        mContext = AddNewCard.this;
        btnBack = (TextView) findViewById(R.id.addcard_tv_back);
        add = (Button) findViewById(R.id.addcard_btn_addtocard);
        month = (TextView) findViewById(R.id.addcard_et_expirydate);
        cvv = (EditText) findViewById(R.id.addcard_et_cvv);
        cardnumber = (EditText) findViewById(R.id.addcard_et_number);
        editnameoncard = (EditText) findViewById(R.id.addcard_et_name);
        try {
            Intent iin = getIntent();
            Bundle b = iin.getExtras();
            if (b != null) {
                String cardNo = b.getString("CardNo");
                cardnumber.setText(cardNo);
                SharedPreferences preferences = getSharedPreferences("Card", MODE_PRIVATE);
                String expMonth = preferences.getString("ExpMonth", null);
                String expYear = preferences.getString("ExpYear", null);
                if (Integer.valueOf(String.valueOf(expMonth)) < 9) {
                    month.setText("0" + expMonth + "/" + expYear.toString());
                } else {
                    month.setText(expMonth + "/" + expYear.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void process() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });

        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                final Calendar c = Calendar.getInstance();
                int y = c.get(Calendar.YEAR);
                int m = c.get(Calendar.MONTH);
                int d = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog dp = new DatePickerDialog(AddNewCard.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                int d = getResources().getSystem().getIdentifier("day", null, null);
                                if (d != 0) {
                                    View dayPicker = findViewById(d);
                                    if (dayPicker != null) {
                                        //Set Day view visibility Off/Gone
                                        dayPicker.setVisibility(View.GONE);
                                    }
                                }

                                String erg = "";
                                erg = String.valueOf(monthOfYear + 1);
                                if (monthOfYear + 1 < 9) {
                                    erg = 0 + erg + "/" + year;
                                } else {
                                    erg += "/" + year;
                                }
                                ((TextView) month).setText(erg);
                            }

                        }, y, m, d);
                dp.setTitle("Calender");
                dp.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dp.getDatePicker().setCalendarViewShown(false);
                dp.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //        AddCard();
                namecardvalue = editnameoncard.getText().toString().trim();
                cardnumbervalue = cardnumber.getText().toString().trim();
                monthvalue = month.getText().toString().trim();
                cvvvalue = cvv.getText().toString().trim();

                if (namecardvalue.length() <= 0)
                    showAlertDialog("Please enter your card name", Alert);
                else if (cardnumbervalue.length() <= 0)
                    showAlertDialog("Please enter your card number", Alert);
                else if (!(cardnumbervalue.length() >= 12 && cardnumbervalue.length() <= 16
                        && cardnumbervalue.startsWith("4")))
                    showAlertDialog("Please enter your valid VISA card number", Alert);
                else if (monthvalue.length() <= 0)
                    showAlertDialog("Please enter the expiry date of card", Alert);
                else if (!monthvalue.matches(IConstant._EXPIRY_DATE))
                    showAlertDialog("Please enter the valid expiry date", Alert);
                else if (cvvvalue.length() <= 0)
                    showAlertDialog("Please enter cvv number", Alert);
                else if (!cvvvalue.matches(IConstant._CVV_PATTERN))
                    showAlertDialog("Please enter your valid CVV", Alert);
                else if (Constant.isConnectingToInternet(mContext))
                    AddCardDetails();
                else
                    TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
            }
        });
    }

    public class AddNewCardAsync extends AsyncTask<Void, Void, Boolean> {

        boolean status;
        String responseParams = null;
        String inputParams = null;

        private AddNewCardAsync(String inputParams) {
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
            responseParams = TrukrWebServiceHelper.httpPostJsonData(IConstant.AddCard, inputParams);
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

            Log.i("Status", "" + aBoolean);
            if (aBoolean) {
                try {
                    JSONObject jsonObject = new JSONObject(responseParams);
                    String message = jsonObject.getString("Message");
                    int statusCode = Integer.parseInt(String.valueOf(jsonObject.getString("StatusCode")));
                    if (statusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, message);
                    } else {
                        alertdialog(mContext, IConstant.alert, message, statusCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void AddCardDetails() {
        try {
            String months = month.getText().toString();
            String[] month = months.split("/");
            String mon = month[0];
            String Year = month[1];
            System.out.println("expiry date-->" + mon + Year);
            AddCardModel reqParams = new AddCardModel();
            preferences = mContext.getSharedPreferences("Trucker", Context.MODE_PRIVATE);// get shared preference and mode to be selected
            authToken = preferences.getString("authToken", null);//getting a userid and authtoken in login screen
            userId = preferences.getString("Userid", null);
            reqParams.AuthToken = authToken;
            reqParams.UserId = userId;
            reqParams.UserType = IConstant.UserType;
            reqParams.CardTypeId = "1";
            reqParams.IsDefault = "0";
            reqParams.CardNumber = cardnumber.getText().toString();
            reqParams.NameOnCard = editnameoncard.getText().toString();
            AddCardModel.ExpiryOn expiryOn = reqParams.new ExpiryOn();
            expiryOn.MM = mon;
            expiryOn.YY = Year;
            reqParams.ExpiryOn = expiryOn;
            String inputParams = TrukrApplication.getToJSON(reqParams, null);
            Log.d("DistanceParams", inputParams);
            System.out.println("UserId >" + userId);
            System.out.println("AuthToken >" + authToken);
            System.out.println("CardNumber >" + cardnumber);

            AddNewCardAsync GetCardDetailsAsync = new AddNewCardAsync(inputParams);
            GetCardDetailsAsync.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
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
                    Intent intent = new Intent(AddNewCard.this, PaymentList.class);
                    startActivity(intent);
                } else
                    dialog.dismiss();
            }
        });
        dialog.show();
    }
}





