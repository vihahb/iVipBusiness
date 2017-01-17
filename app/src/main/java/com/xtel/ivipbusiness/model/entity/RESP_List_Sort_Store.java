package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/17/2017
 */

public class RESP_List_Sort_Store extends RESP_Basic {
    @Expose
    private ArrayList<SortStore> data;

    public ArrayList<SortStore> getData() {
        return data;
    }

    public void setData(ArrayList<SortStore> data) {
        this.data = data;
    }
}