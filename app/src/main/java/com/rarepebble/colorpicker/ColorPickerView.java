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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;


public class ColorPickerView extends View implements ObservableColor.ColorObserver {
    private final Paint borderPaint;
    private final Paint pointerPaint;
    private final Path pointerPath;
    private final Path borderPath;
    private final Rect viewRect = new Rect();
    private float with, radius;
    private Bitmap bitmap;

    private final PointF pointer = new PointF();
    private ObservableColor observableColor = new ObservableColor();

    public ColorPickerView(Context context) {
        this(context, null);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorPickerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        borderPath = new Path();
        borderPaint = Resources.makeLinePaint(context);
        pointerPath = Resources.makePointerPath(context);
        pointerPaint = Resources.makeLinePaint(context);

        final DisplayMetrics dm = getResources().getDisplayMetrics();
        int size = Math.min(dm.widthPixels, dm.heightPixels) / 2;
        size = size > 600 ? 600 : size;
        bitmap = makeBitmap(size);
    }

    public void observeColor(ObservableColor observableColor) {
        this.observableColor = observableColor;
        observableColor.addObserver(this);
    }

    @Override
    public void updateColor(ObservableColor observableColor, int action) {
        setPointer(pointer, observableColor.getHue(), observableColor.getSat(), radius);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Constrain to square
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        final int h = MeasureSpec.getSize(heightMeasureSpec);
        final int modeW = MeasureSpec.getMode(widthMeasureSpec);
        final int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int size;
        if (modeW == MeasureSpec.UNSPECIFIED) {
            size = h;
        } else if (modeH == MeasureSpec.UNSPECIFIED) {
            size = w;
        } else {
            size = Math.min(w, h);
        }
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.with = w;
        this.radius = with / 2;
        viewRect.set(0, 0, w, w);

        borderPath.reset();
        borderPath.addCircle(radius, radius, radius - borderPaint.getStrokeWidth(), Path.Direction.CW);

        // Sets pointer position
        setPointer(pointer, observableColor.getHue(), observableColor.getSat(), radius);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                boolean withinPicker = clamp(pointer, event.getX(), event.getY(), true);
                if (withinPicker) update(action);
                return withinPicker;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                clamp(pointer, event.getX(), event.getY(), false);
                update(action);
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void update(int action) {
        observableColor.updateHueSat(hueForPos(pointer.x, pointer.y, radius), satForPos(pointer.x, pointer.y, radius), this, action);
        invalidate();
    }

    private boolean clamp(PointF pointer, float x, float y, boolean rejectOutside) {
        x = Math.min(x, with);
        y = Math.min(y, with);
        final float dx = radius - x;
        final float dy = radius - y;
        final float r = (float) Math.sqrt(dx * dx + dy * dy);
        boolean outside = r > radius;
        if (!outside || !rejectOutside) {
            if (outside) {
                x = radius - dx * radius / r;
                y = radius - dy * radius / r;
            }
            pointer.set(x, y);
        }
        return !outside;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(borderPath, borderPaint);
        canvas.save(Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG);
        canvas.clipPath(borderPath);
        canvas.drawBitmap(bitmap, null, viewRect, null);
        canvas.translate(pointer.x, pointer.y);
        canvas.drawPath(pointerPath, pointerPaint);
        canvas.restore();
    }

    private static Bitmap makeBitmap(int radiusPx) {
        float r = (float) radiusPx / 2;
        int[] colors = new int[radiusPx * radiusPx];
        float[] hsv = new float[]{0f, 0f, 1f};
        for (int y = 0; y < radiusPx; ++y) {
            for (int x = 0; x < radiusPx; ++x) {
                float sat = satForPos(x, y, r);
                int alpha = (int) (Math.max(0, Math.min(1, (1 - sat) * radiusPx)) * 255); // antialias edge
                if (alpha > 0) {
                    hsv[0] = hueForPos(x, y, r);
                    hsv[1] = sat;
                    colors[x + y * radiusPx] = Color.HSVToColor(alpha, hsv);
                }
            }
        }
        return Bitmap.createBitmap(colors, radiusPx, radiusPx, Bitmap.Config.ARGB_8888);
    }

    private static float hueForPos(float x, float y, float r) {
        final double angle = Math.atan2(y - r, x - r);
        double hue = angle * 180.0 / Math.PI;
        hue = (330 - hue) % 360;
        return (float) hue;
    }

    private static float satForPos(float x, float y, float r) {
        final double dx = (x - r) / r;
        final double dy = (y - r) / r;
        final double sat = dx * dx + dy * dy; // leave it squared -- exaggerates pale colors
        return (float) Math.sqrt(sat);
    }

    private static void setPointer(PointF pointer, float hue, float sat, float r) {
        final double distance = r * sat;
        hue = (510 - hue) % 360;
        final double angle = hue / 180 * Math.PI;
        final double dx = distance * Math.cos(angle);
        final double dy = distance * Math.sin(angle);
        pointer.set(r - (float) dx, r - (float) dy);
    }
}