package com.xtel.ivipbusiness.model;

import com.xtel.ivipbusiness.model.entity.Stores;
import com.xtel.nipservicesdk.model.BasicModel;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/12/2017
 */

public class UserModel extends BasicModel {
    private static UserModel intances;

    public static UserModel getIntances() {
        if (intances == null)
            intances = new UserModel();
        return intances;
    }

    public void getUserInfo() {

    }
}
