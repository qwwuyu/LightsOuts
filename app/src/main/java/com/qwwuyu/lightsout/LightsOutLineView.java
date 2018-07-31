package com.qwwuyu.lightsout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * LightsOuts 点灯游戏线条
 * Created by qiwei on 2018/7/8.
 */
public class LightsOutLineView extends View {
    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int side;
    private int size;
    private float lineSide;

    public LightsOutLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        linePaint.setColor(0xff4D4D4D);
        linePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
        side = size;
        setMeasuredDimension(size, size);
        if (this.size == 0) setSize(2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float diff = (float) side / size;
        for (int i = 0; i <= size; i++) {
            canvas.drawLine(i * diff, 0, i * diff, side, linePaint);
            canvas.drawLine(0, i * diff, side, i * diff, linePaint);
        }
    }

    public void setSize(int num) {
        this.size = Math.min(Math.max(1, num), 200);
        lineSide = size < 50 ? side / 500f : side / 500f * 50 / size;
        linePaint.setStrokeWidth(lineSide * 2);
        invalidate();
    }
}
