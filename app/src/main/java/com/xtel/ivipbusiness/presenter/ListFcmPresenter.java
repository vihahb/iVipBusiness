package com.xtel.ivipbusiness.presenter;

import android.util.Log;

import com.xtel.ivipbusiness.model.FcmModel;
import com.xtel.ivipbusiness.model.entity.NotifyCodition;
import com.xtel.ivipbusiness.model.entity.RESP_Fcm;
import com.xtel.ivipbusiness.view.activity.inf.IListFcmView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Vulcl on 2/22/2017
 */

public class ListFcmPresenter {
    private IListFcmView view;

    private int news_d = -1;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                if (type == 1) {
                    FcmModel.getInstance().sendToPeople((String) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            view.onSendFcmSuccess();
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, params);
                            else if (error.getCode() == 201)
                                view.onNewsNotExists();
                            else
                                view.onRequestError(error);
                        }
                    });
                }
            }
        }
    };

    public ListFcmPresenter(IListFcmView view) {
        this.view = view;
    }

    public void getData() {
        try {
            news_d = view.getActivity().getIntent().getIntExtra(Constants.MODEL, - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (news_d == -1)
            view.onGetDataError();

        Log.e("ListFcmActivity", "null k " + news_d);
    }

    public void sendNotify(int notify_type, NotifyCodition notify_condition) {
        RESP_Fcm resp_fcm = new RESP_Fcm();

        resp_fcm.setNews_id(news_d);
        resp_fcm.setNotify_type(notify_type);
        resp_fcm.setBegin_time(Constants.getNowTime());
        resp_fcm.setNotify_condition(notify_condition);

        iCmd.execute(1, JsonHelper.toJson(resp_fcm));
    }
}