package com.xtel.ivipbusiness.model;

import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.utils.JsonHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/21/2017
 */

public class NewsModel {
    private static NewsModel instance;

    public static NewsModel getInstance() {
        if (instance == null)
            instance = new NewsModel();
        return instance;
    }

    public void getNews(ResponseHandle responseHandle) {
        RESP_News resp_news = new RESP_News();
        ArrayList<News> arrayList = new ArrayList<>();

        for (int i = 1; i < 21; i++) {
            News news = new News();

            news.setId(i);
            news.setTitle("Title " + i);
            news.setDate_create(1483203600000L);
            news.setBanner("http://3.bp.blogspot.com/-fGedPqEK7VA/USrzhrrrPAI/AAAAAAAAGrw/aywfic1mJYY/s1600/mobile_game_banner_jp.naver.lineplay.android.jpeg");

            arrayList.add(news);
        }

        resp_news.setData(arrayList);

        responseHandle.onSuccess(JsonHelper.toJson(resp_news));
    }
}