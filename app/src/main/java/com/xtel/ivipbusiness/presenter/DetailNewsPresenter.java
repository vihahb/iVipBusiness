package com.xtel.ivipbusiness.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.NewsModel;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.ivipbusiness.model.entity.RESP_News;
import com.xtel.ivipbusiness.view.activity.inf.IUpdateNewsView;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.CallbackImageListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ImageManager;

import java.io.File;

/**
 * Created by Vulcl on 2/21/2017
 */

public class DetailNewsPresenter extends BasicPresenter {
    private IUpdateNewsView view;

    private int news_id = -1;
    private boolean isExists = true;

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(final Object... params) {
            if (params.length > 0) {
                int type = ((int) params[0]);
                if (type == 1) {
                    NewsModel.getInstance().getNewsInfo(news_id, new ResponseHandle<RESP_News>(RESP_News.class) {
                        @Override
                        public void onSuccess(RESP_News obj) {
                            if (isExists) {
                                view.onGetNewsInfoSuccess(obj);
                            }
                        }

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists)
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
                        }
                    });
                } else if (type == 2) {
                    NewsModel.getInstance().updateNews(news_id, (String) params[1], new ResponseHandle<RESP_None>(RESP_None.class) {
                        @Override
                        public void onSuccess(RESP_None obj) {
                            if (isExists)
                                view.onUpdateSuccess();
                        }

                        @Override
                        public void onSuccess() {
                            if (isExists)
                                view.onUpdateSuccess();
                        }

                        @Override
                        public void onError(Error error) {
                            if (isExists)
                                if (error.getCode() == 2)
                                    view.getNewSession(iCmd, params);
                                else
                                    view.onRequestError(error);
                        }
                    });
                }
            }
        }
    };

    public DetailNewsPresenter(IUpdateNewsView view) {
        this.view = view;
    }

    public void getData() {
        try {
            news_id = view.getActivity().getIntent().getIntExtra(Constants.MODEL, -1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (news_id == -1)
            view.onGetDataError();
        else
            iCmd.execute(1);
    }

    public void postImage(Bitmap bitmap) {
        ImageManager.getInstance().postImage(view.getActivity(), bitmap, true, new CallbackImageListener() {
            @Override
            public void onSuccess(RESP_Image resp_image, File file) {
                view.onLoadPicture(file);
            }

            @Override
            public void onError() {
                view.closeProgressBar();
                view.showShortToast(-1, view.getActivity().getString(R.string.error_try_again));
            }
        });
    }

    public void setExists(boolean isExists) {
        this.isExists = isExists;
    }
}
