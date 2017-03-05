/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.global.Global;
import com.rogueone.trackmodel.Block;
import com.rogueone.trackmodel.TrackPiece;
import com.rogueone.trainsystem.TrainSystem;

/**
 *
 * @author Tyler
 */
public class GPS implements Updateable{
    
    
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    
    //Train System reference
    private TrainSystem trainSystem;
    public String trainID;
    
    //Speed and Authority
    private double currSpeed;       //Vf in m/s
    private double currSpeedMPH;    //Vf in mph for printing to the screen
    private double lastSpeed;       //Vi in m/s
    private int speedLimit;
    private int driverSetPoint;
    private int ctcSetPoint;
    private double authority;       //feet remaining
    private double distanceIntoBlock;//The distance we have traveled into the block
    private double distanceTraveled;//meters traveled this cycle
    
    //Blocks
    private TrackPiece prevBlock;
    private Block currBlock;
    private Block nextBlock;
    private Block currTempBlock;
    
    /**
     * Constructor for the train GPS that relays position to the MBO
     * 
     * @author Tyler Protivnak
     * @param authority starting authority for train
     * @param trainSystem reference to the overall train system
     * @param trainID ID number of the train
     */
    public GPS(int authority, TrainSystem trainSystem, String trainID){
        
        //Speed and Authority
        this.currSpeed = 0;
        this.currSpeedMPH = 0;
        this.lastSpeed = 0;
        this.speedLimit = 0;
        this.authority = authority;
        this.distanceIntoBlock = 0;
        this.distanceTraveled = 0;
        this.trainID = trainID;
        this.trainSystem = trainSystem;
        
        //Block setting
        this.prevBlock = trainSystem.getTrackModel().getYard();
        this.currBlock = trainSystem.getTrackModel().enterTrack(Global.Line.GREEN);
        this.nextBlock = null;
        this.currTempBlock = null;
    }
        
        
    public void update(){
        
        this.distanceTraveled = ((this.lastSpeed + this.currSpeed)/2) * 1;    //In meters
        this.distanceTraveled = this.distanceTraveled * FEET_IN_A_METER;      //Convert to feet
        
        //Subtract the distance traveled from remaining authority
        this.authority -= this.distanceTraveled;
        
        //Assign the last speed to the current speed for the next cycle
        this.lastSpeed = this.currSpeed;                                                                
                
        //Current speed is in m/s, convert to mph to print out
        this.currSpeedMPH = this.currSpeed * SECONDS_IN_AN_HOUR / METERS_IN_A_MILE;
        
        //Calculate distance into block and ask for new block if we've gone the length of the block
        this.distanceIntoBlock += this.distanceTraveled;
        
        //If we've passsed into the next block, get the next block
        if(this.distanceIntoBlock > currBlock.getLength()) {
            this.distanceIntoBlock -= currBlock.getLength();
            
            this.nextBlock = this.currBlock.exitBlock(this.prevBlock);
            this.currTempBlock = this.currBlock;
            this.currBlock = this.nextBlock;
            this.prevBlock = this.currTempBlock;
        }
        
    }

    /**
     * 
     * @return trainID the ID of the train
     */
    public String getTrainID() {
        return trainID;
    }

    /**
     * 
     * @return distanceIntoBlock how far into the block the train is
     */
    public double getDistanceIntoBlock() {
        return distanceIntoBlock;
    }

    /**
     * 
     * @return currBlock the current block the train is on
     */
    public Block getCurrBlock() {
        return currBlock;
    }
    
    public double getSpeedLimit(){
        return this.currBlock.getSpeedLimit();
    }

    /**
     * 
     * @return the remaining authority of the train
     */
    public double getAuthority() {
        return authority;
    }
    
    /**
     * 
     * @param authority Sets a new authority for the train control to follow 
     */
    public void setAuthority(short authority) {
        this.authority = (double)authority*this.FEET_IN_A_METER;
    }

    public double getCurrSpeed() {
        return currSpeed;
    }

    public void setCurrSpeed(double currSpeed) {
        this.currSpeed = currSpeed;
    }

    
    
}
