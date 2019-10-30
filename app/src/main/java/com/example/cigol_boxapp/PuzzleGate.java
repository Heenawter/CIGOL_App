package com.example.cigol_boxapp;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class PuzzleGate {
    private Resources resources;

    private int gate;
    private PuzzleGate leftGate;
    private PuzzleGate rightGate;
    private int leftInput;
    private int rightInput;

    private int posX;
    private int posY;

    PuzzleGate(Resources resources) {
        this.resources = resources;
        this.gate = (short)(new Random().nextInt(resources.getInteger(R.integer.last_type) + 1));
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
        if(this.gate == resources.getInteger(R.integer.and_type)) {
            output = leftInput & rightInput;
        } else if (this.gate == resources.getInteger(R.integer.or_type)) {
            output = leftInput | rightInput;
        } else if (this.gate == resources.getInteger(R.integer.xor_type)) {
            output = leftInput ^ rightInput;
        }
        return output;
    }

    protected void draw(Canvas canvas, Paint paint, int width, int height) {
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawRect(posX, posY, posX + width, posY + resources.getInteger(R.integer.gate_height), paint);
        paint.setColor(Color.parseColor("#FFFFFF"));
//        paint.setTextSize(30);
//        canvas.drawText(this.getGate(), posX + (GATE_HEIGHT / 4), posY + (GATE_HEIGHT / 2), paint);

        paint.setStrokeWidth(4);
        if(leftGate != null) {
            canvas.drawLine(posX, posY + resources.getInteger(R.integer.gate_height), leftGate.getPosX() + (width / 2), leftGate.getPosY(), paint);
        } else {
            canvas.drawLine(posX, posY + resources.getInteger(R.integer.gate_height), posX, height, paint);
        }

        if(rightGate != null) {
            canvas.drawLine(posX + width, posY + resources.getInteger(R.integer.gate_height), rightGate.getPosX() + (width / 2), rightGate.getPosY(), paint);
        } else {
            canvas.drawLine(posX + width, posY + resources.getInteger(R.integer.gate_height), posX + width, height, paint);
        }
    }

    protected String getGate() {
        String gate = "";
        if(this.gate == resources.getInteger(R.integer.and_type)) {
            gate = "AND";
        } else if (this.gate == resources.getInteger(R.integer.or_type)) {
            gate = "OR";
        } else if (this.gate == resources.getInteger(R.integer.xor_type)) {
            gate = "XOR";
        }
        return gate;
    }

    protected boolean isEqual(int otherGate) {
        return otherGate == this.gate;
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
        canvas.drawLine(posX, posY + resources.getInteger(R.integer.gate_height), posX, height, paint);
    }

    protected void toggle(Canvas canvas, Paint paint, int height, int width) {
        this.rightInput = this.rightInput ^ 1;
        canvas.drawLine(posX + width, posY + resources.getInteger(R.integer.gate_height), posX + width, height, paint);
    }
}

