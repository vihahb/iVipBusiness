package com.xtel.ivipbusiness.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Area;

import java.util.ArrayList;

/**
 * Created by Vulcl on 3/9/2017
 */

public class AreaAdapter extends ArrayAdapter<Area> {
    private ArrayList<Area> items;
    private ArrayList<Area> itemsAll;
    private ArrayList<Area> suggestions;
    private int viewResourceId;
    private LayoutInflater inflater;

    @SuppressWarnings("unchecked")
    public AreaAdapter(Context context, int viewResourceId, ArrayList<Area> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<Area>) items.clone();
        this.suggestions = new ArrayList<Area>();
        this.viewResourceId = viewResourceId;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_spinner_area_dropdown, parent, false);

        Area product = items.get(position);
        if (product != null) {
            TextView productLabel = (TextView) convertView.findViewById(R.id.item_spinner_area_txt);
            productLabel.setText(product.getName());

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.item_spinner_area_chk);
            checkBox.setChecked(items.get(position).isSelected());

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    items.get(position).setSelected(isChecked);
                }
            });
        }
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        public String convertResultToString(Object resultValue) {
            String str = ((Area) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Area product : itemsAll) {
                    if (product.getName().toLowerCase()
                            .startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(product);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            @SuppressWarnings("unchecked")
            ArrayList<Area> filteredList = (ArrayList<Area>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Area c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}