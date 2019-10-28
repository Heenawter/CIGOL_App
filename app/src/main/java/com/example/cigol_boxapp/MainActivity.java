package com.example.cigol_boxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private LinearLayout paintLayout;
    private LinearLayout switchContainer;
    private Button probeButton;

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

    private CompoundButton.OnCheckedChangeListener mOnSwitchSelectedListener
            = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // the isChecked will be true if the switch is in the On position
            Log.e("id of switch", "" + buttonView.getId());
            puzzle.toggleInput(buttonView.getId(), isChecked);
        }
    };

    private CompoundButton.OnClickListener mOnButtonClickedListener
            = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            int value = puzzle.probe();
            Log.e("value", "" + value);
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener layoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            layoutWidth  = paintLayout.getMeasuredWidth();
            layoutHeight = paintLayout.getMeasuredHeight();
            paintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

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
        this.numGates = 5;
        this.puzzle = new Puzzle(this.numGates);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        probeButton.setOnClickListener(mOnButtonClickedListener);

        ViewTreeObserver vto = paintLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
    }

    private void bindViews() {
        navigation = findViewById(R.id.navigation);
        paintLayout = findViewById(R.id.rect);
        switchContainer = findViewById(R.id.toggle_bits_container);
        probeButton = findViewById(R.id.submit_button);
    }

    private void draw() {
        Bitmap bg = this.puzzle.draw(this.layoutHeight, this.layoutWidth);

        paintLayout.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bg));

        Switch test;

//        int buttonStyle = R.style.SwitchCompatTheme;
        int width = this.layoutWidth / (this.numGates + 1);
        for(int i = 0; i < this.numGates + 1; i++) {
//            test = new Switch(new ContextThemeWrapper(this, buttonStyle));
            test = new Switch(this);
            test.setId(i);
            test.setSwitchMinWidth(width - 10);
            test.setPadding(10, 0, 0, 0);
            test.setOnCheckedChangeListener(mOnSwitchSelectedListener);
            switchContainer.addView(test);
        }
    }
}
