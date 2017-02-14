/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import com.rogueone.global.Global.PieceType;
import com.rogueone.global.TrackCircuit;

/**
 *
 * @author Dan
 */
public class Block implements TrackPiece {
    
    private Line line;
    private Section section;
    protected TrackPiece portA;
    protected TrackPiece portB;
    protected int portAID;
    protected int portBID;
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
    private Station station;
    private TrackCircuit trackCircuit;
    
    // Constructor
    public Block(Line newLine, Section newSection, int newBlockID, 
            int newPortAID, int newPortBID, int newSwitchID, boolean newIsStaticSwitchBlock, 
            int newStationID, double newLength, double newGrade, double newSpeedLimit,
            double newElevation, double newCumulativeElevation, 
            boolean newIsHead, boolean newIsTail, boolean newContainsCrossing, 
            boolean newIsUnderground) {
        line = newLine;
        section = newSection;
        blockID = newBlockID;
        portA = null;
        portAID = newPortAID;
        portB = null;
        portBID = newPortBID;
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
        station = null;
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
    public Line getLine() {
        return line;
    }
    public Section getSection() {
        return section;
    }
    public TrackPiece getPortA() {
        return portA;
    }
    public void setPortA(TrackPiece newPortA) {
        portA = newPortA;
        //portAID = newPortA.getID();
    }
    public TrackPiece getPortB() {
        return portB;
    }
    public void setPortB(TrackPiece newPortB) {
        portB = newPortB;
        //portBID = newPortB.getID();
    }
    public int getPortAID() {
        return portAID;
    }
    public int getPortBID() {
        return portBID;
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
    public boolean getFailureBrokenRail() {
        return failureBrokenRail;
    }
    public void setFailurePowerOutage(boolean fail) {
        failurePowerOutage = fail;
    }
    public boolean getFailurePowerOutage() {
        return failurePowerOutage;
    }
    public void setFailureTrackCircuit(boolean fail) {
        failureTrackCircuit = fail;
    }
    public boolean getFailureTrackCircuit() {
        return failureTrackCircuit;
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
    public Station getStation() {
        return station;
    }
    public void setStation(Station newStation) {
        station = newStation;
    }
    public TrackCircuit getTrackCircuit() {
        return trackCircuit;
    }
    public void setTrackCircuit(TrackCircuit newTrackCircuit) {
        trackCircuit = newTrackCircuit;
    }
    
    //Overridden methods
    public boolean equals(Block otherBlock) {
        return this.line.equals(otherBlock.getLine()) && this.section.equals(otherBlock.getSection())&& this.blockID == otherBlock.getID();
    }
    
    public String toString() {
        return "" + this.getID();
    }
    
    public String toStringDetail() {
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
        if(portA != null && portA.getType() == Global.PieceType.YARD) {
            sb.append(" (Yard)");
        }
        if(portB != null) {
           sb.append(", Port B: ");
           sb.append(portB.getID());
        }
        if(portB != null && portB.getType() == Global.PieceType.SWITCH) {
            sb.append(" (");
            if (isStaticSwitchBlock) {
                sb.append("Static ");
            }
            sb.append("Switch: ");
            sb.append(((Switch)portB).getPortA().getID());
            sb.append("-");
            sb.append(((Switch)portB).getPortB().getID());
            sb.append("-");
            sb.append(((Switch)portB).getPortC().getID());
            sb.append(")");
        }
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
        if (station != null) {
            sb.append(", Station ");
            sb.append(stationID);
            sb.append(": ");
            sb.append(station.getStationName());
        }
        sb.append(", Speed Limit: ");
        sb.append(speedLimit);
        sb.append(", Beacon: ");
        sb.append(beaconMessage);
        return sb.toString();
    }
}
