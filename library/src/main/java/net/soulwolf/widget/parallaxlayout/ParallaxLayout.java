/**
 * <pre>
 * Copyright (C) 2015  Soulwolf Universal-PullToZoomView
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
package net.soulwolf.widget.parallaxlayout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.toaker.common.tlog.TLog;

import net.soulwolf.widget.pulltozoom.R;


/**
 * author : Soulwolf Create by 2015/7/31 9:51
 * email  : ToakerQin@gmail.com.
 */
public class ParallaxLayout extends MasterFrameLayout implements ParallaxScrollListener ,OnScrollableSelectedListener{

    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "ParallaxLayout:";

    protected LayoutInflater mInflater;

    protected ParallaxDelegate mParallaxDelegate;

    protected ParallaxScrollListener mParallaxScrollListener;

    protected SparseArray<ParallaxScrollable> mCacheParallaxScrollables;

    public ParallaxLayout(Context context) {
        super(context);
        initialize(context);
    }

    public ParallaxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public ParallaxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParallaxLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context);
    }

    private void initialize(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mCacheParallaxScrollables = new SparseArray<ParallaxScrollable>();
        ParallaxLayoutPresenter.onAttach(context, this);
        if(DEBUG){
            TLog.i(LOG_TAG, "initialize : ParallaxLayoutPresenter.onAttach(context,this);");
        }
    }

    public void setParallaxDelegate(ParallaxDelegate delegate){
        if(mParallaxDelegate != null){
            mParallaxDelegate.onDestroy();
        }
        this.mParallaxDelegate = delegate;
        if(mParallaxDelegate != null){
            if(DEBUG){
                TLog.e(LOG_TAG,"setParallaxDelegate : %s",delegate);
            }
            this.mParallaxDelegate.inflateHierarchy(mInflater, this);
        }
    }

    public void attachParallaxScrollable(int position,ParallaxScrollable parallaxScrollable){
        if(parallaxScrollable != null){
            if(mParallaxDelegate == null){
                throw new IllegalStateException("The not set ParallaxDelegate!");
            }
            if(DEBUG){
                TLog.e(LOG_TAG,"setParallaxScrollListener : %s",parallaxScrollable);
            }
            int height = mParallaxDelegate.getHeight();
            parallaxScrollable.setParallaxScrollListener(this);
            PlaceholderView mPlaceholderView = new PlaceholderView(getContext());
            mPlaceholderView.suggestedLayoutParams(parallaxScrollable,0,height);
            mPlaceholderView.setId(R.id.pi_placeholder_view);
            parallaxScrollable.addAdjustView(mPlaceholderView,height);
            mCacheParallaxScrollables.put(position, parallaxScrollable);
        }
    }

    @Override
    public void onScroll(int scrollX, int scrollY) {
        if(mParallaxDelegate != null){
            this.mParallaxDelegate.onScroll(scrollX,scrollY);
        }
        if(mParallaxScrollListener != null){
            this.mParallaxScrollListener.onScroll(scrollX, scrollY);
        }
    }

    public void setParallaxScrollListener(ParallaxScrollListener listener){
        this.mParallaxScrollListener = listener;
        if(DEBUG){
            TLog.e(LOG_TAG,"setParallaxScrollListener : %s",listener);
        }
    }

    @Override
    public void onScrollableSelected(int position) {
        ParallaxScrollable parallaxScrollable = mCacheParallaxScrollables.get(position);
        if(parallaxScrollable != null && mParallaxDelegate != null){
            parallaxScrollable.adjustScrollY(mParallaxDelegate.getAdjustScrollY());
        }
    }
}
