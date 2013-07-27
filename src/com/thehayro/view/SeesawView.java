package com.thehayro.view;

import com.thehayro.R;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

public class SeesawView extends FrameLayout {

    private TimeInterpolator mInterpolator;

    private float mTension;

    public SeesawView(Context context) {
        this(context, null);
    }

    public SeesawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeesawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeesawView);
        setTension(a.getFloat(R.styleable.SeesawView_tension, 1f));

        int resId = a.getResourceId(R.styleable.SeesawView_interpolation, android.R.anim.linear_interpolator);
        if (resId > 0) {
            setInterpolator(resId);
        }
    }


    @Override
    public void addView(View child) {
        final int childCount = getChildCount();
        if (childCount >= 1)
            throw new RuntimeException(getContext().getString(R.string.one_child));
        super.addView(child);
    }


    @Override
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        final int action = event.getAction();
        if (action != MotionEvent.ACTION_UP) {
            return dispatchTap(event);
        }
        return super.onInterceptTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            reset();
            return super.onTouchEvent(event);
        }

        return dispatchTap(event);

    }

    private float getPivotPointX() {
        final int width = getWidth();
        return (float) width * 0.5f;
    }

    private float getPivotPointY() {
        final int height = getHeight();
        return (float) height * 0.5f;
    }

    private boolean dispatchTap(final MotionEvent e) {
        final Vector r1 = new Vector(e.getX(), e.getY(), 0);
        final Vector r0 = new Vector(getPivotPointX(), getPivotPointY(), 0);
        final Vector f = new Vector(e.getX(), e.getY(), e.getPressure());
        final Vector r = r1.subtract(r0);
        final Vector torqueVector = r.crossProduct(f);
        final float yRot = (float) Math.toRadians(torqueVector.y) * (- 1);
        final float xRot = (float) Math.toRadians(torqueVector.x) * (- 1);
        setRotationX(xRot);
        setRotationY(yRot);
        return true;
    }

    private void reset() {
        animate().rotationX(0f).rotationY(0f).setDuration(800).setInterpolator(mInterpolator).start();
    }


    public void setInterpolator(final int resId) {
        setInterpolator(AnimationUtils.loadInterpolator(getContext(), resId));
    }

    public void setInterpolator(final TimeInterpolator interpolator) {
        mInterpolator = interpolator;
    }

    public float getTension() {
        return mTension;
    }

    public void setTension(final float tension) {
        mTension = tension;
    }

    private class Vector {

        float x, y, z;

        public Vector(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        Vector subtract(Vector v) {
            return new Vector(x - v.x, y - v.y, z - v.z);
        }

        Vector add(Vector v) {
            return new Vector(x + v.x, y + v.y, z + v.z);
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
