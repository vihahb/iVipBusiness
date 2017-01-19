package com.xtel.sdk.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Vulcl on 1/19/2017
 */

public abstract class ViewHolderHelper extends RecyclerView.ViewHolder {

    public ViewHolderHelper(View itemView) {
        super(itemView);
    }

    protected EditText findEditText(int id) {
        return (EditText) itemView.findViewById(id);
    }

    protected TextView findTextView(int id) {
        return (TextView) itemView.findViewById(id);
    }

    protected Button findButton(int id) {
        return (Button) itemView.findViewById(id);
    }

    protected CheckBox findCheckBox(int id) {
        return (CheckBox) itemView.findViewById(id);
    }

    protected Spinner findSpinner(int id) {
        return (Spinner) itemView.findViewById(id);
    }

    protected ProgressBar findProgressBar(int id) {
        return (ProgressBar) itemView.findViewById(id);
    }

    protected SeekBar findSeekBar(int id) {
        return (SeekBar) itemView.findViewById(id);
    }

    protected RatingBar findRatingBar(int id) {
        return (RatingBar) itemView.findViewById(id);
    }

    protected ImageView findImageView(int id) {
        return (ImageView) itemView.findViewById(id);
    }
}
