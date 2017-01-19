package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;

import com.xtel.ivipbusiness.model.entity.History;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/19/2017
 */

public interface IHistoryView {

    void onGetHistorySuccess(ArrayList<History> arrayList);
    void onGetHistoryError(Error error);
    void onNoNetwork();
    Activity getActivity();
}
