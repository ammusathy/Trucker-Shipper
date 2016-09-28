package com.trukr.shipper;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.trukr.shipper.application.TrukrApplication;
import com.trukr.shipper.constants.Constant;

public class DialService extends Service {
    Handler h;
    Runnable r;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("ServiceStarted....","Started...");
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        h = new Handler();
        r = new Runnable() {
            public void run() {
                Log.d("Dial", "DService: " + Constant.DialCountService);
                if (Constant.DialCountService < 1) {
                    Intent lintent = new Intent("DialUpdate");
                    lintent.putExtra("message", "1");
                    h.removeCallbacks(r);
                    LocalBroadcastManager.getInstance(TrukrApplication.mContext).sendBroadcast(lintent);
                } else {
                    Constant.DialCount--;
                    h.postDelayed(r, 1000);
                }
            }
        };
        h.postDelayed(r, 1000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
} 
