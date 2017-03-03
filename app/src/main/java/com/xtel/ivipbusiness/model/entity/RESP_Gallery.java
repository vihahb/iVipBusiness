package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Vulcl on 2/27/2017
 */

public class RESP_Gallery extends RESP_Basic implements Serializable {
    @Expose
    private ArrayList<Gallery> data;
    @Expose
    private Integer position;

    public ArrayList<Gallery> getData() {
        return data;
    }

    public void setData(ArrayList<Gallery> data) {
        this.data = data;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
