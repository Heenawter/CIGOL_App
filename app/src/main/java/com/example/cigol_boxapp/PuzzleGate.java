package com.example.cigol_boxapp;

import android.util.Log;

import java.util.Random;

public class PuzzleGate {
    static final short AND = 0;
    static final short OR = 1;
    static final short XOR = 2;

    private int gate;
    private PuzzleGate leftGate;
    private PuzzleGate rightGate;
    private int posX;
    private int posY;

    public PuzzleGate() {
        this.gate = (short)(new Random().nextInt(XOR + 1));
        this.leftGate = null;
        this.rightGate = null;
        this.posX = 0;
        this.posY = 0;
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

    public int countChildren() {
        PuzzleGate left = this.leftGate;
        int count = 0;
        if(left != null) {
            count++;
            count += left.countChildren();
        }

        PuzzleGate right = this.rightGate;
        if(right != null) {
            count++;
            count += right.countChildren();
        }

        return count;
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
}
