package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.RESP_Setting;
import com.xtel.ivipbusiness.presenter.SettingPresenter;
import com.xtel.ivipbusiness.view.activity.inf.ISettingView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;

public class SettingActivity extends BasicActivity implements ISettingView {
    private SettingPresenter presenter;
    private CallbackManager callbackManager;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        presenter = new SettingPresenter(this);
        callbackManager = CallbackManager.create(this);

        initToolbar();
        initSwwipe();
        presenter.getData();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //    Khởi tạo swipeRefreshLayout để hiển thị load thông tin
    private void initSwwipe() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.update_news_swipe);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
    }
























    @Override
    public void onGetDataSuccess(boolean isChain) {
        if (isChain)
            actionBar.setTitle(getString(R.string.title_activity_setting_chain));
    }

    @Override
    public void onGetSettingSuccess(RESP_Setting resp_setting) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
    }

    @Override
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
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onRequestError(Error error) {
        closeProgressBar();
        showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
    }

    @Override
    public Activity getActivity() {
        return this;
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
