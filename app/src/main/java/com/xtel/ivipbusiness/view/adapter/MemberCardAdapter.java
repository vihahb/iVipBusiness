package com.xtel.ivipbusiness.view.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xtel.ivipbusiness.R;
import com.xtel.ivipbusiness.model.entity.Card;
import com.xtel.sdk.utils.ViewHolderHelper;
import com.xtel.sdk.utils.WidgetHelper;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Vulcl on 3/4/2017
 */

public class MemberCardAdapter extends RecyclerView.Adapter<MemberCardAdapter.ViewHolder> {
    private ArrayList<Card> arrayList;
    private Animator mCurrentAnimator;
    private Activity activity;

    private FrameLayout layout_image;
    private TextView txt_done;
    private ImageView expandedImageView;
    private int mShortAnimationDuration;
    public int position = -1;

    public MemberCardAdapter(Activity activity, ArrayList<Card> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;

        layout_image = (FrameLayout) activity.findViewById(R.id.add_level_layout_image);
        txt_done = (TextView) activity.findViewById(R.id.add_level_txt_done);
        expandedImageView = (ImageView) activity.findViewById(R.id.add_level_expanded_image);
        mShortAnimationDuration = activity.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_card, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Card resp_image = arrayList.get(position);

        if (resp_image.getFile_path() != null) {
            File file = new File(resp_image.getFile_path());
            WidgetHelper.getInstance().setImageFile(holder.imageView, file);
        } else
            WidgetHelper.getInstance().setImageURL(holder.imageView, arrayList.get(position).getCard_url());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomImageFromThumb(holder.imageView, holder.imageView.getDrawable(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends ViewHolderHelper {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = findImageView(R.id.item_member_card_img);
        }
    }

    public void addMemberCard(Card card) {
        arrayList.add(card);
        notifyItemInserted((arrayList.size() - 1));
        notifyItemRangeRemoved((arrayList.size() - 1), arrayList.size());
    }

    private void chageImageViewStatus(int _position) {
        if (_position == position)
            WidgetHelper.getInstance().setTextViewDrawable(txt_done, 2, R.drawable.ic_action_tick_selected);
        else
            WidgetHelper.getInstance().setTextViewDrawable(txt_done, 2, R.drawable.ic_action_done);
    }

    private void zoomImageFromThumb(final View thumbView, Drawable drawable, final int _position) {
        chageImageViewStatus(_position);
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        expandedImageView.setImageDrawable(drawable);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        activity.findViewById(R.id.add_level_container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
                txt_done.setVisibility(View.VISIBLE);
                layout_image.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
                txt_done.setVisibility(View.VISIBLE);
                layout_image.setBackgroundColor(activity.getResources().getColor(android.R.color.white));
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                txt_done.setVisibility(View.GONE);
                layout_image.setBackgroundColor(activity.getResources().getColor(android.R.color.transparent));

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });

        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == _position)
                    position = -1;
                else
                    position = _position;
                chageImageViewStatus(_position);
            }
        });
    }

    public boolean getCurrentAnimation() {
        if (expandedImageView.getVisibility() == View.VISIBLE) {
            expandedImageView.performClick();
            return true;
        }

        return false;
    }

    public Card getMemberCard() {
        if (position >= 0)
            return arrayList.get(position);
        else
            return null;
    }
}