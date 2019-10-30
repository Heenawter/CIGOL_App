package com.example.cigol_boxapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private LinearLayout paintLayout;
    private LinearLayout switchContainer;
    private ConstraintLayout gateContainer;
    private ScrollView scrollContainer;
    private Button probeButton;

    private List<Spinner> spinnerList;

    private int layoutWidth = 0;
    private int layoutHeight = 0;
    private Puzzle puzzle;
    private int numGates;

    private static final int MODE_PROBE = 0;
    private static final int MODE_SOLVE = 1;
    private int mode;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_probe:
                    changeToProbeMode();
                    return true;
                case R.id.navigation_solve:
                    changeToSolveMode();
                    return true;
                case R.id.navigation_reference:
                    setTitle("CIGOL - Reference");
                    probeButton.setText(R.string.title_reference);
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
            Log.d("id of switch", "" + buttonView.getId());
            puzzle.toggleInput(buttonView.getId(), isChecked);
        }
    };

    private CompoundButton.OnClickListener mOnButtonClickedListener
            = new CompoundButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mode == MODE_PROBE) {
                int value = puzzle.probe();
                Log.d("probe result", "" + value);
            } else if (mode == MODE_SOLVE) {
                boolean result = puzzle.solve(spinnerList);
                Log.d("solve result", result + "");
            }
        }
    };

    private ViewTreeObserver.OnGlobalLayoutListener layoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            layoutWidth  = scrollContainer.getMeasuredWidth();
            layoutHeight = scrollContainer.getMeasuredHeight();
            Log.d("layoutWidth", layoutWidth + "");
            Log.d("layoutHeight", layoutHeight + "");

            paintLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            // draw only once width and height are established
            draw();
            buildProbeMode();
            buildSolveMode();

            changeToProbeMode();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        this.numGates = 4;
        this.puzzle = new Puzzle(this, this.numGates);
        this.spinnerList = new ArrayList<>();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        probeButton.setOnClickListener(mOnButtonClickedListener);

        ViewTreeObserver vto = paintLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(layoutListener);
    }

    private void bindViews() {
        navigation = findViewById(R.id.navigation);
        paintLayout = findViewById(R.id.rect);
        switchContainer = findViewById(R.id.toggle_bits_container);
        scrollContainer = findViewById(R.id.scroll_outer);
        gateContainer = findViewById(R.id.gate_container);
        probeButton = findViewById(R.id.submit_button);
    }

    private void draw() {
        Bitmap bg = this.puzzle.draw(this.layoutHeight, this.layoutWidth);

        paintLayout.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bg));
    }

    private void changeToProbeMode() {
        this.mode = MODE_PROBE;
        setTitle("CIGOL - Probe");
        probeButton.setText(R.string.title_probe);

        switchContainer.setVisibility(View.VISIBLE);
        gateContainer.setVisibility(View.INVISIBLE);
    }

    private void buildProbeMode() {
        Switch newSwitch;
//        int buttonStyle = R.style.SwitchCompatTheme;
        int width = this.layoutWidth / (this.numGates + 1);
        for(int i = 0; i < this.numGates + 1; i++) {
//            test = new Switch(new ContextThemeWrapper(this, buttonStyle));
            newSwitch = new Switch(this);
            newSwitch.setId(i);
            newSwitch.setSwitchMinWidth(width - 10);
            newSwitch.setPadding(10, 0, 0, 0);
            newSwitch.setOnCheckedChangeListener(mOnSwitchSelectedListener);
            switchContainer.addView(newSwitch);
        }
    }

    private void changeToSolveMode() {
        this.mode = MODE_SOLVE;
        setTitle("CIGOL - Solve");
        probeButton.setText(R.string.title_solve);

        switchContainer.setVisibility(View.INVISIBLE);
        gateContainer.setVisibility(View.VISIBLE);
    }

    private void buildSolveMode() {
        int width = this.layoutWidth / numGates;
        Spinner newButton;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gate_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for(int i = 0; i < this.numGates; i++) {
            newButton = new Spinner(this);
            newButton.setAdapter(adapter);
            newButton.setId(i);

            gateContainer.addView(newButton);
            this.spinnerList.add(newButton);
            newButton.setDropDownWidth(width);
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(gateContainer);
        PuzzleGate gate;
        for(int i = 0; i < this.numGates; i++) {
            gate = this.puzzle.getGate(i);

            newButton = this.spinnerList.get(i);
            constraintSet.connect(newButton.getId(), ConstraintSet.TOP, gateContainer.getId(), ConstraintSet.TOP, gate.getPosY());
            constraintSet.connect(newButton.getId(), ConstraintSet.LEFT, gateContainer.getId(), ConstraintSet.LEFT, gate.getPosX());
        }

        constraintSet.applyTo(gateContainer);
    }
}
