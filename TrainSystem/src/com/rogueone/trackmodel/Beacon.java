/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

/**
 *
 * @author Dan
 */
public class Beacon {
    
    final private int beaconID;
    final private Block block;
    final private int stationID;
    final private double distance;
    final private boolean rightSide;
    
    public Beacon(int newID, Block newBlock, int newStationID, double newDistance, boolean newRightSide) {
        beaconID = newID;
        block = newBlock;
        stationID = newStationID;
        distance = newDistance;
        rightSide = newRightSide;
    }
    
    public int getID() {
        return beaconID;
    }
    
    public Block getBlock() {
        return block;
    }
    
    public int getStationID() {
        return stationID;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public boolean isOnRight() {
        return rightSide;
    }
    
    public boolean isOnLeft(){
        return !rightSide;
    }
    
    @Override
    public String toString() {
        return beaconID + "";
    }
    
}
