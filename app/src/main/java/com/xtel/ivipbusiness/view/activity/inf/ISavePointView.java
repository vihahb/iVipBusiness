package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.xtel.ivipbusiness.model.entity.RESP_Member_Info;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.io.File;

/**
 * Created by Vulcl on 3/3/2017
 */

public interface ISavePointView {

    void onGetDataSuccess(RESP_Member_Info resp_member_info);
    void onGetDataError();

    void onTakePictureGallary(Uri uri);
    void onTakePictureCamera(Bitmap bitmap);
    void onLoadPicture(File url);
    void onSavePointSuccess(String member_name, int total_point);
    void getNewSession(ICmd iCmd, Object... params);
    void onRequestError(Error error);

    void showProgressBar(boolean isTouchOutside, boolean isCancel, String title, String message);
    void closeProgressBar();
    void showShortToast(int type, String message);

    Activity getActivity();
}