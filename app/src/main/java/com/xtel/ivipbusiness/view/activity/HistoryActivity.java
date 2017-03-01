package com.xtel.ivipbusiness.view.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.History;
import com.xtel.ivipbusiness.model.entity.Member;
import com.xtel.ivipbusiness.presenter.HistoryPresenter;
import com.xtel.ivipbusiness.view.activity.inf.IHistoryView;
import com.xtel.ivipbusiness.view.adapter.HistoryAdapter;
import com.xtel.ivipbusiness.view.widget.ProgressView;
import com.xtel.ivipbusiness.view.widget.RecyclerOnScrollListener;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.callback.DialogListener;
import com.xtel.sdk.utils.WidgetHelper;

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
        presenter.getMemberInfo();
    }

    //    Khởi tạo layout và recyclerview
    private void initProgressView() {
        progressView = new ProgressView(this, null);
        progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        listData = new ArrayList<>();
        adapter = new HistoryAdapter(this, listData);
        progressView.setUpRecyclerView(layoutManager, adapter);

        progressView.onLayoutClicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getMemberHistory();
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
                presenter.getMemberHistory();
            }
        });

        progressView.onSwipeLayoutPost(new Runnable() {
            @Override
            public void run() {
                progressView.setRefreshing(true);
                progressView.showData();
                presenter.getMemberHistory();
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
//                presenter.getMember();
            }
        });
    }

//    Kiểm tra xem danh sách member có trống không
    private void checkListData() {
        progressView.setRefreshing(false);

        if (listData.size() > 0) {
            if (listData.size() < 20)
                adapter.setLoadMore(false);

            adapter.notifyDataSetChanged();
            progressView.showData();
        } else {
            progressView.initData(-1, getString(R.string.no_stores), getString(R.string.click_to_try_again));
            progressView.hideData();
        }
    }

    @Override
    public void onGetMemberSuccess(Member member) {
        ImageView imageView = (ImageView) findImageView(R.id.history_img_avatar);
        TextView textView = findTextView(R.id.history_txt_fullname);

        WidgetHelper.getInstance().setImageURL(imageView, member.getAvatar());
        WidgetHelper.getInstance().setTextViewWithResult(textView, member.getFullname(), getString(R.string.not_update_name));

        initProgressView();
    }

    @Override
    public void onGetMemberError() {
        showMaterialDialog(false, false, null, getString(R.string.error_try_again), null, getString(R.string.back), new DialogListener() {
            @Override
            public void negativeClicked() {
                closeDialog();
                finish();
            }

            @Override
            public void positiveClicked() {
                closeDialog();
                finish();
            }
        });
    }

    @Override
    public void onLoadMore() {
        presenter.getMemberHistory();
    }

    //    Sự kiện load danh sách member thành công
    @Override
    public void onGetHistorySuccess(final ArrayList<History> arrayList) {
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

//    Sự kiện load danh sách member thất bại
    @Override
    public void onGetHistoryError(Error error) {
        if (listData.size() > 0)
            showShortToast(JsonParse.getCodeMessage(error.getCode(), getString(R.string.have_error)));
        else {
            progressView.setRefreshing(false);
            progressView.updateData(-1, JsonParse.getCodeMessage(error.getCode(), getString(R.string.have_error)), getString(R.string.click_to_try_again));
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