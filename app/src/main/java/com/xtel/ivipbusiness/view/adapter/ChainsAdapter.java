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

import com.squareup.picasso.Picasso;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.activity.inf.IChainsView;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ChainsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SortStore> arrayList;
    private IChainsView _view;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public ChainsAdapter(IChainsView view, ArrayList<SortStore> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
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

            if (stores.getLogo() != null && !stores.getLogo().isEmpty())
                Picasso.with(_view.getActivity())
                        .load(stores.getLogo())
                        .noPlaceholder()
                        .fit().centerCrop().into(viewHolder.img_avatar);

            viewHolder.txt_name.setText(stores.getName());

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

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_avatar;
        private TextView txt_name;

        ViewHolder(View itemView) {
            super(itemView);

            img_avatar = (ImageView) itemView.findViewById(R.id.item_chain_img_avatar);
            txt_name = (TextView) itemView.findViewById(R.id.item_chain_txt_name);
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
