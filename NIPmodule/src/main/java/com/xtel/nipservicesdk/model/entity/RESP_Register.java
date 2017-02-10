package com.xtel.nipservicesdk.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by vihahb on 1/4/2017.
 */

public class RESP_Register extends RESP_Basic {

    @Expose
    private String active_url;

    @Expose
    private String activation_code;

    public String getActive_url() {
        return active_url;
    }

    public void setActive_url(String active_url) {
        this.active_url = active_url;
    }

    public String getActivation_code() {
        return activation_code;
    }

    public void setActivation_code(String activation_code) {
        this.activation_code = activation_code;
    }

    @Override
    public String toString() {
        return "RESP_Register{" +
                "active_url='" + active_url + '\'' +
                ", activation_code='" + activation_code + '\'' +
                '}';
    }
}
