package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/19/2017
 */

public class RESP_History extends RESP_Basic {
    @Expose
    private ArrayList<History> data;

    public ArrayList<History> getData() {
        return data;
    }

    public void setData(ArrayList<History> data) {
        this.data = data;
    }
}