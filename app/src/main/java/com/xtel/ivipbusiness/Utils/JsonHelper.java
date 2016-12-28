package com.xtel.ivipbusiness.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by vulcl on 12/2/2016
 */

public class JsonHelper {
    private static Gson gson;

    static {
        gson = new GsonBuilder().serializeNulls().excludeFieldsWithoutExposeAnnotation().create();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T getObject(String json, Class<T> clazz) throws NullPointerException {
        if (json == null || json.isEmpty())
            throw new NullPointerException("INPUT IS NULL OR EMPTY");
        else
            return gson.fromJson(json, clazz);
    }

    public static <T> T getObjectNoException(String json, Class<T> clazz) {
        if (json == null || json.isEmpty())
            return null;

        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }
}