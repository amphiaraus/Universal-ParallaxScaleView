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
package net.soulwolf.widget.parallaxlayout.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import net.soulwolf.widget.parallaxlayout.ParallaxScrollListener;
import net.soulwolf.widget.parallaxlayout.ParallaxScrollable;
import net.soulwolf.widget.parallaxlayout.ScrollableUtils;

/**
 * author: Soulwolf Created on 2015/8/6 23:18.
 * email : Ching.Soulwolf@gmail.com
 */
public class ParallaxListView extends ListView implements ParallaxScrollable, AbsListView.OnScrollListener {

    ParallaxScrollListener mParallaxScrollListener;

    OnScrollListener mSupportOnScrollListener;

    int mParallaxHeight;

    int mCacheScrollY;

    public ParallaxListView(Context context) {
        super(context);
        initialize();
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParallaxListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize() {
        super.setOnScrollListener(this);
    }

    @Override
    public void adjustScrollY(int scrollY) {
        int headerViewsCount = getHeaderViewsCount();
        if (scrollY == 0 && getFirstVisiblePosition() >= headerViewsCount) {
            return;
        }
        setSelectionFromTop(headerViewsCount, scrollY);
    }

    @Override
    public void setParallaxScrollListener(ParallaxScrollListener listener) {
        this.mParallaxScrollListener = listener;
    }

    @Override
    public void addAdjustView(View view,int height) {
        this.mParallaxHeight = height;
        addHeaderView(view);
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.mSupportOnScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mSupportOnScrollListener != null){
            mSupportOnScrollListener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(mParallaxScrollListener != null){
            int scrollY = ScrollableUtils.getAbsListViewScrollY(view, getHeaderViewsCount(), mParallaxHeight);
            if(scrollY != mCacheScrollY){
                mParallaxScrollListener.onScroll(0,scrollY);

            //if(mCacheScrollY != scrollY){
             //   mParallaxScrollListener.onScroll(0,scrollY);
              //  mCacheScrollY = scrollY;

            }
        }
        if(mSupportOnScrollListener != null){
            mSupportOnScrollListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
        }
    }
}
