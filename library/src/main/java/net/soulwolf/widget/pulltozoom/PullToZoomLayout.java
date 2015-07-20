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
package net.soulwolf.widget.pulltozoom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.toaker.common.tlog.TLog;

/**
 * author: Soulwolf Created on 2015/7/20 21:16.
 * email : Ching.Soulwolf@gmail.com
 */
public class PullToZoomLayout extends ViewGroup {

    static final boolean DEBUG = true;

    static final String LOG_TAG = "PullToZoomLayout:";

    private View mZoomView;

    private ViewGroup mContentView;

    public PullToZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyAttributeSet(context,attrs,0,0);
    }

    public PullToZoomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyAttributeSet(context,attrs,defStyleAttr,0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PullToZoomLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyAttributeSet(context, attrs, defStyleAttr, defStyleRes);
    }

    private void applyAttributeSet(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray styleArray = context.obtainStyledAttributes(attrs, R.styleable.PullToZoomLayout, defStyleAttr, defStyleRes);

        styleArray.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if(childCount != 2){
            throw new IllegalStateException("PullToZoomLayout must contain a ZoomView and a Scroll View");
        }
        mZoomView = getChildAt(0);
        mContentView = (ViewGroup) getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(DEBUG){
            TLog.d(LOG_TAG, "onMeasure width:%s height:%s padding: %s %s %s %s"
                    , getMeasuredWidth(), getMeasuredHeight(), getPaddingLeft(), getPaddingTop()
                    , getPaddingRight(), getPaddingBottom());
        }
        onMeasureChild(mZoomView, widthMeasureSpec, heightMeasureSpec);
        onMeasureChild(mContentView, widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * measure Child View;
     * @param child child view!
     * @param widthMeasureSpec child width measure spec!
     * @param heightMeasureSpec child height measure spec!
     */
    private void onMeasureChild(View child, int widthMeasureSpec, int heightMeasureSpec) {
        if(child == null){
            if(DEBUG){
                TLog.d(LOG_TAG, "onMeasureChild Child View the not NULL");
            }
            return;
        }
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
        if(DEBUG){
            TLog.d(LOG_TAG, "onMeasureChild margin params : %s %s %s %s", marginLayoutParams.leftMargin, marginLayoutParams.topMargin
                    , marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        }
        int mChildWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,child.getPaddingLeft() + child.getPaddingRight()
                + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin,marginLayoutParams.width);
        int mChildHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,child.getPaddingTop() + child.getPaddingBottom()
                + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin,marginLayoutParams.height);

        if(DEBUG){
            TLog.d(LOG_TAG, "onMeasureChild %s widthMeasureSpec:%s heightMeasureSpec:%s", child, mChildWidthMeasureSpec, mChildHeightMeasureSpec);
        }
        child.measure(mChildWidthMeasureSpec,mChildHeightMeasureSpec);
    }

    int top;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(DEBUG){
            TLog.d(LOG_TAG, "onLayout %s %s %s %s", l, t, r, b);
        }
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) mZoomView.getLayoutParams();
        int left = getPaddingLeft() + marginLayoutParams.leftMargin;
        int top  = getPaddingTop() + marginLayoutParams.topMargin;
        int right = left + mZoomView.getMeasuredWidth() + marginLayoutParams.rightMargin;
        int bottom = top + mZoomView.getMeasuredHeight() + marginLayoutParams.bottomMargin;
        mZoomView.layout(left,top,right,bottom);
        marginLayoutParams = (MarginLayoutParams) mContentView.getLayoutParams();
        left = getPaddingLeft() + marginLayoutParams.leftMargin;
        top  = bottom + marginLayoutParams.topMargin;
        right = left + mContentView.getMeasuredWidth() + marginLayoutParams.rightMargin;
        bottom = top + mContentView.getMeasuredHeight() + marginLayoutParams.bottomMargin;
        mContentView.layout(left,top,right,bottom);
        if(top == 0)
        this.top = top;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    float y;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float offset = ev.getY() - y;
                offset += ViewHelper.getTranslationY(mContentView);
                ViewHelper.setTranslationY(mContentView,offset);
                y = ev.getY();
                float scale = ViewHelper.getTranslationY(mContentView) / mZoomView.getHeight();
                ViewHelper.setPivotX(mZoomView,mZoomView.getWidth() / 2);
                ViewHelper.setPivotY(mZoomView,0);
                ViewHelper.setScaleX(mZoomView,scale);
                ViewHelper.setScaleY(mZoomView,scale);
                return true;

            case MotionEvent.ACTION_UP:
                float scaleY = ViewHelper.getScaleY(mZoomView);
                if(scaleY != 1.0f){
                    AnimatorSet set = new AnimatorSet();
                    set.play(ObjectAnimator.ofFloat(mZoomView,"scaleX",1.0f));
                    set.play(ObjectAnimator.ofFloat(mZoomView,"scaleY",1.0f));
                    set.play(ObjectAnimator.ofFloat(mContentView,"translationY",top));
                    set.setDuration(200);
                    set.start();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * <p>This set of layout parameters defaults the width and the height of
     * the children to {@link #WRAP_CONTENT} when they are not specified in the
     * XML file. Otherwise, this class ussed the value read from the XML file.</p>
     *
     * <p>See
     * for a list of all child view attributes that this class supports.</p>
     *
     */
    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
