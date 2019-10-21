package com.example.cigol_boxapp;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Puzzle {
    static final int GATE_WIDTH = 100;
    static final int GATE_HEIGHT = 50;

    private int numGates;
    private int numInputs;
    private PuzzleGate puzzle;
    private int height;

    public Puzzle(int numGates) {
        this.numGates = numGates;
        this.numInputs = numGates + 1;
        this.puzzle = Build(numGates);
        this.height = this.getHeight(this.puzzle);

        Log.e("here", "I am here!");
        Log.e("tree height", String.valueOf(this.height));
        Log.e("\ntree!\n", this.toString());
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

    public Bitmap draw(int layoutHeight, int layoutWidth) {
        Bitmap bg = Bitmap.createBitmap(layoutWidth, layoutHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        int width = canvas.getWidth();
        int height = canvas.getHeight();
        Paint paint = new Paint();

        this.drawGrid(canvas, width, height, this.numInputs, paint);
        this.draw(this.puzzle, canvas, width / 2, 50, paint);

        return bg;
    }


    private void drawGrid(Canvas canvas, int width, int height, int numInputs, Paint paint) {
        int gridWidth = width / numInputs;
        int x = gridWidth;
        paint.setColor(Color.parseColor("#FFFFFF"));
        for(int i = 0; i < numInputs; i++) {
            canvas.drawLine(x, 0, x, height, paint);
            x = x + gridWidth;
        }
    }

    private void draw(PuzzleGate head, Canvas canvas, int x, int y, Paint paint) {
        Log.e("coords", String.valueOf(x) + ", " + String.valueOf(y));
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawRect(x, y, x + GATE_WIDTH, y + GATE_HEIGHT, paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextSize(30);
        canvas.drawText(head.getGate(), x + (GATE_HEIGHT / 4), y + (GATE_HEIGHT / 2), paint);

        PuzzleGate rightGate = head.getRightGate();
        if(rightGate != null) {
//            canvas.drawLine(x + (GATE_WIDTH / 2), y + GATE_HEIGHT, x + (GATE_WIDTH * 3), y + (GATE_HEIGHT * 5), paint);
            this.draw(rightGate, canvas, x + (GATE_WIDTH * 2), y + (GATE_HEIGHT * 4), paint);
        }

        PuzzleGate leftGate  = head.getLeftGate();
        if(leftGate != null) {
//            canvas.drawLine(x + (GATE_WIDTH / 2), y + GATE_HEIGHT, x - (GATE_WIDTH * 2), y + (GATE_HEIGHT * 5), paint);
            this.draw(leftGate, canvas, x - (GATE_WIDTH * 2), y + (GATE_HEIGHT * 4), paint);
        }
    }

    private int getHeight(PuzzleGate head) {
        if (head == null)
            return 0;
        else {
            /* compute the depth of each subtree */
            int lDepth = this.getHeight(head.getLeftGate());
            int rDepth = this.getHeight(head.getRightGate());

            /* use the larger one */
            if (lDepth > rDepth)
                return(lDepth + 1);
            else return(rDepth + 1);
        }
    }

    private StringBuilder toString(PuzzleGate head, StringBuilder prefix, boolean isTail, StringBuilder sb) {
        PuzzleGate rightGate = head.getRightGate();
        if(rightGate != null) {
            this.toString(rightGate, new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(String.valueOf(head.getGate())).append("\n");

        PuzzleGate leftGate  = head.getLeftGate();
        if(leftGate !=null) {
            this.toString(leftGate, new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(this.puzzle, new StringBuilder(), true, new StringBuilder()).toString();
    }

}
