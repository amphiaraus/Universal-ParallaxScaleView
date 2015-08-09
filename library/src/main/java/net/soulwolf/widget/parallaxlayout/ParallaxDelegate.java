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
package net.soulwolf.widget.parallaxlayout;

import android.support.annotation.IdRes;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.view.ViewHelper;

import net.soulwolf.widget.parallaxlayout.animator.AnimatorHelper;

/**
 * author: Soulwolf Created on 2015/8/6 21:19.
 * email : Ching.Soulwolf@gmail.com
 */
public abstract class ParallaxDelegate {

    public static final int MIN_SCALE_VALUE = 200;

    protected View mContentView;

    protected int mMinTranslation;

    protected OnScalingListener mOnScalingListener;

    protected boolean mScaling;

    public void setScaling(float scale,int scrollY){
        if(isScalable()){
            AnimatorHelper.setScaleY(getScaleView(),Math.round(scale * getHeight()));
            if(isChangeState(scrollY)){
                onScaleStateChanged(true);
            }
        }
    }

    public synchronized void onScroll(int scrollX,int scrollY){
        System.out.println("onScroll:"+scrollY);
        if(scrollY < 0 && isScalable()){
            float scale = (getHeight() + Math.abs(scrollY)) / (float)getHeight();
            setScaling(scale,scrollY);
        }else {
            System.out.println("mContentView:"+Math.max(-scrollY, mMinTranslation));
           setTranslationY(mContentView, Math.max(-scrollY, mMinTranslation));
           resetScale(getMinScaleValue());
        }
    }

    protected boolean isChangeState(float value){
        return Math.abs(value) >= getMinScaleValue();
    }

    protected void onScaleStateChanged(boolean scaling){
        if(scaling != mScaling){
            mScaling = scaling;
            if(mOnScalingListener != null){
                if(isScaling()){
                    mOnScalingListener.onScaling();
                }else {
                    mOnScalingListener.onScaled();
                }
            }
        }
    }

    public boolean isScaling() {
        return mScaling;
    }

    protected void setTranslationY(View target,float translationY){
        ViewHelper.setTranslationY(target,translationY);
    }

    protected void setTranslationX(View target,float translationX){
        ViewHelper.setTranslationX(target,translationX);
    }

    protected float getTranslationY(View view) {
        return ViewHelper.getTranslationY(view);
    }

    protected float getScaleX(View view) {
        return ViewHelper.getScaleX(view);
    }

    protected void setScaleX(View view, float scaleX) {
        ViewHelper.setScaleX(view,scaleX);
    }

    protected float getScaleY(View view) {
        return ViewHelper.getScaleY(view);
    }

    protected void setScaleY(View view, float scaleY) {
        ViewHelper.setScaleY(view,scaleY);
    }

    protected void setPivotX(View view, float pivotX) {
        ViewHelper.setPivotX(view, pivotX);
    }

    protected void setPivotY(View view, float pivotY) {
        ViewHelper.setPivotY(view,pivotY);
    }

    protected boolean isScalable(){
        return getScaleView() != null;
    }

    protected void resetScale(int offset){
        if(isScalable()){
            int height = getHeight();
            if(AnimatorHelper.getScaleY(getScaleView()) != height){
                setScaling(1.0F,0);
            }
            if(isChangeState(offset)){
                onScaleStateChanged(false);
            }
        }
    }

    public void inflateHierarchy(LayoutInflater inflater,ViewGroup container){
        this.mContentView = onInflateLayout(inflater,container,false);
        container.addView(mContentView);
        this.onViewCreated(mContentView);
        this.mMinTranslation = getMinContentHeight() - getHeight();
    }

    protected void onViewCreated(View view){
        if(isScalable()){
            setPivotX(getScaleView(), getWidth() / 2.0f);
            setPivotY(getScaleView(), getHeight());
        }
    }

    protected int getMinScaleValue(){
        return MIN_SCALE_VALUE;
    }

    protected abstract View getScaleView();

    public int getAdjustScrollY(){
        return (int) (getHeight() + getTranslationY(mContentView));
    }

    protected View findViewById(@IdRes int id){
        return mContentView.findViewById(id);
    }

    protected abstract int getMinContentHeight();

    public void setOnScalingListener(OnScalingListener listener) {
        this.mOnScalingListener = listener;
    }

    public int getWidth(){
        return mContentView.getMeasuredWidth();
    }

    public int getHeight(){
        return mContentView.getMeasuredHeight();
    }

    /**
     * Initialize parallax header layout!Inflate a new view hierarchy from the specified xml resource. Throws
     * {@link InflateException} if there is an error.
     *
     * @param inflater View inflater!
     * @param container Optional view to be the parent of the generated hierarchy.
     * @param attachToRoot Whether the inflated hierarchy should be attached to
     *        the root parameter? If false, root is only used to create the
     *        correct subclass of LayoutParams for the root view in the XML.
     * @return The root View of the inflated hierarchy. If root was supplied,
     *         this is the root View; otherwise it is the root of the inflated
     *         XML file.
     */
    protected abstract View onInflateLayout(LayoutInflater inflater,ViewGroup container, boolean attachToRoot);

    public void onDestroy(){
        this.mContentView = null;
        this.mOnScalingListener = null;
    }
}
