/**
* Contains Beacon information
*
* @author: Dan Bednarczyk
* @creation date: 02/01/2017
* @modification date: 04/20/2017
*/

package com.rogueone.trackmodel;

import com.rogueone.global.UnitConversion;

public class Beacon {
    
    final private int beaconID;
    final private Block block;
    final private Station station;
    final private double distance;
    final private boolean rightSide;
    
    public Beacon(int newID, Block newBlock, Station newStation, double newDistance, boolean newRightSide) {
        beaconID = newID;
        block = newBlock;
        station = newStation;
        distance = newDistance;
        rightSide = newRightSide;
    }
    
    /**
     * Get ID of Beacon
     * @return the int specifying beacon ID
     */
    public int getID() {
        return beaconID;
    }
    
    /**
     * Get beacon block
     * @return the Block the beacon is on
     */
    public Block getBlock() {
        return block;
    }
    
    /**
     * Get beacon Station
     * @return the Station associated with the block, null otherwise
     */
    public Station getStation() {
        return station;
    }
    
    /**
     * Get distance to the next station
     * @return the double specifying distance to the next station
     */
    public double getDistance() {
        return UnitConversion.metersToFeet(distance);
    }
    
    /**
     * Get side of next station
     * @return boolean true if station is on right, false otherwise
     */
    public boolean isOnRight() {
        return rightSide;
    }
    
    /**
     * Get side of next station
     * @return boolean true if station is on left, false otherwise
     */
    public boolean isOnLeft(){
        return !rightSide;
    }
    
    /**
     * Get String representation of beacon
     * @return String containing beacon ID
     */
    @Override
    public String toString() {
        return beaconID + "";
    }
    
    /**
     * Get detailed String representation of beacon
     * @return String containing all beacon values
     */
    public String toStringDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append(", ID: ");
        sb.append(beaconID);
        sb.append(", block: ");
        sb.append(block.getID());
        sb.append(", station: ");
        sb.append(station.toString());
        sb.append(", distance: ");
        sb.append(distance);
        sb.append(", side: ");
        if(rightSide) {
            sb.append("right");
        }
        else {
            sb.append("left");
        }
        return sb.toString();
    }
    
}
