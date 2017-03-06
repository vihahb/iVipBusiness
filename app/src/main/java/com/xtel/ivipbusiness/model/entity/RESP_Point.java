package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

/**
 * Created by Vulcl on 3/5/2017
 */

public class RESP_Point extends RESP_Basic {
    @Expose
    private Integer point;

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }
}
