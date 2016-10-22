package com.tinyandfriend.project.friendstrip;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

/**
 * Created by NewWy on 9/10/2559.
 */

public abstract class FragmentPager extends Fragment implements ViewPager.OnPageChangeListener {
    abstract boolean validateFrom();
    abstract void setInfo(Object info);
}
