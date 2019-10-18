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
    private Switch switchButton;
    private TextView mTextMessage;
    private TextView mainMessage;
    private LinearLayout paintLayout;

    private int layoutWidth = 0;
    private int layoutHeight = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_probe:
                    mTextMessage.setText(R.string.title_probe);
                    return true;
                case R.id.navigation_solve:
                    mTextMessage.setText(R.string.title_solve);
                    return true;
                case R.id.navigation_reference:
                    mTextMessage.setText(R.string.title_reference);
                    return true;
            }
            return false;
        }
    };

    private CompoundButton.OnCheckedChangeListener testListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mainMessage.setText("clicked!");
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener layoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            layoutWidth  = paintLayout.getMeasuredWidth();
            layoutHeight = paintLayout.getMeasuredHeight();
            paintLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            Log.e("testWidth1", String.valueOf(layoutWidth));
            Log.e("testHeight1", String.valueOf(layoutHeight));

            draw();
        }
    };

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        switchButton.setOnCheckedChangeListener(testListener);

        ViewTreeObserver vto = paintLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
    }

    private void bindViews() {
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        switchButton = findViewById(R.id.switch1);
        mainMessage = findViewById(R.id.textView);
        paintLayout = findViewById(R.id.rect);
    }

    private void draw() {

        Paint paint = new Paint();
        Bitmap bg = Bitmap.createBitmap(layoutWidth, layoutHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawRect(0, 0, 200, 200, paint);

        paint.setColor(Color.WHITE);
        int x = canvas.getWidth();
        int y = canvas.getHeight();
        canvas.drawCircle(x / 2, y / 2, 100, paint);

        paintLayout.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), bg));

    }
}
