package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.MemberModel;
import com.xtel.ivipbusiness.model.entity.RESP_Member;
import com.xtel.ivipbusiness.view.activity.inf.IMemberView;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 1/19/2017
 */

public class MemberPresenter {
    private IMemberView view;

    public MemberPresenter(IMemberView view) {
        this.view = view;
    }

    public void getMember() {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.onNoNetwork();
            return;
        }

        MemberModel.getInstance().getListMember(new ResponseHandle<RESP_Member>(RESP_Member.class) {
            @Override
            public void onSuccess(RESP_Member obj) {
                view.onGetMemberSuccess(obj.getData());
            }

            @Override
            public void onError(Error error) {
                view.onGetMemberError(error);
            }
        });
    }
}