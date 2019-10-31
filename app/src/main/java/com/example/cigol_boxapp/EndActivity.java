package com.example.cigol_boxapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class EndActivity  extends AppCompatActivity {
    Button playAgainButton;


    private CompoundButton.OnClickListener mOnButtonClickedListener
            = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Log.d("EndActivity", "hello! I am here!!!");

        bindViews();

        playAgainButton.setOnClickListener(mOnButtonClickedListener);
    }

    private void bindViews() {
        playAgainButton = findViewById(R.id.play_again_button);
    }
}
