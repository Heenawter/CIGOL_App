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
import android.util.Pair;

public class Puzzle {
    static final int GATE_WIDTH = 100;
    static final int GATE_HEIGHT = 100;

    private int numGates;
    private int numInputs;
    private PuzzleGate puzzle;
    private List<Pair<Integer, Integer>> inputPositions;
    private int height;
    private int gridWidth;

    private Canvas canvas;
    private Paint paint;

    public Puzzle(int numGates) {
        this.numGates = numGates;
        this.numInputs = numGates + 1;
        this.puzzle = Build(numGates);
        this.height = this.getHeight(this.puzzle);
        this.paint = new Paint();

        inputPositions = new LinkedList<>();

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
        this.canvas = new Canvas(bg);
        int width = this.canvas.getWidth();
        int height = this.canvas.getHeight();

        this.gridWidth = width / this.numInputs;
        List<PuzzleGate> order = this.positionGates();
        this.drawGrid(height, this.numInputs);
        this.drawPuzzle(order);

        return bg;
    }

    public void toggleInput(int num, boolean on) {
//        int position = (this.gridWidth *  num) - (this.gridWidth / 2);
        Pair<Integer, Integer> position = this.inputPositions.get(num - 1);
        int x = position.first;
        int y = position.second;

        if(on) {
            this.paint.setColor(Color.parseColor("#FF00FF"));
        } else {
            this.paint.setColor(Color.parseColor("#FFFFFF"));
        }

        this.paint.setStrokeWidth(4);
        this.canvas.drawLine(x, y, x, 1000, this.paint);
    }

    private List<PuzzleGate> positionGates() {
        List<PuzzleGate> gateOrder = new LinkedList<>();
        this.inorderTraversal(this.puzzle, gateOrder);
        int startingX = this.gridWidth / 2;
        for(int i = 0; i < gateOrder.size(); i++) {
            gateOrder.get(i).setXPosition(startingX);
            startingX += this.gridWidth;
        }

        this.positionLevelOrder();

        return gateOrder;
    }

    private void drawPuzzle(List<PuzzleGate> order) {
        PuzzleGate current, left, right;
        int x, y;
        int half = this.gridWidth / 2;

        for(int i = 0; i < order.size(); i++) {
            current = order.get(i);
            x = current.getPosX();
            y = current.getPosY();

            this.paint.setColor(Color.parseColor("#CD5C5C"));
            this.canvas.drawRect(x, y, x + this.gridWidth, y + GATE_HEIGHT, this.paint);
            this.paint.setColor(Color.parseColor("#FFFFFF"));
            this.paint.setTextSize(30);
            this.canvas.drawText(current.getGate(), x + (GATE_HEIGHT / 4), y + (GATE_HEIGHT / 2), this.paint);

            this.paint.setStrokeWidth(4);
            left = current.getLeftGate();
            if(left != null) {
                this.canvas.drawLine(x, y + GATE_HEIGHT, left.getPosX() + half, left.getPosY(), this.paint);
            } else {
                this.canvas.drawLine(x, y + GATE_HEIGHT, x, 1000, this.paint);
                inputPositions.add(new Pair<>(x, y + GATE_HEIGHT));
            }
            right = current.getRightGate();
            if(right != null) {
                this.canvas.drawLine(x + this.gridWidth, y + GATE_HEIGHT, right.getPosX() + half, right.getPosY(), this.paint);
            } else {
                this.canvas.drawLine(x + this.gridWidth, y + GATE_HEIGHT, x + this.gridWidth, 1000, this.paint);
                inputPositions.add(new Pair<>(x + this.gridWidth, y + GATE_HEIGHT));
            }
        }
    }

    private void drawGrid(int height, int numInputs) {
        int x = this.gridWidth;
        this.paint.setColor(Color.parseColor("#FFFFFF"));
        for(int i = 0; i < numInputs; i++) {
            this.canvas.drawLine(x, 0, x, height, this.paint);
            x = x + this.gridWidth;
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

            y += 200; // only adjust y when on different level
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
