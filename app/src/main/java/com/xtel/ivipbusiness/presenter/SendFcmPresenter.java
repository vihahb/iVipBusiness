package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.ivipbusiness.view.activity.inf.ISendFcmView;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Vulcl on 2/22/2017
 */

public class SendFcmPresenter {
    private ISendFcmView view;

    private RESP_News resp_news;

    public SendFcmPresenter(ISendFcmView view) {
        this.view = view;
    }

    public void getData() {
        try {
            resp_news = (RESP_News) view.getActivity().getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (resp_news == null)
            view.onGetDataError();
    }
}