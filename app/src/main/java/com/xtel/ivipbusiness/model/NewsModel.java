package com.xtel.ivipbusiness.model;

import android.util.Log;

import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.nipservicesdk.LoginManager;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;
import com.xtel.nipservicesdk.utils.JsonHelper;

import java.util.ArrayList;

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

//        RESP_News resp_news = new RESP_News();
//        ArrayList<News> arrayList = new ArrayList<>();
//
//        for (int i = 1; i < 21; i++) {
//            News news = new News();
//
//            news.setId(i);
//            news.setTitle("Title " + i);
//            news.setDate_create(1483203600000L);
//            news.setBanner("http://cuonghungthinh.com/imagesup/banner%20coffee.png");
//            if (i % 2 == 0)
//                news.setPublic(false);
//            else
//                news.setPublic(true);
//
//            arrayList.add(news);
//        }
//
//        resp_news.setData(arrayList);
//
//        responseHandle.onSuccess(JsonHelper.toJson(resp_news));
    }
}