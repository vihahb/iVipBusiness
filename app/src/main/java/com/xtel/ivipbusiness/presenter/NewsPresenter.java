package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.NewsModel;
import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.fragment.inf.INewsView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 1/21/2017
 */

public class NewsPresenter {
    private INewsView view;

    private boolean isExists = true;
    private SortStore sortStore;

    private int PAGE = 1;
    private int PAGESIZE = 10;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                if ((int) params[0] == 1)
                    NewsModel.getInstance().getNews(PAGE, PAGESIZE, sortStore.getId(), sortStore.getStore_type(), new ResponseHandle<RESP_News>(RESP_News.class) {
                        @Override
                        public void onSuccess(RESP_News obj) {
                            if (isExists) {
                                PAGE++;
                                view.onGetNewsSuccess(obj.getData());
                            }
                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, ((int) params[0]));
                                else
                                    view.onRequestError(error);
                            }
                        }
                    });
            }
        }
    };

    public NewsPresenter(INewsView view) {
        this.view = view;
    }

    public void getNews(boolean isClear) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (!getData())
            view.onGetDataError();

        if (isClear)
            PAGE = 1;

        iCmd.execute(1);
    }

    public boolean getData() {
        if (sortStore != null)
            return true;

        try {
            sortStore = (SortStore) view.getFragment().getArguments().getSerializable(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (sortStore != null);
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }
}