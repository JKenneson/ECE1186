/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.trackmodel.Beacon;
import com.rogueone.trackmodel.Station;
import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trainmodel.entities.TrainFailures;
import com.rogueone.trainsystem.TrainSystem;

/**
 *
 * @author Tyler
 */
public class Vitals {

    //private TrainController trainController;
    //private final TrainSystem trainSystem;
    private final TrainModel trainModel;
    private SpeedControl speedControl;
    private GPS gps;
       
    private double powerCommand;
    private double maxPower;
    private double kP;
    private double kI;
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
    
    //Approaching station
    private boolean approachingStation;
    private boolean doorSide;
    private double distanceToStation;
    private String station = "start";
    private boolean stationStop;
    private String previousStation = "value";
    private boolean specialCase = true;
    
    
    
    /**
     * Constructor for Vitals module
     * 
     * @author Tyler Protivnak
     * @param tm ref to attached train model
     * @param gps ref to tc gps module
     * @param maxPow max power of the attached train
     */
    public Vitals(TrainSystem ts, TrainModel tm, TrainController tc, double maxPow, byte setPointSpeed, short authority, String trainID){
        //this.trainSystem = ts;
        this.trainModel = tm;
        
        this.serviceBrakeActivated = false;
        this.emergencyBrakeActivated = false;
        this.emergencyBrakeOverride = false;
        
        this.antennaStatus = true;
        this.powerStatus = true;
        this.serviceBrakeStatus = true;
        
        this.kP = 100;
        this.kI = 2;
        this.eK = 0;
        this.eK_1 = 0;
        this.uK = 0;
        this.uK_1 = 0;
        this.maxPower = maxPow;
        
        this.gps = new GPS(authority, ts, trainID);
        this.speedControl = new SpeedControl(setPointSpeed, setPointSpeed, tm, this.gps);
    }
 
    public void update(boolean manualMode){
        this.gps.update(this.trainModel.getDistanceTraveledFeet(), this.trainModel.getCurrBlock(), this.trainModel.getCurrSpeedMPH());
        if(this.gps.getAuthority()<0 || this.emergencyBrakeOverride)
            this.setEmergencyBrakeActivated(true);
        else if(!this.emergencyBrakeOverride){
            this.setEmergencyBrakeActivated(false);
        }
        
        //Calculate approaching station work
        boolean stopForStation = false;
        this.setServiceBrakeActivated(this.speedControl.update(manualMode, this.serviceBrakeActivated) || stopForStation);
        if(this.approachingStation){
            
            System.out.println("Distance to station: " + this.distanceToStation + " Stopping distance: " + this.trainModel.safeStoppingDistance());
            stopForStation = (this.distanceToStation < this.trainModel.safeStoppingDistance());
            System.out.println("Apply brake: " + stopForStation);
            this.distanceToStation -= this.trainModel.getDistanceTraveledFeet();
            if(this.trainModel.getCurrSpeed() == 0.0 && this.trainModel.getCurrBlock().getStation() != null){
                System.out.println("Boarding...");
                this.trainModel.boardPassengers(this.doorSide);
                this.approachingStation = false;
                stopForStation = false;
                this.setServiceBrakeActivated(false);
                this.previousStation = this.station;
                System.out.println("Leaving station");
            }
        }
        
        System.out.println("Manual: " + manualMode + " Service Brake: " + this.serviceBrakeActivated);
        this.setServiceBrakeActivated(this.speedControl.update(manualMode, this.serviceBrakeActivated) || stopForStation);
        System.out.println("Exit");
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
    public int getFailureType(){  //get from train model
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
    public double calculatePower(double actualSpeed, double samplePeriod, boolean manualMode){ //should pull speed limit information from
                        //loaded track xlx after calculating location.
        //this.approachingStation ||
        //this.gps.getCurrBlock().getGrade()<0 ||
        if(this.serviceBrakeActivated || this.emergencyBrakeActivated || this.speedControl.getSetPoint(manualMode)<=0.0){
            //Maybe I shouldn't do when grade is less than 0
            //System.out.println("S brake = " + this.serviceBrakeActivated + " E brake" this.emergencyBrakeActivated || this.gps.getCurrBlock().getGrade()<0 || this.speedControl.getSetPoint(manualMode)<=0.0);
            this.powerCommand = 0.0;
            return 0.0;
        }
        
        this.eK = (this.speedControl.getSetPoint(manualMode)-actualSpeed);   //Calc error difference
        //System.out.println("The diff in values is: " + this.eK);
        //this.gps.setCurrSpeed(actualSpeed);
        
        this.uK = this.uK_1 + ((samplePeriod/2)*(this.eK+this.eK_1));

        this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
        if(this.powerCommand > this.maxPower){
            this.uK = this.uK_1;
            //this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
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
    public void setKP(double Kp){
        this.kP = Kp;
    }
    
    /**
     * Sets the Ki as passed by the train controller gui
     * 
     * @author Tyler Protivnak
     * @param Ki 
     */
    public void setKI(double Ki){
        this.kI = Ki;
    }
    
    /**
     * 
     * @return the set KP value
     */
    public double getKP() {
        return this.kP;
    }

    /**
     * 
     * @return the set KI value
     */
    public double getKI() {
        return this.kI;
    }
    
     public boolean isServiceBrakeActivated() {
        return serviceBrakeActivated;
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
     * @return true if E brake is activated
     */
    public boolean isEmergencyBrakeActivated() {
        return emergencyBrakeActivated;
    }
    
    /**
     * 
     * @param emergencyBrakeActivated Boolean to set the status of the e brake. True = on. Also updates train model.
     */
    public void setEmergencyBrakeActivated(boolean emergencyBrakeActivated) {
        this.emergencyBrakeActivated = emergencyBrakeActivated;
        this.trainModel.setEmergencyBrakeActivated(emergencyBrakeActivated);
    }

    public void setSpeedControl(SpeedControl speedControl) {
        this.speedControl = speedControl;
    }

    /**
     * 
     * @return power command that was passed to the train model
     */
    public double getPowerCommand() {
        return powerCommand;
    }
    
    /**
     * 
     * @return the max power for the given train
     */
    public double getMaxPower() {
        return maxPower;
    }

    

    /**
     * 
     * @return true if override is triggered
     */
    public boolean isEmergencyBrakeOverride() {
        return emergencyBrakeOverride;
    }
    
    public SpeedControl getSpeedControl(){
        return this.speedControl;
    }
    
    public GPS getGPS() {
        return this.gps;
    }
    
    public void receieveBeacon(Beacon beacon){
        
        if(beacon.getStation() != null){
            this.station = beacon.getStation().getName();
            if(beacon.getID() == 33 && this.specialCase){
                specialCase = !this.specialCase;
            }
            if(!this.previousStation.equals(this.station) && this.specialCase){
                this.approachingStation = true;
                this.doorSide = beacon.isOnRight();
                this.distanceToStation = beacon.getDistance() + 25;
                if(this.specialCase == false){
                    this.specialCase = !this.specialCase;
                }
            }
        }
        
        
        else{ //Do distance calculation work for red line
            
        } 
    }        
}
