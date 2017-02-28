package com.xtel.ivipbusiness.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.News;
import com.xtel.ivipbusiness.view.activity.UpdateNewsActivity;
import com.xtel.ivipbusiness.view.fragment.inf.INewsView;
import com.xtel.sdk.commons.Constants;
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

    private int bg_pos = 0 ;
    private int[] background_item;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public NewsAdapter(Context context, INewsView view, ArrayList<News> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
        background_item = context.getResources().getIntArray(R.array.news_background);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == arrayList.size())
            _view.onLoadMore();

        if (holder instanceof ViewHolder) {
            if (bg_pos == 9)
                bg_pos = 0;

            if (arrayList.get(position).getBg_id() == 0)
                arrayList.get(position).setBg_id(background_item[bg_pos]);

            ViewHolder viewHolder = (ViewHolder) holder;
            final News news = arrayList.get(position);

            if (news.is_public())
                WidgetHelper.getInstance().setTextViewDrawable(viewHolder.txt_date_create, 2, R.mipmap.ic_world_white_18);
            else
                WidgetHelper.getInstance().setTextViewDrawable(viewHolder.txt_date_create, 2, R.mipmap.ic_private_gray_18);

            WidgetHelper.getInstance().setImageURL(viewHolder.img_banner, news.getBanner());
            WidgetHelper.getInstance().setViewBackgroundColor(viewHolder.img_background, news.getBg_id());

            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_title, news.getTitle(), _view.getActivity().getString(R.string.not_update_title));
            WidgetHelper.getInstance().setTextViewDate(viewHolder.txt_date_create, _view.getActivity().getString(R.string.day_create) + ": ", (news.getCreate_time() * 1000));
            viewHolder.txt_view.setText(String.valueOf(news.getView()));
            viewHolder.txt_like.setText(String.valueOf(news.getLike()));
            viewHolder.txt_share.setText(String.valueOf(news.getShare()));

            bg_pos++;

            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkInfo.isOnline(_view.getActivity())) {
                        _view.showShortToast(_view.getActivity().getString(R.string.error_no_internet));
                        return;
                    }

                    _view.deleteNews(news.getId(), position);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkInfo.isOnline(_view.getActivity())) {
                        _view.showShortToast(_view.getActivity().getString(R.string.error_no_internet));
                        return;
                    }

                    _view.startActivity(UpdateNewsActivity.class, Constants.MODEL, news);
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
        private ImageButton img_delete;
        private ImageView img_banner;
        private View img_background;
        private TextView txt_title, txt_date_create, txt_view, txt_like, txt_share;

        ViewHolder(View itemView) {
            super(itemView);

            img_delete = findImageButton(R.id.item_news_img_delete);
            img_background = findView(R.id.item_news_img_background);
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

    public void removeNews(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, arrayList.size());
    }

    public void setLoadMore(boolean isLoadMore) {
        bg_pos = 0;
        this.isLoadMore = isLoadMore;
    }
}