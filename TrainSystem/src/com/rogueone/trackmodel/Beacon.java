/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.UnitConversion;

/**
 *
 * @author Dan
 */
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
    
    public int getID() {
        return beaconID;
    }
    
    public Block getBlock() {
        return block;
    }
    
    public Station getStation() {
        return station;
    }
    
    public double getDistance() {
        return UnitConversion.metersToFeet(distance);
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
