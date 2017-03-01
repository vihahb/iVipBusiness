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
import com.xtel.ivipbusiness.model.entity.Notify;
import com.xtel.ivipbusiness.model.entity.SortStore;
import com.xtel.ivipbusiness.view.activity.ViewStoreActivity;
import com.xtel.ivipbusiness.view.activity.inf.INotifyView;
import com.xtel.ivipbusiness.view.fragment.inf.IChainsView;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class NotifyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Notify> arrayList;
    private INotifyView _view;

    private final int TYPE_PEOPLE = 1, TYPE_GROUP = 2, TYPE_MEMBER = 3;

//    private boolean isLoadMore = true;
//    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public NotifyAdapter(INotifyView view, ArrayList<Notify> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PEOPLE)
            return new ViewHolderPeople(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify_people, parent, false));
        else if (viewType == TYPE_GROUP)
            return new ViewHolderGroup(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify_group, parent, false));
        else if (viewType == TYPE_MEMBER)
            return new ViewHolderMember(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify_member, parent, false));

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Notify notify = arrayList.get(position);

        if (holder instanceof ViewHolderPeople) {
            ViewHolderPeople viewHolderPeople = (ViewHolderPeople) holder;

            WidgetHelper.getInstance().setTextViewDateTime(viewHolderPeople.txt_date, "", (notify.getCreate_time() * 1000));
        } else if (holder instanceof ViewHolderGroup) {
            ViewHolderGroup viewHolderGroup = (ViewHolderGroup) holder;

            WidgetHelper.getInstance().setTextViewGender(viewHolderGroup.txt_gender, "", notify.getNotify_condition().getGender());
            WidgetHelper.getInstance().setTextViewOneArea(viewHolderGroup.txt_area, "", notify.getNotify_condition().getAreas());
            WidgetHelper.getInstance().setTextViewAgeFromTo(viewHolderGroup.txt_age, "", notify.getNotify_condition().getFrom_age(), notify.getNotify_condition().getTo_age());

            WidgetHelper.getInstance().setTextViewDateTime(viewHolderGroup.txt_date, "", (notify.getCreate_time() * 1000));
        } else if (holder instanceof ViewHolderMember) {
            ViewHolderMember viewHolderMember = (ViewHolderMember) holder;

            WidgetHelper.getInstance().setTextViewGender(viewHolderMember.txt_gender, "", notify.getNotify_condition().getGender());
            WidgetHelper.getInstance().setTextViewOneArea(viewHolderMember.txt_area, "", notify.getNotify_condition().getAreas());
            WidgetHelper.getInstance().setTextViewOneLevel(viewHolderMember.txt_level, "", notify.getNotify_condition().getLevel());
            WidgetHelper.getInstance().setTextViewAgeFromTo(viewHolderMember.txt_age, "", notify.getNotify_condition().getFrom_age(), notify.getNotify_condition().getTo_age());

            WidgetHelper.getInstance().setTextViewDateTime(viewHolderMember.txt_date, "", (notify.getCreate_time() * 1000));
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (position == arrayList.size())
//            return TYPE_LOAD;
//        else
//            return TYPE_VIEW;
        return arrayList.get(position).getNotify_type();
    }

    @Override
    public int getItemCount() {
//        if (isLoadMore && arrayList.size() > 0) {
//            return arrayList.size() + 1;
//        } else
        return arrayList.size();
    }

    private class ViewHolderPeople extends ViewHolderHelper {
        private TextView txt_date;

        ViewHolderPeople(View itemView) {
            super(itemView);

            txt_date = findTextView(R.id.item_notify_txt_people_date);
        }
    }

    private class ViewHolderGroup extends ViewHolderHelper {
        private TextView txt_gender, txt_area, txt_age, txt_date;

        ViewHolderGroup(View itemView) {
            super(itemView);

            txt_gender = findTextView(R.id.item_notify_txt_group_gender);
            txt_area = findTextView(R.id.item_notify_txt_group_area);
            txt_age = findTextView(R.id.item_notify_txt_group_age);
            txt_date = findTextView(R.id.item_notify_txt_group_date);
        }
    }

    private class ViewHolderMember extends ViewHolderHelper {
        private TextView txt_level, txt_gender, txt_area, txt_age, txt_date;

        ViewHolderMember(View itemView) {
            super(itemView);

            txt_level = findTextView(R.id.item_notify_txt_member_level);
            txt_gender = findTextView(R.id.item_notify_txt_member_gender);
            txt_area = findTextView(R.id.item_notify_txt_member_area);
            txt_age = findTextView(R.id.item_notify_txt_member_age);
            txt_date = findTextView(R.id.item_notify_txt_member_date);
        }
    }

//    private class ViewProgressBar extends RecyclerView.ViewHolder {
//        private ProgressBar progressBar;
//
//        ViewProgressBar(View itemView) {
//            super(itemView);
//            progressBar = (ProgressBar) itemView.findViewById(R.id.item_progress_bar);
//        }
//    }

//    public void setLoadMore(boolean isLoadMore) {
//        bg_pos = 0;
//        this.isLoadMore = isLoadMore;
//    }
}