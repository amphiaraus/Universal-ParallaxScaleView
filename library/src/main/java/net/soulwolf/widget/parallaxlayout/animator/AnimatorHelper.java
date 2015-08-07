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
package net.soulwolf.widget.parallaxlayout.animator;

import android.view.View;
import android.view.ViewGroup;

/**
 * author : Soulwolf Create by 2015/7/22 14:59
 * email  : ToakerQin@gmail.com.
 */
public final class AnimatorHelper {

    public static void setScaleX(Object target,int targetValue){
        if(target instanceof View){
            View targetView = (View) target;
            ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
            layoutParams.width = targetValue;
            targetView.setLayoutParams(layoutParams);
        }
    }

    public static void setScaleY(Object target,int targetValue){
        if(target instanceof View){
            View targetView = (View) target;
            ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
            layoutParams.height = targetValue;
            targetView.setLayoutParams(layoutParams);
        }
    }

    public static int getScaleX(Object target){
        if(target instanceof View){
            View targetView = (View) target;
            return targetView.getLayoutParams().width;
        }
        return 0;
    }

    public static int getScaleY(Object target){
        if(target instanceof View){
            View targetView = (View) target;
            return targetView.getLayoutParams().height;
        }
        return 0;
    }
}
