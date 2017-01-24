package com.xtel.nipservice.callback;

import com.xtel.nipservice.model.entity.RESP_Basic;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackListenerBasic {

    void onSuccess(RESP_Basic respBasic);
    void onError(Error error);
}
