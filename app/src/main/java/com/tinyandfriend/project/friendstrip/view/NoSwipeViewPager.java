package com.tinyandfriend.project.friendstrip.view;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by NewWy on 9/10/2559.
 */

public class NoSwipeViewPager extends ViewPager {

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        if (super.onTouchEvent(ev)) {
            return false;
        }
        return false;
    }
}
