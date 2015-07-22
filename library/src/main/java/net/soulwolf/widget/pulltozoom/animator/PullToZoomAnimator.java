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
package net.soulwolf.widget.pulltozoom.animator;

import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * author : Soulwolf Create by 2015/7/22 14:16
 * email  : ToakerQin@gmail.com.
 */
public class PullToZoomAnimator extends Animator{

    public static final int DATUM_WIDTH    = 1;

    public static final int DATUM_HEIGHT   = 2;

    /**
     * Internal constants
     */
    private static float sDurationScale = 1.0f;

    /**
     * Internal variables
     * NOTE: This object implements the clone() method, making a deep copy of any referenced
     * objects. As other non-trivial fields are added to this class, make sure to add logic
     * to clone() to make deep copies of them.
     */

    // The first time that the animation's animateFrame() method is called. This time is used to
    // determine elapsed time (and therefore the elapsed fraction) in subsequent calls
    // to animateFrame()
    long mStartTime;


    // The static sAnimationHandler processes the internal timing loop on which all animations
    // are based
    protected static ThreadLocal<AnimationHandler> sAnimationHandler =
            new ThreadLocal<AnimationHandler>();

    // The time interpolator to be used if none is set on the animation
    private static final TimeInterpolator sDefaultInterpolator =
            new TimeInterpolator() {
                @Override
                public float getInterpolation(float input) {
                    return (float)(Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
                }
            };

    /**
     * Additional playing state to indicate whether an animator has been start()'d. There is
     * some lag between a call to start() and the first animation frame. We should still note
     * that the animation has been started, even if it's first animation frame has not yet
     * happened, and reflect that state in isRunning().
     * Note that delayed animations are different: they are not started until their first
     * animation frame, which occurs after their delay elapses.
     */
    private boolean mRunning = false;

    private TimeInterpolator mTimeInterpolator = sDefaultInterpolator;

    //
    // Backing variables
    //

    // How long the animation should last in ms
    private long mDuration = (long)(300 * sDurationScale);

    private final int mDatumMode;

    private final int mTargetValue;

    private final View mTargetView;

    @IntDef({DATUM_WIDTH,DATUM_HEIGHT})
    @Retention(RetentionPolicy.SOURCE)
    @interface DatumMode{
    }

    public PullToZoomAnimator(View target,@DatumMode int datum,int targetValue){
        if(target == null){
            throw new NullPointerException("target == NULL");
        }
        this.mTargetView = target;
        this.mDatumMode = datum;
        this.mTargetValue = targetValue;
    }

    public static PullToZoomAnimator ofInt(View target,@DatumMode int datum,int targetValue){
        return new PullToZoomAnimator(target,datum,targetValue);
    }

    @Override
    public Animator setDuration(long duration) {
        this.mDuration = (long) (duration * sDurationScale);
        return this;
    }

    @Override
    public long getDuration() {
        return mDuration;
    }

    @Override
    public void setInterpolator(TimeInterpolator value) {
        this.mTimeInterpolator = value;
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void abortAnimator() {
        mRunning = false;
        if(getListeners() != null){
            for (AnimatorListener listener:getListeners()){
                listener.onAnimationCancel(this);
            }
        }
    }

    @Override
    public boolean isAnimating() {
        return isRunning();
    }

    @Override
    public void start() {
        super.start();
        if(!isRunning()){
            getOrCreateAnimationHandler().startAnimation();
            if(getListeners() != null){
                for (AnimatorListener listener:getListeners()){
                    listener.onAnimationStart(this);
                }
            }
        }
    }

    void post(Runnable runnable){
        mTargetView.post(runnable);
    }

    class AnimationHandler implements Runnable{

        float mScale;

        void startAnimation(){
            mStartTime = SystemClock.currentThreadTimeMillis();
            if(mDatumMode == DATUM_WIDTH){
                mScale = mTargetView.getRight() / (float)mTargetValue;
            }else {
                mScale = mTargetView.getBottom() / (float)mTargetValue;
            }
            mRunning = true;

            post(this);
        }

        @Override
        public void run() {
            if(isRunning() && mScale > 1.0f){
                float mCurrentScale = (SystemClock.currentThreadTimeMillis() - mStartTime) / (float)getDuration();
                mCurrentScale = mScale - (mScale - 1.0f) * mTimeInterpolator.getInterpolation(mCurrentScale);
                if(mCurrentScale > 1.0f){
                    int newValue = (int) (mCurrentScale * mTargetValue);
                    if(mDatumMode == DATUM_WIDTH){
                        ViewHelper.setScaleX(mTargetView,newValue);
                    }else {
                        ViewHelper.setScaleY(mTargetView, newValue);
                    }
                    post(this);
                    return;
                }
                mRunning = false;
                if(getListeners() != null){
                    for (AnimatorListener listener:getListeners()){
                        listener.onAnimationEnd(PullToZoomAnimator.this);
                    }
                }
            }

        }
    }

    private AnimationHandler getOrCreateAnimationHandler(){
        AnimationHandler animationHandler = sAnimationHandler.get();
        if(animationHandler == null){
            animationHandler = new AnimationHandler();
            sAnimationHandler.set(animationHandler);
        }
        return animationHandler;
    }
}
