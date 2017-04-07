package com.xtel.ivipbusiness.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.DataObj;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.presenter.StatisticPresenter;
import com.xtel.ivipbusiness.view.activity.LoginActivity;
import com.xtel.ivipbusiness.view.adapter.SpinnerStoreAdapter;
import com.xtel.ivipbusiness.view.fragment.inf.IStatisticView;
import com.xtel.sdk.utils.LineChartView;
import com.xtel.nipservicesdk.CallbackManager;
import com.xtel.nipservicesdk.callback.CallbacListener;
import com.xtel.nipservicesdk.callback.ICmd;
import com.xtel.nipservicesdk.model.entity.Error;
import com.xtel.nipservicesdk.model.entity.RESP_Login;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.nipservicesdk.utils.JsonParse;
import com.xtel.sdk.utils.NetWorkInfo;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/10/2017
 */

public class StatisticFragment extends BasicFragment implements View.OnClickListener, IStatisticView {
    protected StatisticPresenter presenter;
    protected CallbackManager callbackManager;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ImageView img_seven, img_thirday;
    protected TextView noti_textview, txt_checkin_title, txt_buy_title, txt_compare_title;
    protected Spinner sp_store;
    protected View layout_day;
    protected LineChartView chart_checkin, chart_buy, chart_all;

    protected int DAY = 7;
    protected ArrayList<SortStore> arrayList;

    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistic, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new StatisticPresenter(this);
        callbackManager = CallbackManager.create(getActivity());

        arrayList = new ArrayList<>();
        initView();
        initLineChart(view);
        initSwipeLayout();

        presenter.getListStore();
    }

    //    Khởi tạo các view
    protected void initView() {
        swipeRefreshLayout = findSwipeRefreshLayout(R.id.statistic_swipe);

        noti_textview = findTextView(R.id.statistic_noti_txt);
        txt_checkin_title = findTextView(R.id.statistic_txt_checkin_title);
        txt_buy_title = findTextView(R.id.statistic_txt_buy_title);
        txt_compare_title = findTextView(R.id.statistic_txt_compare_title);

        sp_store = findSpinner(R.id.statistic_sp_store);
        layout_day = findLinearLayout(R.id.statistic_layout_header);

        img_seven = findImageView(R.id.statistic_img_seven);
        img_thirday = findImageView(R.id.statistic_img_thirty);
        Button btn_seven = findButton(R.id.statistic_btn_seven);
        Button btn_thirty = findButton(R.id.statistic_btn_thirty);

        btn_seven.setOnClickListener(this);
        btn_thirty.setOnClickListener(this);
        noti_textview.setOnClickListener(this);
    }

    protected void initLineChart(View view) {
        chart_checkin = new LineChartView((LineChart) view.findViewById(R.id.statistic_chart_checkin));
        chart_buy = new LineChartView((LineChart) view.findViewById(R.id.statistic_chart_buy));
        chart_all = new LineChartView((LineChart) view.findViewById(R.id.statistic_chart_all));
    }

    //    Khởi tạo layout với swipe
    protected void initSwipeLayout() {
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
//        swipeRefreshLayout.setRefreshing(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStatistic();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getStatistic();
            }
        });
    }

    //    Khởi tạo danh sách các cửa hàng
    protected void initSpinner() {
        Log.e("initSpinner", JsonHelper.toJson(arrayList));
        sp_store.setVisibility(View.VISIBLE);
        layout_day.setVisibility(View.VISIBLE);

        SpinnerStoreAdapter adapter = new SpinnerStoreAdapter(getActivity(), arrayList);
        sp_store.setAdapter(adapter);

        sp_store.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStatistic();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    protected void getStatistic() {
        if (!NetWorkInfo.isOnline(getActivity())) {
            onNoInternet();
            return;
        }

        if (arrayList.size() > 0)
            presenter.getStatistic(arrayList.get(sp_store.getSelectedItemPosition()), DAY);

        swipeRefreshLayout.setRefreshing(true);
    }

    //    Hiện thông báo lỗi
    protected void hideError() {
        chart_checkin.setVisibility(View.VISIBLE);
        chart_buy.setVisibility(View.VISIBLE);
        chart_all.setVisibility(View.VISIBLE);
        txt_checkin_title.setVisibility(View.VISIBLE);
        txt_buy_title.setVisibility(View.VISIBLE);
        txt_compare_title.setVisibility(View.VISIBLE);

        sp_store.setVisibility(View.VISIBLE);
        noti_textview.setVisibility(View.GONE);
    }

    //    Ẩn thông báo lỗi
    protected void showError(String message) {
        noti_textview.setText(message);

        chart_checkin.setVisibility(View.GONE);
        chart_buy.setVisibility(View.GONE);
        chart_all.setVisibility(View.GONE);
        txt_checkin_title.setVisibility(View.GONE);
        txt_buy_title.setVisibility(View.GONE);
        txt_compare_title.setVisibility(View.GONE);

        noti_textview.setVisibility(View.VISIBLE);

        if (arrayList.size() == 0)
            sp_store.setVisibility(View.GONE);
    }

    protected void changeDay(int day) {
        if (day == 7) {
            DAY = 7;
            img_seven.setVisibility(View.VISIBLE);
            img_thirday.setVisibility(View.GONE);
        } else {
            DAY = 30;
            img_seven.setVisibility(View.GONE);
            img_thirday.setVisibility(View.VISIBLE);
        }

        getStatistic();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.statistic_btn_seven:
                if (swipeRefreshLayout.isRefreshing())
                    break;
                changeDay(7);
                break;
            case R.id.statistic_btn_thirty:
                if (swipeRefreshLayout.isRefreshing())
                    break;
                changeDay(30);
                break;
            case R.id.statistic_noti_txt:
                getStatistic();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetStoresSuccess(ArrayList<SortStore> arrayList) {
        if (arrayList.size() > 0) {
            if (arrayList.size() == 10)
                presenter.getListStore();
            else {
                hideError();
                initSpinner();
            }

            this.arrayList.addAll(arrayList);
        } else {
            if (this.arrayList.size() == 0)
                showError(getString(R.string.error_not_store));
            else {
                hideError();
                initSpinner();
            }
        }
    }

    @Override
    public void onGetStatistic(int action, ArrayList<DataObj> arrayList) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);

        Log.e("onGetStatistic", "action   " + action + "       " + JsonHelper.toJson(arrayList));

        if (action == 1) {
            chart_checkin.updateDay(DAY);
            chart_checkin.setData(action, arrayList);
        } else if (action == 2) {
            chart_buy.updateDay(DAY);
            chart_buy.setData(action, arrayList);
        } else if (action == 3) {
            chart_all.updateDay(DAY);
            chart_all.setDoubleData(arrayList);
        }
    }

    @Override
    public void getNewSession(final ICmd iCmd, final Object... param) {
        callbackManager.getNewSesion(new CallbacListener() {
            @Override
            public void onSuccess(RESP_Login success) {
                iCmd.execute(param);
            }

            @Override
            public void onError(Error error) {
                closeProgressBar();
                showShortToast(getString(R.string.error_end_of_session));
                getActivity().finishAffinity();
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void onRequestError(Error error) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(false);
        showError(JsonParse.getCodeMessage(error.getCode(), getString(R.string.error_try_again)));
    }

    @Override
    public void onNoInternet() {
        showError(getString(R.string.error_no_internet));
    }

    @Override
    public Fragment getFragment() {
        return this;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        callbackManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}