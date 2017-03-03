package com.xtel.ivipbusiness.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.view.widget.TouchImageView;
import com.xtel.sdk.commons.Constants;
import com.xtel.sdk.utils.WidgetHelper;

/**
 * Created by Vulcl on 3/3/2017
 */

public class ItemImageFragment extends Fragment {

    public static ItemImageFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(Constants.MODEL, url);
        ItemImageFragment fragment = new ItemImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = getArguments().getString(Constants.MODEL);
        final TouchImageView imageView = (TouchImageView) view.findViewById(R.id.item_image_img);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.item_image_progress_bar);

        if (progressBar != null)
            progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#5c5ca7"), android.graphics.PorterDuff.Mode.MULTIPLY);

        if (url != null) {
            WidgetHelper.getInstance().setImageURL(imageView, url, new Callback() {
                @Override
                public void onSuccess() {
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    if (progressBar != null)
                        progressBar.setVisibility(View.GONE);
                    imageView.setImageResource(R.mipmap.ic_error);
                }
            });
        } else
            imageView.setImageResource(R.mipmap.ic_error);
    }
}