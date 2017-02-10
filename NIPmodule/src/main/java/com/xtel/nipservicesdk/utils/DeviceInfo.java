package com.xtel.nipservicesdk.utils;

import android.annotation.SuppressLint;
import android.telephony.TelephonyManager;

import com.xtel.nipservicesdk.NipApplication;
import com.xtel.nipservicesdk.model.entity.DeviceObject;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Lê Công Long VŨ on 1/5/2017
 */

public class DeviceInfo {

    @SuppressLint("HardwareIds")
    public static DeviceObject getDeviceObject() {
        String device_id;
        String device_os_name;
        String device_os_ver;
        int device_type;
        String device_vendor;

        TelephonyManager telephonyManager = (TelephonyManager) NipApplication.context.getSystemService(TELEPHONY_SERVICE);
        device_id = telephonyManager.getDeviceId();
        device_os_name = android.os.Build.VERSION.CODENAME;
        device_os_ver = android.os.Build.VERSION.RELEASE;
        device_type = 1;
        device_vendor = android.os.Build.MANUFACTURER;

        DeviceObject deviceObject = new DeviceObject();
        deviceObject.setDeviceid(device_id);
        deviceObject.setOs_name(device_os_name);
        deviceObject.setOs_version(device_os_ver);
        deviceObject.setOther("not yet");
        deviceObject.setType(device_type);
        deviceObject.setVendor(device_vendor);

        return deviceObject;
    }
}
