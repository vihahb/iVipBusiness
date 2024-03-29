package com.xtel.sdk.commons;

import com.xtel.ivipbusiness.model.entity.SortStore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Lê Công Long Vũ on 12/27/2016
 */

public class Constants {
    public static final String SHARED_NAME = "share_business";
    public static final String FCM_TOKEN = "fcm_token";

    public static final String TYPE = "type";
    public static final String URI = "uri";
    public static final String BITMAP = "bitmap";

    public static final String SERVER_PATH = "server_path";
    public static final String FILE = "file";

    public static final String MODEL = "model";
    public static SortStore SORT_STORE = null;
    public static final String ID = "id";

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
            return mDate.getTime()/1000;
        else
            return 0;
    }

    public static long convertTimeToLong(String time) {
        String[] mTime = time.split(":");
        int hour = Integer.parseInt(mTime[0]);
        int minute = Integer.parseInt(mTime[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1, hour, minute);

        return (calendar.getTimeInMillis() / 1000);
    }

    public static long convertTimeToLong(int hour, int minute) {
//        String[] mTime = time.split(":");
//        int hour = Integer.parseInt(mTime[0]);
//        int minute = Integer.parseInt(mTime[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 1, 1, hour, minute);

        return (calendar.getTimeInMillis() / 1000);
    }

    public static long getNowTime() {
        return (System.currentTimeMillis() / 1000);
    }
}