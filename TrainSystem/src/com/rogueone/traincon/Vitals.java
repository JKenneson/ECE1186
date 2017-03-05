/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trainmodel.entities.TrainFailures;

/**
 *
 * @author Tyler
 */
public class Vitals {
    
    private TrainController trainController;
    private TrainModel trainModel;
    private GPS gps;
    private SpeedControl speedControl;
       
    private double powerCommand;
    private double maxPower;
    private int kP;
    private int kI;
    private double eK;
    private double eK_1;
    private double uK;
    private double uK_1;
    
    private boolean serviceBrakeActivated;
    private boolean emergencyBrakeActivated;
    private boolean emergencyBrakeOverride;
    
    //Failures
    private boolean antennaStatus;
    private boolean powerStatus;
    private boolean serviceBrakeStatus;
    
    
    
    public Vitals(TrainController tc, TrainModel tm, GPS gps, SpeedControl sc, double maxPow){
        this.trainController = tc;
        this.trainModel = tm;
        this.gps = gps;
        this.speedControl = sc;
        
        this.serviceBrakeActivated = false;
        this.emergencyBrakeActivated = false;
        this.emergencyBrakeOverride = false;
        
        this.antennaStatus = true;
        this.powerStatus = true;
        this.serviceBrakeStatus = true;
        
        this.kP = 100; //Seem to +=6 .5; +=12 1; +=17 1.5; +=19 2.0 assuming no passengers
        this.kI = 2;
        this.eK = 0;
        this.eK_1 = 0;
        this.uK = 0;
        this.uK_1 = 0;
        
        this.maxPower = maxPow;
    }
 
    /**
     * This method is called from the TrainControllerGUI when a failure is activated and sent through
     * the failure simulation radio buttons. This method will flip the brakes on the 
     * train controller and model.
     * 
     * @author Tyler Protivnak
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void causeFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch(failure) {
            //A power failure will prevent the doors, lights, and temp setting from working.  Activate emergency brake
            case Power:
                this.powerStatus = false;               //In any setter methods below, the value will not be updated if there is a power failure
                this.emergencyBrakeOverride = true;
                this.trainModel.causeFailure(TrainFailures.Power);
                break;
            //A brake failure will prevent the service brake from being activated. Activate the emergency brake
            case Brake:
                this.serviceBrakeStatus = false;
                this.emergencyBrakeOverride = true;
                this.trainModel.causeFailure(TrainFailures.Brake);
                break;
            //Deactivate the track and mbo antenna.  Activate emergency brake
            case Antenna:
                this.antennaStatus = false;
                this.emergencyBrakeOverride = true;
                this.trainModel.causeFailure(TrainFailures.Antenna);
                break;
        }
    }
    
    /**
     * This method is called from the TrainControllerGUI when a failure is deactivated
     * and sent through the failure simulation radio buttons. This method will flip the brakes on the 
     * train controller and model.
     * 
     * @author Tyler Protivnak
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void fixFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch(failure) {
            //Undo the power failure
            case Power:
                this.powerStatus = true;
                this.trainModel.fixFailure(TrainFailures.Power);
                break;
            //Undo the brake failure
            case Brake:
                this.serviceBrakeStatus = true;
                this.trainModel.fixFailure(TrainFailures.Brake);
                break;
            //Undo the antenna failure
            case Antenna:
                this.antennaStatus = true;
                this.trainModel.fixFailure(TrainFailures.Antenna);
                break;
        }
        //If there are no failures, de-activate the emergencyBrakeOverride
        if(this.powerStatus && this.serviceBrakeStatus && this.antennaStatus && this.gps.getAuthority()>0) {
            this.emergencyBrakeOverride = false;
        }
    }
    
    /**
     * This function figures out how to set the gui visual aids for the failures
     * on the train controller
     * 
     * @author Tyler Protivnak
     * @return value corresponding to failure type
     */
    private int getFailureType(){  //get from train model
        if(!this.powerStatus){
            if(!this.antennaStatus){
                if(!this.serviceBrakeStatus){
                    return 7; //All 3 are failed
                }
                return 4; //Power and antenna are out
            }
            else if(!this.serviceBrakeStatus){
                return 5;//Power and service brake are out
            }
            return 1; //Only power has failed
        }
        else if(!this.antennaStatus){
            if(!this.serviceBrakeStatus){
                return 6;
            }
            return 2;
        }
        else if(!this.serviceBrakeStatus){
            return 3;
        }
        return 0; //default, all clear
    }
    
    /**
     * This function should be called by the train model to find out the next power
     * command for the engine.
     * 
     * @author Tyler Protivnak
     * @param actualSpeed The current speed from the Train Model
     * @param samplePeriod The sampling period defined by the Train Model
     * @return power after calculations
     */
    public double calculatePower(double actualSpeed, double samplePeriod){ //should pull speed limit information from
                        //loaded track xlx after calculating location.
                        
        if(this.serviceBrakeActivated || this.emergencyBrakeActivated || this.trainModel.getGrade()<0 || this.speedControl.getSetPoint()==0.0){
            this.powerCommand = 0.0;
            return 0.0;
        }
        
        this.eK = this.speedControl.getSetPoint()-actualSpeed;   //Calc error difference
        this.gps.updatePosition(actualSpeed);           //Save actual speed
        
        this.uK = this.uK_1 + ((samplePeriod/2)*(this.eK+this.eK_1));

        this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
        if(this.powerCommand > this.maxPower){
            this.uK = this.uK_1;
            this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
            this.powerCommand = this.maxPower;
            
        }
        
        this.eK_1 = this.eK;
        this.uK_1 = this.uK;
        if(this.powerCommand<0){
            this.powerCommand = 0;
        }
        return this.powerCommand;
    }
    
    /**
     * Sets the Kp as passed by the train controller gui
     * 
     * @author Tyler Protivnak
     * @param Kp the new kp from the engineer
     */
    public void setKP(int Kp){
        this.kP = Kp;
    }
    
    /**
     * Sets the Ki as passed by the train controller gui
     * 
     * @author Tyler Protivnak
     * @param Ki 
     */
    public void setKI(int Ki){
        this.kI = Ki;
    }
    
    public boolean isServiceBrakeActivated() {
        return serviceBrakeActivated;
    }
    
    /**
     * 
     * @return the set KP value
     */
    public int getkP() {
        return kP;
    }

    /**
     * 
     * @return the set KI value
     */
    public int getkI() {
        return kI;
    }
    
    /**
     * 
     * @param serviceBrakeActivated Boolean to set the status of the service brake. True = on. Also updates train model.
     */
    public void setServiceBrakeActivated(boolean serviceBrakeActivated) {
        this.serviceBrakeActivated = serviceBrakeActivated;
        this.trainModel.setServiceBrakeActivated(serviceBrakeActivated);
    }

    /**
     * 
     * @param emergencyBrakeActivated Boolean to set the status of the e brake. True = on. Also updates train model.
     */
    public void setEmergencyBrakeActivated(boolean emergencyBrakeActivated) {
        this.emergencyBrakeActivated = emergencyBrakeActivated;
        this.trainModel.setEmergencyBrakeActivated(emergencyBrakeActivated);
    }
}
