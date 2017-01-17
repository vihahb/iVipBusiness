package com.xtel.ivipbusiness.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Stores;
import com.xtel.ivipbusiness.view.activity.inf.IStoresView;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.ViewHolder> {
    private ArrayList<Stores> arrayList;
    private IStoresView _view;

    public StoresAdapter(IStoresView view, ArrayList<Stores> arrayList) {
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
