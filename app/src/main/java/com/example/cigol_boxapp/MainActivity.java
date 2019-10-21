package com.example.cigol_boxapp;

import android.util.Log;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private LinearLayout paintLayout;

    private int layoutWidth = 0;
    private int layoutHeight = 0;
    private Puzzle puzzle;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_probe:
                    setTitle("CIGOL - Probe");
                    return true;
                case R.id.navigation_solve:
                    setTitle("CIGOL - Solve");
                    return true;
                case R.id.navigation_reference:
                    setTitle("CIGOL - Reference");
                    return true;
            }
            return false;
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener layoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            layoutWidth  = paintLayout.getMeasuredWidth();
            layoutHeight = paintLayout.getMeasuredHeight();
            paintLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            // draw only once width and height are established
            draw();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("CIGOL - Probe");

        bindViews();
        this.puzzle = new Puzzle(4);

//        for(int i = 0; i < 10; i++) {
//            test = new Puzzle(4);
//        }

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ViewTreeObserver vto = paintLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
    }

    private void bindViews() {
        navigation = findViewById(R.id.navigation);
        paintLayout = findViewById(R.id.rect);
    }

    private void draw() {
        Bitmap bg = this.puzzle.draw(this.layoutHeight, this.layoutWidth);

        paintLayout.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), bg));

    }
}
