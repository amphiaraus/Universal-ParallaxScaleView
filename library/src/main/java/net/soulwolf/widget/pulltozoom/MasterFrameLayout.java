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
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.soulwolf.widget.pulltozoom.delegates.ContentViewDelegate;
import net.soulwolf.widget.pulltozoom.delegates.HeaderViewDelegate;

/**
 * author : Soulwolf Create by 2015/7/31 9:57
 * email  : ToakerQin@gmail.com.
 */
public abstract class MasterFrameLayout extends FrameLayout {

    protected PullZoomHandler mPullZoomHandler;

    protected LayoutInflater mLayoutInflater;

    protected boolean isHeaderShowing = true;

    public MasterFrameLayout(Context context) {
        super(context);
        applyViewDelegate(context);
    }

    public MasterFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyViewDelegate(context);
    }

    public MasterFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyViewDelegate(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MasterFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyViewDelegate(context);
    }

    private void applyViewDelegate(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void onFinishInflate() {
        if(getChildCount() != 0){
            throw new IllegalStateException("PullToZoomLayout not contain child controls!");
        }
        super.onFinishInflate();
    }

    public PullZoomHandler getPullZoomHandler() {
        return mPullZoomHandler;
    }

    public void setPullZoomHandler(PullZoomHandler handler) {
        if(mPullZoomHandler != null){
            View headerView = mPullZoomHandler.getHeaderViewDelegate().getView();
            View contentView = mPullZoomHandler.getContentViewDelegate().getView();
            removeView(headerView);
            removeView(contentView);
            mPullZoomHandler = null;
        }
        this.mPullZoomHandler = handler;
        if(mPullZoomHandler != null){
            mPullZoomHandler.getHeaderViewDelegate().onCreateView(mLayoutInflater,this);
            mPullZoomHandler.getContentViewDelegate().onCreateView(mLayoutInflater, this);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(mPullZoomHandler != null){
            int reservedHeight = mPullZoomHandler.getHeaderViewDelegate().getReservedHeight();
            if(reservedHeight > 0 && !isHeaderShowing()){
                View contentView = mPullZoomHandler.getContentViewDelegate().getView();
                int measuredWidth = contentView.getMeasuredWidth();
                int measuredHeight = contentView.getMeasuredHeight();
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight - reservedHeight,MeasureSpec.EXACTLY);
                contentView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mPullZoomHandler != null){
            View headerView = mPullZoomHandler.getHeaderViewDelegate().getView();
            View contentView = mPullZoomHandler.getContentViewDelegate().getView();
            int contentMeasuredWidth = contentView.getMeasuredWidth();
            int contentMeasuredHeight = contentView.getMeasuredHeight();
            int contentLeft = contentView.getLeft();
            int contentTop = headerView.getTop();
            int contentRight = contentLeft + contentMeasuredWidth;
            int contentBottom = contentTop + contentMeasuredHeight;
            contentView.layout(contentLeft,contentTop,contentRight,contentBottom);
        }
    }

    public void setHeaderShowing(boolean showing) {
        this.isHeaderShowing = showing;
        requestLayout();
    }

    public boolean isHeaderShowing(){
        return isHeaderShowing;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MasterFrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public HeaderViewDelegate getHeaderViewDelegate() {
        return getPullZoomHandler().getHeaderViewDelegate();
    }

    public ContentViewDelegate getContentViewDelegate() {
        return getPullZoomHandler().getContentViewDelegate();
    }


    /**
     * Per-child layout information for layouts that support margins.
     * for a list of all child view attributes that this class supports.
     *
     */
    public static class LayoutParams extends FrameLayout.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }
}
