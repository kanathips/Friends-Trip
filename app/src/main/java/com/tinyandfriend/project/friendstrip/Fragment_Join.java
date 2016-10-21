package com.tinyandfriend.project.friendstrip;

/**
 * Created by StandAlone on 12/10/2559.
 */
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import com.tinyandfriend.project.friendstrip.adapter.CardView_Adapter;
import com.tinyandfriend.project.friendstrip.info.CardView_Info;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class Fragment_Join extends Fragment {

    private RecyclerView recyclerView;
    private CardView_Adapter adapter;
    private List<CardView_Info> albumList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment__join, container, false);

        //////////////////////////////////////// CardView /////////////////////////////////////////////////////
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new CardView_Adapter(getActivity(), albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(),1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), false));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);

        ScaleInAnimationAdapter alphaAdapter = new ScaleInAnimationAdapter(adapter);
        alphaAdapter.setFirstOnly(false);
        alphaAdapter.setDuration(250);
        recyclerView.setAdapter(alphaAdapter);


        prepareAlbums();

        return rootView;
    }

    /////////////////////////////////////////////////////// Start class CardView /////////////////////////////////////////////////////

    private void prepareAlbums() {
        int[] pic = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,R.drawable.pic5,
                R.drawable.pic6,R.drawable.pic7,R.drawable.pic8,R.drawable.pic9,R.drawable.pic10};

        String[] card_names = {"มาเที่ยวทะเลกันเถอะ", "น้ำตกใสแจ๋วปลาชุม", "เดินทางไปด้วยกันนะ", "เมืองแห่งการท่องเที่ยว",
                "มาเที่ยวทะเลกันเถอะ", "น้ำตกใสแจ๋วปลาชุม", "เดินทางไปด้วยกันนะ", "เมืองแห่งการท่องเที่ยว", "มาเที่ยวทะเลกันเถอะ", "น้ำตกใสแจ๋วปลาชุม"};

        String[] dates = {"Date : 01-10-2559 to 07-10-2559","Date : 01-10-2559 to 07-10-2559","Date : 01-10-2559 to 07-10-2559","Date : 01-10-2559 to 07-10-2559",
                "Date : 01-10-2559 to 07-10-2559","Date : 01-10-2559 to 07-10-2559","Date : 01-10-2559 to 07-10-2559","Date : 01-10-2559 to 07-10-2559",
                "Date : 01-10-2559 to 07-10-2559","Date : 01-10-2559 to 07-10-2559"};

        String[] counts = {"Joined 7","Joined 4","Joined 3","Joined 5","Joined 7","Joined 4","Joined 3","Joined 5","Joined 7","Joined 4"};

        for(int i=0;i<=pic.length-1;i++){
            CardView_Info cardView_info = new CardView_Info(card_names[i],dates[i],counts[i],pic[i]);
            albumList.add(cardView_info);
        }

        adapter.notifyDataSetChanged();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /////////////////////////////////////////////////////// End class CardView /////////////////////////////////////////////////////

}