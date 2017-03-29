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
public class GPS{
    
    
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    
    //Train System reference
    public String trainID;
    
    //Speed and Authority
    private double currSpeed;       //Vf in m/s

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
        this.authority = (double)authority*this.FEET_IN_A_METER;//Update how values are brought in
        this.distanceIntoBlock = 0;
        this.distanceTraveled = 0;
        this.trainID = trainID;
        
        //Block setting
        this.prevBlock = trainSystem.getTrackModel().getYard();
        this.currBlock = trainSystem.getTrackModel().enterTrack(Global.Line.GREEN);
        this.nextBlock = null;
        this.currTempBlock = null;
    }
        
        
    public void update(double distanceTraveled, Block currBlock){
                
        //Subtract the distance traveled from remaining authority
        this.authority -= distanceTraveled;
        this.currBlock = currBlock;        
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
    
    /**
     * 
     * @return speed limit of block in MPH
     */
    public double getSpeedLimit(){
        return this.currBlock.getSpeedLimit()*0.621371;
    }

    /**
     * 
     * @return the remaining authority of the train in FEET
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
