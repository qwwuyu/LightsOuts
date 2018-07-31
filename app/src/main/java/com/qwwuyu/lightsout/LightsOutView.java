package com.qwwuyu.lightsout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.nio.IntBuffer;

/**
 * LightsOuts 点灯游戏
 * Created by qiwei on 2018/7/8.
 */
public class LightsOutView extends View implements View.OnTouchListener {
    private final Paint gamePaint = new Paint();
    //未点灯颜色
    private int inColor = 0xff5C90FF;
    //已点颜色
    private int outColor = 0xffE6AB5E;
    //像素长度
    private int side;

    /** 已点数量 */
    private int outSize;
    /** 游戏是否结束 */
    private boolean gameOver = false;
    /** 边长数量 */
    private int size;
    /** 图片像素数组BGR */
    private int[] is;
    private Bitmap gameBitmap;

    public LightsOutView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSize(2);
        setOnTouchListener(this);
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

    private int click = -1;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gameOver) {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (gameListener != null) gameListener.onGameOverClick();
            }
            return true;
        }
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                click = (int) ((y / side) * size) * size + (int) ((x / side) * size);
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (click == -1) break;
                int click = (int) ((y / side) * size) * size + (int) ((x / side) * size);
                if (this.click != click) {
                    this.click = -1;
                    break;
                }
                if (MotionEvent.ACTION_MOVE != event.getAction()) {
                    click(click);
                    this.click = -1;
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.scale((float) side / size, (float) side / size);
        canvas.drawBitmap(gameBitmap, 0, 0, gamePaint);
        canvas.restore();
    }

    public void setSize(int num) {
        outSize = 0;
        gameOver = false;
        this.size = Math.min(Math.max(1, num), 200);
        is = new int[size * size];
        gameBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        for (int i = 0; i < is.length; i++) {
            is[i] = inColor;
        }
        gameBitmap.copyPixelsFromBuffer(IntBuffer.wrap(is));
        invalidate();
    }

    private void click(int click) {
        int x = click % size;
        int y = click / size;
        change(click);
        if (y > 0) change(click - size);
        if (x > 0) change(click - 1);
        if (x < size - 1) change(click + 1);
        if (y < size - 1) change(click + size);
        gameBitmap.copyPixelsFromBuffer(IntBuffer.wrap(is));
        invalidate();
        if (outSize == size * size) {
            gameOver = true;
            if (gameListener != null) gameListener.onGameOver();
        }
    }

    private void change(int click) {
        if (is[click] == inColor) {
            is[click] = outColor;
            outSize++;
        } else {
            is[click] = inColor;
            outSize--;
        }
    }

    public int getSize() {
        return size;
    }

    private GameListener gameListener;

    public void setGameListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }

    public interface GameListener {
        void onGameOver();

        void onGameOverClick();
    }
}
