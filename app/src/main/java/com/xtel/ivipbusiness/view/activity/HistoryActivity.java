package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.History;
import com.xtel.ivipbusiness.presenter.HistoryPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IHistoryView;
import com.xtel.ivipbusiness.view.adapter.HistoryAdapter;
import com.xtel.ivipbusiness.view.fragment.BasicFragment;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.ivipbusiness.view.widget.RecyclerOnScrollListener;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonParse;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/13/2017
 */

public class HistoryActivity extends BasicActivity implements IHistoryView {
    private HistoryPresenter presenter;

    private HistoryAdapter adapter;
    private ArrayList<History> listData;
    private ProgressView progressView;
    private boolean isClearData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        presenter = new HistoryPresenter(this);
        initToolbar(R.id.history_toolbar, null);
        initProgressView();
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView() {
        progressView = new ProgressView(this, null);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again), getString(R.string.loading_data), Color.WHITE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new HistoryAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getMember();
            }
        });

        progressView.onRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isClearData = true;
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getMember();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getMember();
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
                presenter.getMember();
            }
        });
    }

//    Kiểm tra xem danh sách member có trống không
    private void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            adapter.notifyDataSetChanged();
            progressView.showData();
        } else {
            progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again), getString(R.string.loading_data), Color.WHITE);
            progressView.hideData();
        }
    }

//    Sự kiện load danh sách member thành công
    @Override
    public void onGetHistorySuccess(ArrayList<History> arrayList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isClearData) {
                    listData.clear();
                    isClearData = false;
                }
                listData.addAll(arrayList);

                checkListData();
            }
        }, 1000);
    }

//    Sự kiện load danh sách member thất bại
    @Override
    public void onGetHistoryError(Error error) {
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
}