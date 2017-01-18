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
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.inf.IListStoreView;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ListStoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SortStore> arrayList;
    private IListStoreView _view;

    private int[] background_item;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public ListStoreAdapter(IListStoreView view, ArrayList<SortStore> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
        background_item = new int[]{R.mipmap.background_item_1, R.mipmap.background_item_2, R.mipmap.background_item_3, R.mipmap.background_item_4, R.mipmap.background_item_5,
                R.mipmap.background_item_6, R.mipmap.background_item_7, R.mipmap.background_item_8};
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chain, parent, false));
        else if (viewType == TYPE_LOAD)
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            SortStore stores = arrayList.get(position);

            WidgetHelper.getInstance().setImageURL(viewHolder.img_banner, stores.getBanner());
            WidgetHelper.getInstance().setImageURL(viewHolder.img_avatar, stores.getLogo());
            WidgetHelper.getInstance().setImageResource(viewHolder.img_background, background_item[Constants.randInt(0, 7)]);

            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_name, stores.getName(), _view.getActivity().getString(R.string.not_update_name));
            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_address, stores.getAddress(), _view.getActivity().getString(R.string.not_update_address));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_banner, img_avatar, img_background;
        private TextView txt_name, txt_address;

        ViewHolder(View itemView) {
            super(itemView);

            img_background = (ImageView) itemView.findViewById(R.id.item_chain_img_background);
            img_banner = (ImageView) itemView.findViewById(R.id.item_chain_img_banner);
            img_avatar = (ImageView) itemView.findViewById(R.id.item_chain_img_avatar);
            txt_name = (TextView) itemView.findViewById(R.id.item_chain_txt_name);
            txt_address = (TextView) itemView.findViewById(R.id.item_chain_txt_address);
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
