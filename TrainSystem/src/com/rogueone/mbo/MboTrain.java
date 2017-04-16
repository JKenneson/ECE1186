/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.mbo;
/**
 *
 * @author Brian
 */
public class MboTrain {
    private int id =-1;
    private String block = "-1";
    private char section = 'a';
    private double currentSpeed = 0;
    private double suggestedSpeed = 0;
    private double prevSpeed = 0;
    private double brakeDistance = 0;
    private double authority = 0;
    private String nextStation = "null";
    private String timeOfArrival = "never";
    private int passengers = 0;
    private double dist1;
    private double dist2;
    private String time1;
    private String time2;
    private int location;
    
    /**
     * Calculates speed of train 
     * @param time1 first time
     * @param time2 second time
     * @param dist1 distance one
     * @param dist2 distance 2
     * @return current speed of train
     * @author Brian Stevenson
     */
    public double calculateSpeed(String time1, String time2, double dist1, double dist2){
        double delta = dist2-dist1;
        double deltaTime = Integer.parseInt(time2)-Integer.parseInt(time1);
        this.currentSpeed = delta/deltaTime;
        return this.currentSpeed;
    }
    
    /**
     * Calculates safe stopping distance of train using constant braking speed
     * @param speed current speed of train
     * @return safe stopping distance
     * @author Brian Stevenson
     */
    public double calcSafeStop(double speed){
        double stopTime = speed/1.2;
        double avgSpeed = speed/2;
        this.brakeDistance = stopTime * avgSpeed;
        return this.brakeDistance;
    }
    /**
     * Gets train id
     * @return train id
     * @author Brian Stevenson
     */
    public int getTrainId(){
        return this.id;
    }
    /**
     * Gets train speed
     * @return train speed
     * @author Brian Stevenson
     */
    public double getCurrSpeed(){
        return this.currentSpeed;
    }
    /**
     * Gets previous train speed
     * @return train id
     * @author Brian Stevenson
     */
    public double getPrevSpeed(){
        return this.prevSpeed;
    }
    /**
     * Gets train authority
     * @return train authority
     * @author Brian Stevenson
     */
    public double getAuthority(){
        return this.authority;
    }
    /**
     * Gets next station
     * @return next train station
     * @author Brian Stevenson
     */
    public String getNextStation(){
        return this.nextStation;
    }
    /**
     * Gets arrival time at next station
     * @return train arrival time (string)
     * @author Brian Stevenson
     */
    public String getNextArrivalTime(){
        return this.timeOfArrival;
    }
    /**
     * Gets suggested speed
     * @return suggested speed
     * @author Brian Stevenson
     */
    public double getSuggestedSpeed(){
        return suggestedSpeed;
    }
    /**
     * Gets num passengers on train
     * @return num passengers
     * @author Brian Stevenson
     */
    public int getPassengers(){
        return passengers;
    }
    /**
     * Sets train id
     * @param id desired id
     * @author Brian Stevenson
     */
    public void setTrainId(int id){
        this.id = id;
    }
    /**
     * sets current block
     * @param block desired block
     * @author Brian Stevenson
     */
    public void setBlock(String block){
        this.block = block;
    }
    /**
     * sets current section
     * @param section desired section
     * @author Brian Stevenson
     */
    public void setSection(char section){
        this.section = section;
    }
    /**
     * sets num passengers
     * @param passengers desired passenger num
     * @author Brian Stevenson
     */
    public void setPassengers(int passengers){
        this.passengers = passengers;
    }
    /**
     * sets current speed
     * @param currSpeed desired  speed
     * @author Brian Stevenson
     */
    public void setCurrentSpeed(double currSpeed){
        this.currentSpeed = currSpeed;
    }
    /**
     * sets prev speed
     * @param prevSpeed desired prev speed
     * @author Brian Stevenson
     */
    public void setPreviousSpeed(double prevSpeed){
        this.prevSpeed = prevSpeed;
    }
    /**
     * sets breaking distance
     * @param brakeDistance desired breaking distance
     * @author Brian Stevenson
     */
    public void setBrakeDistance(double brakeDistance){
        this.brakeDistance = brakeDistance;
    }
    /**
     * sets current authority
     * @param authority current authority
     * @author Brian Stevenson
     */
    public void setAuthority(double authority){
        this.authority = authority;
    }
    /**
     * sets next station
     * @param nextStation desired next station
     * @author Brian Stevenson
     */
    public void setNextStation(String nextStation){
        this.nextStation = nextStation;
    }
    /**
     * sets next arrival time 
     * @param nextArrival next arrival time
     * @author Brian Stevenson
     */
    public void setNextArrivalTime(String nextArrival){
        this.timeOfArrival = nextArrival;
    }
    /**
     * sets current speed
     * @param speed desired speed
     * @author Brian Stevenson
     */
    public void setSpeed(double speed){
        this.currentSpeed = speed;
    }
    /**
     * sets current speed
     * @param speed desired speed
     * @author Brian Stevenson
     */
    public void setSuggSpeed(double speed){
        this.suggestedSpeed = speed;
    }
    
}
