package com.xtel.ivipbusiness.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.StoresPresenter;
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

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class StoresFragment extends BasicFragment implements IStoresView {
    private StoresPresenter presenter;
    private CallbackManager callbackManager;

    private StoresAdapter adapter;
    private ArrayList<SortStore> listData;
    private ProgressView progressView;

    private BottomNavigationView bottomNavigationView;

    private boolean isClearData = false;

    public static StoresFragment newInstance(int store_id) {
        Bundle args = new Bundle();
        args.putInt(Constants.MODEL, store_id);

        StoresFragment fragment = new StoresFragment();
        fragment.setArguments(args);
        return fragment;
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
        bottomNavigationView = (BottomNavigationView) getActivity().findViewById(R.id.view_store_bottom_navigation);
        initProgressView(view);
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
                isClearData = true;
                adapter.setLoadMore(false);
                adapter.notifyDataSetChanged();
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getStores(true);
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

        progressView.onScrollRecyclerview(new RecyclerOnScrollListener(layoutManager) {
            @Override
            public void onScrollUp() {
//                hideBottomView(bottomNavigationView);
            }

            @Override
            public void onScrollDown() {
//                showBottomView(bottomNavigationView);
            }

            @Override
            public void onLoadMore() {
//                presenter.getChains();
            }
        });
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

    private void hideBottomView(View view) {
        view.animate().translationY(view.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showBottomView(View view) {
        view.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
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
        if (arrayList.size() < 10) {
            adapter.setLoadMore(false);
            adapter.notifyDataSetChanged();
        }

        if (isClearData) {
            listData.clear();
            adapter.setLoadMore(true);
            isClearData = false;
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
}