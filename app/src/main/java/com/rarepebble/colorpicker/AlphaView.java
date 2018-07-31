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
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

public class AlphaView extends SliderViewBase implements ObservableColor.ColorObserver {
    public AlphaView(Context context) {
        super(context);
    }

    public AlphaView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void updateColor(ObservableColor observableColor, int action) {
        setPos((float) observableColor.getAlpha() / 0xff);
        updateBitmap();
        invalidate();
    }

    @Override
    protected void notifyListener(float currentPos, int action) {
        observableColor.updateAlpha((int) (currentPos * 0xff), this, action);
    }

    @Override
    protected void makeRect(int w, int h, Paint rectPaint) {
        int color = observableColor.getColor();
        int leftColor = color & 0x00ffffff;
        int rightColor = color | 0xff000000;
        rectPaint.setShader(new LinearGradient(0, 0, w, 0, new int[]{leftColor, rightColor}, null, Shader.TileMode.CLAMP));
    }
}