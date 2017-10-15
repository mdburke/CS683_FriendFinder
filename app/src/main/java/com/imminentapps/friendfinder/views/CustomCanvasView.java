package com.imminentapps.friendfinder.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by mburke on 10/15/17.
 */

//adapted from https://examples.javacodegeeks.com/android/core/graphics/canvas-graphics/android-canvas-example/
public class CustomCanvasView extends View {
    private Path mPath;
    private Paint mPaint;
    private float mX, mY;

    public CustomCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setDrawingCacheEnabled(true);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4f);
    }

    public CustomCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setDrawingCacheEnabled(true);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4f);
    }

    public CustomCanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.setDrawingCacheEnabled(true);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4f);
    }

    public CustomCanvasView(Context context) {
        super(context);
        this.setDrawingCacheEnabled(true);
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4f);

    }

    protected void onDraw(Canvas canvas) {
        getParent().requestDisallowInterceptTouchEvent(true);
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(x - mY);

        if (dx >= 5 || dy >= 5) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

    public boolean myOnTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                Log.d("hi", "down " + x + " " + y);
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                Log.d("hi", "move " + x + " " + y);
                break;
            case MotionEvent.ACTION_UP:
                upTouch();
                invalidate();
                Log.d("hi", "up " + x + " " + y);
                break;
        }
        return true;
    }
}
