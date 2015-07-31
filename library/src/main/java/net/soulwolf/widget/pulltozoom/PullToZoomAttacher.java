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
package net.soulwolf.widget.pulltozoom;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import net.soulwolf.widget.pulltozoom.delegates.ContentViewDelegate;
import net.soulwolf.widget.pulltozoom.delegates.HeaderViewDelegate;
import net.soulwolf.widget.pulltozoom.delegates.ViewDelegate;

/**
 * author : Soulwolf Create by 2015/7/31 11:14
 * email  : ToakerQin@gmail.com.
 */
public class PullToZoomAttacher {

    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "PullToZoomAttacher:";

    PullToZoomLayout mPullToZoomLayout;

    OnPullToZoomListener mOnPullToZoomListener;

    HeaderViewDelegate mHeaderViewDelegate;

    ContentViewDelegate mContentViewDelegate;

    boolean mIntercept;

    public PullToZoomAttacher(PullToZoomLayout layout){
        this.mPullToZoomLayout = layout;
        this.mHeaderViewDelegate = mPullToZoomLayout.getHeaderViewDelegate();
        this.mContentViewDelegate = mPullToZoomLayout.getContentViewDelegate();
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIntercept;
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        return false;
    }

    public void setOnPullToZoomListener(OnPullToZoomListener listener) {
        this.mOnPullToZoomListener = listener;
    }

    public void onConfigurationChanged(Configuration newConfig) {

    }

    public void registerViewDelegate(ViewDelegate delegate) {

    }

    private void pullZoom(float scale){
        if(mHeaderViewDelegate != null){
            mHeaderViewDelegate.pullZoom(scale);
        }
    }

    private void onDown(float downX,float downY){
        if(mHeaderViewDelegate != null){
            mHeaderViewDelegate.onEventDown(downX, downY);
        }
        if(mContentViewDelegate != null){
            mContentViewDelegate.onEventDown(downX, downY);
        }
    }

    private void onMove(float x,float y,float diffX,float diffY){
        if(mHeaderViewDelegate != null){
            mHeaderViewDelegate.onEventMove(x, y, diffX, diffY);
        }
        if(mContentViewDelegate != null){
            mContentViewDelegate.onEventMove(x, y, diffX, diffY);
        }
    }

    private void onUp(float upX,float upY){
        if(mHeaderViewDelegate != null){
            mHeaderViewDelegate.onEventUp(upX, upY);
        }
        if(mContentViewDelegate != null){
            mContentViewDelegate.onEventUp(upX, upY);
        }
    }

    public void onDestroy(){
        mPullToZoomLayout = null;
    }
}
