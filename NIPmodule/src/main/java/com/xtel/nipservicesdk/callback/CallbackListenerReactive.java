package com.xtel.nipservicesdk.callback;

import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Reactive;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackListenerReactive {

    void onSuccess(RESP_Reactive reactive);

    void onError(Error error);
}
