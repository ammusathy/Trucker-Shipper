package com.trukr.shipper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.trukr.shipper.activity.Login;

public class GCMIntentService extends GCMBaseIntentService {

    private final String TAG = "GCMIntentService";
    NotificationCompat.Builder builderd = null;
    private String messages = "";

    public GCMIntentService() {
        super(BuildConfig.GCM_ID);
    }

    /* Method called on device registered*/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        //Log.i(TAG, "Device registered: regId = " + registrationId);
        CommonUtilities.displayMessage(context, "Your device registred with GCM");
    }

    /* Method called on device unregistred*/
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        //Log.i(TAG, "Device unregistered");
        CommonUtilities.displayMessage(context, getString(R.string.gcm_unregistered));
    }

    /* Method called on Receiving a new message**/
    @Override
    protected void onMessage(Context context, Intent intent) {
        String message = intent.getExtras().getString("message");
        String Msg = message;
        CommonUtilities.displayMessage(context, message);
        Log.i(TAG, "Received message : " + message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private void generateNotification(Context context, String message) {
        builderd = new NotificationCompat.Builder(GCMIntentService.this);
        int icon = R.mipmap.app_icon;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

     /*   if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builderd.setSmallIcon(R.mipmap.app_icon);
        } else {
            builderd.setSmallIcon(R.mipmap.app_icon);
        }*/

        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.app_name);
        Intent notificationIntent = null;

        notificationIntent = new Intent(context, Login.class);

        notificationIntent.putExtra("message", message);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, (int) System.nanoTime(), notificationIntent, 0);

        notification = builderd.setContentIntent(intent)
                .setSmallIcon(icon).setTicker(message).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setContentTitle(title)
                .setContentText(message).build();


        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify((int) System.nanoTime(), notification);
    }

    /* Method called on receiving a deleted message**/
    @Override
    protected void onDeletedMessages(Context context, int total) {
        //Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        CommonUtilities.displayMessage(context, message);
        // notifies user generateNotification(context, message);
    }

    /* Method called on Error*/
    @Override
    public void onError(Context context, String errorId) {
        //Log.i(TAG, "Received error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        //Log.i(TAG, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }
}
