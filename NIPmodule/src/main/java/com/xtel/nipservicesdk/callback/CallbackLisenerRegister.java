package com.xtel.nipservicesdk.callback;

import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Register;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackLisenerRegister {

    void onSuccess(RESP_Register register);

    void onError(Error error);
}
