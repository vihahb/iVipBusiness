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

    public ArrayList<Stores> getListStore() {
        ArrayList<Stores> arrayList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            Stores stores = new Stores();
            stores.setImage("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
            stores.setName("Store " + i);

            arrayList.add(stores);
        }

        return arrayList;
    }
}