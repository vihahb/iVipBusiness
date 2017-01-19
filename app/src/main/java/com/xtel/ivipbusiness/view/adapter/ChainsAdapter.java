package com.xtel.ivipbusiness.view.adapter;

import android.content.Intent;
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
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.activity.inf.IChainsView;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ChainsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SortStore> arrayList;
    private IChainsView _view;

    private int[] background_item;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public ChainsAdapter(IChainsView view, ArrayList<SortStore> arrayList) {
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
            if (arrayList.get(position).getBg_id() == 0)
                arrayList.get(position).setBg_id(background_item[Constants.randInt(1, 7)]);

            ViewHolder viewHolder = (ViewHolder) holder;
            SortStore stores = arrayList.get(position);

            WidgetHelper.getInstance().setImageURL(viewHolder.img_banner, stores.getBanner());
            WidgetHelper.getInstance().setImageURL(viewHolder.img_avatar, stores.getLogo());
            WidgetHelper.getInstance().setImageResource(viewHolder.img_background, stores.getBg_id());

            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_name, stores.getName(), _view.getActivity().getString(R.string.not_update_name));
            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_address, stores.getAddress(), _view.getActivity().getString(R.string.not_update_address));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _view.getActivity().startActivity(new Intent(_view.getActivity(), ViewStoreActivity.class));
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
        private ImageView img_banner, img_avatar, img_background;
        private TextView txt_name, txt_address;

        ViewHolder(View itemView) {
            super(itemView);

            img_background = findImageView(R.id.item_chain_img_background);
            img_banner = findImageView(R.id.item_chain_img_banner);
            img_avatar = findImageView(R.id.item_chain_img_avatar);
            txt_name = findTextView(R.id.item_chain_txt_name);
            txt_address = findTextView(R.id.item_chain_txt_address);
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