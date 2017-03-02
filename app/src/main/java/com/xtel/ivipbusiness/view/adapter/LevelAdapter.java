package com.xtel.ivipbusiness.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.LevelObject;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Vulcl on 3/2/2017
 */

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {
    private ArrayList<LevelObject> arrayList;

    public LevelAdapter(ArrayList<LevelObject> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_level, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LevelObject levelObject = arrayList.get(position);

        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_stt, (position + 1));
        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_point, levelObject.getLevel_limit());
        WidgetHelper.getInstance().setTextViewNoResult(holder.txt_name, levelObject.getLevel_name());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends ViewHolderHelper {
        private TextView txt_stt, txt_point, txt_name;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_stt = findTextView(R.id.item_level_txt_stt);
            txt_point = findTextView(R.id.item_level_txt_point);
            txt_name = findTextView(R.id.item_level_txt_name);
        }
    }

    public void addLevel(LevelObject levelObject) {
        arrayList.add(levelObject);
        notifyItemInserted((arrayList.size() - 1));
        notifyItemRangeRemoved((arrayList.size() - 1), arrayList.size());
    }
}
