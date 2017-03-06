package com.xtel.ivipbusiness.view.activity.inf;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.xtel.ivipbusiness.model.entity.Card;
import com.xtel.ivipbusiness.model.entity.RESP_Image;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/4/2017
 */

public interface IAddLevelView {

    void onGetDataSuccess();
    void onGetDataError();

    void onGetCardDefaultSuccess(ArrayList<Card> arrayList);
    void onTakePictureGallary(Uri uri);
    void onTakePictureCamera(Bitmap bitmap);
    void onPostImageSuccess(RESP_Image resp_image);

    void getNewSession(ICmd iCmd, Object... params);
    void onRequestError(Error error);

    void closeProgressBar();
    void showShortToast(String message);
    Activity getActivity();
}