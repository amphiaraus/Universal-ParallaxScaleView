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
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.toaker.common.tlog.TLog;

import net.soulwolf.widget.pulltozoom.animator.PullToZoomAnimator;
import net.soulwolf.widget.pulltozoom.animator.TimeInterpolator;
import net.soulwolf.widget.pulltozoom.animator.ViewHelper;

/**
 * author: Soulwolf Created on 2015/7/20 21:16.
 * email : Ching.Soulwolf@gmail.com
 */
public class PullToZoomLayout extends ViewGroup {

    static final boolean DEBUG = true;

    static final String LOG_TAG = "PullToZoomLayout:";

    private static final TimeInterpolator mTimeInterpolator = new TimeInterpolator() {
        public float getInterpolation(float value) {
            float f = value - 1.0F;
            return 1.0F + f * (f * (f * (f * f)));
        }
    };

    private static final long DURATION = 200L;

    private View mZoomView;

    private ViewGroup mContentView;

    private boolean mPullZoomEnabled = true;

    private boolean mPullZooming     = false;

    private boolean mDropDowning     = false;

    private int     mScaledTouchSlop;

    private float   mInitializeTouchX;

    private float   mInitializeTouchY;

    private float   mLastTouchX;

    private float   mLastTouchY;

    private int     mZoomViewHeight;

    private boolean mZoomViewVisibility = true;

    private boolean mIntercept = false;

    private Scrollable mScrollable;

    private PullToZoomAnimator mPullToZoomAnimator;

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
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
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
        if(mContentView instanceof Scrollable){
            mScrollable = (Scrollable) mContentView;
        }else {
            throw new IllegalStateException("PullToZoomLayout Scroll view must implement the Scrollable");
        }
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
        if(mZoomViewHeight == 0)
        mZoomViewHeight = mZoomView.getMeasuredHeight();
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
        int mChildHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, child.getPaddingTop() + child.getPaddingBottom()
                + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, marginLayoutParams.height);

        if(DEBUG){
            TLog.d(LOG_TAG, "onMeasureChild %s widthMeasureSpec:%s heightMeasureSpec:%s", child, mChildWidthMeasureSpec, mChildHeightMeasureSpec);
        }
        child.measure(mChildWidthMeasureSpec, mChildHeightMeasureSpec);
    }

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
        mContentView.layout(left, top, right, bottom);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(isPullZoomEnabled()){
            switch (ev.getAction()){
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    mIntercept = mDropDowning = false;
                    break;

                case MotionEvent.ACTION_DOWN:
                    mInitializeTouchX = ev.getX();
                    mInitializeTouchY = ev.getY();
                    mLastTouchX = mInitializeTouchX;
                    mLastTouchY = mInitializeTouchY;
                    mIntercept = mDropDowning = false;
                    break;

                case MotionEvent.ACTION_MOVE:

                    if(canScrollToTop() || isZoomViewVisibility()){
                        final float offset = ev.getY() - mLastTouchY;
                        final float scopeOffset = ev.getX()  - mLastTouchX;
                        if (Math.abs(offset) > mScaledTouchSlop
                                && Math.abs(offset) > Math.abs(scopeOffset)) {
                            if (offset >= 1.0f || offset <= -1.0f) {
                                mLastTouchX  = ev.getX();
                                mLastTouchY = ev.getY();
                                mIntercept = mDropDowning = true;
                            }
                        }
                    }
                    break;
            }
        }
        return mIntercept;
    }

    private boolean isContentScrollable(){
        return mContentView.getTop() >= 0;
    }

    public boolean isZoomViewVisibility() {
        return mZoomViewVisibility;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isPullZoomEnabled()){
            switch (ev.getAction()){
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if(mDropDowning){
                        mDropDowning = false;
                        if(isPullZooming()){
                            pullToZoomRebound();
                        }
                        mPullZooming = false;
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_DOWN:
                    if((canScrollToTop() || isZoomViewVisibility()) && ev.getEdgeFlags() == 0){
                        mInitializeTouchX = ev.getX();
                        mInitializeTouchY = ev.getY();
                        mLastTouchX = mInitializeTouchX;
                        mLastTouchY = mInitializeTouchY;
                        return true;
                    }
                    break;

                case MotionEvent.ACTION_MOVE:
                    mLastTouchX = ev.getX();
                    mLastTouchY = ev.getY();
                    float offset = mLastTouchY - mInitializeTouchY;
                    if(mDropDowning){
                        if(DEBUG){
                            TLog.d(LOG_TAG,"scrollOffset:%s",offset);
                        }
                        if(offset > 0){
                            pullToZoom(Math.round(Math.max(offset,0) / 2));
                        }else {
                            scrollContentView((int) (offset / 10));
                        }
                        mPullZooming = true;
                        return true;
                    }
                    break;

            }
        }
        return false;
    }

    private void scrollContentView(int offset){
        mContentView.offsetTopAndBottom(offset);
    }

    private void pullToZoom(int scrollOffset) {
        int mNewHeight = Math.abs(scrollOffset) + mZoomViewHeight;
        ViewHelper.setScaleY(mZoomView,mNewHeight);
    }

    private void pullToZoomRebound(){
        if(DEBUG){
            TLog.d(LOG_TAG,"pullToZoomRebound");
        }
        if(mPullToZoomAnimator == null){
            mPullToZoomAnimator = PullToZoomAnimator.ofInt(mZoomView,PullToZoomAnimator.DATUM_HEIGHT,mZoomViewHeight);
            mPullToZoomAnimator.setInterpolator(mTimeInterpolator);
            mPullToZoomAnimator.setDuration(DURATION);
        }
        if(mPullToZoomAnimator.isRunning()){
            mPullToZoomAnimator.abortAnimator();
        }
        mPullToZoomAnimator.start();
    }

    public boolean canScrollToTop(){
        return mScrollable != null && mScrollable.canScrollToTop();
    }


    public View getZoomView() {
        return mZoomView;
    }

    public ViewGroup getContentView() {
        return mContentView;
    }

    public boolean isPullZoomEnabled() {
        return mPullZoomEnabled;
    }

    public void setPullZoomEnabled(boolean mPullZoomEnabled) {
        this.mPullZoomEnabled = mPullZoomEnabled;
    }

    public boolean isPullZooming() {
        return mPullZooming;
    }

    public void setPullZooming(boolean mPullZooming) {
        this.mPullZooming = mPullZooming;
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
