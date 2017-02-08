package com.xtel.ivipbusiness.view.fragment.inf;

import android.app.Activity;

import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.ivipbusiness.model.entity.Member;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/19/2017
 */

public interface IMemberView {

    void onLoadMore();
    void onGetMemberSuccess(ArrayList<Member> arrayList);
    void onGetMemberError(Error error);
    void onNoNetwork();
    Activity getActivity();
}
