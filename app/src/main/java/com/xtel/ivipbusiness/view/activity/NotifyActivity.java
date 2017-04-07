package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Notify;
import com.xtel.ivipbusiness.model.entity.NotifyCodition;
import com.xtel.ivipbusiness.presenter.NotifyPresenter;
import com.xtel.ivipbusiness.view.activity.inf.INotifyView;
import com.xtel.ivipbusiness.view.adapter.NotifyAdapter;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.sdk.utils.RecyclerOnScrollListener;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class NotifyActivity extends BasicActivity implements INotifyView {
    private NotifyPresenter presenter;
    private CallbackManager callbackManager;

    private NotifyAdapter adapter;
    private ArrayList<Notify> listData;
    private ProgressView progressView;
    private FabSpeedDial fabSpeedDial;

    private boolean isClearData = false;
    private final int REQUEST_GROUP = 88, REQUEST_MEMBER = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fcm);
        callbackManager = CallbackManager.create(this);

        presenter = new NotifyPresenter(this);
        initToolbar(R.id.list_fcm_toolbar, null);
        initProgressView();
        initFloatingActionButton();
        presenter.getData();
    }

    //    Khởi tạo progress view gồm refresh layout và recyclerview
    private void initProgressView() {
        progressView = new ProgressView(this, null);
        progressView.initData(-1, getString(R.string.no_notify), getString(R.string.click_to_try_again));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new NotifyAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getListNotify();
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListNewsAgain();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getListNotify();
            }
        });

        progressView.onScrollRecyclerview(new RecyclerOnScrollListener(layoutManager) {
            @Override
            public void onScrollUp() {
                hideBottomView(fabSpeedDial);
            }

            @Override
            public void onScrollDown() {
                showBottomView(fabSpeedDial);
            }

            @Override
            public void onLoadMore() {
//                presenter.getChains();
            }
        });
    }

    /*
    * Load lại danh sách bản tin
    * */
    protected void getListNewsAgain() {
        isClearData = true;
        progressView.setRefreshing(true);
        progressView.showData();
        presenter.getListNotify();
    }

    //    Khởi tạo floating action button để lựa chọn gửi notify
    private void initFloatingActionButton() {
        fabSpeedDial = (FabSpeedDial) findViewById(R.id.list_fcm_fab);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.nav_list_fcm_send_people:
                        showProgressBar(false, false, null, getString(R.string.doing_send_fcm));
                        presenter.sendNotify(1, null);
                        break;
                    case R.id.nav_list_fcm_send_group:
                        startActivityForResult(SendFcmActivity.class, Constants.MODEL, 1, REQUEST_GROUP);
                        break;
                    case R.id.nav_list_fcm_send_member:
                        startActivityForResult(SendFcmActivity.class, Constants.MODEL, 2, REQUEST_MEMBER);
                        break;
                    default:
                        break;
                }

                return super.onMenuItemSelected(menuItem);
            }
        });
    }

    //    hàm gửi notify sau khi lựa chọn option để gửi tới nhóm hoặc member
    private void sendNotify(int type, Intent data) {
        showProgressBar(false, false, null, getString(R.string.doing_send_fcm));

        NotifyCodition notifyCodition = null;
        try {
            notifyCodition = (NotifyCodition) data.getSerializableExtra(Constants.MODEL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (notifyCodition != null)
            presenter.sendNotify(type, notifyCodition);
        else
            showShortToast(getString(R.string.error_try_again));
    }

    //    Kiểm tra xem danh sách cửa hàng có trống không
    private void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            progressView.showData();
            adapter.notifyDataSetChanged();
        } else {
            progressView.initData(-1, getString(R.string.no_notify), getString(R.string.click_to_try_again));
            progressView.hideData();
        }
    }

    private void hideBottomView(View view) {
        view.animate().translationY(view.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showBottomView(View view) {
        view.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
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
    public void onGetNotifySuccess(ArrayList<Notify> arrayList) {
        if (isClearData) {
            listData.clear();
//            adapter.setLoadMore(true);
            isClearData = false;
        }

        listData.addAll(arrayList);
        checkListData();
    }

    @Override
    public void onSendFcmSuccess() {
        closeProgressBar();
        getListNewsAgain();
        showMaterialDialog(false, false, null, getString(R.string.success_send_fcm), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {

            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onRequestError(Error error) {
        closeProgressBar();

        if (error.getCode() == 201)
            showShortToast(getString(R.string.error_news_not_exitst));
        else
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
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
        if (requestCode == REQUEST_GROUP && resultCode == RESULT_OK) {
            sendNotify(2, data);
        } else if (requestCode == REQUEST_MEMBER && resultCode == RESULT_OK) {
            sendNotify(3, data);
        }
    }
}
