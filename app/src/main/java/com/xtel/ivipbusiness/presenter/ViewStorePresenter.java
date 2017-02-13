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
        SortStore sortStore = null;
        try {
            sortStore = (SortStore) view.getActivity().getIntent().getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sortStore != null)
            view.onGetDataSuccess(sortStore);
        else
            view.onGetDataError();
    }
}
