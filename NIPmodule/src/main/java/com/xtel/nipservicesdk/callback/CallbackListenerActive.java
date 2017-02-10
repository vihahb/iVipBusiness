package com.xtel.nipservicesdk.callback;

import com.xtel.nipservicesdk.model.entity.Error;

/**
 * Created by Lê Công Long Vũ on 1/6/2017
 */

public interface CallbackListenerActive {

    void onSuccess();

    void onError(Error error);
}
