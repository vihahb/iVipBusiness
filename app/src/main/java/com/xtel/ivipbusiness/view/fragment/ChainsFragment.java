package com.xtel.ivipbusiness.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.ChainsPresenter;
import com.xtel.ivipbusiness.view.activity.AddStoreActivity;
import com.xtel.ivipbusiness.view.activity.inf.IChainsView;
import com.xtel.ivipbusiness.view.adapter.ChainsAdapter;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.ivipbusiness.view.widget.RecyclerOnScrollListener;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonParse;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/13/2017
 */

public class ChainsFragment extends BasicFragment implements IChainsView {
    private ChainsPresenter presenter;

    private ChainsAdapter adapter;
    private ArrayList<SortStore> listData;
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
        FloatingActionButton fab = findFloatingActionButton(R.id.chain_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddStoreActivity.class);
            }
        });
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again), getString(R.string.loading_data), Color.WHITE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new ChainsAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getStores();
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isClearData = true;
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getStores();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getStores();
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
                showShortToast("load");
                presenter.getStores();
            }
        });
    }

    //    Sự kiện load danh sách store thành công
    @Override
    public void onGetStoresSuccess(final ArrayList<SortStore> arrayList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressView.showData();
                progressView.setRefreshing(false);

                if (isClearData) {
                    listData.clear();
                    isClearData = false;
                }

                listData.addAll(arrayList);
                adapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onGetStoresError(Error error) {
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
        progressView.updateData(-1, getString(R.string.error_no_internet), getString(R.string.click_to_try_again));
        progressView.hideData();
    }
}