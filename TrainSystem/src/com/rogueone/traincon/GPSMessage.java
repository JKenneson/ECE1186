/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.trackmodel.Block;

/**
 *
 * @author Tyler
 */
public class GPSMessage {
    //currSpeed, currBlock, stopping distance, line, trainId
    private double currSpeed;
    private Block currBlock;
    private double stoppingDistance;
    private double distanceIntoBlock;
    private String line;
    private String trainID;
    
/**
 * 
 * @param currSpeed current speed in MPH
 * @param currBlock current occupied block
 * @param line line train is on
 * @param trainID ID of train
 */    
    public GPSMessage(double currSpeed, Block currBlock, String line, String trainID){
        this.currSpeed = currSpeed;
        this.currBlock = currBlock;
        this.line = line;
        this.trainID = trainID;
    }

    /**
     * 
     * @return current speed in MPH
     */
    public double getCurrSpeed() {
        return currSpeed;
    }

    /**
     * 
     * @return current block train occupies
     */
    public Block getCurrBlock() {
        return currBlock;
    }

    /**
     * 
     * @return stopping distance in feet
     */
    public double getStoppingDistance() {
        return stoppingDistance;
    }

    /**
     * 
     * @param stoppingDistance stopping distance in feet
     */
    public void setStoppingDistance(double stoppingDistance) {
        this.stoppingDistance = stoppingDistance;
    }

    /**
     * 
     * @return line the train is on
     */
    public String getLine() {
        return line;
    }

    /**
     * 
     * @return ID of train
     */
    public String getTrainID() {
        return trainID;
    }               

    public double getDistanceIntoBlock() {
        return distanceIntoBlock;
    }

    public void setDistanceIntoBlock(double distanceIntoBlock) {
        this.distanceIntoBlock = distanceIntoBlock;
    }
    
    
}
