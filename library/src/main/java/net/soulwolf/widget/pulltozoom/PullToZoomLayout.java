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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.toaker.common.tlog.TLog;

/**
 * author : Soulwolf Create by 2015/7/31 9:51
 * email  : ToakerQin@gmail.com.
 */
public class PullToZoomLayout extends MasterFrameLayout {

    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "PullToZoomLayout1:";

    private PullToZoomAttacher mPullToZoomAttacher;

    public PullToZoomLayout(Context context) {
        super(context);
    }

    public PullToZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToZoomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullToZoomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setPullZoomHandler(PullZoomHandler handler) {
        super.setPullZoomHandler(handler);
        if(mPullToZoomAttacher != null){
            mPullToZoomAttacher.onDestroy();
            mPullToZoomAttacher = null;
            PullToZoomController.unregisterViewAttach(getContext());
        }
        if(mPullZoomHandler != null){
            mPullToZoomAttacher = new PullToZoomAttacher(this);
            PullToZoomController.registerViewAttach(getContext(),mPullToZoomAttacher);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(DEBUG){
            TLog.i(LOG_TAG,"onInterceptTouchEvent : %s",ev.toString());
        }
        if(isEnabled() && mPullToZoomAttacher != null){
            mPullToZoomAttacher.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if(DEBUG){
            TLog.i(LOG_TAG,"onTouchEvent : %s",event.toString());
        }
        if(isEnabled() &&mPullToZoomAttacher != null){
            return mPullToZoomAttacher.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    public void setOnPullToZoomListener(OnPullToZoomListener listener) {
        if(mPullToZoomAttacher != null){
            mPullToZoomAttacher.setOnPullToZoomListener(listener);
        }
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        if(DEBUG){
            TLog.i(LOG_TAG,"onConfigurationChanged : %s",newConfig);
        }
        if (mPullToZoomAttacher != null) {
            mPullToZoomAttacher.onConfigurationChanged(newConfig);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onAttachedToWindow() {
        if(mPullToZoomAttacher == null && mPullZoomHandler != null){
            mPullToZoomAttacher = new PullToZoomAttacher(this);
            PullToZoomController.registerViewAttach(getContext(),mPullToZoomAttacher);
        }
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        if(mPullToZoomAttacher != null){
            mPullToZoomAttacher.onDestroy();
            PullToZoomController.unregisterViewAttach(getContext());
        }
        super.onDetachedFromWindow();
    }
}
