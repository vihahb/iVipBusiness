package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.NewsModel;
import com.xtel.ivipbusiness.model.entity.RESP_List_News;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.fragment.inf.INewsView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 1/21/2017
 */

public class NewsPresenter {
    private INewsView view;

    private boolean isExists = true;

    private int PAGE = 1;
    private int PAGESIZE = 10;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                switch (((int) params[0])) {
                    case 1:
                        NewsModel.getInstance().getNews(PAGE, PAGESIZE, Constants.SORT_STORE.getId(), Constants.SORT_STORE.getStore_type(), new ResponseHandle<RESP_List_News>(RESP_List_News.class) {
                            @Override
                            public void onSuccess(RESP_List_News obj) {
                                if (isExists) {
                                    PAGE++;
                                    view.onGetNewsSuccess(obj.getData());
                                }
                            }

                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Error error) {
                                if (isExists) {
                                    if (error.getCode() == 2)
                                        view.getNewSession(iCmd, params);
                                    else
                                        view.onRequestError(error);
                                }
                            }
                        });
                        break;
                    case 2:
                        NewsModel.getInstance().deleteNews((int) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                            @Override
                            public void onSuccess(RESP_None obj) {
                                if (isExists)
                                    view.onDeleteNewsSuccess(((int) params[2]));
                            }

                            @Override
                            public void onSuccess() {
                                if (isExists)
                                    view.onDeleteNewsSuccess(((int) params[2]));
                            }

                            @Override
                            public void onError(Error error) {
                                if (isExists) {
                                    if (error.getCode() == 2)
                                        view.getNewSession(iCmd, params);
                                    else
                                        view.onRequestError(error);
                                }
                            }
                        });
                        break;
                    default:
                        break;
                }
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
        return Constants.SORT_STORE != null;

    }

    public void deleteNews(int id, int position) {
        iCmd.execute(2, id, position);
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }
}