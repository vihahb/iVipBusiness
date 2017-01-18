package com.xtel.sdk.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

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

    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
    }

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