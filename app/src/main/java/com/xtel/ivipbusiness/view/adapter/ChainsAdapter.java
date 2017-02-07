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
import com.xtel.ivipbusiness.view.fragment.inf.IChainsView;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ChainsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SortStore> arrayList;
    private IChainsView _view;

    private int bg_pos = 0 ;
    private int[] background_item;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public ChainsAdapter(IChainsView view, ArrayList<SortStore> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
        background_item = new int[]{R.drawable.item_background_1, R.drawable.item_background_2, R.drawable.item_background_3, R.drawable.item_background_4, R.drawable.item_background_5,
                R.drawable.item_background_6, R.drawable.item_background_7, R.drawable.item_background_8, R.drawable.item_background_9};
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
        if (position == arrayList.size())
            _view.onLoadMore();
        if (holder instanceof ViewHolder) {
            if (bg_pos == 9)
                bg_pos = 0;

            if (arrayList.get(position).getBg_id() == 0)
                arrayList.get(position).setBg_id(background_item[bg_pos]);

            ViewHolder viewHolder = (ViewHolder) holder;
            SortStore stores = arrayList.get(position);

            WidgetHelper.getInstance().setImageURL(viewHolder.img_banner, stores.getBanner());
            WidgetHelper.getInstance().setImageURL(viewHolder.img_avatar, stores.getLogo());
            WidgetHelper.getInstance().setViewBackground(viewHolder.img_background, stores.getBg_id());

            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_name, stores.getName(), _view.getActivity().getString(R.string.not_update_name));
            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_address, stores.getAddress(), _view.getActivity().getString(R.string.not_update_address));

            bg_pos++;
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkInfo.isOnline(_view.getActivity())) {
                        _view.onNoNetwork();
                        return;
                    }

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
        private ImageView img_banner, img_avatar;
        private View img_background;
        private TextView txt_name, txt_address;

        ViewHolder(View itemView) {
            super(itemView);

            img_background = findView(R.id.item_chain_img_background);
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
        bg_pos = 0;
        this.isLoadMore = isLoadMore;
    }
}