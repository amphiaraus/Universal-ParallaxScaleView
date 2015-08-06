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

/**
 * author: Soulwolf Created on 2015/8/6 21:19.
 * email : Ching.Soulwolf@gmail.com
 */
public abstract class ParallaxDelegate {

    protected View mContentView;

    protected int mMinTranslation;

    public abstract void setScaling(float scale);

    public void onScroll(int scrollX,int scrollY){
        System.out.println("Math.max(-scrollY, mMinTranslation) :" + Math.max(-scrollY, mMinTranslation));
        ViewHelper.setTranslationY(mContentView, Math.max(-scrollY, mMinTranslation));
    }

    public void inflateHierarchy(LayoutInflater inflater,ViewGroup container){
        this.mContentView = onInflateLayout(inflater,container,false);
        container.addView(mContentView);
        this.onViewCreated(mContentView);
        this.mMinTranslation = getMinContentHeight() - getHeight();
    }

    protected void onViewCreated(View view){

    }

    public int getAdjustScrollY(){
        return (int) (getHeight() + ViewHelper.getTranslationY(mContentView));
    }

    protected View findViewById(@IdRes int id){
        return mContentView.findViewById(id);
    }

    protected abstract int getMinContentHeight();

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
    }
}
