package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.util.ArrayList;

/**
 * Created by Vulcl on 2/28/2017
 */

public class RESP_Notify extends RESP_Basic {
    @Expose
    private ArrayList<Notify> data;

    public ArrayList<Notify> getData() {
        return data;
    }

    public void setData(ArrayList<Notify> data) {
        this.data = data;
    }
}
