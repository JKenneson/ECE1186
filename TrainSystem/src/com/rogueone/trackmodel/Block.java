/**
* Contains Block information
*
* @author: Dan Bednarczyk
* @creation date: 02/01/2017
* @modification date: 04/20/2017
*/

package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import com.rogueone.global.Global.PieceType;
import com.rogueone.global.TrackCircuit;

public class Block implements TrackPiece {
    
    final private Line line;
    final private Section section;
    protected TrackPiece portA;
    protected TrackPiece portB;
    protected int portAID;
    protected int portBID;
    final private int blockID;
    final private int switchID;
    final private int stationID;
    private double length;
    private double grade;
    private double elevation;
    private double cumulativeElevation;
    private double speedLimit;
    final private boolean isStaticSwitchBlock;
    final private boolean isHead;
    final private boolean isTail;
    final private boolean isUnderground;
    private boolean isOpen;
    private boolean failureBrokenRail;
    private boolean failurePowerOutage;
    private boolean failureTrackCircuit;
    private boolean occupied;
    final private Crossing crossing;
    private Beacon beacon;
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
     * @param containsCrossing crossing indicator
     * @param newIsUnderground underground indicator
     */
    public Block(Line newLine, Section newSection, int newBlockID, 
            int newPortAID, int newPortBID, int newSwitchID, boolean newIsStaticSwitchBlock, 
            int newStationID, double newLength, double newGrade, double newSpeedLimit,
            double newElevation, double newCumulativeElevation, 
            boolean newIsHead, boolean newIsTail, boolean containsCrossing, 
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
        isUnderground = newIsUnderground;
        isOpen = true;
        failureBrokenRail = false;
        failurePowerOutage = false;
        failureTrackCircuit = false;
        occupied = false;
        beacon = null;
        station = null;
        trackCircuit = new TrackCircuit();
        if(containsCrossing) {
            crossing = new Crossing();
        }
        else {
            crossing = null;
        }
    }
    
    /**
    * Get the next Block (without altering presence) using previous Block as a means of direction specification.
    * @author Dan Bednarczyk
    * @param previous the previous block
    * @return the next Block object
    */
    public TrackPiece getNext(TrackPiece previous) {
        
        if(portA == null) {
            System.err.println("Block " + this.getID() + " is missing Port A. Please check your track configuation.");
            return null;
        }
        if(portB == null) {
            System.err.println("Block " + this.getID() + " is missing Port B. Please check your track configuation.");
            return null;
        }
        
        TrackPiece portABlock = getNextViaPortA();
        TrackPiece portBBlock = getNextViaPortB();
        
        // Train is on regular piece of track
        if(previous.getType() == Global.PieceType.BLOCK) {
            if(portABlock != null && portABlock.equals(previous)) {
                //Train came from Port A, get next block via Port B
                return portBBlock;
            }
            else if(portBBlock != null && portBBlock.equals(previous)) {
                //Train came from Port B, get next block via Port A
                return portABlock;
            }
            else {
                //Previous does not match either port, an error occured
                System.err.println("Next block not found for Block " + this.blockID + " and previous Block " + previous + ". Port A = " + portABlock + ", Port B = " + portBBlock);
                return null;  
            } 
        }
        // Train is dispatched from the yard
        else if(previous.getType() == Global.PieceType.YARD) {
            return portBBlock; // Yard is always portA
        }
        else {
            System.err.println("Invalid argument. Previous must be of type BLOCK or YARD.");
            return null;
        }   
    }
    
    /**
    * Get the next Block via Port A.
    * @author Dan Bednarczyk
    * @return (1) Port A if Port A is a block (2) The next Block if Port A is a Switch (3) null otherwise 
    */
    private TrackPiece getNextViaPortA() {
        if (portA.getType() == Global.PieceType.BLOCK || portA.getType() == Global.PieceType.YARD) {
            return portA;
        }
        else if (portA.getType() == Global.PieceType.SWITCH) {
            return ((Switch) portA).getNext(this);
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
    private TrackPiece getNextViaPortB() {
        if (portB.getType() == Global.PieceType.BLOCK || portB.getType() == Global.PieceType.YARD) {
            return portB;
        }
        else if (portB.getType() == Global.PieceType.SWITCH) {
            return ((Switch) portB).getNext(this);
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
    * Get crossing, if block contains one
    * @author Dan Bednarczyk
    * @return Crossing on the Block, otherwise null
    */
    public Crossing getCrossing() {
        return crossing;
    }
    
    public boolean containsCrossing() {
        return crossing != null;
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
    * Check if block is open.
    * @author Dan Bednarczyk
    * @return boolean indicating if block is open
    */
    public boolean isOpen() {
        return isOpen;
    }
    
    /**
    * Open block
    * @author Dan Bednarczyk
    */
    public void open() {
        isOpen = true;
    }
    
    /**
    * Close block
    * @author Dan Bednarczyk
    */
    public void close() {
        isOpen = false;
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
    * Get block beacon
    * @author Dan Bednarczyk
    * @return Beacon on Block, null otherwise
    */
    public Beacon getBeacon() {
        return beacon;
    }
    
    /**
    * Set beacon on Block.
    * @author Dan Bednarczyk
    * @param newBeacon beacon object
    */
    public void setBeacon(Beacon newBeacon) {
        beacon = newBeacon;
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
    * @param otherPiece the Block to compare
    * @return boolean indicating equality
    */
    public boolean equals(TrackPiece otherPiece) {
        if (this.getType() == otherPiece.getType() && this.line.getLineID() == ((Block)otherPiece).getLine().getLineID()  && this.section.getSectionID() == ((Block)otherPiece).getSection().getSectionID() && this.blockID == ((Block)otherPiece).getID()) {
            return true;
        }
        return false;
    }
    
    /**
    * Get ID of block as a String.
    * @author Dan Bednarczyk
    * @return String containing block ID
    */
    @Override
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
        if (crossing != null) {
            sb.append(", Crossing (State = ");
            sb.append(crossing.getState());
            sb.append(")");
        }
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
        if (beacon != null) {
            sb.append(", Beacon: ");
            sb.append(beacon.getID());
        }
        return sb.toString();
    }
}
