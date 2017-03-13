package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/10/2017
 */

public class RESP_Statistic extends RESP_Basic {
    @Expose
    private ArrayList<DataObj> data;

    public ArrayList<DataObj> getData() {
        return data;
    }

    public void setData(ArrayList<DataObj> data) {
        this.data = data;
    }
}
