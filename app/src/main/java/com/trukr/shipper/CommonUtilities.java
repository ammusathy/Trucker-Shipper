package com.trukr.shipper;

import android.content.Context;
import android.content.Intent;

import org.apache.commons.lang3.StringEscapeUtils;

public final class CommonUtilities {

    /* Tag used on log messages.*/
    public static final String TAG = "CommonUtilities";

    public static final String DISPLAY_MESSAGE_ACTION = "com.trukr.app.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }

    static String Display(String charUnicode) {
        return StringEscapeUtils.unescapeJava(charUnicode);
    }

    public static String RemoveWatch(String str) {
        if (str.length() > 0)
            str = str.substring(0, str.length() - 1);
        return str;
    }
}
