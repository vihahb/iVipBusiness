package com.xtel.ivipbusiness.view.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Gallery;
import com.xtel.ivipbusiness.model.entity.RESP_Gallery;
import com.xtel.ivipbusiness.view.activity.ShowImageActivity;
import com.xtel.ivipbusiness.view.fragment.inf.IGalleryView;
import com.xtel.nipservicesdk.utils.JsonHelper;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Gallery> arrayList;
    private IGalleryView _view;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public GalleryAdapter(IGalleryView view, ArrayList<Gallery> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false));
        else if (viewType == TYPE_LOAD)
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int _position = position;
        if (isLoadMore && position == arrayList.size())
            _view.onLoadMore();

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final Gallery gallery = arrayList.get(position);

            WidgetHelper.getInstance().setImageURL(viewHolder.img_gallery, gallery.getUrl());
            viewHolder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkInfo.isOnline(_view.getActivity())) {
                        _view.onNoNetwork();
                        return;
                    }

                    _view.onDeleteGallery(gallery.getId(), _position);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkInfo.isOnline(_view.getActivity())) {
                        _view.onNoNetwork();
                        return;
                    }

                    RESP_Gallery resp_gallery = new RESP_Gallery();
                    resp_gallery.setData(arrayList);
                    resp_gallery.setPosition(_position);
                    _view.startActivity(ShowImageActivity.class, Constants.MODEL, JsonHelper.toJson(resp_gallery));
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
        private ImageView img_gallery;
        private ImageButton img_delete;

        ViewHolder(View itemView) {
            super(itemView);

            img_gallery = findImageView(R.id.item_gallery_img);
            img_delete = findImageButton(R.id.item_gallery_img_delete);
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

    public void deleteGallery(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemChanged(position, arrayList.size());
    }
}
