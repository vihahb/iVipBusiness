package com.xtel.ivipbusiness.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
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
 * Created by Vulcl on 1/13/2017
 */

public class ChainsFragment extends BasicFragment implements IChainsView {
    private ChainsPresenter presenter;

    private ChainsAdapter adapter;
    private ArrayList<SortStore> listData;
    private ProgressView progressView;
    private boolean isClearData = false;
    private CallbackManager callbackManager;

    private final int REQUEST_ADD = 99;
    private final String STORE_TYPE = "CHAIN";

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
        initFloatingActionButton();
        initProgressView(view);
    }

    private void initFloatingActionButton() {
        FloatingActionButton fab = findFloatingActionButton(R.id.chain_fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), AddStoreActivity.class);
//                intent.putExtra(Constants.MODEL, STORE_TYPE);
                startActivityForResult(AddStoreActivity.class, Constants.MODEL, STORE_TYPE, REQUEST_ADD);
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
                presenter.getChains();
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
                presenter.getChains();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getChains();
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
            if (listData.size() < 10)
                adapter.setLoadMore(false);

            progressView.showData();
            adapter.notifyDataSetChanged();
        } else {
            progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again), getString(R.string.loading_data), Color.WHITE);
            progressView.hideData();
        }
    }

    @Override
    public void onLoadMore() {
        presenter.getChains();
    }

    //    Sự kiện load danh sách store thành công
    @Override
    public void onGetStoresSuccess(final ArrayList<SortStore> arrayList) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isClearData) {
                    listData.clear();
                    adapter.setLoadMore(true);
                    isClearData = false;
                }
                listData.addAll(arrayList);

                checkListData();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}