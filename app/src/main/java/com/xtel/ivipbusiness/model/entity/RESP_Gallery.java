package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.util.ArrayList;

/**
 * Created by Vulcl on 2/27/2017
 */

public class RESP_Gallery extends RESP_Basic {
    @Expose
    private ArrayList<Gallery> data;

    public ArrayList<Gallery> getData() {
        return data;
    }

    public void setData(ArrayList<Gallery> data) {
        this.data = data;
    }
}
