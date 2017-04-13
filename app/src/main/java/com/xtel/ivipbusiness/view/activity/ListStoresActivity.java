package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.ListStoresPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.ivipbusiness.view.adapter.ListStoreAdapter;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.RecyclerOnScrollListener;

import java.util.ArrayList;

public class ListStoresActivity extends BasicActivity implements IListStoreView {
    private ListStoresPresenter presenter;
    private CallbackManager callbackManager;

    private ProgressView progressView;
    private ArrayList<SortStore> listData;
    private ListStoreAdapter adapter;
    private boolean isClearData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_stores);
        callbackManager = CallbackManager.create(this);

        presenter = new ListStoresPresenter(this);
        initToolbar(R.id.list_store_toolbar, null);
        getData();
    }

    protected void getData() {
        if (Constants.SORT_STORE == null) {
            showMaterialDialog(false, false, null, getString(R.string.have_error), null, getString(R.string.back), new DialogListener() {
                @Override
                public void negativeClicked() {

                }

                @Override
                public void positiveClicked() {
                    finish();
                }
            });
        } else
            initProgressView();
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView() {
        progressView = new ProgressView(this, null);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listData = new ArrayList<>();
        adapter = new ListStoreAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getListStores(true);
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isClearData = true;
                adapter.setLoadMore(false);
                adapter.notifyDataSetChanged();
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getListStores(true);
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getListStores(true);
            }
        });

        progressView.onScrollRecyclerview(new RecyclerOnScrollListener(layoutManager) {
            @Override
            public void onScrollUp() {
//                hideBottomView();
            }

            @Override
            public void onScrollDown() {
//                showBottomView();
            }

            @Override
            public void onLoadMore() {
//                showShortToast("load");
//                presenter.getListStores();
            }
        });
    }

    //    Kiểm tra xem danh sách cửa hàng có trống không
    private void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            progressView.showData();
            adapter.notifyDataSetChanged();
        } else {
            progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again));
            progressView.hideData();
        }
    }

    @Override
    public void onLoadMore() {
        presenter.getListStores(false);
    }

    @Override
    public void onGetListStoresSuccess(final ArrayList<SortStore> arrayList) {
        if (isClearData) {
            listData.clear();
            adapter.setLoadMore(true);
            isClearData = false;
        }

        if (arrayList.size() < 10)
            adapter.setLoadMore(false);

        listData.addAll(arrayList);
        checkListData();
    }

    @Override
    public void onGetListStoresError(Error error) {
        if (listData.size() > 0)
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateData(-1, JsonParse.getCodeMessage(error.getCode(), getString(R.string.have_error)), getString(R.string.click_to_try_again));
            progressView.hideData();

            listData.clear();
            adapter.notifyDataSetChanged();
            if (isClearData)
                isClearData = false;
        }
    }

    @Override
    public void onChooseSuccess() {
        showMaterialDialog(false, false, null, getString(R.string.success_add_list_store), null, getString(R.string.ok), new DialogListener() {
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

    @Override
    public void onChooseError() {
        showMaterialDialog(false, false, null, getString(R.string.error_add_list_store), null, getString(R.string.back), new DialogListener() {
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

    @Override
    public void getNewSession(final ICmd iCmd) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute();
            }

            @Override
            public void onError(Error error) {
                showShortToast(getString(R.string.error_end_of_session));
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void onNoNetwork() {
        progressView.setRefreshing(false);
        progressView.updateData(-1, getString(R.string.error_no_internet), getString(R.string.click_to_try_again));
        progressView.hideData();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_list_store_done)
            presenter.chooseList(listData);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
