package com.qwwuyu.lightsout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        ToastUtilImpl.getInstance().init(this);
    }

    public void play(View view) {
        startActivity(new Intent(this, GameActivity.class));
    }

    public void Solve(View view) {
        startActivity(new Intent(this, SolveActivity.class));
    }
}
