/*
 *     Copyright 2014, 2021 hayribakici
 *     Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     Unless required by applicable law or agreed to in writing,
 *     software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and limitations under the License.
 */

package com.thehayro.android.seesawview;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.InterpolatorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

public class SeesawView extends FrameLayout {

    private TimeInterpolator interpolator;

    public SeesawView(Context context) {
        this(context, null);
    }

    public SeesawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeesawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeesawView);

        int resId = a.getResourceId(R.styleable.SeesawView_interpolation, android.R.interpolator.linear);
        if (resId > 0) {
            setInterpolator(resId);
        }
        a.recycle();
    }

    @Override
    public final void addView(View child) {
        final int childCount = getChildCount();
        if (childCount >= 1) {
            throw new RuntimeException(getContext().getString(R.string.one_child));
        }
        super.addView(child);
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        final int action = event.getAction();
        if (action != MotionEvent.ACTION_UP) {
            return dispatchTap(event);
        }
        reset();
        return super.onInterceptTouchEvent(event);
    }

    private float getPivotPointX() {
        return (float) getWidth() * 0.5f;
    }

    private float getPivotPointY() {
        return (float) getHeight() * 0.5f;
    }

    private boolean dispatchTap(@NonNull final MotionEvent e) {
        final float x = e.getX();
        final float y = e.getY();

        final Vector r1 = new Vector(x, y, 0);
        final Vector r0 = new Vector(getPivotPointX(), getPivotPointY(), 0);
        final Vector f = new Vector(x, y, e.getPressure());
        final Vector r = r1.subtract(r0);
        final Vector torqueVector = r.crossProduct(f);
        final float yRot = (float) Math.toRadians(torqueVector.y) * (-1);
        final float xRot = (float) Math.toRadians(torqueVector.x) * (-1);
        setRotationX(xRot);
        setRotationY(yRot);
        return false;
    }

    private void reset() {
        animate().rotationX(0f).rotationY(0f).setDuration(800).setInterpolator(interpolator).start();
    }

    public void setInterpolator(@InterpolatorRes final int resId) {
        setInterpolator(AnimationUtils.loadInterpolator(getContext(), resId));
    }

    public void setInterpolator(final TimeInterpolator interpolator) {
        this.interpolator = interpolator;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener listener) {
        if (getChildCount() == 0) {
            return;
        }
        getChildAt(0).setOnClickListener(listener);
    }

    private static class Vector {

        float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        Vector subtract(Vector v) {
            return new Vector(x - v.x, y - v.y, z - v.z);
        }

        Vector crossProduct(Vector v) {
            Vector a = new Vector(y * v.z, z * v.x, x * v.y);
            Vector b = new Vector(z * v.y, x * v.z, y * v.x);
            return a.subtract(b);
        }

        @Override
        public String toString() {
            return String.format("x: %s y: %s z: %s", x, y, z);
        }
    }
}
