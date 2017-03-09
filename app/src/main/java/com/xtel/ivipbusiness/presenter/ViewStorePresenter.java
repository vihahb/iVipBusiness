package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.inf.IViewStoreView;
import com.xtel.sdk.commons.Constants;

/**
 * Created by Vulcl on 2/11/2017
 */

public class ViewStorePresenter {
    IViewStoreView view;

    public ViewStorePresenter(IViewStoreView view) {
        this.view = view;
    }

    public void getData() {
        if (Constants.SORT_STORE != null)
            view.onGetDataSuccess();
        else
            view.onGetDataError();
    }
}
