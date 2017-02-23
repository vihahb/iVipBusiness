package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.NotifyCodition;
import com.xtel.ivipbusiness.presenter.ListFcmPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IListFcmView;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class ListFcmActivity extends BasicActivity implements IListFcmView {
    private ListFcmPresenter presenter;
    private CallbackManager callbackManager;

    private ProgressView progressView;

    private final int REQUEST_CHOOSE_OPTION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fcm);
        callbackManager = CallbackManager.create(this);

        presenter = new ListFcmPresenter(this);
        initToolbar(R.id.list_fcm_toolbar, null);
        initProgressView();
        initFloatingActionButton();
        presenter.getData();
    }

    private void initProgressView() {
        progressView = new ProgressView(this, null);
    }

    private void initFloatingActionButton() {
        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.list_fcm_fab);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.nav_list_fcm_send_people:
                        showProgressBar(false, false, null, getString(R.string.doing_send_fcm));
                        presenter.sendToPeople();
                        break;
                    case R.id.nav_list_fcm_send_group:
                        startActivityForResult(ChooseOptionFcmActivity.class, Constants.MODEL, 1, REQUEST_CHOOSE_OPTION);
                        break;
                    case R.id.nav_list_fcm_send_member:

                        break;
                    default:
                        break;
                }

                return super.onMenuItemSelected(menuItem);
            }
        });
    }

    @Override
    public void onGetDataError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
                finish();
            }

            @Override
            public void onCancel() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onSendFcmSuccess() {
        closeProgressBar();
        showMaterialDialog(false, false, null, getString(R.string.success_send_fcm), null, getString(R.string.back), new DialogListener() {
            @Override
            public void onClicked(Object object) {
                closeDialog();
            }

            @Override
            public void onCancel() {
                closeDialog();
            }
        });
    }

    @Override
    public void onRequestError(Error error) {
        closeProgressBar();
        showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
    }

    @Override
    public void onNewsNotExists() {
        closeProgressBar();
        showShortToast(getString(R.string.error_news_not_exitst));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHOOSE_OPTION && resultCode == RESULT_OK) {
            NotifyCodition notifyCodition = null;
            try {
                notifyCodition = (NotifyCodition) data.getSerializableExtra(Constants.MODEL);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (notifyCodition != null) {

            }
        }
    }
}
