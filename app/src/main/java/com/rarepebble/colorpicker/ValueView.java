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
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;

public class ValueView extends SliderViewBase {
    public ValueView(Context context) {
        super(context);
    }

    public ValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void updateColor(ObservableColor observableColor, int action) {
        setPos(this.observableColor.getValue());
        updateBitmap();
        invalidate();
    }

    @Override
    protected void notifyListener(float currentPos, int action) {
        observableColor.updateValue(currentPos, this, action);
    }

    @Override
    protected void makeRect(int w, int h, Paint rectPaint) {
        float[] hsv = new float[]{0, 0, 0};
        observableColor.getHsv(hsv);
        hsv[2] = 0;
        int leftColor = Color.HSVToColor(hsv);
        hsv[2] = 1;
        int rightColor = Color.HSVToColor(hsv);
        rectPaint.setShader(new LinearGradient(0, 0, w, 0, new int[]{leftColor, rightColor}, null, Shader.TileMode.CLAMP));
    }
}