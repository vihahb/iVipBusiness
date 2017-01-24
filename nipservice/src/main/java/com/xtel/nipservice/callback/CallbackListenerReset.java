package com.xtel.nipservice.callback;

import com.xtel.nipservice.model.entity.Error;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackListenerReset {
    void onSuccess();

    void onError(Error error);

}
