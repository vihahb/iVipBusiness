package com.xtel.ivipbusiness.model.entity;

import com.google.gson.annotations.Expose;
import com.xtel.nipservicesdk.model.entity.RESP_Basic;

/**
 * Created by Vulcl on 3/1/2017
 */

public class RESP_Setting extends RESP_Basic {
    @Expose
    private int id;
    private TransferObject money2Points;
    private TransferObject point2Moneys;
}