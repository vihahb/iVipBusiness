package com.xtel.ivipbusiness.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Error;
import com.xtel.ivipbusiness.model.entity.Stores;
import com.xtel.ivipbusiness.presenter.ListStorePresenter;
import com.xtel.ivipbusiness.view.activity.AddStoreActivity;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.ivipbusiness.view.adapter.ListStoreAdapter;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.ivipbusiness.view.widget.RecyclerOnScrollListener;

import java.util.ArrayList;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ListStoresFragment extends BasicFragment implements IListStoreView {
    private ListStorePresenter presenter;

    private ListStoreAdapter adapter;
    private ArrayList<Stores> listData;
    private ProgressView progressView;
    private FrameLayout bottomNavigationView;
    private boolean isClearData = false;

    public static ListStoresFragment newInstance() {
        return new ListStoresFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_store, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new ListStorePresenter(this);
        bottomNavigationView = (FrameLayout) getActivity().findViewById(R.id.view_store_layout_bnv);
        initFloatingActionButton(view);
        initProgressView(view);
    }

    private void initFloatingActionButton(View view) {
        FabSpeedDial fabSpeedDial = (FabSpeedDial) view.findViewById(R.id.list_store_fab_show);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                switch (menuItem.getItemId()) {
                    case R.id.nav_floating_create_store:
                        startActivity(AddStoreActivity.class);
                        break;
                    case R.id.nav_floating_add_store:

                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again), getString(R.string.loading_data), Color.WHITE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new ListStoreAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(view1 -> {
            progressView.setRefreshing(true);
            progressView.showData();
            presenter.getStores();
        });

        progressView.onRefreshListener(() -> {
            isClearData = true;
            progressView.setRefreshing(true);
            progressView.showData();
            presenter.getStores();
        });

        progressView.onSwipeLayoutPost(() -> {
            progressView.setRefreshing(true);
            progressView.showData();
            presenter.getStores();
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

            }
        });
    }

    private void hideBottomView() {
        bottomNavigationView.animate().translationY(bottomNavigationView.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showBottomView() {
        bottomNavigationView.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    //    Sự kiện load danh sách store thành công
    @Override
    public void onGetStoresSuccess(ArrayList<Stores> arrayList) {
        new Handler().postDelayed(() -> {
            progressView.showData();
            progressView.setRefreshing(false);

            if (isClearData) {
                listData.clear();
                isClearData = false;
            }
            listData.addAll(arrayList);
            adapter.notifyDataSetChanged();
        }, 1000);
    }

    @Override
    public void onGetStoresError(Error error) {

    }

    @Override
    public void onNoNetwork() {
        progressView.setRefreshing(false);
        progressView.updateData(-1, getString(R.string.error_no_internet), getString(R.string.click_to_try_again));
        progressView.hideData();
    }
}