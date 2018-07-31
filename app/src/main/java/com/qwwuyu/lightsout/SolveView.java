package com.qwwuyu.lightsout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rarepebble.colorpicker.ObservableColor;

import java.nio.IntBuffer;

/**
 * Created by qiwei on 2018/7/9 9:45
 * Description 解题控件,类似游戏View.
 */
public class SolveView extends View {
    private final Paint SolvePaint = new Paint();
    private int inColor = 0xffffffff;
    private int outColor = 0xffff9988;
    private int side;

    private SolveUtils solveUtils;
    private int size;
    private int[] is;
    private Bitmap SolveBitmap;
    private long mPosition;
    private boolean special = true;

    private ObservableColor color = new ObservableColor(outColor);

    public SolveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSize(1);
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale((float) side / size, (float) side / size);
        canvas.drawBitmap(SolveBitmap, 0, 0, SolvePaint);
        canvas.restore();
    }

    public void setSize(int num) {
        this.size = Math.min(Math.max(1, num), 103);
        is = new int[size * size];
        SolveBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        solveUtils = new SolveUtils(size);
        setPosition(0);
    }

    public void setPosition(long position) {
        mPosition = Math.min(Math.max(0, position), solveUtils.getResultNum() - 1);
        boolean[] result = solveUtils.back(mPosition);
        float max = (float) Math.sqrt((size / 2f) * (size / 2f) * 2) * 3;
        for (int i = 0; i < is.length; i++) {
            if (result[i]) {
                if (special) {
                    float x = size / 2f - (i % size);
                    float y = size / 2f - (i / size);
                    float r = (float) Math.sqrt(x * x + y * y);
                    color.updateValue(1f - r / max, null, 0);
                    is[i] = color.getColor();
                } else {
                    is[i] = outColor;
                }
            } else {
                is[i] = inColor;
            }
        }
        SolveBitmap.copyPixelsFromBuffer(IntBuffer.wrap(is));
        invalidate();
    }

    public long getResultNum() {
        return solveUtils.getResultNum();
    }

    public int getSize() {
        return size;
    }

    public long getPosition() {
        return mPosition;
    }

    public void setSpecial() {
        this.special = !special;
        setPosition(mPosition);
    }

    public void setOutColor(int outColor) {
        int r = (outColor & 0x00ff0000) >> 16;
        int b = (outColor & 0x000000ff) << 16;
        this.outColor = outColor & 0xff00ff00 | r | b;
        color.updateColor(this.outColor, null);
        setPosition(mPosition);
    }
}
