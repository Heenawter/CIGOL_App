package com.example.cigol_boxapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class PuzzleGate {
    private static final short AND = 0;
    private static final short OR = 1;
    private static final short XOR = 2;

    private int gate;
    private PuzzleGate leftGate;
    private PuzzleGate rightGate;
    private int leftInput;
    private int rightInput;

    private int posX;
    private int posY;

    private static final int GATE_HEIGHT = 125;

    PuzzleGate() {
        this.gate = (short)(new Random().nextInt(XOR + 1));
        this.leftGate = null;
        this.rightGate = null;

        this.leftInput = 0;
        this.rightInput = 0;

        this.posX = 0;
        this.posY = 0;
    }

    @SuppressWarnings("WeakerAccess")
    protected int getOutput(int leftInput, int rightInput) {
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

    protected void draw(Canvas canvas, Paint paint, int width, int height) {
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawRect(posX, posY, posX + width, posY + GATE_HEIGHT, paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
//        paint.setTextSize(30);
//        canvas.drawText(this.getGate(), posX + (GATE_HEIGHT / 4), posY + (GATE_HEIGHT / 2), paint);

        paint.setStrokeWidth(4);
        if(leftGate != null) {
            canvas.drawLine(posX, posY + GATE_HEIGHT, leftGate.getPosX() + (width / 2), leftGate.getPosY(), paint);
        } else {
            canvas.drawLine(posX, posY + GATE_HEIGHT, posX, height, paint);
        }

        if(rightGate != null) {
            canvas.drawLine(posX + width, posY + GATE_HEIGHT, rightGate.getPosX() + (width / 2), rightGate.getPosY(), paint);
        } else {
            canvas.drawLine(posX + width, posY + GATE_HEIGHT, posX + width, height, paint);
        }
    }

    protected String getGate() {
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

    protected void setXPosition(int x) {
        this.posX = x;
    }

    protected void setYPosition(int y) {
        this.posY = y;
    }

    protected void addLeftGate(PuzzleGate gate) { this.leftGate = gate; }

    protected void addRightGate(PuzzleGate gate) { this.rightGate = gate; }

    protected int getPosX() { return this.posX; }

    protected int getPosY() { return this.posY; }

    protected PuzzleGate getLeftGate() {
        return this.leftGate;
    }

    protected PuzzleGate getRightGate() { return this.rightGate; }

    protected int getLeftInput() { return this.leftInput; }

    protected int getRightInput() { return this.rightInput; }

    protected void toggle(Canvas canvas, Paint paint, int height) {
        this.leftInput = this.leftInput ^ 1;
        canvas.drawLine(posX, posY + GATE_HEIGHT, posX, height, paint);
    }

    protected void toggle(Canvas canvas, Paint paint, int height, int width) {
        this.rightInput = this.rightInput ^ 1;
        canvas.drawLine(posX + width, posY + GATE_HEIGHT, posX + width, height, paint);
    }
}

