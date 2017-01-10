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

    private boolean isPagingEnabled = false;

    public NoSwipeViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}
