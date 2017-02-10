package com.xtel.nipservicesdk;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.CallbackLisenerRegister;
import com.xtel.nipservicesdk.callback.CallbackListenerActive;
import com.xtel.nipservicesdk.callback.CallbackListenerReactive;
import com.xtel.nipservicesdk.callback.CallbackListenerReset;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.callback.ResponseHandle;
import com.xtel.nipservicesdk.commons.Cts;
import com.xtel.nipservicesdk.model.LoginModel;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.model.entity.RESP_None;
import com.xtel.nipservicesdk.model.entity.RESP_Reactive;
import com.xtel.nipservicesdk.model.entity.RESP_Register;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.nipservicesdk.utils.SharedUtils;

import java.util.ArrayList;

/**
 * Created by Lê Công Long Vũ on 1/5/2017
 */

public class CallbackManager {
    private final int REQUEST_PERMISSION = 111;
    private CallbacListener callbacListener;
    private CallbackLisenerRegister callbacListenerRegister;
    private CallbackListenerReset callbackListenerReset;
    private Activity activity;
    private ArrayList<Object> object = new ArrayList<>();

    private ICmd iCmd = new ICmd() {
        @Override
        public void execute(Object... params) {
            if ((Integer) object.get(0) == 1) {

                LoginModel.getInstance().getNewSession((String) object.get(1), new ResponseHandle<RESP_Login>(RESP_Login.class) {
                    @Override
                    public void onSuccess(RESP_Login obj) {
                        saveLoginInfo(obj);
                        if (callbacListener != null)
                            callbacListener.onSuccess(obj);
                    }

                    @Override
                    public void onError(Error error) {
                        if (callbacListener != null)
                            callbacListener.onError(error);
                    }
                });
            } else if ((Integer) object.get(0) == 2) {
                LoginModel.getInstance().postFacebookData2Server((String) object.get(1), (String) object.get(2), new ResponseHandle<RESP_Login>(RESP_Login.class) {
                    @Override
                    public void onSuccess(RESP_Login obj) {
                        saveLoginInfo(obj);
                        callbacListener.onSuccess(obj);
                    }

                    @Override
                    public void onError(Error error) {
                        callbacListener.onError(error);
                    }
                });
            } else if ((Integer) object.get(0) == 3){
                LoginModel.getInstance().postAccountKitData2Server((String) object.get(1), (String) object.get(2), new ResponseHandle<RESP_Login>(RESP_Login.class) {
                    @Override
                    public void onSuccess(RESP_Login obj) {
                        saveLoginInfo(obj);
                        callbacListener.onSuccess(obj);
                    }

                    @Override
                    public void onError(Error error) {
                        callbacListener.onError(error);
                    }
                });
            } else if ((Integer) object.get(0) == 4){
                LoginModel.getInstance().loginNipServices((String) object.get(1), (String) object.get(2), (String) object.get(3), (Boolean) object.get(4), new ResponseHandle<RESP_Login>(RESP_Login.class) {
                    @Override
                    public void onSuccess(RESP_Login obj) {
                        saveLoginInfo(obj);
                        callbacListener.onSuccess(obj);
                    }

                    @Override
                    public void onError(Error error) {
                        callbacListener.onError(error);
                    }
                });
            } else if ((Integer) object.get(0) == 5){
                LoginModel.getInstance().registerAccountNip((String) object.get(1), (String) object.get(2), (String) object.get(3), (boolean) object.get(4), (String) object.get(5), new ResponseHandle<RESP_Register>(RESP_Register.class) {
                    @Override
                    public void onSuccess(RESP_Register obj) {
                        SharedUtils.getInstance().putStringValue(Cts.USER_ACTIVATION_CODE, obj.getActivation_code());
                        callbacListenerRegister.onSuccess(obj);
                    }

                    @Override
                    public void onError(Error error) {
                        callbacListenerRegister.onError(error);
                    }
                });
            } else if ((Integer) object.get(0) == 6) {
                LoginModel.getInstance().resetPassworf((String) object.get(1), (String) object.get(2), (String) object.get(3), (boolean) object.get(4), (String) object.get(5), new ResponseHandle<RESP_None>(RESP_None.class) {
                    @Override
                    public void onSuccess(RESP_None obj) {
                        callbackListenerReset.onSuccess();
                    }

                    @Override
                    public void onError(Error error) {
                        callbackListenerReset.onError(error);
                    }
                });
            }
        }
    };

    private CallbackManager(Activity activity) {
        this.activity = activity;
    }

    public static CallbackManager create(Activity activity) {
        return new CallbackManager(activity);
    }

    public String getCurrentSession() {
        return LoginModel.getInstance().getSession();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                iCmd.execute();
        }
    }

    public void LoginFaceook(String token_key, final CallbacListener callbacListener) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);

        if (service_code == null || service_code.isEmpty()) {
            callbacListener.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        this.callbacListener = callbacListener;

        object.clear();
        object.add(2);
        object.add(service_code);
        object.add(token_key);


        if (checkPermission())
            iCmd.execute();
    }

    public void LoginAccountKit(String authorization_code, final CallbacListener callbacListener) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);

        if (service_code == null || service_code.isEmpty()) {
            callbacListener.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        this.callbacListener = callbacListener;

        object.clear();
        object.add(3);
        object.add(service_code);
        object.add(authorization_code);


        if (checkPermission())
            iCmd.execute();
    }

    public void LoginNipAcc(String user_name, String password, boolean isPhone, final CallbacListener callbacListener) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);
        if (service_code == null || service_code.isEmpty()) {
            callbacListener.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        this.callbacListener = callbacListener;

        object.clear();
        object.add(4);
        object.add(user_name);
        object.add(password);
        object.add(service_code);
        object.add(isPhone);

        if (checkPermission())
            iCmd.execute();
    }

    public void registerNipService(String user_name, String password, String email, boolean isPhone, final CallbackLisenerRegister callbackLisenerRegister) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);
        if (service_code == null || service_code.isEmpty()) {
            callbacListenerRegister.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        this.callbacListenerRegister = callbackLisenerRegister;

        object.clear();
        object.add(5);
        object.add(user_name);
        object.add(password);
        object.add(email);
        object.add(isPhone);
        object.add(service_code);

        if (checkPermission())
            iCmd.execute();

    }

    public void AdapterReset(String user_email, String password, String authorization_code, final CallbackListenerReset callbackListenerReset) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);
        if (service_code == null || service_code.isEmpty()) {
            callbackListenerReset.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        this.callbackListenerReset = callbackListenerReset;
        if (user_email != null) {
            resetNipAccount(user_email, null, false, null);
        } else {
            resetNipAccount("", password, true, authorization_code);
        }
    }

    private boolean checkNumber(String number) {
        long number_long;
        boolean check = true;

        if (number.length() < 10 && number.length() > 11) {

        } else {
            try {
                number_long = Long.parseLong(number);
            } catch (Exception e) {
                e.printStackTrace();
                check = false;
            }
        }

        if (!check) {

            return false;
        } else {
            return true;
        }
    }


    public void resetNipAccount(String email, String password, boolean isPhone, String authorization_code) {

        String service_code = LoginModel.getInstance().getServiceCode(activity);
        if (service_code == null || service_code.isEmpty()) {
            callbackListenerReset.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        object.clear();
        object.add(6);
        object.add(email);
        object.add(password);
        object.add(service_code);
        object.add(isPhone);
        object.add(authorization_code);
        object.add(service_code);

        iCmd.execute();

    }

    public void activeNipAccount(String authorization_code, String accountType, final CallbackListenerActive callbackListenerActive) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);

        if (service_code == null || service_code.isEmpty()) {
            callbackListenerActive.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        LoginModel.getInstance().activeAccount(authorization_code, accountType, service_code, new ResponseHandle<RESP_None>(RESP_None.class) {
            @Override
            public void onSuccess(RESP_None obj) {
                callbackListenerActive.onSuccess();
            }

            @Override
            public void onError(Error error) {
                callbackListenerActive.onError(error);
            }
        });
    }

    public void reactiveNipAccount(String user_name, boolean isPhone, final CallbackListenerReactive callbacListener) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);

        if (service_code == null || service_code.isEmpty()) {
            callbacListener.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        LoginModel.getInstance().reactiveNipAccoint(user_name, service_code, isPhone, new ResponseHandle<RESP_Reactive>(RESP_Reactive.class) {
            @Override
            public void onSuccess(RESP_Reactive obj) {
                SharedUtils.getInstance().putStringValue(Cts.USER_ACTIVATION_CODE, obj.getActivation_code());
                callbacListener.onSuccess(obj);
            }

            @Override
            public void onError(Error error) {
                callbacListener.onError(error);
            }
        });
    }

    private void saveLoginInfo(RESP_Login obj) {
//        SharedUtils.getInstance().putLongValue(Cts.TIME_ALIVE, (obj.getTime_alive() * 60));
//        SharedUtils.getInstance().putLongValue(Cts.BEGIN_TIME, System.currentTimeMillis());

        if (obj.getAuthenticationid() != null && !obj.getAuthenticationid().isEmpty())
            SharedUtils.getInstance().putStringValue(Cts.USER_AUTH_ID, obj.getAuthenticationid());
        if (obj.getSession() != null && !obj.getSession().isEmpty())
            SharedUtils.getInstance().putStringValue(Cts.USER_SESSION, obj.getSession());

//        checkLogedTime();
    }

//    private void checkLogedTime() {
//        final long current_time = System.currentTimeMillis();
//        long begin_time = SharedUtils.getInstance().getLongValue(Cts.BEGIN_TIME);
//        long time_alive = SharedUtils.getInstance().getLongValue(Cts.TIME_ALIVE);
//        final long total_time = begin_time + time_alive;
//
//        if (time_alive <= 0) {
//            return;
//        }
//
//        if (current_time > total_time) {
//            if (checkPermission())
//                getNewSesion(null);
//        } else {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    getNewSesion(null);
//                }
//            }, (total_time - current_time));
//        }
//    }

    public void getNewSesion(final CallbacListener callbacListener) {
        String service_code = LoginModel.getInstance().getServiceCode(activity);

        if (service_code == null || service_code.isEmpty()) {
            callbacListener.onError(new Error(-2, activity.getString(R.string.error), activity.getString(R.string.error_no_service_code)));
            return;
        }

        this.callbacListener = callbacListener;

        object.clear();
        object.add(1);
        object.add(service_code);

        if (checkPermission())
            iCmd.execute();
    }

    private boolean checkPermission() {
        return PermissionHelper.checkOnlyPermission(Manifest.permission.READ_PHONE_STATE, activity, REQUEST_PERMISSION);
    }
}