package com.example.lenovo.fragment_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import site.gemus.openingstartanimation.LineDrawStrategy;
import site.gemus.openingstartanimation.OpeningStartAnimation;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
                .setDrawStategy(new LineDrawStrategy())
                .setAppStatement("科技改变世界")
                .setAnimationFinishTime(500)
                .create();
        openingStartAnimation.show(this);

    }

    public void login(View view) {
        Intent intent = new Intent(this,AdminActivity.class);
        startActivity(intent);
        finish();
    }
}
