package com.xtel.nipservice.callback;

import com.xtel.nipservice.model.entity.Error;
import com.xtel.nipservice.model.entity.RESP_Reactive;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackListenerReactive {

    void onSuccess(RESP_Reactive reactive);

    void onError(Error error);
}
