package com.xtel.ivipbusiness.view;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Lê Công Long Vũ on 12/28/2016
 */

public class MyApplication extends Application {
    public static Context context;
//    public static String PACKAGE_NAME;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
//        PACKAGE_NAME = context.getPackageName();
//        getKeyHash(PACKAGE_NAME);
    }

    private void getKeyHash(String pkg_name) {
        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(pkg_name, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
