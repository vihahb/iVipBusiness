package com.xtel.nipservicesdk.callback;

import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Reset;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackListenerReset {
    void onSuccess();

    void onError(Error error);

}
