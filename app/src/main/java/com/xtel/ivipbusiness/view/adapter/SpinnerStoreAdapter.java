package com.xtel.ivipbusiness.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by vulclph03762 on 12/11/2016
 */

public class SpinnerStoreAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<SortStore> arrayList;

    private boolean isEnable = true, isOutline = false;

    public SpinnerStoreAdapter(Activity activity, ArrayList<SortStore> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolderDropdown viewHolder;
        if (convertView == null) {
            if (isOutline)
                convertView = inflater.inflate(R.layout.item_spinner_gender_dropdown_outline, parent, false);
            else
                convertView = inflater.inflate(R.layout.item_spinner_gender_dropdown, parent, false);
            viewHolder = new ViewHolderDropdown();

            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_txt_gender);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderDropdown) convertView.getTag();
        }

        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, arrayList.get(position).getName(), activity.getString(R.string.updating));

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            if (isOutline)
                convertView = inflater.inflate(R.layout.item_spinner_gender_outline, parent, false);
            else
                convertView = inflater.inflate(R.layout.item_spinner_gender_normal, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.view = convertView.findViewById(R.id.item_spinner_gender_view);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.item_spinner_gender_txt_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!isOutline) {
            if (isEnable)
                viewHolder.view.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
            else
                viewHolder.view.setBackgroundColor(activity.getResources().getColor(R.color.line_disable));
        } else
            viewHolder.view.setVisibility(View.GONE);

        WidgetHelper.getInstance().setTextViewWithResult(viewHolder.textView, arrayList.get(position).getName(), activity.getString(R.string.updating));

        return convertView;
    }

    private class ViewHolder {
        private View view;
        private TextView textView;
    }

    private class ViewHolderDropdown {
        private TextView textView;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
        notifyDataSetChanged();
    }

    public void setOutline(boolean outline) {
        isOutline = outline;
    }
}
