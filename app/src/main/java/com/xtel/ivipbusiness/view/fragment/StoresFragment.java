package com.xtel.ivipbusiness.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.StoresPresenter;
import com.xtel.ivipbusiness.view.activity.AddStoreActivity;
import com.xtel.ivipbusiness.view.activity.ListStoresActivity;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.adapter.StoresAdapter;
import com.xtel.ivipbusiness.view.fragment.inf.IStoresView;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.ivipbusiness.view.widget.RecyclerOnScrollListener;
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
 * Created by Mr. M.2 on 1/13/2017
 */

public class StoresFragment extends BasicFragment implements IStoresView {
    private StoresPresenter presenter;
    private CallbackManager callbackManager;

    private StoresAdapter adapter;
    private ArrayList<SortStore> listData;
    private ProgressView progressView;

    private final String STORE_TYPE = "STORE";
    private boolean isClearData = false;
    private final int REQUEST_ADD_STORE = 11;

    public static StoresFragment newInstance() {
        return new StoresFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.create(getActivity());

        presenter = new StoresPresenter(this);
//        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.view_store_bottom_navigation);
        initProgressView(view);
        initFloatingActionButton(view);
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new StoresAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getStores(true);
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
                presenter.getStores(true);
            }
        });
    }

    private void initFloatingActionButton(View view) {
        FabSpeedDial fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.store_fab_add);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.nav_store_create_store:
                        Intent intent = new Intent(getActivity(), AddStoreActivity.class);
                        intent.putExtra(Constants.ID, Constants.SORT_STORE.getId());
                        intent.putExtra(Constants.MODEL, STORE_TYPE);
                        startActivityForResult(intent, REQUEST_ADD_STORE);
                        break;
                    case R.id.nav_store_choose_store:
                        startActivityForResult(ListStoresActivity.class, REQUEST_ADD_STORE);
                        break;
                    default:
                        break;
                }

                return super.onMenuItemSelected(menuItem);
            }
        });
    }

    /*
    * Load lại danh sách cửa hàng
    * */
    protected void getDataAgain() {
        isClearData = true;
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();
        progressView.setRefreshing(true);
        progressView.showData();
        presenter.getStores(true);
    }

    //    Kiểm tra xem danh sách cửa hàng có trống không
    private void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            adapter.notifyDataSetChanged();
            progressView.showData();
        } else {
            progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again));
            progressView.hideData();
        }
    }

    @Override
    public void onGetDataError() {
        progressView.setRefreshing(false);
        progressView.updateData(-1, getString(R.string.have_error), getString(R.string.click_to_try_again));
        progressView.hideData();
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
    public void onLoadMore() {
        presenter.getStores(false);
    }

    //    Sự kiện load danh sách store thành công
    @Override
    public void onGetStoresSuccess(final ArrayList<SortStore> arrayList) {
        if (isClearData) {
            listData.clear();
            adapter.setLoadMore(true);
            isClearData = false;
        }

        if (arrayList.size() < 10) {
            adapter.setLoadMore(false);
            adapter.notifyDataSetChanged();
        }

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
    public void startActivity(Class clazz, String key, Object object) {
        super.startActivity(clazz, key, object);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onDestroy() {
        presenter.setExists(false);
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_STORE && resultCode == Activity.RESULT_OK) {
            getDataAgain();
        }
    }
}