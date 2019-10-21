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
}
