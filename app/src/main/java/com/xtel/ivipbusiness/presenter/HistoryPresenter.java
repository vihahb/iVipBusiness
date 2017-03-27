package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.Member;
import com.xtel.ivipbusiness.model.entity.RESP_History;
import com.xtel.ivipbusiness.view.activity.inf.IHistoryView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;

/**
 * Created by Vulcl on 1/19/2017
 */

public class HistoryPresenter {
    private IHistoryView view;

    private Member member;
    private int PAGE = 1;
    private int PAGE_SIZE = 10;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            StoresModel.getInstance().getHistoryInStore(member.getStore_id(), member.getMember_code(), PAGE, PAGE_SIZE, new ResponseHandle<RESP_History>(RESP_History.class) {
                @Override
                public void onSuccess(RESP_History obj) {
                    PAGE++;
                    view.onGetHistorySuccess(obj.getData());
                }

                @Override
                public void onSuccess() {}

                @Override
                public void onError(Error error) {
                    if (error.getCode() == 2)
                        view.getNewSession(iCmd);
                    else
                        view.onGetHistoryError(error);
                }
            });
        }
    };

    public HistoryPresenter(IHistoryView view) {
        this.view = view;
    }

    public void getMemberInfo() {
        try {
            member = (Member) view.getActivity().getIntent().getExtras().getSerializable(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (member != null) {
            view.onGetMemberSuccess(member);
        } else {
            view.onGetMemberError();
        }
    }

    public void getMemberHistory(boolean isClear) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        if (isClear)
            PAGE = 1;

        iCmd.execute();
    }
}