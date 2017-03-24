package com.lv.listenkeyboardevent;

import android.view.ViewTreeObserver;

/**
 * Created by Vulcl on 3/24/2017
 */

public interface Unregistrar {

    /**
     * unregisters the {@link ViewTreeObserver.OnGlobalLayoutListener} and there by does provide any more callback to the {@link KeyboardVisibilityEventListener}
     */
    void unregister();

}