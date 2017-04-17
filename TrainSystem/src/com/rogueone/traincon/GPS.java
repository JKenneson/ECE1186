/**
 * The gps is used to hold our speed and location properties
 *
 * @author Tyler Protivnak
 * @Creation 3/4/17
 * @Modification 4/17/2017
 */
package com.rogueone.traincon;

import com.rogueone.global.Clock;
import com.rogueone.global.Global;
import com.rogueone.trackmodel.Block;
import com.rogueone.trainsystem.TrainSystem;

/**
 * Class declaration for GPS
 * 
 * @author Tyler Protivnak
 */
public class GPS{
    
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    
    private final TrainSystem trainSystem;
    
    //Train System reference
    public String trainID;
    
    //Speed and Authority
    private double currSpeed;       //Vf in m/s

    private double authority;       //feet remaining
    //private double distanceIntoBlock;//The distance we have traveled into the block
    
    //Blocks
    private Block currBlock;
    
    /**
     * Constructor for the train GPS that relays position to the MBO
     * 
     * @author Tyler Protivnak
     * @param authority starting authority for train
     * @param ts reference to the overall train system
     * @param trainID ID number of the train
     * @param line the line we are on    
     */
    public GPS(int authority, TrainSystem ts, String trainID, String line){
        
        this.trainSystem = ts;
        //Speed and Authority
        this.currSpeed = 0;
        this.authority = (double)authority*this.FEET_IN_A_METER;//Update how values are brought in
        this.trainID = trainID;

        //Need to account below for which line the train is on
        if(line.equals("GREEN")){
            this.currBlock = trainSystem.getTrackModel().enterTrack(Global.Line.GREEN);
        }
        else{
            this.currBlock = trainSystem.getTrackModel().enterTrack(Global.Line.RED);
        }
    }
        
    /**
     * Update the location, speed, and authority
     * Note, the current block is passed from the train model since the calculations
     * are identical. There was no reason for us to do the work twice.
     * 
     * @author Tyler Protivnak
     * @param distanceTraveled how far have we traveled since the last update
     * @param currBlock which block am I on?
     * @param currSpeed how fast am I going?
     */
    public void update(double distanceTraveled, Block currBlock, double currSpeed){
                
        //Subtract the distance traveled from remaining authority
        this.currSpeed = currSpeed;
        this.authority -= distanceTraveled;
        this.currBlock = currBlock;        
    }

    /**
     * Get the id of this train
     * 
     * @author Tyler Protivnak
     * @return trainID the ID of the train
     */
    public String getTrainID() {
        return trainID;
    }

    /**
     * get the block I currently occupy
     * 
     * @author Tyler Protivnak
     * @return currBlock the current block the train is on
     */
    public Block getCurrBlock() {
        return currBlock;
    }
    
    /**
     * Get the speed limit of my current block
     * 
     * @author Tyler Protivnak
     * @return speed limit of block in MPH
     */
    public double getSpeedLimit(){
        return this.currBlock.getSpeedLimit();
    }

    /**
     * How much authority do I have left?
     * 
     * @author Tyler Protivnak
     * @return the remaining authority of the train in FEET
     */
    public double getAuthority() {
        return authority;
    }
    
    /**
     * Set a new authority value for the gps
     * 
     * @author Tyler Protivnak
     * @param authority Sets a new authority for the train control to follow 
     */
    public void setAuthority(short authority) {
        this.authority = (double)authority*this.FEET_IN_A_METER;
    }

    /**
     * Get the current speed from the gps
     * 
     * @author Tyler Protivnak
     * @return the current speed of the train
     */
    public double getCurrSpeed() {
        return currSpeed;
    }

    /**
     * set the current speed the gps sees us at
     * 
     * @author Tyler Protivnak
     * @param currSpeed the new current speed value to use
     */
    public void setCurrSpeed(double currSpeed) {
        this.currSpeed = currSpeed;
    }
    
    /**
     * How should the lights be set at this moment?
     * 
     * @author Tyler Protivnak
     * @return how the lights should be set 
     */
    public boolean setLights(){
       return (this.currBlock.isUnderground() || (8>this.trainSystem.getClock().getHour() && this.trainSystem.getClock().getTimeOfDay() == Clock.TimeOfDay.AM)
                || (5<this.trainSystem.getClock().getHour() && this.trainSystem.getClock().getTimeOfDay() == Clock.TimeOfDay.PM));
    }
    
    /**
     * Return a gps message for the mbo with relevant information
     * 
     * @author Tyler Protivnak
     * @return GPSMessage with currspeed, block, line, and ID
     */
    public GPSMessage getGPSMessage(){
        return new GPSMessage(currSpeed, currBlock, currBlock.getLine().toString(), trainID);
    }
}