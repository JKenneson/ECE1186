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
public class SpeedControl {
    
    NumberFormat commaFormatter = NumberFormat.getInstance(Locale.US);
    DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
    
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    
    private TrainController trainController;
    private TrainModel trainModel;
    private GPS gps;
    private Vitals vitals;
    
    private byte driverSetPoint;
    private byte recommendedSetPoint;
    
    private byte safeSetPoint;
    
    public SpeedControl(byte driverSetPoint, byte recommendedSetPoint, TrainModel tm, TrainController tc, GPS gps, Vitals vitals){
        this.driverSetPoint = driverSetPoint;
        this.recommendedSetPoint = recommendedSetPoint;
        this.trainModel = tm;
        this.trainController = tc;
        this.gps = gps;
        this.vitals = vitals;
    }

    private void updateSafeSpeed(double currSpeed){
        if((currSpeed>this.findSetPoint() && !this.trainController.isManualMode()) || this.vitals.isServiceBrakeActivated()){
            this.trainController.setServiceBrakeActivated(true);
        }
        else{
            this.trainController.setServiceBrakeActivated(false);
        }
    }
       
    /**
     * 
     * @return the set point based on the mode (man or auto) of the train
     */
    private double findSetPoint(){
        if(this.trainController.isManualMode()){
            if(this.trainModel.getDriverSetPoint() > this.gps.getSpeedLimit()){
                this.driverSetPoint = (byte) this.gps.getSpeedLimit();
            }
            else{
                this.driverSetPoint = (byte) this.trainModel.getDriverSetPoint();
            }
            return this.driverSetPoint;
        }
        else{
            if(this.trainModel.getCtcSetPoint() > this.gps.getSpeedLimit()){
                this.recommendedSetPoint = (byte) this.gps.getSpeedLimit();
            }
            else{
                this.recommendedSetPoint = (byte) this.trainModel.getCtcSetPoint();//Is this legal?
            }
            return this.recommendedSetPoint; //////////////////////////////////////////////////////////////NEED TO GET CTC OR MBO!!!
        }
    }
    
    public double getSetPoint(){
        return this.safeSetPoint;
    }
    
    /**
     * Below are the getters and setters for the SpeedControl class
     * @author Tyler Protivnak
     */

    public byte getDriverSetPoint() {
        return driverSetPoint;
    }

    public void setDriverSetPoint(byte driverSetPoint) {
        this.driverSetPoint = driverSetPoint;
    }

    public byte getRecommendedSetPoint() {
        return recommendedSetPoint;
    }

    public void setRecommendedSetPoint(byte recommendedSetPoint) {
        this.recommendedSetPoint = recommendedSetPoint;
    }
    
}
