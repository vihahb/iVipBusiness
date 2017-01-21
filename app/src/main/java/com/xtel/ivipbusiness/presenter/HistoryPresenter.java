package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.MemberModel;
import com.xtel.ivipbusiness.model.entity.RESP_History;
import com.xtel.ivipbusiness.view.activity.inf.IHistoryView;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.ivipbusiness.model.entity.Member;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 1/19/2017
 */

public class HistoryPresenter {
    private IHistoryView view;

    public HistoryPresenter(IHistoryView view) {
        this.view = view;
    }

    public void getMemberInfo() {
        Member member = null;

        try {
            member = (Member) view.getActivity().getIntent().getExtras().getSerializable(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (member != null) {

        }
    }

    public void getMemberHistory() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        MemberModel.getInstance().getHistoryById(new ResponseHandle<RESP_History>(RESP_History.class) {
            @Override
            public void onSuccess(RESP_History obj) {
                view.onGetHistorySuccess(obj.getData());
            }

            @Override
            public void onError(Error error) {
                view.onGetHistoryError(error);
            }
        });
    }
}