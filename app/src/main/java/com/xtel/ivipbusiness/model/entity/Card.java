package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 3/5/2017
 */

public class Card {
    private String file_path;
    @Expose
    private String card_name;
    @Expose
    private String card_url;
    @Expose
    private String card_path;

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getCard_path() {
        return card_path;
    }

    public void setCard_path(String card_path) {
        this.card_path = card_path;
    }

    public String getCard_url() {
        return card_url;
    }

    public void setCard_url(String card_url) {
        this.card_url = card_url;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }
}