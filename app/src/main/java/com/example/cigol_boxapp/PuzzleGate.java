package com.example.cigol_boxapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

public class PuzzleGate {
    static final short AND = 0;
    static final short OR = 1;
    static final short XOR = 2;

    private int gate;
    private PuzzleGate leftGate;
    private PuzzleGate rightGate;

    public PuzzleGate() {
        this.gate = (short)(new Random().nextInt(XOR + 1));
        this.leftGate = null;
        this.rightGate = null;
    }

    public short getOutput(short leftInput, short rightInput) {
        short output = -1;
        if(this.gate == AND) {
            output = (short)(leftInput & rightInput);
        } else if (this.gate == OR) {
            output = (short)(leftInput | rightInput);
        } else if (this.gate == XOR) {
            output = (short)(leftInput ^ rightInput);
        }
        return output;
    }

    public void draw(Canvas canvas, int x, int y, Paint paint) {
        Log.e("coords", String.valueOf(x) + ", " + String.valueOf(y));
        canvas.drawRect(x, y, x + 100, y + 50, paint);
        if(this.rightGate != null) {
            this.rightGate.draw(canvas, x + 75, y + 100, paint);
        }

        if(this.leftGate != null) {
            this.leftGate.draw(canvas, x - 75, y + 100, paint);
        }
    }

    public void addLeftGate(PuzzleGate gate) {
        this.leftGate = gate;
    }

    public void addRightGate(PuzzleGate gate) {
        this.rightGate = gate;
    }

    public PuzzleGate getLeftGate() {
        return this.leftGate;
    }

    public PuzzleGate getRightGate() {
        return this.rightGate;
    }


    private StringBuilder toString(StringBuilder prefix, boolean isTail, StringBuilder sb) {
        if(this.rightGate != null) {
            this.rightGate.toString(new StringBuilder().append(prefix).append(isTail ? "│   " : "    "), false, sb);
        }
        sb.append(prefix).append(isTail ? "└── " : "┌── ").append(String.valueOf(this.gate)).append("\n");
        if(this.leftGate !=null) {
            this.leftGate.toString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true, sb);
        }
        return sb;
    }

    @Override
    public String toString() {
        return this.toString(new StringBuilder(), true, new StringBuilder()).toString();
    }
}
