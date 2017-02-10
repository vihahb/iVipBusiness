package com.xtel.nipservicesdk.callback;

import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackListenerBasic {

    void onSuccess(RESP_Basic respBasic);

    void onError(Error error);
}
