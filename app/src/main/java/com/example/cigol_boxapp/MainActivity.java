package com.example.cigol_boxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private Switch switchButton;
    private TextView mTextMessage;
    private TextView mainMessage;
    private DrawPuzzle test;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        test = new DrawPuzzle(this);
//        setContentView(R.layout.activity_main);
        setContentView(test);
//
//        bindViews();
//
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        switchButton.setOnCheckedChangeListener(testListener);
    }

    private void bindViews() {
        mTextMessage = findViewById(R.id.message);
        navigation = findViewById(R.id.navigation);
        switchButton = findViewById(R.id.switch1);
        mainMessage = findViewById(R.id.textView);
//        test = findViewById(R.id.testDraw);
    }
}
