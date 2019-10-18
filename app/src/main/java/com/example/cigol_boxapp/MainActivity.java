package com.example.cigol_boxapp;

import android.os.Build;
import android.util.Log;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
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

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ViewTreeObserver vto = paintLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
    }

    private void bindViews() {
        navigation = findViewById(R.id.navigation);
        paintLayout = findViewById(R.id.rect);
    }

    private void draw() {

        Paint paint = new Paint();
        Bitmap bg = Bitmap.createBitmap(layoutWidth, layoutHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawRect(0, 0, 50, 50, paint);
        canvas.drawRect(canvas.getWidth() - 50, canvas.getHeight() - 100, canvas.getWidth(), canvas.getHeight(), paint);
        Log.e("canvasWidth", String.valueOf(canvas.getWidth()));
        Log.e("canvasHeight", String.valueOf(canvas.getHeight()));
        Log.e("layoutWidth", String.valueOf(layoutWidth));
        Log.e("layoutHeight", String.valueOf(layoutHeight));
//        paint.setColor(Color.WHITE);
//        int x = canvas.getWidth();
//        int y = canvas.getHeight();
//        canvas.drawCircle(x / 2, y / 2, 200, paint);

        paintLayout.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), bg));

    }
}
