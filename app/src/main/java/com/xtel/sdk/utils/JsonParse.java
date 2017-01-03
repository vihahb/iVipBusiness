package com.xtel.sdk.utils;

import android.util.Log;
import android.view.View;

import org.json.JSONObject;

/**
 * Created by Lê Công Long Vũ on 11/9/2016
 */

public class JsonParse {

    public static boolean checkJsonObject(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            return true;
        } catch (Exception e) {
            Log.e("Loi_check_json", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String getCodeError(View view, int code, String content) {

        return content;
    }

    public static String getCodeMessage(int code, String content) {

        return content;
    }
}