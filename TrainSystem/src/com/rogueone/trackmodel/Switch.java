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
    
    private final int switchID;
    private final Line line;
    private final TrackPiece portA;
    private final TrackPiece portB;
    private final TrackPiece portC;
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
    
    /**
    * Get line switch is on
    * @author Dan Bednarczyk
    * @return Line object switch is on
    */
    public Line getLine() {
        return line;
    }
    
    /**
    * Get portA (static block)
    * @author Dan Bednarczyk
    * @return TrackPiece for portA
    */
    public TrackPiece getPortA() {
        return portA;
    }
    
    /**
    * Get portB (default block)
    * @author Dan Bednarczyk
    * @return TrackPiece for portB
    */
    public TrackPiece getPortB() {
        return portB;
    }
    
    /**
    * Get portC (alternate block)
    * @author Dan Bednarczyk
    * @return TrackPiece for portC
    */
    public TrackPiece getPortC() {
        return portC;
    }
    
    /**
    * Check if Switch is activated
    * @return boolean indicating state
    * @author Dan Bednarczyk
    */
    public boolean isActivated() {
        return isActivated;
    }
    
    /**
    * Toggle current switch state
    * @author Dan Bednarczyk
    */
    public void toggleSwitch() {
        isActivated = !isActivated;
    }
    
    /**
    * Set switch state
    * @author Dan Bednarczyk
    * @param activate boolean for new switch state
    */
    public void setSwitch(boolean activate) {
        isActivated = activate;
    }
    
    //TrackPiece interface methods
    
    /**
    * Get ID of Switch
    * @author Dan Bednarczyk
    * @return int ID of Switch
    */
    @Override
    public int getID() {
        return switchID;
    }
    
    /**
    * Get type of the TrackPiece
    * @author Dan Bednarczyk
    * @return PieceType Switch
    */
    @Override
    public Global.PieceType getType() {
        return PieceType.SWITCH;
    }
    
    /**
    * Get next TrackPiece
    * @author Dan Bednarczyk
    * @param previous The previous TrackPiece
    * @return TrackPiece on the other side of the Switch
    */
    @Override
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
            System.err.println("Incorrect state at switch " + switchID + ". Port A = " + portA + ", Port B = " + portB + ". Port C = " + portC + ", Activated = " + isActivated);
            return null;
        }
        //entering from dependent block B, no switch available until switch is deactivated
        else if (isActivated && previous.getType() == portB.getType() && previous.getID() == portB.getID()) {
            System.err.println("Incorrect state at switch " + switchID + ". Port A = " + portA + ", Port B = " + portB + ". Port C = " + portC + ", Activated = " + isActivated);
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
    
    /**
    * Compare equality of TrackPiece
    * @author Dan Bednarczyk
    * @param otherPiece TrackPiece to compare
    * @return boolean true if equal, false otherwise
    */
    @Override
    public boolean equals(TrackPiece otherPiece) {
        if (this.getType() == otherPiece.getType() && this.switchID == otherPiece.getID()) {
            return true;
        }
        return false;
    }
    
    /**
    * Get String representation of Switch
    * @author Dan Bednarczyk
    * @return String representation of Switch containing ID
    */
    @Override
    public String toString() {
        return "" + switchID;
    }
    
    /**
    * Get detailed String representation of Switch
    * @author Dan Bednarczyk
    * @return String representation of Switch containing all info
    */
    @Override
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