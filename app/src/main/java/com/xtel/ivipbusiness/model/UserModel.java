package com.xtel.ivipbusiness.model;

import com.xtel.ivipbusiness.model.entity.RESP_Full_Profile;
import com.xtel.ivipbusiness.model.entity.RESP_Short_Profile;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.BasicModel;
import com.xtel.nipservicesdk.utils.JsonHelper;

/**
 * Created by Mr. M.2 on 1/12/2017
 */

public class UserModel extends BasicModel {
    private static UserModel intances;

    public static UserModel getIntances() {
        if (intances == null)
            intances = new UserModel();
        return intances;
    }

    public void getShortUserInfo(ResponseHandle responseHandle) {
        RESP_Short_Profile resp_short_profile = new RESP_Short_Profile();

        resp_short_profile.setFullname("Le Cong Long Vu");
        resp_short_profile.setAvatar("http://www.pngget.com/resize/resize-img.php?src=http://img.pngget.com/clip2/5dnm2s5l2ua.png&h=131&w=131");

        responseHandle.onSuccess(JsonHelper.toJson(resp_short_profile));
    }

    public void getUserInfo(ResponseHandle responseHandle) {
        RESP_Full_Profile resp_full_profile = new RESP_Full_Profile();
        resp_full_profile.setFullname("Le Cong Long Vu");
        resp_full_profile.setGender(1);
        resp_full_profile.setBirthday(763405200000L);
        resp_full_profile.setEmail("leconglongvu@gmail.com");
        resp_full_profile.setPhonenumber("0986495949");
        resp_full_profile.setAddress("Ha Noi, Viet Nam");
        resp_full_profile.setAvatar("http://www.pngget.com/resize/resize-img.php?src=http://img.pngget.com/clip2/5dnm2s5l2ua.png&h=131&w=131");
        resp_full_profile.setQr_code("http://teammagento.com/images.png");
        resp_full_profile.setBar_code("https://lh3.googleusercontent.com/-hJ48FJsv_Jc/TXe5XDFnHBI/AAAAAAAAAEs/0wQowx8hpGE/s1600/ab.bmp");
        resp_full_profile.setStatus("new");
        resp_full_profile.setStore_number(30);
        resp_full_profile.setJoin_date(1262278800000L);

        responseHandle.onSuccess(JsonHelper.toJson(resp_full_profile));
    }
}