package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.History;
import com.xtel.ivipbusiness.model.entity.Member;
import com.xtel.nipservice.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/19/2017
 */

public interface IHistoryView {

    void onGetMemberSuccess(Member member);
    void onGetMemberError();
    void onLoadMore();
    void onGetHistorySuccess(ArrayList<History> arrayList);
    void onGetHistoryError(Error error);
    void onNoNetwork();
    Activity getActivity();
}
