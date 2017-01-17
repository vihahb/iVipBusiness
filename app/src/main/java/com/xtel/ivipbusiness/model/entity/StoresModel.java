package com.xtel.ivipbusiness.model.entity;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/17/2017
 */

public class StoresModel {
    private static StoresModel instance;

    public static StoresModel getInstance() {
        if (instance == null)
            instance = new StoresModel();
        return instance;
    }

    public ArrayList<SortStore> getListStore() {
        ArrayList<SortStore> arrayList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            SortStore stores = new SortStore();
            stores.setLogo("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
            stores.setName("Store " + i);

            arrayList.add(stores);
        }

        return arrayList;
    }

    public ArrayList<SortStore> getListStoreNotInChain() {
        ArrayList<SortStore> arrayList = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            SortStore stores = new SortStore();
            stores.setLogo("https://www.merryallcenter.org/wp-content/uploads/2016/04/cup-150x150.png");
            stores.setName("Store " + i);

            arrayList.add(stores);
        }

        return arrayList;
    }
}