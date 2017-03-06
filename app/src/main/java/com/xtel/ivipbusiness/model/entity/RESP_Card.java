package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/5/2017
 */

public class RESP_Card extends RESP_Basic {
    @Expose
    private ArrayList<Card> data;

    public ArrayList<Card> getData() {
        return data;
    }

    public void setData(ArrayList<Card> data) {
        this.data = data;
    }
}