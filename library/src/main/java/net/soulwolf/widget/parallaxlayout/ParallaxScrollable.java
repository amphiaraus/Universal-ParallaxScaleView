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

import android.view.View;

/**
 * author: Soulwolf Created on 2015/8/6 21:03.
 * email : Ching.Soulwolf@gmail.com
 */
public interface ParallaxScrollable {

    /**
     * The parallax scroll y adjust!
     * @param scrollY The distance from the top edge of the Scrollable View (plus padding) that the
     *        item will be positioned.
     */
    public void adjustScrollY(int scrollY);

    /**
     * Set scrollable view scroll callback!
     *
     * @param listener scrollable view scroll monitor!
     */
    public void setParallaxScrollListener(ParallaxScrollListener listener);

    /**
     * Add scrollable view from the top of the occupying View!
     * @param view occupying View!
     * @param height scrollable view adjust view height!
     */
    public void addAdjustView(View view,int height);
}
