/*
 * Copyright (C) 2015 Martin Stone
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rarepebble.colorpicker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class SliderViewBase extends View implements ObservableColor.ColorObserver {
    protected ObservableColor observableColor = new ObservableColor();
    private final Paint borderPaint, checkerPaint, pointerPaint, rectPaint;
    private final Rect viewRect = new Rect();
    private int w, h;
    private final Path borderPath, pointerPath;
    private float currentPos;

    public SliderViewBase(Context context) {
        this(context, null);
    }

    public SliderViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        checkerPaint = Resources.makeCheckerPaint(context);
        borderPaint = Resources.makeLinePaint(context);
        pointerPaint = Resources.makeLinePaint(context);
        pointerPath = Resources.makePointerPath(context);
        borderPath = new Path();
    }

    public void observeColor(ObservableColor observableColor) {
        this.observableColor = observableColor;
        observableColor.addObserver(this);
    }

    protected abstract void notifyListener(float currentPos, int action);

    protected abstract void makeRect(int w, int h, Paint rectPaint);

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        viewRect.set(0, 0, w, h);
        float inset = borderPaint.getStrokeWidth() / 2;
        borderPath.reset();
        borderPath.addRect(new RectF(inset, inset, w - inset, h - inset), Path.Direction.CW);
        updateBitmap();
    }

    protected void setPos(float pos) {
        currentPos = pos;
    }

    protected void updateBitmap() {
        if (w > 0 && h > 0) {
            makeRect(w, h, rectPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                currentPos = valueForTouchPos(event.getX(), event.getY());
                notifyListener(currentPos,action);
                invalidate();
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(borderPath, checkerPaint);
        canvas.drawRect(0, 0, w, h, rectPaint);
        canvas.drawPath(borderPath, borderPaint);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.translate(w * currentPos, h / 2);
        canvas.drawPath(pointerPath, pointerPaint);
        canvas.restore();
    }

    private float valueForTouchPos(float x, float y) {
        final float val = x / w;
        return Math.max(0, Math.min(1, val));
    }
}