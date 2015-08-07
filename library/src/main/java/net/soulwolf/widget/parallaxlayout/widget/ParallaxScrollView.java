/**
 * <pre>
 * Copyright (C) 2015  Soulwolf Universal-ParallaxScaleView
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
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import net.soulwolf.widget.parallaxlayout.ParallaxScrollListener;
import net.soulwolf.widget.parallaxlayout.ParallaxScrollable;
import net.soulwolf.widget.pulltozoom.R;

/**
 * author : Soulwolf Create by 2015/8/7 13:54
 * email  : ToakerQin@gmail.com.
 */
public class ParallaxScrollView extends ScrollView implements ParallaxScrollable{

    ParallaxScrollListener mParallaxScrollListener;

    int mParallaxHeight;

    public ParallaxScrollView(Context context) {
        super(context);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ParallaxScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void adjustScrollY(int scrollY) {
        if(scrollY != 0){
            scrollTo(0,scrollY + mParallaxHeight);
        }
    }

    @Override
    public void setParallaxScrollListener(ParallaxScrollListener listener) {
        this.mParallaxScrollListener = listener;
    }

    @Override
    public void addAdjustView(View view, int height) {
        this.mParallaxHeight = height;
        View childRoot = findViewById(R.id.pi_header_root);
        if(getChildCount() == 1 && childRoot == null){
            View child = getChildAt(0);
            ViewGroup.LayoutParams params = child.getLayoutParams();
            LinearLayout mHeaderRoot = new LinearLayout(getContext());
            mHeaderRoot.setOrientation(LinearLayout.VERTICAL);
            mHeaderRoot.setId(R.id.pi_header_root);
            mHeaderRoot.addView(view);
            removeView(child);
            mHeaderRoot.addView(child,new LinearLayout.LayoutParams(params));
            addView(mHeaderRoot, params);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(mParallaxScrollListener != null){
            mParallaxScrollListener.onScroll(l,t);
        }
    }
}
