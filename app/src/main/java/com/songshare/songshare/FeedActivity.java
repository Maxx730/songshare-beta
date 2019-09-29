package com.songshare.songshare;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.ViewGroupUtils;
import androidx.viewpager.widget.ViewPager;

import jp.wasabeef.blurry.Blurry;

public class FeedActivity extends AppCompatActivity {
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_feed);

        pager = findViewById(R.id.ContentPager);
        pager.setAdapter(new SongsharePagerAdapter(getSupportFragmentManager()));

    }
}
