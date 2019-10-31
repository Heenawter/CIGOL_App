package com.example.cigol_boxapp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.Pair;
import android.widget.Spinner;

public class Puzzle {
    private Resources resources;

    private int numGates;
    private int numInputs;
    private PuzzleGate puzzle;
    private List<PuzzleGate> gateOrder;
    private List<Pair<Integer, PuzzleGate>> inputOrder;
    private int height;

    private int canvasHeight;
    private int gridWidth;
    private int levelHeight;

    private Canvas canvas;
    private Paint paint;

    protected Puzzle(Context context, int numGates) {
        this.resources = context.getResources();
        this.numGates = numGates;
        this.numInputs = numGates + 1;
        this.puzzle = Build(numGates);
        this.height = this.getHeight(this.puzzle);
        this.paint = new Paint();

        Log.d("\ntreeeee!!!\n", this.toString());
    }

    private PuzzleGate Build(int size) {
        PuzzleGate n = null;
        if (size > 0) {
            n = new PuzzleGate(this.resources);
            size--;
            int leftSize = new Random().nextInt(size + 1);
            n.addLeftGate(Build(leftSize));
            n.addRightGate(Build(size - leftSize));
        }
        return n;
    }

    /* ******************************************************************************* */
    /*                                     DRAW                                        */
    /* ******************************************************************************* */

    protected Bitmap draw(int layoutHeight, int layoutWidth) {
        Bitmap bg = Bitmap.createBitmap(layoutWidth, layoutHeight - 100, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(bg);
        this.canvasHeight = this.canvas.getHeight();
        this.levelHeight = (this.canvasHeight - (this.height * resources.getInteger(R.integer.gate_height))) / (this.height + 1);
        int width = this.canvas.getWidth();
        this.gridWidth = width / this.numInputs;

        Log.e("height of tree", this.height + "");
        Log.e("height of canvas", this.canvasHeight + "");
        Log.e("numGates", this.numGates + "");
        Log.e("levelHeight", levelHeight + "");

        this.gateOrder = this.positionGates();
        this.inputOrder = this.buildInputList();
//        this.drawGrid(this.canvasHeight, this.numInputs);
        this.drawPuzzle();

        return bg;
    }



    private void drawPuzzle() {
        PuzzleGate current;

        for(int i = 0; i < this.gateOrder.size(); i++) {
            current = this.gateOrder.get(i);
            current.draw(this.canvas, this.paint, this.gridWidth, this.canvasHeight);
        }

        // draw the top output line
        current = this.puzzle; // head of the tree
        this.canvas.drawLine(current.getPosX() + (this.gridWidth / 2), current.getPosY(),
                current.getPosX() + (this.gridWidth / 2), current.getPosY() - this.levelHeight, this.paint);
    }

    private void drawGrid(int height, int numInputs) {
        int x = this.gridWidth;
        this.paint.setColor(Color.parseColor("#FFFFFF"));
        for(int i = 0; i < numInputs; i++) {
            this.canvas.drawLine(x, 0, x, height, this.paint);
            x = x + this.gridWidth;
        }
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

        int y = this.levelHeight;
        int nodeCount;
        while (!queue.isEmpty())
        {
            nodeCount = queue.size();

            while(nodeCount > 0) {
                PuzzleGate tempNode = queue.poll();
                if(tempNode != null) {
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
            }

            y += this.levelHeight + resources.getInteger(R.integer.gate_height); // only adjust y when on different level
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

    /* ******************************************************************************* */
    /*                                    INPUT                                        */
    /* ******************************************************************************* */

    protected void toggleInput(int num, boolean on) {
        if(on) {
            this.paint.setColor(Color.parseColor("#FF00FF"));
        } else {
            this.paint.setColor(Color.parseColor("#FFFFFF"));
        }
        paint.setStrokeWidth(4);

        Pair<Integer, PuzzleGate> inputPair = this.inputOrder.get(num);
        if(inputPair.first == -1) {
            inputPair.second.toggle(this.canvas, this.paint, this.canvasHeight);
        } else {
            inputPair.second.toggle(this.canvas, this.paint, this.canvasHeight, this.gridWidth);
        }
    }

    private List<Pair<Integer, PuzzleGate>> buildInputList() {
        List<Pair<Integer, PuzzleGate>> inputList = new LinkedList<>();
        PuzzleGate current;
        Pair<Integer, PuzzleGate> pair;
        for(int i = 0; i < this.gateOrder.size(); i++) {
            current = this.gateOrder.get(i);
            if(current.getLeftGate() == null) {
                pair = new Pair<>(-1, current);
                inputList.add(pair);
            }
            if(current.getRightGate() == null) {
                pair = new Pair<>(1, current);
                inputList.add(pair);
            }
        }
        return inputList;
    }


    /* ******************************************************************************* */
    /*                                     PROBE                                       */
    /* ******************************************************************************* */

    protected int probe() {
        int result = probe(this.puzzle);
        if(result == 1) {
            this.paint.setColor(Color.parseColor("#FF00FF"));
        } else {
            this.paint.setColor(Color.parseColor("#FFFFFF"));
        }
        paint.setStrokeWidth(4);
        PuzzleGate head = this.puzzle; // head of the tree
        this.canvas.drawLine(head.getPosX() + (this.gridWidth / 2), head.getPosY(),
                head.getPosX() + (this.gridWidth / 2), 0, this.paint);
        return result;
    }

    private int probe(PuzzleGate head) {
        PuzzleGate left = head.getLeftGate();
        PuzzleGate right = head.getRightGate();

        int leftInput;
        if(left == null) {
            leftInput = head.getLeftInput();
        } else {
            leftInput = probe(left);
        }

        int rightInput;
        if(right == null) {
            rightInput = head.getRightInput();
        } else {
            rightInput = probe(right);
        }

        return head.getOutput(leftInput, rightInput);
    }

    /* ******************************************************************************* */
    /*                                    SOLVE                                        */
    /* ******************************************************************************* */

    protected boolean solve(List<Spinner> guesses) {
        boolean success = true;

        int guess;
        for(int i = 0; i < this.numGates && success; i++) {
            guess = (int)guesses.get(i).getSelectedItemId();
            success = this.gateOrder.get(i).isEqual(guess);
        }

        return success;
    }


    /* ******************************************************************************* */
    /*                                HELPER FUNCTIONS                                 */
    /* ******************************************************************************* */

    private StringBuilder toString(PuzzleGate head, StringBuilder prefix, boolean isTail, StringBuilder sb) {
        PuzzleGate rightGate = head.getRightGate();
        if(rightGate != null) {
            this.toString(rightGate, new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(head.getGate()).append("\n");

        PuzzleGate leftGate  = head.getLeftGate();
        if(leftGate != null) {
            this.toString(leftGate, new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(this.puzzle, new StringBuilder(), true, new StringBuilder()).toString();
    }


    public PuzzleGate getGate(int i) {
        return gateOrder.get(i);
    }
}
