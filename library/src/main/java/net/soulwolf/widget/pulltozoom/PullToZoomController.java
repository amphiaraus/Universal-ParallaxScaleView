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

import android.content.Context;

import net.soulwolf.widget.pulltozoom.delegates.ViewDelegate;

import java.util.concurrent.ConcurrentHashMap;

/**
 * author : Soulwolf Create by 2015/7/31 14:54
 * email  : ToakerQin@gmail.com.
 */
public class PullToZoomController {

    private static ConcurrentHashMap<Object,PullToZoomAttacher> mViewAttach = new ConcurrentHashMap<>();

    public static void registerViewAttach(Context context,PullToZoomAttacher attacher){
        mViewAttach.put(context,attacher);
    }

    public static void unregisterViewAttach(Context context){
        if(mViewAttach.containsKey(context)){
            mViewAttach.remove(context);
        }
    }

    public static void registerViewDelegate(Context context,ViewDelegate delegate){
        if(context != null && mViewAttach.containsKey(context)){
            PullToZoomAttacher pullToZoomAttacher = mViewAttach.get(context);
            if(pullToZoomAttacher != null){
                pullToZoomAttacher.registerViewDelegate(delegate);
            }
        }
    }

    public static void onDestroy(){
        mViewAttach.clear();
        mViewAttach = null;
    }
}
