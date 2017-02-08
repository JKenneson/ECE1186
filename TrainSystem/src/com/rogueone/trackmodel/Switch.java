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
    
    private int centralSwitchID;
    private Global.Line line;
    private TrackPiece portA;
    private TrackPiece portB;
    private TrackPiece portC;
    private boolean activated;
    
    //Constructor
    public Switch(int newCentralSwitchID, Global.Line newLine, TrackPiece newPortA, TrackPiece newPortB, TrackPiece newPortC) {
        centralSwitchID = newCentralSwitchID;
        line = newLine;
        portA = newPortA;   //static port
        portB = newPortB;   //default dependent port
        portC = newPortC;   //alternate dependent port
        activated = false;
    }
    
    //Getters & Setters
    public void setPortA(TrackPiece port){
        portA = port;
    }
    public TrackPiece getPortA() {
        return portA;
    }
    public void setPortB(TrackPiece port){
        portB = port;
    }
    public TrackPiece getPortB() {
        return portB;
    }
    public void setPortC(TrackPiece port){
        portC = port;
    }
    public TrackPiece getPortC() {
        return portC;
    }
    public void toggleSwitch() {
        activated = !activated;
    }
    public void setSwitch(boolean activate) {
        activated = activate;
    }
    
    //TrackPiece interface methods
    public int getID() {
        return centralSwitchID;
    }
    public Global.PieceType getType() {
        return PieceType.SWITCH;
    }
    public TrackPiece getNext(TrackPiece previous) {
        //entering from dependent block B, exiting the static port
        if (previous.getType() == portB.getType() && previous.getID() == portB.getID()) {
            return portA;
        }
        //entering from dependent block C, exiting the static port
        else if (previous.getType() == portC.getType() && previous.getID() == portC.getID()) {
            return portA;
        }
        //entering from the static port, exiting the default dependent port
        else if (previous.getType() == portA.getType() && previous.getID() == portA.getID() && !activated) {
            return portB;
        }
        //entering from the static port, exiting the alternate dependent port
        else if (previous.getType() == portA.getType() && previous.getID() == portA.getID() && activated) {
            return portC;
        }
        //invalid block
        else {
            return null;
        }
    }
    
    //Overridden methods
    public String toString() {
        return "" + centralSwitchID;
    }
    
    public String toStringDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append("Switch: ");
        sb.append(centralSwitchID);
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