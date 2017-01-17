package com.xtel.sdk.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lê Công Long Vũ on 12/27/2016
 */

public class Constants {
    public static final String SHARED_NAME = "share_name";
    public static final String SESSION = "session";
    public static final String CODE = "code";
    public static final String TYPE = "type";
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String SERVER_UPLOAD = "replace to server upload image";

    public static final String FCM_TOKEN = "fcm_token";


    public static long convertDataToLong(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date mDate = null;

        try {
            mDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mDate != null)
            return mDate.getTime();
        else
            return 0;
    }
}