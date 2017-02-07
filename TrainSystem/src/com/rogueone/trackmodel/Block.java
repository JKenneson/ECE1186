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
    
    private String line;
    private String section;
    private int blockID;
    protected TrackPiece portA;
    protected TrackPiece portB;
    private int switchID;
    private boolean isStaticSwitchBlock;
    private int length;
    private double grade;
    private int speedLimit;
    private double elevation;
    private double cumulativeElevation;
    private boolean isHead;
    private boolean isTail;
    private boolean containsCrossing;
    private boolean isUnderground;
    private boolean isCrossingDown;
    private Station station;
    private boolean failureBrokenRail;
    private boolean failurePowerOutage;
    private boolean failureTrackCircuit;
    //occupied
    //beacon
    //beacon message
    
    public Block(String newLine, String newSection, int newBlockID, 
            int newSwitchID, boolean newIsStaticSwitchBlock, 
            String newStationName, int newLength, double newGrade, int newSpeedLimit,
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
        length = newLength;
        grade = newGrade;
        speedLimit = newSpeedLimit;
        elevation = newElevation;
        cumulativeElevation = newCumulativeElevation;
        isHead = newIsHead;
        isTail = newIsTail;
        containsCrossing = newContainsCrossing;
        isUnderground = newIsUnderground;
        isCrossingDown = false;
        failureBrokenRail = false;
        failurePowerOutage = false;
        failureTrackCircuit = false;
        
        if (newStationName != null) {
            station = new Station(newStationName, true);
        }
        else {
            station = null;
        }
    }
    
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
    
    public PieceType getType() {
        return PieceType.BLOCK;
    }
    
    public int getID() {
        return blockID;
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
    public String getLine() {
        return line;
    }
    public String getSections() {
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
    public boolean isStaticSwitchBlock() {
        return isStaticSwitchBlock;
    }
    public int getLength() {
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
    public Station getStation() {
        return station;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Line: ");
        sb.append(line);
        sb.append(", Section: ");
        sb.append(section);
        sb.append(", Block: ");
        sb.append(blockID);
        sb.append(", Port A: ");
        sb.append(portA);
        sb.append(", Port B: ");
        sb.append(portB);
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
        sb.append(station);
        return sb.toString();
    }
}
