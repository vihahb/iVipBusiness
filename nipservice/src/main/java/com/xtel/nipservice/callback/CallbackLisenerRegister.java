package com.xtel.nipservice.callback;

import com.xtel.nipservice.model.entity.Error;
import com.xtel.nipservice.model.entity.RESP_Register;

/**
 * Created by vihahb on 1/5/2017.
 */

public interface CallbackLisenerRegister {

    void onSuccess(RESP_Register register);

    void onError(Error error);
}
