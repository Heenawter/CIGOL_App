package com.example.cigol_boxapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.util.Pair;

import java.util.Random;

public class PuzzleGate {
    static final short AND = 0;
    static final short OR = 1;
    static final short XOR = 2;

    private int gate;
    private PuzzleGate leftGate;
    private PuzzleGate rightGate;
    private int leftInput;
    private int rightInput;

    private int posX;
    private int posY;

    static final int GATE_HEIGHT = 100;

    public PuzzleGate() {
        this.gate = (short)(new Random().nextInt(XOR + 1));
        this.leftGate = null;
        this.rightGate = null;

        this.leftInput = 0;
        this.rightInput = 0;

        this.posX = 0;
        this.posY = 0;
    }

    public int getOutput(int leftInput, int rightInput) {
        int output = -1;
        if(this.gate == AND) {
            output = leftInput & rightInput;
        } else if (this.gate == OR) {
            output = leftInput | rightInput;
        } else if (this.gate == XOR) {
            output = leftInput ^ rightInput;
        }
        return output;
    }

    public void draw(Canvas canvas, Paint paint, int width) {
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawRect(posX, posY, posX + width, posY + GATE_HEIGHT, paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setTextSize(30);
        canvas.drawText(this.getGate(), posX + (GATE_HEIGHT / 4), posY + (GATE_HEIGHT / 2), paint);

        paint.setStrokeWidth(4);
        if(leftGate != null) {
            canvas.drawLine(posX, posY + GATE_HEIGHT, leftGate.getPosX() + (width / 2), leftGate.getPosY(), paint);
        } else {
            canvas.drawLine(posX, posY + GATE_HEIGHT, posX, 1000, paint);
        }

        if(rightGate != null) {
            canvas.drawLine(posX + width, posY + GATE_HEIGHT, rightGate.getPosX() + (width / 2), rightGate.getPosY(), paint);
        } else {
            canvas.drawLine(posX + width, posY + GATE_HEIGHT, posX + width, 1000, paint);
        }
    }

    public String getGate() {
        String gate = "";
        if(this.gate == AND) {
            gate = "AND";
        } else if (this.gate == OR) {
            gate = "OR";
        } else if (this.gate == XOR) {
            gate = "XOR";
        }
        return gate;
    }

    public void setXPosition(int x) {
        this.posX = x;
    }

    public void setYPosition(int y) {
        this.posY = y;
    }

    public void addLeftGate(PuzzleGate gate) { this.leftGate = gate; }

    public void addRightGate(PuzzleGate gate) { this.rightGate = gate; }

    public int getPosX() { return this.posX; }

    public int getPosY() { return this.posY; }

    public PuzzleGate getLeftGate() {
        return this.leftGate;
    }

    public PuzzleGate getRightGate() { return this.rightGate; }

    public int getLeftInput() { return this.leftInput; }

    public int getRightInput() { return this.rightInput; }


    public int getInputCount() {
        int num = 0;
        if(leftGate == null)
            num++;
        if(rightGate == null)
            num++;
        return num;
    }

    public void toggle(Canvas canvas, Paint paint) {
        this.leftInput = this.leftInput ^ 1;
        canvas.drawLine(posX, posY, posX, 1000, paint);
    }

    public void toggle(Canvas canvas, Paint paint, int width) {
        this.rightInput = this.rightInput ^ 1;
        canvas.drawLine(posX + width, posY, posX + width, 1000, paint);
    }
}

