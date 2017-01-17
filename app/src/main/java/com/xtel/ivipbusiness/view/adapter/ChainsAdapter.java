package com.xtel.ivipbusiness.view.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Stores;
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.activity.inf.IChainsView;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class ChainsAdapter extends RecyclerView.Adapter<ChainsAdapter.ViewHolder> {
    private ArrayList<Stores> arrayList;
    private IChainsView _view;

    public ChainsAdapter(IChainsView view, ArrayList<Stores> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_stores, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stores stores = arrayList.get(position);

        if (stores.getImage() != null && !stores.getImage().isEmpty())
            Picasso.with(_view.getActivity())
                    .load(stores.getImage())
                    .noPlaceholder()
                    .fit().centerCrop().into(holder.img_avatar);

        holder.txt_name.setText(stores.getName());

        holder.itemView.setOnClickListener(v -> {
            _view.getActivity().startActivity(new Intent(_view.getActivity(), ViewStoreActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_avatar;
        private TextView txt_name;

        ViewHolder(View itemView) {
            super(itemView);

            img_avatar = (ImageView) itemView.findViewById(R.id.item_list_store_img_avatar);
            txt_name = (TextView) itemView.findViewById(R.id.item_list_store_txt_name);
        }
    }
}
