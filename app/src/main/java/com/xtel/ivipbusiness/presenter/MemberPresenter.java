package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.StoresModel;
import com.xtel.ivipbusiness.model.entity.RESP_Member;
import com.xtel.ivipbusiness.view.fragment.inf.IMemberView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class MemberPresenter {
    private IMemberView view;

    private boolean isExists = true;
    private final String STORE_TYPE = "STORE";

    private final String TYPE = "ALL";
    private int PAGE = 1;
    private int PAGESIZE = 10;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (((int) params[0]) == 1) {
                boolean store_type = (Constants.SORT_STORE.getStore_type().equals(STORE_TYPE));

                StoresModel.getInstance().getMemberCheckIn(Constants.SORT_STORE.getId(), store_type, PAGE, PAGESIZE, new ResponseHandle<RESP_Member>(RESP_Member.class) {
                    @Override
                    public void onSuccess(RESP_Member obj) {
                        if (isExists) {
                            PAGE ++;
                            view.onGetMemberSuccess(obj.getData());
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
                                view.onGetMemberError(error);
                        }
                    }
                });
            }
        }
    };

    public MemberPresenter(IMemberView view) {
        this.view = view;
    }

    public void getMember(boolean isClear) {
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
        if (Constants.SORT_STORE != null) {
            view.onGetDataSuccess(Constants.SORT_STORE.getId());
            return true;
        } else {
            view.onGetDataError();
            return false;
        }
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }
}