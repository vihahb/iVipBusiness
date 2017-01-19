package com.xtel.ivipbusiness.model.entity;

import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.model.entity.Member;
import com.xtel.nipservicesdk.utils.JsonHelper;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/19/2017
 */

public class MemberModel {
    private static MemberModel instance;

    public static MemberModel getInstance() {
        if (instance == null)
            instance = new MemberModel();
        return instance;
    }

    public void getListMember(ResponseHandle responseHandle) {
        RESP_Member resp_member = new RESP_Member();
        ArrayList<Member> arrayList = new ArrayList<>();

        for (int i = 1; i < 31; i++) {
            Member member = new Member();
            member.setId(i);
            member.setAvatar("http://www.pngget.com/resize/resize-img.php?src=http://img.pngget.com/clip2/5dnm2s5l2ua.png&h=131&w=131");
            member.setFullname("Le Cong Long Vu");
            member.setTotal_point(100000);
            member.setRemaining_point(1000000);
            member.setTotal_checkin(1000);
            member.setTotal_shopping(1000);
            member.setTotal_shopping_in_store(1000);
            member.setLast_checkin(1484758800000L);

            arrayList.add(member);
        }

        resp_member.setData(arrayList);
        responseHandle.onSuccess(JsonHelper.toJson(resp_member));
    }
}