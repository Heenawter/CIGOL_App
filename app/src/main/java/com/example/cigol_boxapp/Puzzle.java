package com.example.cigol_boxapp;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Puzzle {
    private int numGates;
    private int numInputs;
    private PuzzleGate puzzle;

    public Puzzle(int numGates) {
        this.numGates = numGates;
        this.numInputs = numGates + 1;
        this.puzzle = Build(numGates);

        Log.e("here", "I am here!");
        Log.e("\ntree!\n", this.puzzle.toString());
    }

    private PuzzleGate Build(int size) {
        PuzzleGate n = null;
        if (size > 0) {
            n = new PuzzleGate();
            size--;
            int leftSize = new Random().nextInt(size + 1);
            n.addLeftGate(Build(leftSize));
            n.addRightGate(Build(size - leftSize));
        }
        return n;
    }

    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#CD5C5C"));
        this.puzzle.draw(canvas,width / 2, 50, paint);
    }

}
