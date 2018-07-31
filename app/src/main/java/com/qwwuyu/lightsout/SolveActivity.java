package com.qwwuyu.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by qiwei on 2018/7/8.
 * 解题图片界面
 */
public class SolveActivity extends AppCompatActivity {
    private SolveView solveView;
    private SolveLineView solveLineView;
    private EditText etSize;
    private EditText etPosition;
    private Button numButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_solve);
        solveView = findViewById(R.id.v_solve);
        solveLineView = findViewById(R.id.v_solve_line);
        numButton = findViewById(R.id.btn_num);
        etSize = findViewById(R.id.et_size);
        etPosition = findViewById(R.id.et_position);
        solve(null);
    }

    public void solve(View view) {
        int size = solveView.getSize();
        String str = etSize.getText().toString();
        try {
            size = Integer.parseInt(str);
        } catch (NumberFormatException ignored) {
        }
        solveView.setSize(size);
        solveLineView.setSize(solveView.getSize());
        etSize.setText(String.valueOf(solveView.getSize()));
        numButton.setText("解数:" + solveView.getResultNum());
    }

    public void previous(View view) {
        solveView.setPosition(solveView.getPosition() - 1);
        etPosition.setText(String.valueOf(solveView.getPosition()));
    }

    public void next(View view) {
        solveView.setPosition(solveView.getPosition() + 1);
        etPosition.setText(String.valueOf(solveView.getPosition()));
    }
//
//    public void position(View view) {
//        long position = solveView.getPosition();
//        String str = etPosition.getText().toString();
//        try {
//            position = Integer.parseInt(str);
//        } catch (NumberFormatException ignored) {
//        }
//        solveView.setPosition(position);
//        etPosition.setText(String.valueOf(solveView.getPosition()));
//    }

    public void line(View view) {
        solveLineView.setVisibility(solveLineView.isShown() ? View.INVISIBLE : View.VISIBLE);
    }

    public void color(View view) {
        Intent intent = new Intent(this, ColorActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            solveView.setOutColor(data.getIntExtra("color", 0xff00ff00));
        }
    }

    public void special(View view) {
        solveView.setSpecial();
    }
}
