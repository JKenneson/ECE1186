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
    
    public double calculateSpeed(String time1, String time2, double dist1, double dist2){
        double delta = dist2-dist1;
        double deltaTime = Integer.parseInt(time2)-Integer.parseInt(time1);
        this.currentSpeed = delta/deltaTime;
        return this.currentSpeed;
    }
    
    public double calcSafeStop(double speed){
        double stopTime = speed/1.2;
        double avgSpeed = speed/2;
        this.brakeDistance = stopTime * avgSpeed;
        return this.brakeDistance;
    }
    public String getPosition(){
        return "position";
    }
    public int getTrainId(){
        return this.id;
    }
    public double getCurrSpeed(){
        return this.currentSpeed;
    }
    public double getPrevSpeed(){
        return this.prevSpeed;
    }
    public double getAuthority(){
        return this.authority;
    }
    public String getNextStation(){
        return this.nextStation;
    }
    public String getNextArrivalTime(){
        return this.timeOfArrival;
    }
    public double getSuggestedSpeed(){
        return suggestedSpeed;
    }
    public int getPassengers(){
        return passengers;
    }
    
    public void setTrainId(int id){
        this.id = id;
    }
    public void setBlock(String block){
        this.block = block;
    }
    public void setSection(char section){
        this.section = section;
    }
    public void setPassengers(int passengers){
        this.passengers = passengers;
    }
    public void setCurrentSpeed(double currSpeed){
        this.currentSpeed = currSpeed;
    }
    public void setPreviousSpeed(double prevSpeed){
        this.prevSpeed = prevSpeed;
    }
    public void setBrakeDistance(double brakeDistance){
        this.brakeDistance = brakeDistance;
    }
    public void setAuthority(double authority){
        this.authority = authority;
    }
    public void setNextStation(String nextStation){
        this.nextStation = nextStation;
    }
    public void setNextArrivalTime(String nextArrival){
        this.timeOfArrival = nextArrival;
    }
    public void setSpeed(double speed){
        this.currentSpeed = speed;
    }
    public void setSuggSpeed(double speed){
        this.suggestedSpeed = speed;
    }
    
}
