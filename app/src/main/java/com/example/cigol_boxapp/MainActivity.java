package com.example.cigol_boxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private LinearLayout paintLayout;
    private LinearLayout switchContainer;

    private int layoutWidth = 0;
    private int layoutHeight = 0;
    private Puzzle puzzle;
    private int numGates;

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
        this.numGates = 4;
        this.puzzle = new Puzzle(this.numGates);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ViewTreeObserver vto = paintLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
    }

    private void bindViews() {
        navigation = findViewById(R.id.navigation);
        paintLayout = findViewById(R.id.rect);
        switchContainer = findViewById(R.id.toggle_bits_container);
    }

    private void draw() {
        Bitmap bg = this.puzzle.draw(this.layoutHeight, this.layoutWidth);

        paintLayout.setBackgroundDrawable(new BitmapDrawable(getApplicationContext().getResources(), bg));

        Switch test;

//        int buttonStyle = R.style.SwitchCompatTheme;
        int width = this.layoutWidth / (this.numGates + 1);
        for(int i = 0; i < this.numGates + 1; i++) {
//            test = new Switch(new ContextThemeWrapper(this, buttonStyle));
            test = new Switch(this);
            test.setSwitchMinWidth(width - 10);
            test.setPadding(10, 0, 0, 0);
            switchContainer.addView(test);
        }
    }
}
