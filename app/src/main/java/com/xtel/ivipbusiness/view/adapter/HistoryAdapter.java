package com.xtel.ivipbusiness.view.adapter;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.History;
import com.xtel.ivipbusiness.view.activity.inf.IHistoryView;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.util.ArrayList;

/**
 * Created by Mr. M.2 on 1/19/2017
 */

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<History> arrayList;
    private IHistoryView _view;

    private boolean isLoadMore = true;
    private final int TYPE_TITLE = 1, TYPE_VIEW = 2, TYPE_LOAD = 3;

    public HistoryAdapter(IHistoryView view, ArrayList<History> arrayList) {
        this.arrayList = arrayList;
        this._view = view;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE)
            return new ViewHolderTitle(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_title, parent, false));
        else if (viewType == TYPE_VIEW)
            return new ViewHolderView(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
        else if (viewType == TYPE_LOAD)
            return new ViewProgressBar(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progressbar, parent, false));
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == arrayList.size())
            _view.onLoadMore();

        if (holder instanceof ViewHolderTitle) {
            ViewHolderTitle viewHolder = (ViewHolderTitle) holder;

            WidgetHelper.getInstance().setTextViewHistoryDate(viewHolder.txt_date, viewHolder.view_line, arrayList.get(position).getDate());
        } else if (holder instanceof ViewHolderView) {
            ViewHolderView viewHolder = (ViewHolderView) holder;
            History history = arrayList.get(position);

            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_date, arrayList.get(position).getTime(), _view.getActivity().getString(R.string.updating));
            WidgetHelper.getInstance().setTextViewWithResult(viewHolder.txt_content, history.getAction_name(), _view.getActivity().getString(R.string.updating));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
        else if (arrayList.get(position).isTitle())
            return TYPE_TITLE;
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

    private class ViewHolderTitle extends ViewHolderHelper {
        private TextView txt_date;
        private View view_line;

        ViewHolderTitle(View itemView) {
            super(itemView);

            txt_date = findTextView(R.id.item_history_title_txt_date);
            view_line = findView(R.id.item_history_title_view);
        }
    }

    private class ViewHolderView extends ViewHolderHelper {
        private TextView txt_date, txt_content;

        ViewHolderView(View itemView) {
            super(itemView);

            txt_date = findTextView(R.id.item_history_txt_date);
            txt_content = findTextView(R.id.item_history_txt_name);
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

    public void notifyChange() {
        new SortHistory().execute();
    }

    private class SortHistory extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (arrayList.size() == 0)
                return null;

            for (int i = arrayList.size() - 1; i >= 0; i--) {
                if (arrayList.get(i).getAction_time() == null)
                    arrayList.remove(i);
            }

            for (int i = arrayList.size() - 1; i >= 0; i--) {
                arrayList.get(i).setDate(WidgetHelper.getInstance().getDate(arrayList.get(i).getAction_time()));
                arrayList.get(i).setTime(WidgetHelper.getInstance().getTime(arrayList.get(i).getAction_time()));
            }

            for (int i = arrayList.size() - 1; i > 0; i--) {
                if (!arrayList.get(i).getDate().equals(arrayList.get((i - 1)).getDate())) {
                    arrayList.add(i, new History(true, arrayList.get(i).getDate()));
                }
            }

            History history = arrayList.get(0);
            arrayList.add(0, new History(true, history.getDate()));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            notifyDataSetChanged();
        }
    }
}
