package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/10/2017
 */

public class DataObj {
    @Expose
    private Long action_time;
    @Expose
    private ArrayList<ValueObj> values;

    public Long getAction_time() {
        return action_time;
    }

    public void setAction_time(Long action_time) {
        this.action_time = action_time;
    }

    public ArrayList<ValueObj> getValues() {
        return values;
    }

    public void setValues(ArrayList<ValueObj> values) {
        this.values = values;
    }
}