package com.xtel.ivipbusiness.view.widget;

import android.support.design.widget.AppBarLayout;

/**
 * Created by Vulcl on 1/23/2017
 */

public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    private enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateEXPANDED();
            }
            mCurrentState = State.EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != State.COLLAPSED) {
//                onStateChanged(appBarLayout, State.COLLAPSED);
            }
            mCurrentState = State.COLLAPSED;
        } else {
            if (mCurrentState != State.IDLE) {
                onStateIDLE();
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateEXPANDED();
    public abstract void onStateIDLE();
}