package com.xtel.ivipbusiness.presenter;

import android.location.Location;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.NewsModel;
import com.xtel.ivipbusiness.model.entity.RESP_Valid_Check_News;
import com.xtel.ivipbusiness.view.activity.inf.ICheckNewsView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 3/20/2017
 */

public class CheckNewsPresenter {
    protected ICheckNewsView view;

    protected String VOUCHER_CODE;

    protected ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                if (type == 1) {
                    NewsModel.getInstance().validCheckNews((String) params[1], new ResponseHandle<RESP_Valid_Check_News>(RESP_Valid_Check_News.class) {
                        @Override
                        public void onSuccess(RESP_Valid_Check_News obj) {
                            view.onValidCheckSuccess(obj);
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Error error) {
                            VOUCHER_CODE = null;

                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else
                                view.onRequestError(error);
                        }
                    });
                } else if (type == 2) {
                    NewsModel.getInstance().useVoucher(VOUCHER_CODE, (double) params[1], (double) params[2], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            view.onUseVoucherSuccess();
                        }

                        @Override
                        public void onSuccess() {
                            view.onUseVoucherSuccess();
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd);
                            else
                                view.onRequestError(error);
                        }
                    });
                }
            }
        }
    };

    public CheckNewsPresenter(ICheckNewsView view) {
        this.view = view;
    }

    private boolean checkInternet() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.showShortToast(view.getActivity().getString(R.string.error_no_internet));
            return false;
        }

        return true;
    }

    public void checkNews(String result) {
        if (checkInternet()) {
            iCmd.execute(1, result);
            VOUCHER_CODE = result;
        }
    }

    public void useVoucher(Location location) {
        if (checkInternet()) {
            iCmd.execute(2, location.getLatitude(), location.getLongitude());
        }
    }
}
