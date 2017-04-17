/**
 * GPSMessages are used to provide the MBO with train info and location
 *
 * @author Tyler Protivnak
 * @Creation 3/4/17
 * @Modification 4/17/2017
 */
package com.rogueone.traincon;

import com.rogueone.trackmodel.Block;

/**
 * Class declaration for GPSMessage
 * 
 * @author Tyler Protivnak
 */
public class GPSMessage {
    private double currSpeed;
    private Block currBlock;
    private double stoppingDistance;
    private double distanceIntoBlock;
    private String line;
    private String trainID;
    
/**
 * GPSmessage constructor
 * 
 * @author Tyler Protivnak
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
     * get the current speed of the train
     * 
     * @author Tyler Protivnak
     * @return current speed in MPH
     */
    public double getCurrSpeed() {
        return currSpeed;
    }

    /**
     * get the current block we occupy
     * 
     * @author Tyler Protivnak
     * @return current block train occupies
     */
    public Block getCurrBlock() {
        return currBlock;
    }

    /**
     * get the stopping distance for the train at the current speed
     * 
     * @author Tyler Protivnak
     * @return stopping distance in feet
     */
    public double getStoppingDistance() {
        return stoppingDistance;
    }

    /**
     * set the stopping distance for the train at the current speed
     * 
     * @author Tyler Protivnak
     * @param stoppingDistance stopping distance in feet
     */
    public void setStoppingDistance(double stoppingDistance) {
        this.stoppingDistance = stoppingDistance;
    }

    /**
     * get the line of the train
     * 
     * @author Tyler Protivnak
     * @return line the train is on
     */
    public String getLine() {
        return line;
    }

    /**
     * Get the id of the train
     * 
     * @author Tyler Protivnak
     * @return ID of train
     */
    public String getTrainID() {
        return trainID;
    }               

    /**
     * get the distance we are into the current block
     * 
     * @author Tyler Protivnak
     * @return how far into the block we are
     */
    public double getDistanceIntoBlock() {
        return distanceIntoBlock;
    }

    /**
     * set the distance into the block that we are for stopping distance calculations
     * 
     * @author Tyler Protivnak
     * @param distanceIntoBlock how far into the current block we are
     */
    public void setDistanceIntoBlock(double distanceIntoBlock) {
        this.distanceIntoBlock = distanceIntoBlock;
    }   
}