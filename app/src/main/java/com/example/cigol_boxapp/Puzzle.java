package com.example.cigol_boxapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Puzzle {
    static final int GATE_WIDTH = 100;
    static final int GATE_HEIGHT = 100;

    private int numGates;
    private int numInputs;
    private PuzzleGate puzzle;
    private int height;
    private int leftNodeWeight;
    private int rightNodeWeight;

    public Puzzle(int numGates) {
        this.numGates = numGates;
        this.numInputs = numGates + 1;
        this.puzzle = Build(numGates);
        this.height = this.getHeight(this.puzzle);

        Log.d("here", "I am here!");
        Log.d("tree height", String.valueOf(this.height));
        Log.d("\ntree!\n", this.toString());

        List<Integer> nodeWeights = new ArrayList<>();
        this.nodeDistances(this.puzzle, nodeWeights, 0);
        this.leftNodeWeight = 0;
        this.rightNodeWeight = 0;
        int value;
        for(int i = 0; i < nodeWeights.size(); i++) {
            Log.e("Value " + i, nodeWeights.get(i).toString());
            value = nodeWeights.get(i);
            if(value < leftNodeWeight) {
                leftNodeWeight = value;
            } else if (value > rightNodeWeight) {
                rightNodeWeight = value;
            }
        }
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

        // find if left-heavy or right-heavy to determine starting position
        int gridWidth = width / this.numInputs;
        int puzzleWeight = this.leftNodeWeight + this.rightNodeWeight;
        int shiftAmount = (-1) * (puzzleWeight / 2);
        int startingX = (width / 2) - (gridWidth / 2); // start in the middle;
        startingX += (shiftAmount * gridWidth);
        if(puzzleWeight % 2 != 0) {
            startingX += (int)(Math.signum(puzzleWeight)) * -1 * (gridWidth / 2);
        }

        Log.d("puzzleWeight", String.valueOf(puzzleWeight));
        Log.d("shiftAmount", String.valueOf(shiftAmount));
        Log.d("min ----", String.valueOf(this.leftNodeWeight));
        Log.d("max ----", String.valueOf(this.rightNodeWeight));
        Log.d("x:", String.valueOf(startingX));

        this.drawGrid(canvas, width, height, this.numInputs, paint);
        this.draw(this.puzzle, canvas, startingX, 50, gridWidth, paint);

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

    private void draw(PuzzleGate head, Canvas canvas, int x, int y, int width, Paint paint) {
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawRect(x, y, x + width, y + GATE_HEIGHT, paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextSize(30);
        canvas.drawText(head.getGate(), x + (GATE_HEIGHT / 4), y + (GATE_HEIGHT / 2), paint);

        paint.setColor(Color.parseColor("#00FFFF"));
        paint.setStrokeWidth(5);
        PuzzleGate rightGate = head.getRightGate();
        if(rightGate != null) {
            canvas.drawLine(x + (width / 2), y + GATE_HEIGHT, x + (int)(width * 1.5), y + (GATE_HEIGHT * 2), paint);
            this.draw(rightGate, canvas, x + width, y + (GATE_HEIGHT * 2), width, paint);
        }

        PuzzleGate leftGate  = head.getLeftGate();
        if(leftGate != null) {
            canvas.drawLine(x + (width / 2), y + GATE_HEIGHT, x - (width / 2), y + (GATE_HEIGHT * 2), paint);
            this.draw(leftGate, canvas, x - width, y + (GATE_HEIGHT * 2), width, paint);
        }
    }


    private void nodeDistances(PuzzleGate head, List<Integer> minMax, int currentDistance) {
        minMax.add(currentDistance);
        PuzzleGate leftGate  = head.getLeftGate();
        if(leftGate != null) {
            nodeDistances(leftGate, minMax, currentDistance - 1);
        }
        PuzzleGate rightGate  = head.getRightGate();
        if(rightGate != null) {
            nodeDistances(rightGate, minMax, currentDistance + 1);
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
