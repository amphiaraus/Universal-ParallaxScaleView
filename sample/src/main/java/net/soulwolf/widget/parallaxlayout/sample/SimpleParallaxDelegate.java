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
package net.soulwolf.widget.parallaxlayout.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import net.soulwolf.widget.parallaxlayout.ParallaxDelegate;

/**
 * author: Soulwolf Created on 2015/8/6 23:38.
 * email : Ching.Soulwolf@gmail.com
 */
public class SimpleParallaxDelegate extends ParallaxDelegate {

    ImageView mPictureView;

    Context mContext;

    public SimpleParallaxDelegate(Context context){
        this.mContext = context;
    }

    @Override
    protected void onViewCreated(View view) {
        super.onViewCreated(view);
        mPictureView = (ImageView) findViewById(R.id.image);
        Picasso.with(mContext)
                .load("http://img.zcool.cn/community/event/5360559f7ec26ac72520eee32867.jpg")
                .into(mPictureView);
    }

    @Override
    public void setScaling(float scale) {

    }

    @Override
    protected int getMinContentHeight() {
        return 88;
    }

    @Override
    protected View onInflateLayout(LayoutInflater inflater, ViewGroup container, boolean attachToRoot) {
        return inflater.inflate(R.layout.simple_parallax_header,container,attachToRoot);
    }

    @Override
    public int getHeight() {
        return mContext.getResources().getDimensionPixelSize(R.dimen.parallax_header_height);
    }
}
