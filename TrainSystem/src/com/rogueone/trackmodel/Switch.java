/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import com.rogueone.global.Global.PieceType;

/**
 *
 * @author Dan
 */
public class Switch implements TrackPiece {
    
    private int switchID;
    private Line line;
    private TrackPiece portA;
    private TrackPiece portB;
    private TrackPiece portC;
    private boolean isActivated;
    
    //Constructor
    public Switch(int newSwitchID, Line newLine, TrackPiece newPortA, TrackPiece newPortB, TrackPiece newPortC) {
        switchID = newSwitchID;
        line = newLine;
        portA = newPortA;   //static port
        portB = newPortB;   //default dependent port
        portC = newPortC;   //alternate dependent port
        isActivated = false;
    }
    
    //Getters & Setters
    public Line getLine() {
        return line;
    }
    public TrackPiece getPortA() {
        return portA;
    }
    public TrackPiece getPortB() {
        return portB;
    }
    public TrackPiece getPortC() {
        return portC;
    }
    public void toggleSwitch() {
        isActivated = !isActivated;
    }
    public void setSwitch(boolean activate) {
        isActivated = activate;
    }
    
    //TrackPiece interface methods
    public int getID() {
        return switchID;
    }
    public Global.PieceType getType() {
        return PieceType.SWITCH;
    }
    public TrackPiece getNext(TrackPiece previous) {
        if (portA == null || portB == null || portC == null) {
            System.err.println("Switch " + switchID + " is not fully connected. Please check your track configuration.");
        }
        //entering from dependent block B, exiting the static port
        if (!isActivated && previous.getType() == portB.getType() && previous.getID() == portB.getID()) {
            return portA;
        }
        //entering from dependent block C, no block available until switch is activated
        else if (!isActivated && previous.getType() == portC.getType() && previous.getID() == portC.getID()) {
            System.err.println("Train derailed at switch " + switchID + ". Port A = " + portA + ", Port B = " + portB + ". Port C = " + portC + ", Activated = " + isActivated);
            return null;
        }
        //entering from dependent block B, no switch available until switch is deactivated
        else if (isActivated && previous.getType() == portB.getType() && previous.getID() == portB.getID()) {
            System.err.println("Train derailed at switch " + switchID + ". Port A = " + portA + ", Port B = " + portB + ". Port C = " + portC + ", Activated = " + isActivated);
            return null;
        }
        //entering from dependent block C, exiting the static port
        else if (isActivated && previous.getType() == portC.getType() && previous.getID() == portC.getID()) {
            return portA;
        }
        //entering from the static port, exiting the default dependent port
        else if (!isActivated && previous.getType() == portA.getType() && previous.getID() == portA.getID()) {
            return portB;
        }
        //entering from the static port, exiting the alternate dependent port
        else if (isActivated && previous.getType() == portA.getType() && previous.getID() == portA.getID()) {
            return portC;
        }
        //invalid block
        else {
            System.err.println("Invalid block passed to switch.");
            return null;
        }
    }
    
    public boolean equals(TrackPiece otherPiece) {
        if (this.getType() == otherPiece.getType() && this.switchID == otherPiece.getID()) {
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "" + switchID;
    }
    
    public String toStringDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append("Switch: ");
        sb.append(switchID);
        sb.append(", Line: ");
        sb.append(line);
        sb.append(", Port A: ");
        sb.append(portA.getID());
        sb.append(", Port B: ");
        sb.append(portB.getID());
        sb.append(", Port C: ");
        sb.append(portC.getID());
        return sb.toString();
    }  
}