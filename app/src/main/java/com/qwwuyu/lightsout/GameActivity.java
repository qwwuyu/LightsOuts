package com.qwwuyu.lightsout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by qiwei on 2018/7/31 16:44
 * Description 游戏界面.
 */
public class GameActivity extends AppCompatActivity implements LightsOutView.GameListener {
    private LightsOutView lightsOutView;
    private LightsOutLineView lightsOutLineView;
    private EditText etSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_game);
        new LogUtil.Builder().enableLog(true).enableLogBorder(false).setLogTag("qwwuyu");
        lightsOutView = findViewById(R.id.v_lights_out);
        lightsOutLineView = findViewById(R.id.v_lights_out_line);
        etSize = findViewById(R.id.et_size);
        lightsOutView.setSize(5);
        lightsOutView.setGameListener(this);
        etSize.setText(String.valueOf(lightsOutView.getSize()));
    }

    @Override
    public void onGameOver() {
        Toast.makeText(this, "游戏通过", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGameOverClick() {
        Toast.makeText(this, "游戏已经结束!", Toast.LENGTH_SHORT).show();
    }

    public void replay(View view) {
        int size = lightsOutView.getSize();
        String str = etSize.getText().toString();
        try {
            size = Integer.parseInt(str);
        } catch (NumberFormatException ignored) {
        }
        lightsOutView.setSize(size);
        lightsOutLineView.setSize(lightsOutView.getSize());
        etSize.setText(String.valueOf(lightsOutView.getSize()));
    }

    public void add(View view) {
        lightsOutView.setSize(lightsOutView.getSize() + 1);
        lightsOutLineView.setSize(lightsOutView.getSize());
        etSize.setText(String.valueOf(lightsOutView.getSize()));
    }

    public void subtract(View view) {
        lightsOutView.setSize(lightsOutView.getSize() - 1);
        lightsOutLineView.setSize(lightsOutView.getSize());
        etSize.setText(String.valueOf(lightsOutView.getSize()));
    }
}
