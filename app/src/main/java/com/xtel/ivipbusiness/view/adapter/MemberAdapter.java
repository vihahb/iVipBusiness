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
import com.xtel.ivipbusiness.model.entity.Member;
import com.xtel.ivipbusiness.view.activity.HistoryActivity;
import com.xtel.ivipbusiness.view.fragment.inf.IMemberView;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.NetWorkInfo;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/13/2017
 */

public class MemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Member> arrayList;
    private IMemberView _view;

    private boolean isLoadMore = true;
    private final int TYPE_VIEW = 1, TYPE_LOAD = 2;

    public MemberAdapter(IMemberView view, ArrayList<Member> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_VIEW)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false));
        else if (viewType == TYPE_LOAD)
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == arrayList.size())
            _view.onLoadMore();

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final Member member = arrayList.get(position);

            WidgetHelper.getInstance().setAvatarImageURL(viewHolder.img_avatar, member.getAvatar());
            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_fullname, member.getFullname(), _view.getActivity().getString(R.string.not_update_name));
            WidgetHelper.getInstance().setTextViewNoResult(viewHolder.txt_total_point, String.valueOf(member.getTotal_point()));
            WidgetHelper.getInstance().setTextViewNoResult(viewHolder.txt_remaining_point, String.valueOf(member.getCurrent_point()));
            WidgetHelper.getInstance().setTextViewNoResult(viewHolder.txt_total_checkin, _view.getActivity().getString(R.string.total_checkin), String.valueOf(member.getCheckin_number()));
            WidgetHelper.getInstance().setTextViewNoResult(viewHolder.txt_total_money, _view.getActivity().getString(R.string.total_money), String.valueOf(member.getTotal_money()));
            WidgetHelper.getInstance().setTextViewNoResult(viewHolder.txt_shopping_in_store, _view.getActivity().getString(R.string.total_shopping_in_store), String.valueOf(member.getBuy_number()));
            WidgetHelper.getInstance().setTextViewDate(viewHolder.txt_last_checkin,  _view.getActivity().getString(R.string.last_checkin) + ": ", member.getLast_checkin());

            viewHolder.txt_total_checkin.setSelected(true);
            viewHolder.txt_total_money.setSelected(true);
            viewHolder.txt_shopping_in_store.setSelected(true);
            viewHolder.txt_last_checkin.setSelected(true);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!NetWorkInfo.isOnline(_view.getActivity())) {
                        _view.onNoNetwork();
                        return;
                    }

                    Intent intent = new Intent(_view.getActivity(), HistoryActivity.class);
                    intent.putExtra(Constants.MODEL, member);
                    _view.getActivity().startActivity(intent);
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
        private ImageView img_avatar;
        private TextView txt_fullname, txt_total_point, txt_remaining_point, txt_total_checkin, txt_total_money, txt_shopping_in_store, txt_last_checkin;

        ViewHolder (View itemView) {
            super(itemView);

            img_avatar = findImageView(R.id.item_member_img_avatar);
            txt_fullname = findTextView(R.id.item_member_txt_fullname);
            txt_total_point = findTextView(R.id.item_member_txt_total_point);
            txt_remaining_point = findTextView(R.id.item_member_txt_remaining_point);
            txt_total_checkin = findTextView(R.id.item_member_txt_total_checkin);
            txt_total_money = findTextView(R.id.item_member_txt_total_money);
            txt_shopping_in_store = findTextView(R.id.item_member_txt_total_shopping_in_store);
            txt_last_checkin = findTextView(R.id.item_member_txt_last_checkin);
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