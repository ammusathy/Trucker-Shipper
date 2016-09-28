package com.trukr.shipper.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;
import com.trukr.shipper.R;
import com.trukr.shipper.activity.ChangePassword;
import com.trukr.shipper.activity.Login;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.components.CropImage;
import com.trukr.shipper.components.InternalStorageContentProvider;
import com.trukr.shipper.components.RoundedImageView;
import com.trukr.shipper.constants.Common;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.LoginResponseParams;
import com.trukr.shipper.model.ResponseParams.ProfileDetailsResponseParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class Settings extends Fragment {

    View rootView;
    private Context mContext;
    private Dialog imageoptionDialog, dialog;
    private static final Context CONTEXT = null;
    private static final int _GaleryCode = 101;
    private static final int _CameraCode = 100;
    private static final int _CROP_FROM_CAMERA = 102;
    private static EditText editTextEmail, editTextMobileNo, editTextFirstName, editTextLastName, editCompanyName, editStreet, editCity, editState, editZipCode, editOfficeNo, editTaxId;
    private static String authToken, userId, mLastBeforeText, imageString = "";
    private Typeface Gibson_Light, HnThin, HnLight, HnBold;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    private String email, mobileNumber, firstName, lastName, companyName, streetName, cityName, stateName, zipCode, officeNumber, taxId, countryCode;
    private Button btnDelete, btnChangePassword;
    private static RoundedImageView profileImage;
    private RequestQueue queue;
    private SharedPreferences preference;
    private File mFileTemp;
    private ProgressDialog pd;
    private int StatusCode;

    public Settings() {
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.settings, container, false);

        mContext = getActivity();
        init();
        process();
        if (Constant.isConnectingToInternet(mContext)) {
            getProfileInfo();//Method declaration
        } else  {
            TrukrApplication.alertDialog(mContext, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
        }

        return rootView;
    }

    public void init() {
        HnThin = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Thin.otf");
        countryCode = Common.GetTeleCountryCode(mContext);

        editTextEmail = (EditText) rootView.findViewById(R.id.editemailaddress);
        editTextMobileNo = (EditText) rootView.findViewById(R.id.editmobileno);
        editTextFirstName = (EditText) rootView.findViewById(R.id.editfirstname);
        editTextLastName = (EditText) rootView.findViewById(R.id.editlastname);
        editCompanyName = (EditText) rootView.findViewById(R.id.editcompanyname);
        editStreet = (EditText) rootView.findViewById(R.id.editstreet);
        editCity = (EditText) rootView.findViewById(R.id.editcityname);
        editState = (EditText) rootView.findViewById(R.id.editstatename);
        editZipCode = (EditText) rootView.findViewById(R.id.editzipcode);
        editOfficeNo = (EditText) rootView.findViewById(R.id.editofficeNo);
        editTaxId = (EditText) rootView.findViewById(R.id.edittaxid);
        profileImage = (RoundedImageView) rootView.findViewById(R.id.setting_profileimage);
        btnDelete = (Button) rootView.findViewById(R.id.btndeleteaccount);
        btnChangePassword = (Button) rootView.findViewById(R.id.btnchangepassword);

        int maxLength;
        if (countryCode.length() == 2) {
            maxLength = 17;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editTextMobileNo.setFilters(fArray);
        } else if (countryCode.length() == 3) {
            maxLength = 19;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editTextMobileNo.setFilters(fArray);
        } else {
            maxLength = 20;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            editTextMobileNo.setFilters(fArray);
        }

        TrukrApplication.setHelveticaNeue_Thin(editTextEmail);
        TrukrApplication.setHelveticaNeue_Thin(editTextMobileNo);
        TrukrApplication.setHelveticaNeue_Thin(editTextFirstName);
        TrukrApplication.setHelveticaNeue_Thin(editTextLastName);
        TrukrApplication.setHelveticaNeue_Thin(editStreet);
        TrukrApplication.setHelveticaNeue_Thin(editState);
        TrukrApplication.setHelveticaNeue_Thin(editCity);
        TrukrApplication.setHelveticaNeue_Thin(editCompanyName);
        TrukrApplication.setHelveticaNeue_Thin(editZipCode);
        TrukrApplication.setHelveticaNeue_Thin(editOfficeNo);
        TrukrApplication.setHelveticaNeue_Thin(editTaxId);
        TrukrApplication.setGibson_Light(btnDelete);
        TrukrApplication.setGibson_Light(btnChangePassword);

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mFileTemp = new File(Environment.getExternalStorageDirectory(), Settings.TEMP_PHOTO_FILE_NAME);
        } else {
            mFileTemp = new File(getActivity().getFilesDir(), Settings.TEMP_PHOTO_FILE_NAME);
        }

        if (Constant.settingEditFlag == 1) {
            editTextMobileNo.setEnabled(true);
            editTextFirstName.setEnabled(true);
            editTextLastName.setEnabled(true);
            editCompanyName.setEnabled(true);
            editStreet.setEnabled(true);
            editCity.setEnabled(true);
            editState.setEnabled(true);
            editZipCode.setEnabled(true);
            editOfficeNo.setEnabled(true);
            editTaxId.setEnabled(true);
            profileImage.setEnabled(true);

            editTextMobileNo.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    mLastBeforeText = s.toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Common.formetText(mLastBeforeText, s, countryCode, editTextMobileNo);

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


        } else {
            editTextEmail.setEnabled(false);
            editTextMobileNo.setEnabled(false);
            editTextFirstName.setEnabled(false);
            editTextLastName.setEnabled(false);
            editCompanyName.setEnabled(false);
            editStreet.setEnabled(false);
            editCity.setEnabled(false);
            editState.setEnabled(false);
            editZipCode.setEnabled(false);
            editOfficeNo.setEnabled(false);
            editTaxId.setEnabled(false);
            profileImage.setEnabled(false);
        }
    }

    public void process() {

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChangePassword.class);
                intent.putExtra("Type", 0);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomImageDialog();
            }

            private void CustomImageDialog() {
                imageoptionDialog = new Dialog(getActivity(), R.style.Dialog);
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
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            //takePictureButton.setEnabled(false);
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                        }
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        try {
                            Uri mImageCaptureUri = null;
                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state))
                                mImageCaptureUri = Uri.fromFile(mFileTemp);
                            else
                                mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
                            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                            intent.putExtra("return-data", true);
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
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, _GaleryCode);
                        imageoptionDialog.dismiss();
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog(mContext, IConstant.delete, "Are you sure to delete?", StatusCode);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_OK) return;
        switch (requestCode) {
            case _CameraCode:
                startCropImage();
                break;
            case _GaleryCode:
                try {
                    InputStream inputStream = getActivity().getContentResolver().openInputStream(data.getData());
                    FileOutputStream fileOutputStream = new FileOutputStream(mFileTemp);
                    copyStream(inputStream, fileOutputStream);
                    fileOutputStream.close();
                    inputStream.close();
                    startCropImage();
                } catch (Exception e) {
                    Log.e("CreateAccount gallery", "Error while creating temp file", e);
                }
                break;
            case _CROP_FROM_CAMERA:
                String path = data.getStringExtra(CropImage.IMAGE_PATH);
                if (path == null) {
                    return;
                } else {
                    Bitmap photo = BitmapFactory.decodeFile(mFileTemp.getPath());
                    profileImage.setImageBitmap(TrukrApplication.getRoundedCornerImage(photo));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    imageString = Constant.encodeImage(stream.toByteArray());
                    Constant.profilePic = imageString;
                    System.out.println("imageString = " + imageString);
                }
                break;
        }
    }

    private void startCropImage() {
        Intent intent = new Intent(getActivity(), CropImage.class);
        intent.putExtra(CropImage.IMAGE_PATH, mFileTemp.getPath());
        intent.putExtra(CropImage.SCALE, true);
        intent.putExtra(CropImage.ASPECT_X, 5);
        intent.putExtra(CropImage.ASPECT_Y, 5);
        startActivityForResult(intent, _CROP_FROM_CAMERA);
    }

    public void getProfileInfo() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preference = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);
        authToken = preference.getString("authToken", null);
        userId = preference.getString("Userid", null);
        Log.d("ProfileResponse", authToken + ":" + userId + IConstant.UserType);
        queue = Volley.newRequestQueue(getActivity());
        //    String url = "http://softwaredevelopersusa.com/ws-get-profile";
        JSONObject object = null;

        try {
            object = new JSONObject();
            object.put("AuthToken", authToken);
            object.put("UserId", userId);
            object.put("UserType", IConstant.UserType);
            Log.d("response123", authToken + ":" + userId + IConstant.UserType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.GetProfile, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    ProfileDetailsResponseParams mProfileResponse;
                    Gson gson = new Gson();
                    mProfileResponse = gson.fromJson(response.toString(), ProfileDetailsResponseParams.class);
                    Log.d("Get profile info >> ", response.toString());
                    try {
                        int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                        String Message = response.getString("Message");
                        if (StatusCode == 97) {
                            TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, Message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    editStreet.setText(mProfileResponse.getPersonal().getStreet());
                    editTextEmail.setText(mProfileResponse.getPersonal().getEmail());
                    editTextMobileNo.setText(mProfileResponse.getPersonal().getMobileNumber());
                    editTextFirstName.setText(mProfileResponse.getPersonal().getFirstname());
                    editTextLastName.setText(mProfileResponse.getPersonal().getLastname());
                    editCompanyName.setText(mProfileResponse.getPersonal().getCompany());
                    editCity.setText(mProfileResponse.getPersonal().getCity());
                    editState.setText(mProfileResponse.getPersonal().getState());
                    editOfficeNo.setText(mProfileResponse.getPersonal().getOfficeNumber());
                    editZipCode.setText(mProfileResponse.getPersonal().getZipcode());
                    editTaxId.setText(mProfileResponse.getPersonal().getTaxId());

                    try {

                        if (mProfileResponse.getPersonal().getProfilePicture() != "" || mProfileResponse.getPersonal().getProfilePicture() != null) {
                            Picasso.with(getActivity())
                                    .load(mProfileResponse.getPersonal().getProfilePicture())
                                    /*.placeholder(R.drawable.icon_user)
                                    .error(R.drawable.icon_user)*/
                                    .into(profileImage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject personal = jsonObject.getJSONObject("Personal");
                        email = personal.getString("Email");
                        firstName = personal.getString("Firstname");
                        lastName = personal.getString("Lastname");
                        mobileNumber = personal.getString("MobileNumber");
                        companyName = personal.getString("Company");
                        stateName = personal.getString("State");
                        cityName = personal.getString("City");
                        streetName = personal.getString("Street");
                        zipCode = personal.getString("Zipcode");
                        taxId = personal.getString("TaxId");
                        officeNumber = personal.getString("OfficeNumber");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                closeprogress();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                Log.d("ErrorResponse", error.toString());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error password-->", json);
                        try {
                            if (json != null) {

                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                             //   Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext);
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static void getValuesFromUI() {
        Constant.email = editTextEmail.getText().toString();
        Constant.mobileNumber = editTextMobileNo.getText().toString();
        Constant.firstName = editTextFirstName.getText().toString();
        Constant.lastName = editTextLastName.getText().toString();
        Constant.companyName = editCompanyName.getText().toString();
        Constant.streetName = editStreet.getText().toString();
        Constant.cityName = editCity.getText().toString();
        Constant.stateName = editState.getText().toString();
        Constant.zipCode = editZipCode.getText().toString();
        Constant.officeNumber = editOfficeNo.getText().toString();
        Constant.taxId = editTaxId.getText().toString();
        Constant.profilePic = imageString;
        Constant.countryCode = "Contry Code";
    }

    private void delete() {
        pd = new ProgressDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        preference = getActivity().getSharedPreferences("Trucker", Context.MODE_PRIVATE);
        final String userId = preference.getString("Userid", null);
        final String authToken = preference.getString("authToken", null);
        Log.d("Test", userId + " : " + authToken);
        queue = Volley.newRequestQueue(getActivity());
        //      String url = "http://softwaredevelopersusa.com/ws-delete-account";
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("AuthToken", authToken);
            object.put("UserId", userId);
            Log.d("Test123", userId + " : " + authToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.DeleteAccount, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response:", response.toString());
                try {
                    LoginResponseParams responseData = (LoginResponseParams) TrukrApplication.getFromJSON(response.toString(), LoginResponseParams.class);
                    StatusCode = Integer.parseInt(responseData.getStatusCode());
                    String message = responseData.getMessage();
                    closeprogress();
                    if (StatusCode == 100) {
                        showAlertDialog(message, StatusCode);
                    } else if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(mContext, IConstant.alert, message);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                closeprogress();
                Log.d("Response: ", error.toString());
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error login-->", json);
                        try {
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                                //Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void showAlertDialog(String content, final int statusCode) {
        dialog = new Dialog(mContext, R.style.Dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alertdialog);

        TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
        alertHead.setText(mContext.getResources().getString(R.string.alert));
        TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
        alertContent.setText(content);

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
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
            }
        });
        dialog.show();
    }


    public void alertDialog(final Context mContext, String Title, String Content, final int StatusCode) {
        try {
            final Dialog dialog = new Dialog(getContext(), R.style.Dialog);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_alertdialog);
            TextView alertHead = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alerthead);
            alertHead.setText(Title);
            TextView alertContent = (TextView) dialog.findViewById(R.id.custom_alertdialog_tv_alertcontent);
            alertContent.setText(Content);
            View line = (View) dialog.findViewById(R.id.centerLineDialog);
            Button btnDialogCancel = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_cancel);
            Button btnDialogOk = (Button) dialog.findViewById(R.id.custom_alertdialog_btn_ok);
            TrukrApplication.setHelveticaNeue_Bold(alertHead);
            TrukrApplication.setHelveticaNeue_Light(alertContent);
            TrukrApplication.setHelveticaNeue_Bold(btnDialogCancel);
            TrukrApplication.setHelveticaNeue_Bold(btnDialogOk);
            btnDialogOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete();
                    dialog.dismiss();
                }
            });
            btnDialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

}
