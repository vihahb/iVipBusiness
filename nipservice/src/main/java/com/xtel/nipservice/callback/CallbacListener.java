package com.xtel.nipservice.callback;

import com.xtel.nipservice.model.entity.Error;
import com.xtel.nipservice.model.entity.RESP_Login;

/**
 * Created by vihahb on 1/4/2017
 */

public interface CallbacListener {

    void onSuccess(RESP_Login success);
    void onError(Error error);
}
