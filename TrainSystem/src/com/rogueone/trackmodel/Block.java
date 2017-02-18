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
    
    /**
     * Creates new Block with minimum necessary information.
     * @author Dan Bednarczyk
     * @param newLine the Line object
     * @param newSection the Section object
     * @param newBlockID the Block ID
     * @param newPortAID the first port ID
     * @param newPortBID the second port ID
     * @param newSwitchID the switch ID, -1 otherwise
     * @param newIsStaticSwitchBlock static switch block indicator
     * @param newStationID the station ID, -1 otherwise
     * @param newLength the length
     * @param newGrade the grade
     * @param newSpeedLimit the speed limit
     * @param newElevation the elevation
     * @param newCumulativeElevation the cumulative elevation
     * @param newIsHead section head indicator
     * @param newIsTail section tail indicator
     * @param newContainsCrossing crossing indicator
     * @param newIsUnderground underground indicator
     */
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
        beaconMessage = "";
        station = null;
        trackCircuit = new TrackCircuit();
    }
    
    /**
    * Get the next Block using previous Block as a means of direction specification, also updates occupancy of current and next blocks.
    * @author Dan Bednarczyk
    * @return the next Block object, null if it is yard
    */
    public Block exitBlock(Block previous) {
        Block next = getNextBlock(previous);
        if (next != null) {
            this.setOccupancy(false);
            next.setOccupancy(true); 
        }
        else {
            System.err.println("Next block could not be found.");
        }
        return next;
    }
    
    /**
    * Get the TrackPiece that train will exit from (should not be mistaken for getNextBlock, which only returns Blocks).
    * @author Dan Bednarczyk
    * @return the TrackPiece from which the train did not enter
    */
    public TrackPiece getNext(TrackPiece previous) {
        //Train came from Port A, return Port B
        if(portA.getType() == previous.getType() && portA.getID() == previous.getID()) {
            return portB;
        }
        //Train came from Port B, return Port A
        if(portB.getType() == previous.getType() && portB.getID() == previous.getID()) {
            return portA;
        }
        //Previous does not match either port, an error occured
        else {
            System.err.println("Next TrackPiece not found");
            return null;  
        } 
    }
    
    /**
    * Get the next Block (without altering presence) using previous Block as a means of direction specification.
    * @author Dan Bednarczyk
    * @return the next Block object
    */
    public Block getNextBlock(TrackPiece previous) {
        //System.out.println(previous.getType() + " " + previous.getID());
        //System.out.println(portA.getType() + " " + portA.getID());
        //System.out.println(portB.getType() + " " + portB.getID());
        //Train came from Port A, get next block via Port B
        if(portA.getType() == previous.getType() && portA.getID() == previous.getID()) {
            return getNextBlockViaPortB();
        }
        //Train came from Port B, get next block via Port A
        if(portB.getType() == previous.getType() && portB.getID() == previous.getID()) {
            return getNextBlockViaPortA();
        }
        //Previous does not match either port, an error occured
        else {
            System.err.println("Next block not found");
            return null;  
        } 
    }
    
    /**
    * Get the next Block via Port A.
    * @author Dan Bednarczyk
    * @return (1) Port A if Port A is a block (2) The next Block if Port A is a Switch (3) null otherwise 
    */
    private Block getNextBlockViaPortA() {
        if (portA.getType() == Global.PieceType.BLOCK) {
            return (Block) portA;
        }
        else if (portA.getType() == Global.PieceType.SWITCH) {
            return (Block) (((Switch) portA).getNext(this));
        }
        else {
            System.err.println("Next block not found via port A");
            return null;
        }
    }
    
    /**
    * Get the next Block via Port B.
    * @author Dan Bednarczyk
    * @return (1) Port B if Port B is a block (2) The next Block if Port B is a Switch (3) null otherwise 
    */
    private Block getNextBlockViaPortB() {
        if (portB.getType() == Global.PieceType.BLOCK) {
            return (Block) portB;
        }
        else if (portB.getType() == Global.PieceType.SWITCH) {
            return (Block) (((Switch) portB).getNext(this));
        }
        else {
            System.err.println("Next block not found via port B");
            return null;
        }
    }
    
    /**
    * Get type of TrackPeice.
    * @author Dan Bednarczyk
    * @return the type enum implementing the TrackPeice interface (Block)
    */
    public Global.PieceType getType() {
        return PieceType.BLOCK;
    }
    /**
    * Get block ID.
    * @author Dan Bednarczyk
    * @return the block ID
    */
    public int getID() {
        return blockID;
    }
    
    // Getters & Setters
    
    /**
    * Get Line object that owns the Block.
    * @author Dan Bednarczyk
    * @return the Line object that owns the Block
    */
    public Line getLine() {
        return line;
    }
    
    /**
    * Get Section object that owns the Block.
    * @author Dan Bednarczyk
    * @return the Section object that owns the Block
    */
    public Section getSection() {
        return section;
    }
    
    /**
    * Get the first port.
    * @author Dan Bednarczyk
    * @return the TrackPeice in the first port, should be of type Block or Yard
    */
    public TrackPiece getPortA() {
        return portA;
    }
    
    /**
    * Set the first port.
    * @author Dan Bednarczyk
    * @param newPortA A TrackPeice to be set in port A
    */
    public void setPortA(TrackPiece newPortA) {
        portA = newPortA;
        portAID = newPortA.getID();
    }
    
    /**
    * Get the second port.
    * @author Dan Bednarczyk
    * @return the TrackPeice in the second port, should be of type Block or Switch
    */
    public TrackPiece getPortB() {
        return portB;
    }
    
    /**
    * Set the second port.
    * @author Dan Bednarczyk
    * @param newPortB A TrackPeice to be set in port B
    */
    public void setPortB(TrackPiece newPortB) {
        portB = newPortB;
        portBID = newPortB.getID();
    }
    
    /**
    * Get the ID of the first port.
    * @author Dan Bednarczyk
    * @return the ID of the first port
    */
    public int getPortAID() {
        return portAID;
    }
    
    /**
    * Get the ID of the second port.
    * @author Dan Bednarczyk
    * @return the ID of the second port
    */
    public int getPortBID() {
        return portBID;
    }
    
    /**
    * Get the ID of the switch.
    * @author Dan Bednarczyk
    * @return the ID of the switch, -1 otherwise
    */
    public int getSwitchID() {
        return switchID;
    }
    
    /**
    * Get the ID of the station.
    * @author Dan Bednarczyk
    * @return the ID of the station, -1 otherwise
    */
    public int getStationID() {
        return stationID;
    }
    
    /**
    * Get the block length.
    * @author Dan Bednarczyk
    * @return the block length
    */
    public double getLength() {
        return length;
    }
    
    /**
    * Set the block length.
    * @author Dan Bednarczyk
    * @param newLength the block length
    */
    public void setLength(int newLength) {
        length = newLength;
    }
    
    /**
    * Get the block grade.
    * @author Dan Bednarczyk
    * @return the block grade
    */
    public double getGrade() {
        return grade;
    }
    
    /**
    * Set the block grade.
    * @author Dan Bednarczyk
    * @param newGrade the block grade
    */
    public void setGrade(double newGrade) {
        grade = newGrade;
    }
    
    /**
    * Get the block elevation.
    * @author Dan Bednarczyk
    * @return the block elevation
    */
    public double getElevation() {
        return elevation;
    }
    
    /**
    * Set the block elevation.
    * @author Dan Bednarczyk
    * @param newElevation the block elevation
    */
    public void setElevation(double newElevation) {
        elevation = newElevation;
    }
    
    /**
    * Get the block cumulative elevation.
    * @author Dan Bednarczyk
    * @return the block cumulative elevation
    */
    public double getCumulativeElevation() {
        return cumulativeElevation;
    }
    
    /**
    * Set the cumulative block elevation.
    * @author Dan Bednarczyk
    * @param newCumulativeElevation the block cumulative elevation
    */
    public void setCumulativeElevation(double newCumulativeElevation) {
        cumulativeElevation = newCumulativeElevation;
    }
    
    /**
    * Get the block speed limit.
    * @author Dan Bednarczyk
    * @return the block speed limit
    */
    public double getSpeedLimit() {
        return speedLimit;
    }
    
    /**
    * Set the block speed limit.
    * @author Dan Bednarczyk
    * @param newSpeedLimit the block speed limit
    */
    public void setSpeedLimit(double newSpeedLimit) {
        speedLimit = newSpeedLimit;
    }
    
    /**
    * Check if block is static switch block.
    * @author Dan Bednarczyk
    * @return boolean indicating if block is static switch block
    */
    public boolean isStaticSwitchBlock() {
        return isStaticSwitchBlock;
    }
    
    /**
    * Check if block is section head.
    * @author Dan Bednarczyk
    * @return boolean indicating if block is section head
    */
    public boolean isHead() {
        return isHead;
    }
    
    /**
    * Check if block is section tail.
    * @author Dan Bednarczyk
    * @return boolean indicating if block is section tail
    */
    public boolean isTail() {
        return isTail;
    }
    
    /**
    * Check if block contains crossing.
    * @author Dan Bednarczyk
    * @return boolean indicating if block contains crossing
    */
    public boolean containsCrossing() {
        return containsCrossing;
    }
    
    /**
    * Check if block is underground.
    * @author Dan Bednarczyk
    * @return boolean indicating if block is underground
    */
    public boolean isUnderground() {
        return isUnderground;
    }
    
    /**
    * Check if block crossing is down.
    * @author Dan Bednarczyk
    * @return boolean indicating if block crossing is down, false by default
    */
    public boolean isCrossingDown() {
        return isCrossingDown;
    }
    
    /**
    * Set broken rail failure state.
    * @author Dan Bednarczyk
    * @param fail boolean indicating new state of failure
    */
    public void setFailureBrokenRail(boolean fail) {
        failureBrokenRail = fail;
    }
    
    /**
    * Check if block has broken rail.
    * @author Dan Bednarczyk
    * @return boolean indicating if block has broken rail
    */
    public boolean getFailureBrokenRail() {
        return failureBrokenRail;
    }
    
    /**
    * Set power outage failure state.
    * @author Dan Bednarczyk
    * @param fail boolean indicating new state of failure
    */
    public void setFailurePowerOutage(boolean fail) {
        failurePowerOutage = fail;
    }
    
    /**
    * Check if block has power outage.
    * @author Dan Bednarczyk
    * @return boolean indicating if block has power outage
    */
    public boolean getFailurePowerOutage() {
        return failurePowerOutage;
    }
    
    /**
    * Set track circuit failure state.
    * @author Dan Bednarczyk
    * @param fail boolean indicating new state of failure
    */
    public void setFailureTrackCircuit(boolean fail) {
        failureTrackCircuit = fail;
    }
    
    /**
    * Check if block has track circuit failure.
    * @author Dan Bednarczyk
    * @return boolean indicating if block has track circuit failure
    */
    public boolean getFailureTrackCircuit() {
        return failureTrackCircuit;
    }
    
    /**
    * Check if block is occupied by a train.
    * @author Dan Bednarczyk
    * @return boolean indicating block occupancy
    */
    public boolean isOccupied() {
        return occupied;
    }
    
    /**
    * Set block occupancy
    * @author Dan Bednarczyk
    * @param presence boolean indicating whether or not block is occupied
    */
    public void setOccupancy(boolean presence) {
        occupied = presence;
    }
    
    /**
    * Get block beacon message.
    * @author Dan Bednarczyk
    * @return String containing beacon message, empty by default
    */
    public String getBeaconMessage() {
        return beaconMessage;
    }
    
    /**
    * Set block beacon message.
    * @author Dan Bednarczyk
    * @param newBeaconMessage String specifying beacon message
    */
    public void setBeaconMessage(String newBeaconMessage) {
        beaconMessage = newBeaconMessage;
    }
    
    /**
    * Get station on Block.
    * @author Dan Bednarczyk
    * @return Station object on block, null otherwise
    */
    public Station getStation() {
        return station;
    }
    
    /**
    * Set station on Block.
    * @author Dan Bednarczyk
    * @param newStation Station object
    */
    public void setStation(Station newStation) {
        station = newStation;
    }
    
    /**
    * Get Track Circuit object.
    * @author Dan Bednarczyk
    * @return Track Circuit object, 0s by default, -1s if there is a failure
    */
    public TrackCircuit getTrackCircuit() {
        if(failureTrackCircuit) {
            return new TrackCircuit((byte) -1,(short) -1);
        }
        else {
            return trackCircuit;
        }
    }
    
    /**
    * Set Track Circuit
    * @author Dan Bednarczyk
    * @param newTrackCircuit Track Circuit object
    */
    public void setTrackCircuit(TrackCircuit newTrackCircuit) {
        trackCircuit = newTrackCircuit;
    }
    
    /**
    * Check equality with other block, true if and only if they share the same Line, Section, and Block ID.
    * @author Dan Bednarczyk
    * @return boolean indicating equality
    */
    public boolean equals(Block otherBlock) {
        return this.line.equals(otherBlock.getLine()) && this.section.equals(otherBlock.getSection())&& this.blockID == otherBlock.getID();
    }
    
    /**
    * Get ID of block as a String.
    * @author Dan Bednarczyk
    * @return String containing block ID
    */
    public String toString() {
        return "" + this.getID();
    }
    
    /**
    * Get cleanly formatted String containing all Block details.
    * @author Dan Bednarczyk
    * @return One-line String containing all Block details
    */
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
            sb.append(station.getName());
        }
        sb.append(", Speed Limit: ");
        sb.append(speedLimit);
        sb.append(", Beacon: ");
        sb.append(beaconMessage);
        return sb.toString();
    }
}
