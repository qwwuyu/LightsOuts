package com.qwwuyu.lightsout;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.rarepebble.colorpicker.AlphaView;
import com.rarepebble.colorpicker.ColorPickerView;
import com.rarepebble.colorpicker.ObservableColor;
import com.rarepebble.colorpicker.ValueView;

/**
 * Created by qiwei on 2018/7/9 13:28
 * Description .
 */
public class ColorActivity extends AppCompatActivity implements ObservableColor.ColorObserver {
    private Intent intent = new Intent();
    private ColorPickerView pickerView;
    private ValueView valueView;
    private AlphaView alphaView;
    private ImageView resultView;
    private ObservableColor observableColor = new ObservableColor();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_color);
        pickerView = findViewById(R.id.pickerView);
        valueView = findViewById(R.id.valueView);
        alphaView = findViewById(R.id.alphaView);
        resultView = findViewById(R.id.resultView);
        pickerView.observeColor(observableColor);
        valueView.observeColor(observableColor);
        alphaView.observeColor(observableColor);
        observableColor.addObserver(this);
        observableColor.updateColor(observableColor.getColor(), null);
    }

    public void onClick1(View view) {
        observableColor.updateColor(0x88ff0000, null);
    }

    @Override
    public void updateColor(ObservableColor observableColor, int action) {
        resultView.setImageDrawable(new ColorDrawable(observableColor.getColor()));
        intent.putExtra("color", observableColor.getColor());
        intent.putExtra("colors", observableColor.getHsv());
        ToastUtilImpl.getInstance().show(Integer.toHexString(observableColor.getColor()) + " \naction:" + action);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}