package com.xtel.ivipbusiness.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.ChainsPresenter;
import com.xtel.ivipbusiness.view.activity.AddStoreActivity;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.adapter.ChainsAdapter;
import com.xtel.ivipbusiness.view.fragment.inf.IChainsView;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * Created by Vulcl on 1/13/2017
 */

public class ChainsFragment extends BasicFragment implements IChainsView {
    protected ChainsPresenter presenter;

    protected ChainsAdapter adapter;
    protected ArrayList<SortStore> listData;
    protected ProgressView progressView;
    protected boolean isClearData = false;
    protected CallbackManager callbackManager;

    protected final String CHAIN_TYPE = "CHAIN", STORE_TYPE = "STORE";
    protected final int REQUEST_STORE_INFO = 11, REQUEST_ADD_STORE = 33;

    public static ChainsFragment newInstance() {
        return new ChainsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chains, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callbackManager = CallbackManager.create(getActivity());
        presenter = new ChainsPresenter(this);
        initProgressView(view);
        initFloatingActionButton(view);
    }

    //    Khởi tạo layout và recyclerview
    protected void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new ChainsAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getChains(true);
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataAgain();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getChains(true);
            }
        });
    }

    protected void initFloatingActionButton(View view) {
        FabSpeedDial fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.home_fab);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.nav_home_create_store:
                        if (!NetWorkInfo.isOnline(getContext())) {
                            showShortToast(getString(R.string.error_no_internet));
                        } else
                            startActivityForResult(AddStoreActivity.class, Constants.MODEL, STORE_TYPE, REQUEST_ADD_STORE);
                        break;
                    case R.id.nav_home_create_chain:
                        if (!NetWorkInfo.isOnline(getContext())) {
                            showShortToast(getString(R.string.error_no_internet));
                        } else
                            startActivityForResult(AddStoreActivity.class, Constants.MODEL, CHAIN_TYPE, REQUEST_ADD_STORE);
                        break;
                    default:
                        break;
                }

                return super.onMenuItemSelected(menuItem);
            }
        });
    }
    
    protected void getDataAgain() {
        isClearData = true;
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();
        progressView.setRefreshing(true);
        progressView.showData();
        presenter.getChains(true);
    }

    //    Kiểm tra xem danh sách cửa hàng có trống không
    protected void checkListData() {
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
        presenter.getChains(false);
    }

    //    Sự kiện load danh sách store thành công
    @Override
    public void onGetStoresSuccess(final ArrayList<SortStore> arrayList) {
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
    public void onGetStoresError(Error error) {
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();

        if (listData.size() > 0)
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateData(-1, JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)), getString(R.string.click_to_try_again));
            progressView.hideData();

            listData.clear();
            adapter.notifyDataSetChanged();
            if (isClearData)
                isClearData = false;
        }
    }

    @Override
    public void onNoNetwork() {
        progressView.setRefreshing(false);

        if (listData.size() > 0)
            showShortToast(getString(R.string.error_no_internet));
        else {
            progressView.updateData(-1, getString(R.string.error_no_internet), getString(R.string.click_to_try_again));
            progressView.hideData();
        }
    }

    @Override
    public void getNewSession(final ICmd iCmd) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(1);
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
    public void onDestroy() {
        presenter.setExists(false);
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Class clazz, String key, Object object, int requestCode) {
        super.startActivityForResult(clazz, key, object, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_STORE_INFO && resultCode == Activity.RESULT_OK) {
            getDataAgain();
        } else if (requestCode == REQUEST_ADD_STORE && resultCode == Activity.RESULT_OK) {
            getDataAgain();
        }
    }
}