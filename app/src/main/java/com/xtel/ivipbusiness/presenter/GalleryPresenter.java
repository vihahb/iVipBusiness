package com.xtel.ivipbusiness.presenter;

import com.xtel.ivipbusiness.model.GalleryModel;
import com.xtel.ivipbusiness.model.entity.RESP_Gallery;
import com.xtel.ivipbusiness.view.fragment.inf.IGalleryView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class GalleryPresenter {
    private IGalleryView view;

    private boolean isExists = true;
    private int store_id = -1;

    private final String TYPE = "ALL";
    private int PAGE = 1;
    private int PAGESIZE = 10;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = ((int) params[0]);
                if (type == 1)
                    GalleryModel.getInstance().getListGallery(store_id, PAGE, PAGESIZE, new ResponseHandle<RESP_Gallery>(RESP_Gallery.class) {
                        @Override
                        public void onSuccess(RESP_Gallery obj) {
                            if (isExists) {
                                PAGE++;
                                view.onGetStoresSuccess(obj.getData());
                            }
                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists) {
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onGetStoresError(error);
                            }
                        }
                    });
                else if (type == 2)
                    GalleryModel.getInstance().deleteGallery((int) params[1], (int) params[2], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            view.onDeleteSuccess();
                        }

                        @Override
                        public void onError(Error error) {
                            if (error.getCode() == 2)
                                view.getNewSession(iCmd, params);
                            else
                                view.onRequestError(error);
                        }
                    });
            }
        }
    };

    public GalleryPresenter(IGalleryView view) {
        this.view = view;
    }

    public void getGallery(boolean isClear) {
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

    public void deleteGallery(int gallery_id) {
        iCmd.execute(2, store_id, gallery_id);
    }

    public boolean getData() {

        if (store_id != -1)
            return true;

        try {
            store_id = view.getFragment().getArguments().getInt(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (store_id != -1);
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }
}