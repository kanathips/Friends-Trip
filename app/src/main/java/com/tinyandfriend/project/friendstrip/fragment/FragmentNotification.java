package com.tinyandfriend.project.friendstrip.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tinyandfriend.project.friendstrip.R;

/**
 * Created by StandAlone on 12/10/2559.
 */

public class FragmentNotification extends Fragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        return rootView;
    }


}
