package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.zxing.Result;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.presenter.CheckInUserPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ICheckInUserView;
import com.xtel.ivipbusiness.view.adapter.CustomViewFinderView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.NetWorkInfo;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckInUserActivity extends BasicActivity implements ZXingScannerView.ResultHandler, ICheckInUserView {
    protected ZXingScannerView mScannerView;
    protected CallbackManager callbackManager;
    protected CheckInUserPresenter presenter;

    protected ViewGroup contentFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_user);
        callbackManager = CallbackManager.create(this);

        presenter = new CheckInUserPresenter(this);
        initToolbar(R.id.check_in_user_toolbar, null);
        initScannerView();
        presenter.getData();
    }

    /*
    * Khởi tạo Scanner View để quét mã QR code
    * */
    protected void initScannerView() {
        contentFrame = (ViewGroup) findViewById(R.id.check_in_user_zxing);
        mScannerView = new ZXingScannerView(getApplicationContext()) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }

    /*
    * Hiển thị thông báo lỗi khi check in
    * */
    protected void showCheckInError(String message) {
        showMaterialDialog(false, false, null, message, null, getString(R.string.ok), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeProgressBar();
                closeDialog();
                mScannerView.resumeCameraPreview(CheckInUserActivity.this);
            }
        });
    }

    /*
    * Hiển thị lỗi khi thành viên chưa có thẻ thành viên
    * */
    protected void showCheckInError501(final String member_code) {
        closeProgressBar();

        showMaterialDialog(false, false, null, getString(R.string.error_member_not_have_card), getString(R.string.ok), getString(R.string.cancel), new DialogListener() {
            @Override
            public void negativeClicked() {
                showProgressBar(false, false, null, getString(R.string.doing_create_member_card));
                presenter.createMemberCard(member_code);
            }

            @Override
            public void positiveClicked() {
                closeProgressBar();
                closeDialog();
                mScannerView.resumeCameraPreview(CheckInUserActivity.this);
            }
        });
    }










    /*
    * Lấy được mã QR code
    * Kiểm tra mã QR code
    * */
    @Override
    public void handleResult(Result result) {
        if (NetWorkInfo.isOnline(this)) {
            showProgressBar(false, false, null, getString(R.string.doing_check_in));
            debug(result.getText());
            presenter.checkIn(result.getText());
        } else {
            showShortToast(getString(R.string.error_no_internet));
            mScannerView.resumeCameraPreview(this);
        }
    }

    /*
    * Thông báo lỗi khi nhận data bị lỗi, hoặc không có data truyền sang
    * */
    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    /*
    * Lấy session mới khi session cũ đã hết hạn
    * */
    public void getNewSession(final ICmd iCmd, final Object... params) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(params);
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(getString(R.string.error_end_of_session));
                finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void showShortToast(String message) {
        super.showShortToast(message);
    }

    @Override
    public void startActivityAndFinish(Class clazz, String key, Object object) {
        super.startActivityAndFinish(clazz, key, object);
    }

    @Override
    public void closeProgressBar() {
        super.closeProgressBar();
    }

    @Override
    public void onRequetServerError(String member_code, Error error) {
        closeProgressBar();

        if (error.getCode() == 101)
            showCheckInError(getString(R.string.error_store_not_exists));
        else if (error.getCode() == 501)
            showCheckInError501(member_code);
        else
            showCheckInError(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));

    }

    @Override
    public Activity getActivity() {
        return this;
    }













    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}