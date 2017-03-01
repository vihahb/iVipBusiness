package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;

/**
 * Created by Vulcl on 3/1/2017
 */

public class TransferObject {
    @Expose
    private Integer value;
    @Expose
    private Long begin_time;
    @Expose
    private Long end_time;
    @Expose
    private Integer flag = 1;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Long getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Long begin_time) {
        this.begin_time = begin_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
