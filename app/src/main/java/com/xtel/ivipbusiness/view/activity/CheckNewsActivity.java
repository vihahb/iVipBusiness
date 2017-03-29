package com.xtel.ivipbusiness.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.zxing.Result;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Valid_Check_News;
import com.xtel.ivipbusiness.presenter.CheckNewsPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ICheckNewsView;
import com.xtel.ivipbusiness.view.widget.CustomViewFinderView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.nipservicesdk.utils.PermissionHelper;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.commons.NetWorkInfo;
import com.xtel.sdk.utils.WidgetHelper;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class CheckNewsActivity extends BasicActivity implements ZXingScannerView.ResultHandler, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ICheckNewsView {
    protected CallbackManager callbackManager;
    protected CheckNewsPresenter presenter;

    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    protected ZXingScannerView mScannerView;
    protected ViewGroup contentFrame;

    protected View layout_content;
    protected TextView txt_noti, txt_date_create, txt_expired_time, txt_used_time, txt_title;
    protected Button btn_use_voucher;
    protected ImageView img_banner;
    protected ImageButton img_valid_again;

    protected boolean isChecked = false;
    protected String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    protected final int REQUEST_LOCATION = 1, REQUEST_UPDATE = 8;
    protected int news_id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_news);
        callbackManager = CallbackManager.create(this);

        presenter = new CheckNewsPresenter(this);
        initToolbar(R.id.check_news_toolbar, null);
        initScannerView();
        initView();
        initListener();

        createLocationRequest();
        initListenLocation();
    }

    protected void initListenLocation() {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void createLocationRequest() {
        if (mLocationRequest == null) {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }
    }

    /*
    * Khởi tạo Scanner View để quét mã QR code
    * */
    protected void initScannerView() {
        contentFrame = (ViewGroup) findViewById(R.id.check_news_zxing);
        mScannerView = new ZXingScannerView(getApplicationContext()) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };
        contentFrame.addView(mScannerView);
    }

    /*
    * Khởi tạo các view trong layout
    * */
    protected void initView() {
        layout_content = findView(R.id.check_news_layout_content);

        txt_noti = findTextView(R.id.check_news_txt_noti);
        txt_date_create = findTextView(R.id.check_news_txt_date_create);
        txt_expired_time = findTextView(R.id.check_news_txt_expired_time);
        txt_used_time = findTextView(R.id.check_news_txt_used_time);
        txt_title = findTextView(R.id.check_news_txt_title);

        btn_use_voucher = findButton(R.id.check_news_btn_use_voucher);
        img_banner = findImageView(R.id.check_news_img_banner);
        img_valid_again = findImageButton(R.id.check_news_img_valid_again);
    }

    /*
    * Lắng nghe sự kiện khi view được click
    * */
    protected void initListener() {
        btn_use_voucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!PermissionHelper.checkListPermission(permission, CheckNewsActivity.this, REQUEST_LOCATION))
                    return;

                if (!NetWorkInfo.checkGPS(CheckNewsActivity.this, getString(R.string.ask_gps_use_voucher)))
                    return;

                Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if (mLastLocation != null) {
                    showProgressBar(false, false, null, getString(R.string.doing_use_voucher));
                    presenter.useVoucher(mLastLocation);
                } else
                    showShortToast(getString(R.string.error_get_location));
            }
        });

        img_valid_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScannerView.setResultHandler(CheckNewsActivity.this);
                mScannerView.startCamera();
            }
        });

        img_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(DetailNewsActivity.class, Constants.MODEL, news_id);
            }
        });
    }

    /*
    * Hiển thị hoặc ẩn thông tin của bản tin
    * True là show, false là hide
    * */
    protected void showOrHideNewsInfo(boolean isShow) {
        if (isShow) {
            txt_noti.setVisibility(View.GONE);
            layout_content.setVisibility(View.VISIBLE);
        } else {
            txt_noti.setVisibility(View.VISIBLE);
            layout_content.setVisibility(View.GONE);
        }
    }

    protected void startLocationUpdates() {
        if (PermissionHelper.checkListPermission(permission, CheckNewsActivity.this, REQUEST_LOCATION))
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }










    /*
    * Lấy được mã QR code
    * Kiểm tra mã QR code
    * */
    @Override
    public void handleResult(Result result) {
        if (NetWorkInfo.isOnline(this)) {
            showProgressBar(false, false, null, getString(R.string.doing_valid_check_voucher));
            debug(result.getText());
            presenter.checkNews(result.getText());
        } else {
            showShortToast(getString(R.string.error_no_internet));
            mScannerView.resumeCameraPreview(this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    /*
    * Kiểm tra voucher thành công
    * */
    @Override
    public void onValidCheckSuccess(RESP_Valid_Check_News obj) {
        closeProgressBar();
        news_id = obj.getNews_id();
        isChecked = true;
        mScannerView.stopCamera();
        showOrHideNewsInfo(true);

        WidgetHelper.getInstance().setTextViewDate(txt_date_create, getString(R.string.day_create) + ": ", obj.getCreate_time());
        WidgetHelper.getInstance().setTextViewTime(txt_expired_time, getString(R.string.expired_tịme) + ": ", obj.getExpired_time());
        WidgetHelper.getInstance().setTextViewWithResult(txt_title, obj.getTitle(), getString(R.string.updating));
        WidgetHelper.getInstance().setImageURL(img_banner, obj.getBanner());

        if (obj.getUsed_time() == null)
            txt_used_time.setVisibility(View.GONE);
        else {
            txt_used_time.setVisibility(View.VISIBLE);
            WidgetHelper.getInstance().setTextViewDate(txt_used_time, getString(R.string.used_time) + ": ", obj.getUsed_time());
        }
    }

    /*
    * Thông báo đã sử dụng voucher thành công
    * */
    @Override
    public void onUseVoucherSuccess() {
        closeProgressBar();

        showMaterialDialog(false, false, null, getString(R.string.success_use_voucher), null, getString(R.string.ok), new DialogListener() {
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
    * Thông báo request api lỗi
    * */
    @Override
    public void onRequestError(Error error) {
        showOrHideNewsInfo(false);
        mScannerView.resumeCameraPreview(this);
        closeProgressBar();

        if (error.getCode() == 701) {
            showShortToast(getString(R.string.error_voucher_code_not_exists));
        } else if (error.getCode() == 702) {
            showShortToast(getString(R.string.error_used_voucher_code));
        } else if (error.getCode() == 703) {
            showShortToast(getString(R.string.error_voucher_code_is_end_time));
        } else if (error.getCode() == 3) {
            showShortToast(getString(R.string.error_no_power_to_use_voucher));
        } else
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.have_error)));
    }

    @Override
    public void getNewSession(ICmd iCmd, Object... params) {
        final ICmd iCmd1 = iCmd;
        final Object[] object = params;

        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd1.execute(object);
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
    public Activity getActivity() {
        return this;
    }

















    @Override
    public void onDestroy() {
        try {
            mGoogleApiClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    protected void onStart() {
        try {
            mGoogleApiClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        try {
            stopLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    /*
    * Tiếp tục khởi chạy zxing khi mở lại app
    * */
    @Override
    public void onResume() {
        super.onResume();
        if (!isChecked) {
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }

        if (mGoogleApiClient.isConnected())
            try {
                startLocationUpdates();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /*
    * Dừng zxing khi ẩn app
    * */
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
        if (requestCode == REQUEST_LOCATION) {
            boolean isOk = true;

            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED)
                    isOk = false;
            }

            if (isOk) {
                NetWorkInfo.checkGPS(CheckNewsActivity.this, getString(R.string.ask_gps_use_voucher));
            }
        } else
            callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}