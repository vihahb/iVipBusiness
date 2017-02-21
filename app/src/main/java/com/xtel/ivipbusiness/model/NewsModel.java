package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;

/**
 * Created by Vulcl on 1/21/2017
 */

public class NewsModel extends BasicModel {
    private static NewsModel instance;

    public static NewsModel getInstance() {
        if (instance == null)
            instance = new NewsModel();
        return instance;
    }

    public void getNews(int page, int pagesize, int id, String type, ResponseHandle responseHandle) {
        String url = API_BASE + GET_NEWS_PAGE + page + GET_NEWS_PAGESISE + pagesize + GET_NEWS_ID + id + GET_NEWS_TYPE + type;
        String session = LoginManager.getCurrentSession();

        Log.e("getNews", url + "     " + session);
        requestServer.getApi(url, session, responseHandle);
    }

    public void addNews(String jsonObject, ResponseHandle responseHandle) {
        String url = API_BASE + ADD_NEWS;
        String session = LoginManager.getCurrentSession();

        Log.e("getNews", url + "     " + session);
        Log.e("getNews", "Object " + jsonObject);
        requestServer.postApi(url, jsonObject, session, responseHandle);
    }
}