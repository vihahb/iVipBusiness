package com.xtel.ivipbusiness.presenter;

import android.util.Log;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.MemberModel;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.SavePointActivity;
import com.xtel.ivipbusiness.view.activity.inf.ICheckInUserView;
import com.xtel.ivipbusiness.model.entity.RESP_Member_Info;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Vulcl on 3/3/2017
 */

public class CheckInUserPresenter {
    private ICheckInUserView view;

    private SortStore sortStore;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = (int) params[0];

                if (type == 1) {
                    MemberModel.getInstance().getMemberInfo(sortStore.getId(), (String) params[1], new ResponseHandle<RESP_Member_Info>(RESP_Member_Info.class) {
                        @Override
                        public void onSuccess(RESP_Member_Info obj) {
                            obj.setStore_id(sortStore.getId());
                            obj.setUser_code((String) params[1]);

                            Log.e("getMemberInfo", JsonHelper.toJson(obj));

                            view.closeProgressBar();
                            view.startActivityAndFinish(SavePointActivity.class, Constants.MODEL, obj);
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, params);
                            else
                                view.onRequetServerError((String) params[1], error);
                        }
                    });
                } else if (type == 2) {
                    MemberModel.getInstance().createMemberCard(sortStore.getId(), (String) params[1], new ResponseHandle<RESP_Member_Info>(RESP_Member_Info.class) {
                        @Override
                        public void onSuccess(RESP_Member_Info obj) {
                            obj.setStore_id(sortStore.getId());
                            obj.setUser_code((String) params[1]);

                            Log.e("createMemberCard", JsonHelper.toJson(obj));

                            view.closeProgressBar();
                            view.startActivityAndFinish(SavePointActivity.class, Constants.MODEL, obj);
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, params);
                            else
                                view.onRequetServerError((String) params[1], error);
                        }
                    });
                }
            }
        }
    };

    public CheckInUserPresenter(ICheckInUserView view) {
        this.view = view;
    }

    public void getData() {
        try {
            sortStore = (SortStore) view.getActivity().getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sortStore == null)
            view.onGetDataError();
    }

    public void checkIn(String member_code) {
        if (!NetWorkInfo.isOnline(view.getActivity())) {
            view.showShortToast(view.getActivity().getString(R.string.error_no_internet));
            return;
        }

        iCmd.execute(1, member_code);
    }

    public void createMemberCard(String member_code) {
        iCmd.execute(2, member_code);
    }
}
