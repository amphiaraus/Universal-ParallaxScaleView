/**
 * <pre>
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for Universal-PullToZoomView
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </pre>
 */
package net.soulwolf.widget.parallaxlayout.sample;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.squareup.picasso.Picasso;

import net.soulwolf.widget.parallaxlayout.ParallaxDelegate;

/**
 * author: Soulwolf Created on 2015/8/6 23:38.
 * email : Ching.Soulwolf@gmail.com
 */
public class SimpleParallaxDelegate extends ParallaxDelegate {

    ImageView mPictureView;

    Context mContext;

    PagerSlidingTabStrip mPagerSlidingTabStrip;

    ViewPager mViewPager;

    public void setViewPager(ViewPager viewPager){
        this.mViewPager = viewPager;
        this.mPagerSlidingTabStrip.setViewPager(mViewPager);
    }

    public SimpleParallaxDelegate(Context context){
        this.mContext = context;
    }

    @Override
    public void onScroll(int scrollX, int scrollY) {
        super.onScroll(scrollX, scrollY);
    }

    @Override
    public void setScaling(float scale, int scrollY) {
        super.setScaling(scale, scrollY);
        //setTranslationY(mPagerSlidingTabStrip,-scrollY);
        //setTranslationY(mContentView,-scrollY);
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
        mPictureView = (ImageView) findViewById(R.id.image);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tab_strip);
        mPagerSlidingTabStrip.setTextColor(Color.WHITE);
        mPagerSlidingTabStrip.setIndicatorColor(Color.WHITE);
        mPagerSlidingTabStrip.setShouldExpand(true);
        Picasso.with(mContext)
                .load("http://img.zcool.cn/community/event/5360559f7ec26ac72520eee32867.jpg")
                .into(mPictureView);
    }

    @Override
    protected View getScaleView() {
        return findViewById(R.id.image);
    }


    @Override
    protected int getMinContentHeight() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.parallax_header_tab_strip_height);
    }

    @Override
    protected View onInflateLayout(LayoutInflater inflater, ViewGroup container, boolean attachToRoot) {
        return inflater.inflate(R.layout.simple_parallax_header,container,attachToRoot);
    }

    @Override
    public int getHeight() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.parallax_header_height);
    }

    @Override
    public int getWidth() {
        return 1080;
    }

    /**
     * Add a listener that will be invoked whenever the page changes or is incrementally
     * scrolled. See {@link ViewPager.OnPageChangeListener}.
     *
     * @param listener listener to add
     */
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mPagerSlidingTabStrip.setOnPageChangeListener(listener);
    }
}
