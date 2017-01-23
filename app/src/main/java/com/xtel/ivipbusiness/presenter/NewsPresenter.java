package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.NewsModel;
import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.ivipbusiness.view.fragment.inf.INewsView;
import com.xtel.nipservice.callback.ResponseHandle;
import com.xtel.nipservice.model.entity.Error;

/**
 * Created by Vulcl on 1/21/2017
 */

public class NewsPresenter {
    private INewsView view;

    public NewsPresenter(INewsView view) {
        this.view = view;
    }

    public void getNews() {
        NewsModel.getInstance().getNews(new ResponseHandle<RESP_News>(RESP_News.class) {
            @Override
            public void onSuccess(RESP_News obj) {
                view.onGetNewsSuccess(obj.getData());
            }

            @Override
            public void onError(Error error) {
                view.onGetNewError(error);
            }
        });
    }
}