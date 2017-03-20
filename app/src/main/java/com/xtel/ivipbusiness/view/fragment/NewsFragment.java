package com.xtel.ivipbusiness.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.presenter.NewsPresenter;
import com.xtel.ivipbusiness.view.activity.AddNewsActivity;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.adapter.NewsAdapter;
import com.xtel.ivipbusiness.view.fragment.inf.INewsView;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;

import java.util.ArrayList;

/**
 * Created by Vulcl on 1/21/2017
 */

public class NewsFragment extends BasicFragment implements INewsView {
    protected NewsPresenter presenter;
    protected CallbackManager callbackManager;

    protected NewsAdapter adapter;
    protected ArrayList<News> listData;
    protected ProgressView progressView;
    protected boolean isClearData = false;
    
    protected final static int REQUEST_ADD_NEWS = 22;

    public static NewsFragment newInstance() {
        return new NewsFragment();
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
        initFloatingActionButton();
    }

    //    Khởi tạo layout và recyclerview
    protected void initProgressView(View view) {
        progressView = new ProgressView(null, view);
        progressView.initData(-1, getString(R.string.no_news), getString(R.string.click_to_try_again));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new NewsAdapter(getContext().getApplicationContext(), this, listData);
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
                getDataAgain();
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
    }

    protected void getDataAgain() {
        isClearData = true;
        adapter.setLoadMore(false);
        adapter.notifyDataSetChanged();
        progressView.setRefreshing(true);
        progressView.showData();
        presenter.getNews(true);
    }

    //    Kiểm tra xem danh sách cửa hàng có trống không
    protected void checkListData() {
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

    protected void initFloatingActionButton() {
        FloatingActionButton fab = findFloatingActionButton(R.id.news_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AddNewsActivity.class, REQUEST_ADD_NEWS);
            }
        });
    }

    @Override
    public void onGetDataError() {
        progressView.setRefreshing(false);
        progressView.updateData(-1, getString(R.string.have_error), getString(R.string.click_to_try_again));
        progressView.hideData();
    }

//    Load data của page tiếp theo
    @Override
    public void onLoadMore() {
        presenter.getNews(false);
    }

    /* Khi lấy dữ liệu của page thành công */
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
    public void deleteNews(final int id,final int position) {
        showMaterialDialog(false, false, null, getString(R.string.delete_this_news), getString(R.string.delete), getString(R.string.cancel), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                showProgressBar(false, false, null, getString(R.string.doing_delete_news));
                presenter.deleteNews(id, position);
            }

            @Override
            public void positiveClicked() {
                closeDialog();
            }
        });
    }

    @Override
    public void onDeleteNewsSuccess(int position) {
        closeProgressBar();
        adapter.removeNews(position);
    }

    /* Khi request lên server lỗi */
    @Override
    public void onRequestError(Error error) {
        if (listData.size() > 0)
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateData(-1, getString(R.string.error_try_again), getString(R.string.click_to_try_again));
            progressView.hideData();

            listData.clear();
            adapter.notifyDataSetChanged();
            if (isClearData)
                isClearData = false;
        }
    }

    /* Lấy phiên làm việc mới */
    @Override
    public void getNewSession(final ICmd iCmd, final Object... params) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(params);
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
    public void startActivity(Class clazz, String key, Object object) {
        super.startActivity(clazz, key, object);
    }

    /* Sự kiện khi không có internet */
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_NEWS && resultCode == Activity.RESULT_OK)
            getDataAgain();
    }
}