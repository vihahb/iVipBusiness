package com.xtel.sdk.callback;

import com.xtel.ivipbusiness.model.entity.RESP_Image;

/**
 * Created by Lê Công Long Vũ on 12/1/2016
 */

public interface CallbackImageListener {
    void onSuccess(RESP_Image resp_image);
    void onError();
}