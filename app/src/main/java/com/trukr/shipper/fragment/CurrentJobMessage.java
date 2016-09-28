package com.trukr.shipper.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.trukr.shipper.R;
import com.trukr.shipper.adapter.ChatAdapter;
import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.components.ImageLoadingUtils;
import com.trukr.shipper.constants.Constant;
import com.trukr.shipper.constants.IConstant;
import com.trukr.shipper.model.ResponseParams.ChatResponseParams;
import com.trukr.shipper.model.ResponseParams.GeneralResponseParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.trukr.shipper.constants.Constant.encodeImage;

/**
 * Created by nijamudhin on 5/27/2016.
 */
public class CurrentJobMessage extends Fragment {

    private final int _GaleryCode = 101;
    private final int _CROP_FROM_CAMERA = 102;
    private final int _CameraCode = 100;
    public Activity act;
    public File mFileTemp;
    EditText et_edit;
    View rootView;
    Context ctx;
    TextView send;
    ImageView camera;
    SharedPreferences pref, preference;
    RequestQueue queue;
    String chat_Message, Orderid;
    Dialog imageoptionDialog;
    String imageString_Camera = "", imageString_Gallery = "";
    ArrayList<ChatResponseParams> chatArrayList;
    ListView listView;
    int StatusCode;
    BitmapFactory.Options options;
    String selectedImagePath, filename1;
    Bitmap bitmap;
    ChatAdapter chatAdapter = null;
    String encodedstring;
    ChatResponseParams chatResponseParams;
    private ProgressDialog pd;
    private ImageLoadingUtils utils;
    private boolean side = false;

    public CurrentJobMessage() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preference = getActivity().getSharedPreferences("CurrentJobTabLayout", Context.MODE_PRIVATE);
        Orderid = preference.getString("Order", null);
        ctx = getActivity();
        rootView = inflater.inflate(R.layout.fragment_message, container, false);
        listView = (ListView) rootView.findViewById(R.id.chat_listview);
        send = (TextView) rootView.findViewById(R.id.txt_send);
        camera = (ImageView) rootView.findViewById(R.id.fragment_image_camera);
        et_edit = (EditText) rootView.findViewById(R.id.editText_message);
        receivechat();
        chatArrayList = new ArrayList<ChatResponseParams>();
        chatAdapter = new ChatAdapter(ctx, chatArrayList);
        listView.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
        utils = new ImageLoadingUtils(ctx);
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
            mFileTemp = new File(Environment.getExternalStorageDirectory(), IConstant.TEMP_PHOTO_FILE_NAME);
        else
            mFileTemp = new File(getActivity().getFilesDir(), IConstant.TEMP_PHOTO_FILE_NAME);

        et_edit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendchat();
                }
                return false;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((et_edit.getText().toString().length()) == 0) {
                    TrukrApplication.alertDialog(ctx, IConstant.alert, "Please enter a message");
                } else {
                    if (Constant.isConnectingToInternet(ctx)) {
                        sendchat();//Method Declaration
                    } else {
                        TrukrApplication.alertDialog(ctx, IConstant.alert, IConstant._ERR_NO_INTERNET_CONNECTION);
                    }

                    // add();
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
                CustomImageDialog();
            }
        });

        return rootView;
    }


    private void CustomImageDialog() {
        imageoptionDialog = new Dialog(ctx, R.style.Dialog);
        imageoptionDialog.setCancelable(false);
        imageoptionDialog.setContentView(R.layout.dialog_camera_or_gallery);

        TextView dialog_title = (TextView) imageoptionDialog.findViewById(R.id.tvalerthead);
        TextView camera = (TextView) imageoptionDialog.findViewById(R.id.btncamera);
        TextView gallery = (TextView) imageoptionDialog.findViewById(R.id.btngallery);

        Button cancel = (Button) imageoptionDialog.findViewById(R.id.btncancel);
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
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, _CameraCode);
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
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, _GaleryCode);
                imageoptionDialog.dismiss();
            }
        });
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), IConstant.SharedImage_Path);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IConstant.SharedImage_Path, "Oops! Failed create " + IConstant.SharedImage_Path + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == _CameraCode) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            /*} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "VID_" + timeStamp + ".mp4");*/
        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.d("OnActivity", "OnActivity");
            if (resultCode != getActivity().RESULT_OK)
                return;

            switch (requestCode) {

                case _CameraCode:
                    if (data.getData() == null) {
                        Uri tempUri = getImageUri(ctx, (Bitmap) data.getExtras().get("data"));
                        new ImageCompressionAsyncTask(ctx).execute(tempUri.toString());
                    } else
                        new ImageCompressionAsyncTask(ctx).execute(data.getDataString());
                    break;

                  /*  Bitmap bitmapImage = BitmapFactory.decodeFile(mFileTemp.getPath());
                    int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                    Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                    your_imageview.setImageBitmap(scaled);
                    try {
                        bitmap = BitmapFactory.decodeFile(mFileTemp.getPath());
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                        encodedstring = encodeImage(outputStream.toByteArray());
                        System.out.println("encodedString_Camera = " + encodedstring);
                        chatArrayList.remove(chatResponseParams);
                        sendchat();

                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }*/


                case _GaleryCode:
                    new ImageCompressionAsyncTask(ctx).execute(data.getDataString());

                  /*  try {
                        if (null == data)
                            return;
                        Uri selectedImageUri1 = data.getData();
                        selectedImagePath = ImageFilePath.getPath(getContext(), selectedImageUri1);
                        String path2 = selectedImagePath;//it contain your path of image..im using a temp string..
                        filename1 = path2.substring(path2.lastIndexOf("/") + 1);
                        try {
                            bitmap = BitmapFactory.decodeFile(selectedImagePath);
                            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream2);
                            encodedstring = encodeImage(byteArrayOutputStream2.toByteArray());
                            System.out.println("encodedString_Gallery = " + encodedstring);
                        } catch (Exception e) {

                        }
                        sendchat();

                    } catch (Exception e) {
                        Log.e("CreateAccount", "Error while creating temp file", e);
                    }*/

                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(), inImage, "Image_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    private void receivechat() {
        pref = getActivity().getSharedPreferences("Fragment", Context.MODE_PRIVATE);
        String userid = pref.getString("UserId", null);
        String authToken = pref.getString("AuthToken", null);
        Log.d("Test", userid + " : " + authToken + ":" + Orderid);
        queue = Volley.newRequestQueue(getActivity());
        JSONObject object = null;
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userid);
            object.put("AuthToken", authToken);
            object.put("OrderId", Orderid);
            Log.d("Testing", userid + ":" + authToken + ":" + Orderid + object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.ReceiveChat, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test_receive", response.toString());
                try {
                    String message = response.getString("Message");
                    int StatusCode = Integer.parseInt(response.getString("StatusCode"));
                    if (StatusCode == 97) {
                        TrukrApplication.unauthorisedAlertDialog(getActivity(), IConstant.alert, message);
                    }
                    System.out.println("chat message--->" + message + StatusCode);
                    JSONArray array = response.getJSONArray("Chat");
                    if (chatArrayList.size() > 0) {
                        chatArrayList.clear();

                    }

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cardObject = array.getJSONObject(i);
                        chatResponseParams = new ChatResponseParams();
                        chatResponseParams.ChatId = cardObject.optString("ChatId");
                        chatResponseParams.UserId = cardObject.optString("UserId");
                        chatResponseParams.message = cardObject.optString("message");
                        chatResponseParams.image = cardObject.optString("image");
                        chatResponseParams.Date = cardObject.optString("Date");
                        chatArrayList.add(chatResponseParams);
                    }
                    chatAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response: ", error.toString());
            }
        });
        // volley time out error
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private boolean sendchat() {
        pd = new ProgressDialog(ctx, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        pref = getActivity().getSharedPreferences("Fragment", Context.MODE_PRIVATE);
        String userid = pref.getString("UserId", null);
        String authToken = pref.getString("AuthToken", null);
        chat_Message = et_edit.getText().toString();
        Log.d("Test", userid + " : " + authToken + ":" + Orderid);
        queue = Volley.newRequestQueue(getActivity());
        JSONObject object = null;
        JSONObject chat = new JSONObject();
        try {
            object = new JSONObject();
            object.put("UserType", IConstant.UserType);
            object.put("UserId", userid);
            object.put("AuthToken", authToken);
            object.put("OrderId", Orderid);
            chat.put("Message", chat_Message);
            if (chat_Message.length() > 0) {
                encodedstring = "";
            } else if (encodedstring.length() > 0) {
                chat_Message = "";
            }
            chat.put("UserId", userid);
            object.put("Chat", chat);
            chat.put("Image", encodedstring);
            Log.d("TestingS", object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, IConstant.SendChat, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Test_send", response.toString());
                GeneralResponseParams responseData = (GeneralResponseParams) TrukrApplication.getFromJSON(response.toString(), GeneralResponseParams.class);
                StatusCode = Integer.parseInt(String.valueOf(responseData.getStatusCode()));
                String Message = responseData.getMessage();
                if (StatusCode == 100) {
                    et_edit.setText("");
                    receivechat();
                    InputMethodManager imm = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

                } else if (StatusCode == 97) {
                    TrukrApplication.unauthorisedAlertDialog(getActivity(), IConstant.alert, Message);
                }
                closeprogress();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Response: ", error.toString());
                closeprogress();
                String json;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    try {
                        json = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        Log.e("Error list-->", json);
                        try {
                            //      Parsing json object response response will be a json object
                            if (json != null) {
                                JSONObject jsonObject = new JSONObject(json);
                                String message = jsonObject.getString("message");
                                //    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
                                dlgAlert.setMessage(message);
                                dlgAlert.setTitle("Login ");
                                dlgAlert.setPositiveButton("Ok",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                //dismiss the dialog
                                                dialog.dismiss();
                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //  Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } catch (UnsupportedEncodingException e) {
                        Log.e("Error 111", e.getMessage());
                    }
                }
            }
        });
        // volley time out error
        request.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
        return true;
    }


    private void closeprogress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }

    class ImageCompressionAsyncTask extends AsyncTask<String, Void, String> {
        ProgressDialog pd;
        private Context context;

        public ImageCompressionAsyncTask(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // SevenClicksApplication.progressdialogpopup(context, IConstant.Loading);
            // SevenClicksApplication.customProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = compressImage(params[0]);
            return filePath;
        }

        public String compressImage(String imageUri) {
            String filename = "";
            try {
                String filePath = getRealPathFromURI(imageUri);
                Bitmap scaledBitmap = null;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

                int actualHeight = options.outHeight;
                int actualWidth = options.outWidth;
                float maxHeight = 816.0f;
                float maxWidth = 612.0f;
                float imgRatio = actualWidth / actualHeight;
                float maxRatio = maxWidth / maxHeight;

                if (actualHeight > maxHeight || actualWidth > maxWidth) {
                    if (imgRatio < maxRatio) {
                        imgRatio = maxHeight / actualHeight;
                        actualWidth = (int) (imgRatio * actualWidth);
                        actualHeight = (int) maxHeight;
                    } else if (imgRatio > maxRatio) {
                        imgRatio = maxWidth / actualWidth;
                        actualHeight = (int) (imgRatio * actualHeight);
                        actualWidth = (int) maxWidth;
                    } else {
                        actualHeight = (int) maxHeight;
                        actualWidth = (int) maxWidth;
                    }
                }

                options.inSampleSize = utils.calculateInSampleSize(options, actualWidth, actualHeight);
                options.inJustDecodeBounds = false;
                options.inDither = false;
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inTempStorage = new byte[16 * 1024];

                try {
                    bmp = BitmapFactory.decodeFile(filePath, options);
                } catch (OutOfMemoryError exception) {
                    exception.printStackTrace();
                }
                try {
                    scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError exception) {
                    exception.printStackTrace();
                }

                float ratioX = actualWidth / (float) options.outWidth;
                float ratioY = actualHeight / (float) options.outHeight;
                float middleX = actualWidth / 2.0f;
                float middleY = actualHeight / 2.0f;

                Matrix scaleMatrix = new Matrix();
                scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
                Canvas canvas = new Canvas(scaledBitmap);
                canvas.setMatrix(scaleMatrix);
                canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
                ExifInterface exif;
                try {
                    exif = new ExifInterface(filePath);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                    //Log.d("EXIF", "Exif: " + orientation);
                    Matrix matrix = new Matrix();
                    if (orientation == 6) {
                        matrix.postRotate(90);
                        //Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 3) {
                        matrix.postRotate(180);
                        //Log.d("EXIF", "Exif: " + orientation);
                    } else if (orientation == 8) {
                        matrix.postRotate(270);
                        //Log.d("EXIF", "Exif: " + orientation);
                    }
                    scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream out = null;
                filename = getFilename();
                try {
                    out = new FileOutputStream(filename);
                    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.d("Options", "options");
                e.printStackTrace();
            }
            return filename;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //Log.d("FilePath", result);
            try {
                if (result.length() > 0) {
                    Bitmap bitmap = BitmapFactory.decodeFile(result, options);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    encodedstring = Constant.encodeImage(stream.toByteArray());
                    Log.d("encodedstringhjhggug", encodedstring);
                    sendchat();

                }
            } catch (Exception e) {

            }
        }

        public String getFilename() {
            String uriSting = "";
            try {
                File file = new File(Environment.getExternalStorageDirectory().getPath(), IConstant.SharedImage_Path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                uriSting = (file.getAbsolutePath() + File.separator + System.currentTimeMillis() + IConstant.ImageFormat);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return uriSting;
        }
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
}
