package com.xtel.ivipbusiness.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Error;
import com.xtel.ivipbusiness.model.entity.Stores;
import com.xtel.ivipbusiness.presenter.ChainsPresenter;
import com.xtel.ivipbusiness.view.activity.AddStoreActivity;
import com.xtel.ivipbusiness.view.activity.inf.IChainsView;
import com.xtel.ivipbusiness.view.adapter.ChainsAdapter;
import com.xtel.ivipbusiness.view.widget.ProgressView;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ChainsFragment extends BasicFragment implements IChainsView {
    private ChainsPresenter presenter;

    private ChainsAdapter adapter;
    private ArrayList<Stores> listData;
    private ProgressView progressView;
    private boolean isClearData = false;

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

        presenter = new ChainsPresenter(this);
        initFloatingActionButton();
        initProgressView(view);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = findFloatingActionButton(R.id.store_fab_add);
        fab.setOnClickListener(v -> startActivity(AddStoreActivity.class));
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again), getString(R.string.loading_data), Color.WHITE);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new ChainsAdapter(this, listData);
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