package com.xtel.sdk.utils;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Lê Công Long Vũ on 1/3/2017
 */

public class FindView {

//    Activity
    public static DrawerLayout findDrawerLayout(Activity activity, int id) {
        return (DrawerLayout) activity.findViewById(id);
    }

    public static RecyclerView findRecyclerView(Activity activity, int id) {
        return (RecyclerView) activity.findViewById(id);
    }

    public static EditText findEditText(Activity activity, int id) {
        return (EditText) activity.findViewById(id);
    }

    public static TextView findTextView(Activity activity, int id) {
        return (TextView) activity.findViewById(id);
    }

    public static Button findButton(Activity activity, int id) {
        return (Button) activity.findViewById(id);
    }

    public static CheckBox findCheckBox(Activity activity, int id) {
        return (CheckBox) activity.findViewById(id);
    }

    public static Spinner findSpinner(Activity activity, int id) {
        return (Spinner) activity.findViewById(id);
    }

    public static ProgressBar findProgressBar(Activity activity, int id) {
        return (ProgressBar) activity.findViewById(id);
    }

    public static SeekBar findSeekBar(Activity activity, int id) {
        return (SeekBar) activity.findViewById(id);
    }

    public static RatingBar findRatingBar(Activity activity, int id) {
        return (RatingBar) activity.findViewById(id);
    }


//    Fragment
    public static DrawerLayout findDrawerLayout(View View, int id) {
        return (DrawerLayout) View.findViewById(id);
    }

    public static RecyclerView findRecyclerView(View View, int id) {
        return (RecyclerView) View.findViewById(id);
    }

    public static EditText findEditText(View View, int id) {
        return (EditText) View.findViewById(id);
    }

    public static TextView findTextView(View View, int id) {
        return (TextView) View.findViewById(id);
    }

    public static Button findButton(View View, int id) {
        return (Button) View.findViewById(id);
    }

    public static CheckBox findCheckBox(View View, int id) {
        return (CheckBox) View.findViewById(id);
    }

    public static Spinner findSpinner(View View, int id) {
        return (Spinner) View.findViewById(id);
    }

    public static ProgressBar findProgressBar(View View, int id) {
        return (ProgressBar) View.findViewById(id);
    }

    public static SeekBar findSeekBar(View View, int id) {
        return (SeekBar) View.findViewById(id);
    }

    public static RatingBar findRatingBar(View View, int id) {
        return (RatingBar) View.findViewById(id);
    }
}