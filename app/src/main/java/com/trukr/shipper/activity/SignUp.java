package com.trukr.shipper.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trukr.shipper.BuildConfig;
import com.trukr.shipper.R;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.components.CropImage;
import com.trukr.shipper.constants.Common;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.helper.TrukrWebServiceHelper;
import com.trukr.shipper.model.RequestParams.RegistrationBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SignUp extends Activity {

    private static final int _GaleryCode = 101;
    private static final int _CROP_FROM_CAMERA = 102;
    private static final int _CameraCode = 100;
    private Context mContext;
    private ProgressDialog pd;
    EditText editEmailId, editMobileNo, editPassword, editRetypePassword, editFirstName, editLastName, editCompanyName, editStreet, editCity, eidtState, editZipCode, editOfficeNo, editTaxId;
    CheckBox checkBoxTermsandConditions;
    TextView btnSubmit;
    Button btnBackToSignIn;
    String emailIdValue, mobileNoValue, passwordValue, retypePasswordValue, firstNameValue, lastNameValue, companyNameValue, streetValue, cityValue, stateValue, zipcodeValue, officeNoValue, taxIdValue, a, countryCode;
    Dialog dialog;
    int Success = 1, Alert = 0;
    ImageView profileimage;
    Dialog imageoptionDialog;
    String imageString = "";
    String userId;
    String mLastBeforeText;
    File mFileTemp;
    public Typeface Gibson_Light, HnThin, HnBold;

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();
        process();
    }

    public void init() {
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        Gibson_Light = Typeface.createFromAsset(getAssets(), "Gibson_Light.otf");
        mContext = SignUp.this;
        countryCode = Common.GetTeleCountryCode(mContext);

        editEmailId = (EditText) findViewById(R.id.signup_et_emailaddress);
        editMobileNo = (EditText) findViewById(R.id.signup_et_mobileno);
        editPassword = (EditText) findViewById(R.id.signup_et_password);
        editRetypePassword = (EditText) findViewById(R.id.signup_et_retypepassword);
        editFirstName = (EditText) findViewById(R.id.signup_et_firstname);
        editLastName = (EditText) findViewById(R.id.signup_et_lastname);
        editCompanyName = (EditText) findViewById(R.id.signup_et_companyname);
        editStreet = (EditText) findViewById(R.id.signup_et_streetname);
        editCity = (EditText) findViewById(R.id.signup_et_cityname);
        eidtState = (EditText) findViewById(R.id.signup_et_statename);
        editZipCode = (EditText) findViewById(R.id.signup_et_zipcode);
        editOfficeNo = (EditText) findViewById(R.id.signup_et_officeno);
        editTaxId = (EditText) findViewById(R.id.signup_et_taxid);
        btnSubmit = (TextView) findViewById(R.id.signup_btn_submit);
        btnBackToSignIn = (Button) findViewById(R.id.signup_btn_backtosignin);
        checkBoxTermsandConditions = (CheckBox) findViewById(R.id.signup_chk_termsandconditions);
        profileimage = (ImageView) findViewById(R.id.signup_iv_profileimage);

        editEmailId.setTypeface(HnThin);
        editMobileNo.setTypeface(HnThin);
        editPassword.setTypeface(HnThin);
        editRetypePassword.setTypeface(HnThin);
        editFirstName.setTypeface(HnThin);
        editLastName.setTypeface(HnThin);
        editCompanyName.setTypeface(HnThin);
        editStreet.setTypeface(HnThin);
        editCity.setTypeface(HnThin);
        eidtState.setTypeface(HnThin);
        editZipCode.setTypeface(HnThin);
        editOfficeNo.setTypeface(HnThin);
        editTaxId.setTypeface(HnThin);
        checkBoxTermsandConditions.setTypeface(HnThin);
        btnBackToSignIn.setTypeface(Gibson_Light);
        btnSubmit.setTypeface(Gibson_Light);

        btnSubmit.setBackgroundResource(R.drawable.btnbackground);
        btnSubmit.setClickable(false);

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), IConstant.TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getFilesDir(), IConstant.TEMP_PHOTO_FILE_NAME);
        }
    }

    public void process() {

        int maxLength;
        if (countryCode.length() == 2) {
            maxLength = 17;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editMobileNo.setFilters(fArray);
        } else if (countryCode.length() == 3) {
            maxLength = 19;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editMobileNo.setFilters(fArray);
        } else {
            maxLength = 20;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editMobileNo.setFilters(fArray);
        }
        editMobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mLastBeforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Common.formetText(mLastBeforeText, s, countryCode, editMobileNo);
            }
        });

        editOfficeNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mLastBeforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Common.formetText(mLastBeforeText, s, countryCode, editOfficeNo);

            }
        });
        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
                CustomImageDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailIdValue = editEmailId.getText().toString().trim();
                mobileNoValue = editMobileNo.getText().toString().trim();
                passwordValue = editPassword.getText().toString().trim();
                retypePasswordValue = editRetypePassword.getText().toString().trim();
                firstNameValue = editFirstName.getText().toString().trim();
                lastNameValue = editLastName.getText().toString().trim();
                companyNameValue = editCompanyName.getText().toString().trim();
                streetValue = editStreet.getText().toString().trim();
                cityValue = editCity.getText().toString().trim();
                stateValue = eidtState.getText().toString().trim();
                zipcodeValue = editZipCode.getText().toString().trim();
                officeNoValue = editOfficeNo.getText().toString().trim();
                taxIdValue = editTaxId.getText().toString().trim();

                if (emailIdValue.length() <= 0)
                    showAlertDialog("Please enter your email id", Alert);
                else if (!emailIdValue.matches(IConstant._EMAIL_PATTERN))
                    showAlertDialog("Please enter valid email id", Alert);
                else if (mobileNoValue.length() <= 0)
                    showAlertDialog("Please enter your mobile number", Alert);
                else if (mobileNoValue.length() < 17)
                    showAlertDialog("Mobile number should be 10 characters", Alert);
                else if (passwordValue.length() <= 0)
                    showAlertDialog("Please enter your password", Alert);
                else if (passwordValue.length() < 8)
                    showAlertDialog("Password should contain minimum 8 characters", Alert);
                else if (retypePasswordValue.length() <= 0)
                    showAlertDialog("Please retype your password", Alert);
                else if (retypePasswordValue.length() < 8)
                    showAlertDialog("Passwords does not match", Alert);
                else if (!passwordValue.equals(retypePasswordValue))
                    showAlertDialog("Mismatched password and retype password", Alert);
                else if (firstNameValue.length() <= 0)
                    showAlertDialog("Please enter your firstname", Alert);
                else if (lastNameValue.length() <= 0)
                    showAlertDialog("Please enter your lastname", Alert);
                else if (companyNameValue.length() <= 0)
                    showAlertDialog("Please enter your company name", Alert);
                else if (streetValue.length() <= 0)
                    showAlertDialog("Please enter your street", Alert);
                else if (cityValue.length() <= 0)
                    showAlertDialog("Please enter your city", Alert);
                else if (stateValue.length() <= 0)
                    showAlertDialog("Please enter your state", Alert);
                else if (zipcodeValue.length() <= 0)
                    showAlertDialog("Please enter your zipcode", Alert);
                else if (officeNoValue.length() <= 0)
                    showAlertDialog("Please enter your Office Number", Alert);
                else if (officeNoValue.length() < 17)
                    showAlertDialog("Office number should be 10 characters", Alert);
                else if (taxIdValue.length() <= 0)
                    showAlertDialog("Please enter your tax id", Alert);
                else if (!checkBoxTermsandConditions.isChecked()) {
                    showAlertDialog("Please agree to the terms & conditions", Alert);
                } else if (Constant.isConnectingToInternet(mContext))
                    setUserRegistrationParams();
                else
                    showAlertDialog(IConstant._ERR_NO_INTERNET_CONNECTION, Alert);
            }
        });

        checkBoxTermsandConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        checkBoxTermsandConditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //              btnSubmit.setBackgroundResource(R.drawable.redbtnbackground);
                    btnSubmit.setClickable(true);
                } else {
                    //              btnSubmit.setBackgroundResource(R.drawable.btnbackground);
                    btnSubmit.setClickable(false);
                }
            }
        });
        btnBackToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMethod();
            }
        });
        btnSubmit.setBackgroundResource(R.drawable.redbtnbackground);
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

    public void showAlert(String content, final int statusCode) {
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
                if (statusCode == Success) {
                    Intent i = new Intent(SignUp.this, Login.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                    finish();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void backMethod() {
        Intent intent = new Intent(mContext, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backMethod();
    }

    public void CustomImageDialog() {
        HnThin = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Thin.otf");
        HnBold = Typeface.createFromAsset(getAssets(), "HelveticaNeue-Bold.ttf");
        imageoptionDialog = new Dialog(mContext, R.style.Dialog);
        imageoptionDialog.setCancelable(false);
        imageoptionDialog.setContentView(R.layout.dialog_camera_or_gallery);
        TextView dialog_title = (TextView) imageoptionDialog.findViewById(R.id.tvalerthead);
        TextView camera = (TextView) imageoptionDialog.findViewById(R.id.btncamera);
        TextView gallery = (TextView) imageoptionDialog.findViewById(R.id.btngallery);
        Button cancel = (Button) imageoptionDialog.findViewById(R.id.btncancel);
        camera.setTypeface(HnThin);
        gallery.setTypeface(HnThin);
        cancel.setTypeface(HnBold);
        imageoptionDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageoptionDialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    mFileTemp = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileTemp));
                    startActivityForResult(intent, _CameraCode);
                } catch (ActivityNotFoundException e) {
                    Log.d("CreateAccount", "cannot take picture", e);
                }
                imageoptionDialog.dismiss();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, _GaleryCode);
                    imageoptionDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case _CameraCode:
                startCropImage();
                break;
            case _GaleryCode:
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage();
                } catch (Exception e) {
                    Log.e("CreateAccount", "Error while creating temp file", e);
                }
                break;
            case _CROP_FROM_CAMERA:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                try {
                    if (path == null) {
                        return;
                    } else {
                        Bitmap photo = BitmapFactory.decodeFile(mFileTemp.getPath());
                        profileimage.setImageBitmap(TrukrApplication.getRoundedCornerImage(photo));
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        imageString = Constant.encodeImage(stream.toByteArray());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void startCropImage() {
        Intent intent = new Intent(this, CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 5);
        intent.putExtra(CropImage.ASPECT_Y, 5);
        startActivityForResult(intent, _CROP_FROM_CAMERA);
    }

    private void setUserRegistrationParams() {
        RegistrationBean registrationBean = new RegistrationBean();
        registrationBean.setUserType(IConstant.UserType);
        RegistrationBean.Personal personal = registrationBean.new Personal();
        personal.setEmail(emailIdValue);
        personal.setMobile(mobileNoValue);
        personal.setPassword(passwordValue);
        personal.setFirstName(firstNameValue);
        personal.setLastName(lastNameValue);
        personal.setCompany(companyNameValue);
        personal.setStreet(streetValue);
        personal.setCity(cityValue);
        personal.setState(stateValue);
        personal.setZipcode(zipcodeValue);
        personal.setTaxId(taxIdValue);
        personal.setOfficeNumber(officeNoValue);
        personal.setProfilePicture(imageString);
        registrationBean.setPersonal(personal);

        String requestJsonString = TrukrApplication.getToJSON(registrationBean, null);
        Log.d("UserRegistrationInput", requestJsonString);
        new UserRegistraionAsync(requestJsonString).execute();
    }

    public class UserRegistraionAsync extends AsyncTask<Void, Void, Boolean> {
        boolean status;
        String inputParams, responseParams = null;

        private UserRegistraionAsync(String inputParams) {
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
            responseParams = TrukrWebServiceHelper.httpPostJsonData(IConstant.UserRegistration, inputParams);
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
            if (status) {
                try {
                    JSONObject jobj = new JSONObject(responseParams);
                    int StatusCode = Integer.parseInt(jobj.getString(BuildConfig.STATUS_CODE));
                    String message = jobj.getString(BuildConfig.RESPONSE_MSG);
                    if (StatusCode == BuildConfig.RESPONSE_SUCCESS) {
                        JSONObject userObj = jobj.getJSONObject("User");
                        userId = userObj.getString("UserId");
                        System.out.println("userid-->" + userId);
                        showAlert("Account created successfully ", Success);
                        SharedPreferences preferences = getSharedPreferences("Mobile", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("MobileNo", mobileNoValue);
                        editor.putString("UserId", userId);
                        editor.commit();
                    } else {
                        showAlert(message, Alert);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
              //  Toast.makeText(mContext, IConstant._ERR_SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }
}
