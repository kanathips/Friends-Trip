package com.tinyandfriend.project.friendstrip;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

public class JoinDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_detail);

        Intent intent = getIntent();

        ImageView imageView = (ImageView) findViewById(R.id.image_thumbnail);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        if(intent.hasExtra("pic_thumbnail") && intent.hasExtra("name_trip")){
            imageView.setImageResource(intent.getIntExtra("pic_thumbnail",0));
            collapsingToolbar.setTitle(intent.getStringExtra("name_trip"));
        }




    }
}
