package com.xtel.ivipbusiness.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.view.fragment.inf.INewsView;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<News> arrayList;
    private INewsView _view;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public NewsAdapter(INewsView view, ArrayList<News> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
        else if (viewType == TYPE_LOAD)
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == arrayList.size())
            _view.onLoadMore();

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            News news = arrayList.get(position);

            WidgetHelper.getInstance().setImageURL(viewHolder.img_banner, news.getBanner());

            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_title, news.getTitle(), _view.getActivity().getString(R.string.not_update_title));
            WidgetHelper.getInstance().setTextViewDate(viewHolder.txt_date_create, _view.getActivity().getString(R.string.day_create) + ": ", news.getDate_create());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkInfo.isOnline(_view.getActivity())) {
                        _view.onNoNetwork();
                        return;
                    }
                }
            });
        } else {
            ViewProgressBar viewProgressBar = (ViewProgressBar) holder;
            viewProgressBar.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == arrayList.size())
            return TYPE_LOAD;
        else
            return TYPE_VIEW;
    }

    @Override
    public int getItemCount() {
        if (isLoadMore && arrayList.size() > 0)
            return arrayList.size() + 1;
        else
            return arrayList.size();
    }

    private class ViewHolder extends ViewHolderHelper {
        private ImageView img_banner;
        private TextView txt_title, txt_date_create, txt_view, txt_like, txt_share;

        ViewHolder(View itemView) {
            super(itemView);

            img_banner = findImageView(R.id.item_news_img_banner);
            txt_title = findTextView(R.id.item_news_txt_title);
            txt_date_create = findTextView(R.id.item_news_txt_day_create);
            txt_view = findTextView(R.id.item_news_txt_view);
            txt_like = findTextView(R.id.item_news_txt_like);
            txt_share = findTextView(R.id.item_news_txt_share);
        }
    }

    private class ViewProgressBar extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        ViewProgressBar(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.item_progress_bar);
        }
    }

    public void setLoadMore(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
    }
}