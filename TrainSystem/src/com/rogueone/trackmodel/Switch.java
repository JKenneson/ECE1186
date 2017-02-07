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
    private TrackPiece portA;
    private TrackPiece portB;
    private TrackPiece portC;
    private boolean activated;
    
    public Switch(int newCentralSwitchID) {
        centralSwitchID = newCentralSwitchID;
        portA = null;   //static port
        portB = null;   //default dependent port
        portC = null;   //alternate dependent port
        activated = false;
    }
    
    public void setPortA(TrackPiece port){
        portA = port;
    }
    
    public void setPortB(TrackPiece port){
        portB = port;
    }
    
    public void setPortC(TrackPiece port){
        portC = port;
    }
    
    public void toggleSwitch() {
        activated = !activated;
    }
    
    public void setSwitch(boolean activate) {
        activated = activate;
    }
    
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Switch: ");
        sb.append(centralSwitchID);
        sb.append(", Port A: ");
        sb.append(portA);
        sb.append(", Port B: ");
        sb.append(portB);
        sb.append(", Port C: ");
        sb.append(centralSwitchID);
        return sb.toString();
    }
    
}