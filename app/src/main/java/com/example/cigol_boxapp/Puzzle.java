package com.example.cigol_boxapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
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

    public Puzzle(int numGates) {
        this.numGates = numGates;
        this.numInputs = numGates + 1;
        this.puzzle = Build(numGates);
        this.height = this.getHeight(this.puzzle);

        Log.d("\ntreeeee!!!\n", this.toString());
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

        int gridWidth = width / this.numInputs;
        this.positionGates(gridWidth);
        this.drawGrid(canvas, width, height, this.numInputs, paint);
        this.drawPuzzle(this.puzzle, gridWidth, canvas, paint);
        return bg;
    }

    private void positionGates(int width) {
        List<PuzzleGate> gateOrder = new LinkedList<>();
        this.inorderTraversal(this.puzzle, gateOrder);
        int startingX = width / 2;
        for(int i = 0; i < gateOrder.size(); i++) {
            gateOrder.get(i).setXPosition(startingX);
            startingX += width;
        }

        this.positionLevelOrder();
    }

    private void drawPuzzle(PuzzleGate head, int width, Canvas canvas, Paint paint) {
        int x;
        int y;

        if(head == null) {
            return;
        } else {
            x = head.getPosX();
            y = head.getPosY();

            paint.setColor(Color.parseColor("#CD5C5C"));
            canvas.drawRect(x, y, x + width, y + GATE_HEIGHT, paint);
            paint.setColor(Color.parseColor("#FFFFFF"));
            paint.setTextSize(30);
            canvas.drawText(head.getGate(), x + (GATE_HEIGHT / 4), y + (GATE_HEIGHT / 2), paint);

            Log.d("current", head.getGate());
            Log.d("x and y", x + ", " + y);

            this.drawPuzzle(head.getLeftGate(), width, canvas, paint);
            this.drawPuzzle(head.getRightGate(), width, canvas, paint);
        }
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

    private void inorderTraversal(PuzzleGate head, List<PuzzleGate> order) {
        if(head == null) {
            return;
        }

        // traverse left
        this.inorderTraversal(head.getLeftGate(), order);

        // visit
        order.add(head);

        // traverse right
        this.inorderTraversal(head.getRightGate(), order);
    }

    private void positionLevelOrder() {
        Queue<PuzzleGate> queue = new LinkedList<>();
        queue.add(this.puzzle);

        int y = 50;
        int nodeCount = 0;
        while (!queue.isEmpty())
        {
            nodeCount = queue.size();

            while(nodeCount > 0) {
                PuzzleGate tempNode = queue.poll();
                tempNode.setYPosition(y);

                /*Enqueue left child */
                PuzzleGate left = tempNode.getLeftGate();
                if (left != null) {
                    queue.add(left);
                }

                /*Enqueue right child */
                PuzzleGate right = tempNode.getRightGate();
                if (right != null) {
                    queue.add(right);
                }
                nodeCount--;
            }

            y += 300; // only adjust y when on different level
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
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(head.getGate()).append("\n");

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
