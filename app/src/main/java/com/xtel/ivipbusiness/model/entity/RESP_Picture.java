package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 2/28/2017
 */

public class RESP_Picture {
    @Expose
    private int id;
    @Expose
    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
