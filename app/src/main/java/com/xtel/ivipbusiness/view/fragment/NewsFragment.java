package com.xtel.ivipbusiness.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.NewsPresenter;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.adapter.NewsAdapter;
import com.xtel.ivipbusiness.view.fragment.inf.INewsView;
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
 * Created by Vulcl on 1/21/2017
 */

public class NewsFragment extends BasicFragment implements INewsView {
    private NewsPresenter presenter;
    private CallbackManager callbackManager;

    private NewsAdapter adapter;
    private ArrayList<News> listData;
    private ProgressView progressView;
    private boolean isClearData = false;

    public static NewsFragment newInstance(SortStore sortStore) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.MODEL, sortStore);

        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.create(getActivity());

        presenter = new NewsPresenter(this);
        initProgressView(view);
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_news), getString(R.string.click_to_try_again));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new NewsAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getNews(true);
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
                presenter.getNews(true);
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getNews(true);
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
//                presenter.getChains();
            }
        });
    }

    //    Kiểm tra xem danh sách cửa hàng có trống không
    private void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            if (listData.size() < 20)
                adapter.setLoadMore(false);

            progressView.showData();
            adapter.notifyDataSetChanged();
        } else {
            progressView.initData(-1, getString(R.string.no_news), getString(R.string.click_to_try_again));
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
    public void onLoadMore() {
        presenter.getNews(false);
    }

    @Override
    public void onGetNewsSuccess(final ArrayList<News> arrayList) {
        if (arrayList.size() < 10)
            adapter.setLoadMore(false);

        if (isClearData) {
            listData.clear();
            adapter.setLoadMore(true);
            isClearData = false;
        }
        listData.addAll(arrayList);

        checkListData();
    }

    @Override
    public void onRequestError(Error error) {
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
    public void getNewSession(final ICmd iCmd, final int type) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(type);
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
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.setExists(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}