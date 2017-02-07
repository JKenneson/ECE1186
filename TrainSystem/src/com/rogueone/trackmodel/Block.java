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
public class Block implements TrackPiece {
    
    private Global.Line line;
    private Global.Section section;
    protected TrackPiece portA;
    protected TrackPiece portB;
    private int blockID;
    private int switchID;
    private int stationID;
    private double length;
    private double grade;
    private double elevation;
    private double cumulativeElevation;
    private double speedLimit;
    private boolean isStaticSwitchBlock;
    private boolean isHead;
    private boolean isTail;
    private boolean containsCrossing;
    private boolean isCrossingDown;
    private boolean isUnderground;
    private boolean failureBrokenRail;
    private boolean failurePowerOutage;
    private boolean failureTrackCircuit;
    private boolean occupied;
    private String beaconMessage;
    
    // Constructor
    public Block(Global.Line newLine, Global.Section newSection, int newBlockID, 
            int newSwitchID, boolean newIsStaticSwitchBlock, 
            int newStationID, double newLength, double newGrade, double newSpeedLimit,
            double newElevation, double newCumulativeElevation, 
            boolean newIsHead, boolean newIsTail, boolean newContainsCrossing, 
            boolean newIsUnderground) {
        line = newLine;
        section = newSection;
        blockID = newBlockID;
        portA = null;
        portB = null;
        switchID = newSwitchID;
        isStaticSwitchBlock = newIsStaticSwitchBlock;
        stationID = newStationID;
        length = newLength;
        grade = newGrade;
        speedLimit = newSpeedLimit;
        elevation = newElevation;
        cumulativeElevation = newCumulativeElevation;
        isHead = newIsHead;
        isTail = newIsTail;
        containsCrossing = newContainsCrossing;
        isCrossingDown = false;
        isUnderground = newIsUnderground;
        failureBrokenRail = false;
        failurePowerOutage = false;
        failureTrackCircuit = false;
        occupied = false;
        beaconMessage = null;
    }
    
    // TrackPiece interface methods
    public TrackPiece getNext(TrackPiece previous) {
        if (previous.getType() == portA.getType() && previous.getID() == portA.getID())
            return portB;
        else if (previous.getType() == portB.getType() && previous.getID() == portB.getID()) {
            return portA;
        }
        else {
            return null;
        }
    } 
    public Global.PieceType getType() {
        return PieceType.BLOCK;
    }
    public int getID() {
        return blockID;
    }
    
    // Getters & Setters
    public Global.Line getLine() {
        return line;
    }
    public Global.Section getSections() {
        return section;
    }
    public TrackPiece getPortA() {
        return portA;
    }
    public void setPortA(TrackPiece newPortA) {
        portA = newPortA;
    }
    public TrackPiece getPortB() {
        return portB;
    }
    public void setPortB(TrackPiece newPortB) {
        portB = newPortB;
    }
    public int getSwitchID() {
        return switchID;
    }
    public int getStationID() {
        return stationID;
    }
    public double getLength() {
        return length;
    }
    public void setLength(int newLength) {
        length = newLength;
    }
    public double getGrade() {
        return grade;
    }
    public void setGrade(double newGrade) {
        grade = newGrade;
    }
    public double getElevation() {
        return elevation;
    }
    public void setElevation(double newElevation) {
        elevation = newElevation;
    }
    public double getCumulativeElevation() {
        return cumulativeElevation;
    }
    public void setCumulativeElevation(double newCumulativeElevation) {
        cumulativeElevation = newCumulativeElevation;
    }
    public double getSpeedLimit() {
        return speedLimit;
    }
    public void setSpeedLimit(double newSpeedLimit) {
        speedLimit = newSpeedLimit;
    }
    public boolean isStaticSwitchBlock() {
        return isStaticSwitchBlock;
    }
    public boolean isHead() {
        return isHead;
    }
    public boolean isTail() {
        return isTail;
    }
    public boolean containsCrossing() {
        return containsCrossing;
    }
    public boolean isUnderground() {
        return isUnderground;
    }
    public boolean isCrossingDown() {
        return isCrossingDown;
    }
    public void setFailureBrokenRail(boolean fail) {
        failureBrokenRail = fail;
    }
    public void setFailurePowerOutage(boolean fail) {
        failurePowerOutage = fail;
    }
    public void setFailureTrackCircuit(boolean fail) {
        failureTrackCircuit = fail;
    }
    public boolean isOccupied() {
        return occupied;
    }
    public void setOccupied(boolean newOccupied) {
        occupied = newOccupied;
    }
    public String getBeaconMessage() {
        return beaconMessage;
    }
    public void setBeaconMessage(String newBeaconMessage) {
        beaconMessage = newBeaconMessage;
    }
    
    //Overridden methods
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Line: ");
        sb.append(line);
        sb.append(", Section: ");
        sb.append(section);
        sb.append(", Block: ");
        sb.append(blockID);
        if(portA != null) {
           sb.append(", Port A: ");
           sb.append(portA.getID()); 
        }
        if(portB != null) {
           sb.append(", Port B: ");
           sb.append(portB.getID());
        }
        sb.append(", Switch: ");
        sb.append(switchID);
        sb.append(", Static Switch Block: ");
        sb.append(isStaticSwitchBlock);
        sb.append(", Length: ");
        sb.append(length);
        sb.append(", Grade: ");
        sb.append(grade);
        sb.append(", Elevation: ");
        sb.append(elevation);
        sb.append(", Cumulative: ");
        sb.append(cumulativeElevation);
        sb.append(", Head: ");
        sb.append(isHead);
        sb.append(", Tail: ");
        sb.append(isTail);
        sb.append(", Crossing: ");
        sb.append(containsCrossing);
        sb.append(", Underground: ");
        sb.append(isUnderground);
        sb.append(", Station: ");
        sb.append(stationID);
        sb.append(", Speed Limit: ");
        sb.append(speedLimit);
        sb.append(", Beacon: ");
        sb.append(beaconMessage);
        return sb.toString();
    }
}
