/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.trainmodel.TrainModel;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author Tyler
 */
public class SpeedControl{
    
    NumberFormat commaFormatter = NumberFormat.getInstance(Locale.US);
    DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
    
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    
    private final TrainModel trainModel;
    private final GPS gps;
    private byte driverSetPoint;
    private byte recommendedSetPoint;
    
    public SpeedControl(byte driverSetPoint, byte recommendedSetPoint, TrainModel tm, GPS gps){
        this.driverSetPoint = driverSetPoint;
        this.recommendedSetPoint = recommendedSetPoint;
        this.trainModel = tm;
        this.gps = gps;
    }

    /**
     * 
     * @param manualMode
     * @param serviceBrakeActivated
     * @return boolean as to weather or not to activate the service brake
     */
    public boolean update(boolean manualMode, boolean serviceBrakeActivated){
        double currSpeed = this.gps.getCurrSpeed();
        System.out.println("CurrSpeed: " + currSpeed + " SetSpeed: " + this.findSetPoint(manualMode));
        if((currSpeed>this.findSetPoint(manualMode))){
            return true;
        }
        else{
            return false;
        }
    }
       
    /**
     * 
     * @return the set point based on the mode (man or auto) of the train
     */
    private double findSetPoint(boolean manualMode){
        if(manualMode){
            if(this.driverSetPoint > this.gps.getSpeedLimit()){
                return this.gps.getSpeedLimit();
            }
            return this.driverSetPoint;
        }
        else{
            if(this.recommendedSetPoint > this.gps.getSpeedLimit()){
                return this.gps.getSpeedLimit();
            }
            return this.recommendedSetPoint; //////////////////////////////////////////////////////////////NEED TO GET CTC OR MBO!!!
        }
    }
    
    public double getSetPoint(boolean manualMode){
        return this.findSetPoint(manualMode);
    }
    
    /**
     * Below are the getters and setters for the SpeedControl class
     * @author Tyler Protivnak
     */

    /**
     * 
     * @return driver set point in mph
     */
    public byte getDriverSetPoint() {
        return this.driverSetPoint;
    }

    public void setDriverSetPoint(byte driverSetPoint) {
        this.driverSetPoint = driverSetPoint;
    }

    /**
     * 
     * @return rec set point in mph
     */
    public byte getRecommendedSetPoint() {
        return this.recommendedSetPoint;
    }

    public void setRecommendedSetPoint(byte recommendedSetPoint) {
        this.recommendedSetPoint = recommendedSetPoint;
    }
    
    
    
}
