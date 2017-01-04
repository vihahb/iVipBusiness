package com.xtel.sdk.commons;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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


    public static String convertDate(String date) {
        String newData[] = date.split("/");
        return newData[2] + "/" + newData[1] + "/" + newData[0];
    }

    @SuppressLint("SimpleDateFormat")
    public static String convertDataTime(String dateTime) {
        try {
            DateFormat defaultFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            Date date = defaultFormat.parse(dateTime);
            SimpleDateFormat newFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
            return newFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}