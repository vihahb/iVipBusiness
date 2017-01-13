package com.xtel.ivipbusiness.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Stores;
import com.xtel.ivipbusiness.presenter.ListStorePresenter;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.ivipbusiness.view.adapter.ListStoreAdapter;
import com.xtel.ivipbusiness.view.widget.ProgressView;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ListStoresFragment extends BasicFragment implements IListStoreView {
    private ListStorePresenter presenter;

    private ListStoreAdapter adapter;
    private ArrayList<Stores> listData;
    private ProgressView progressView;

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
        initProgressView(view);
    }

    private void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again), getString(R.string.loading_data), Color.WHITE);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new ListStoreAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);


        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressView.setRefreshing(true);
                presenter.getStores();
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                progressView.setRefreshing(true);
                presenter.getStores();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                presenter.getStores();
            }
        });
    }

    @Override
    public void onGetStoresSuccess(ArrayList<Stores> arrayList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressView.showData();
                progressView.setRefreshing(false);

                listData.addAll(arrayList);
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onNoNetwork() {
        progressView.updateData(-1, getString(R.string.error_no_internet), getString(R.string.click_to_try_again));
        progressView.showData();
    }
}