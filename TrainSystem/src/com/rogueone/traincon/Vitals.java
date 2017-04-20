/**
 * The vitals handle the safety critical operations of the train controller to 
 * ensure that the train operates safely
 *
 * @author Tyler Protivnak
 * @Creation 3/4/17
 * @Modification 4/17/2017
 */
package com.rogueone.traincon;

import com.rogueone.trackmodel.Beacon;
import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trainmodel.entities.TrainFailures;
import com.rogueone.trainsystem.TrainSystem;

/**
 * Class declaration for Vitals
 *
 * @author Tyler Protivnak
 */
public class Vitals {

    private final TrainModel trainModel;
    private PowerSystems powerSystem;
    private SpeedControl speedControl;
    private GPS gps;
    private String line;

    private double powerCommand;
    private double maxPower;
    private double kP;
    private double kI;
    private double eK;
    private double eK_1;
    private double uK;
    private double uK_1;

    private boolean serviceBrakeActivated;
    private boolean serviceBrakeOverride;
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
    private String previousStation = "value";
    private boolean specialCase = true;
    private int stationStopTimer = -1;
    private double oldSetPoint = -5;

    /**
     * Constructor for Vitals module
     *
     * @author Tyler Protivnak
     * @param ts reference to the overall train system that is used for global values
     * @param tm direct reference to the train model used for access to brakes and other parameters
     * @param maxPow maximum power of the train
     * @param setPointSpeed the initial set point we were give to operate at
     * @param authority the initial authority we were given
     * @param trainID our simple id number
     * @param ps reference to the power system for electrical operation handling
     * @param line the line are we a part of
     */
    public Vitals(TrainSystem ts, TrainModel tm, double maxPow, byte setPointSpeed, short authority, String trainID, PowerSystems ps, String line) {
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
        this.line = line;
        this.gps = new GPS(authority, ts, trainID, line);
        this.speedControl = new SpeedControl(setPointSpeed, setPointSpeed, this.gps);
        this.powerSystem = ps;
    }

    /**
     * Update the service and emergency brakes. Also, handle the approaching of stations
     * 
     * @author Tyler Protivnak
     * @param manualMode the operation mode of the train controller
     */
    public void update(boolean manualMode) {
        double newSetPoint = this.speedControl.getSetPoint(manualMode);
        if(this.oldSetPoint != newSetPoint  || this.trainModel.getCurrSpeed() == 0.0){
            this.resetPower();
        }
        this.oldSetPoint = newSetPoint;
        
        this.gps.update(this.trainModel.getDistanceTraveledFeet(), this.trainModel.getCurrBlock(), this.trainModel.getCurrSpeedMPH());
        if (this.gps.getAuthority() < (this.trainModel.safeStoppingDistance() + 3.2) || this.emergencyBrakeOverride) {
            this.setEmergencyBrakeActivated(true);
        } else if (!this.emergencyBrakeOverride) {
            this.setEmergencyBrakeActivated(false);
        }

        //Calculate approaching station work
        boolean stopForStation = false;
        boolean setTimer = this.stationStopTimer < 0;
//        System.out.println("Set Timer: " + (this.stationStopTimer < 0));
        this.setServiceBrakeActivated(this.speedControl.update(manualMode) || stopForStation);
        if (this.approachingStation) { // initiate the approaching sequence

//            System.out.println("Train "+ this.gps.trainID + ": " + "Distance to station: " + this.distanceToStation + " Stopping distance: " + this.trainModel.safeStoppingDistance());
            stopForStation = (this.distanceToStation < this.trainModel.safeStoppingDistance());
//            System.out.println("Apply brake: " + stopForStation);
            this.distanceToStation -= this.trainModel.getDistanceTraveledFeet();
//            if (this.trainModel.getCurrSpeed() == 0.0 && this.trainModel.getCurrBlock().getStation() == null) {
//                //System.out.println("Train "+ this.gps.trainID + ": " + "Didn't make it to station!!!");
//            }
            if (this.trainModel.getCurrSpeed() == 0.0 && this.trainModel.getCurrBlock().getStation() != null) { // we are at a station 
//                System.out.println("Train "+ this.gps.trainID + ": " + "Boarding...");
                this.resetPower();
                if (this.doorSide) {
                    this.powerSystem.setRightDoorOpen(true && !manualMode);
                } else {
                    this.powerSystem.setLeftDoorOpen(true && !manualMode);
                }

                if(setTimer){
                    this.stationStopTimer = 60;
                }
            }
        }

        if (this.stationStopTimer == 30 && (this.powerSystem.isLeftDoorOpen() || this.powerSystem.isRightDoorOpen())) {
            this.trainModel.boardPassengers();
        }

        if (this.stationStopTimer == 0) {
            if(!manualMode){
                if (this.doorSide) {
                    this.powerSystem.setRightDoorOpen(false);
                } else {
                    this.powerSystem.setLeftDoorOpen(false);
                }
                this.approachingStation = false;
                stopForStation = false;
                this.setServiceBrakeActivated(false);
                this.previousStation = this.station;
            }
            else if(!(this.powerSystem.isLeftDoorOpen() || this.powerSystem.isRightDoorOpen())){
                this.approachingStation = false;
                stopForStation = false;
                this.setServiceBrakeActivated(false);
                this.previousStation = this.station;
            }
            else{
                this.stationStopTimer++;
            }
        }

        this.setServiceBrakeActivated(this.speedControl.update(manualMode) || stopForStation || this.serviceBrakeOverride);
        this.stationStopTimer--;
    }

    /**
     * This method is called from the TrainControllerGUI when a failure is
     * activated and sent through the failure simulation radio buttons. This
     * method will flip the brakes on the train controller and model.
     *
     * @author Tyler Protivnak
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void causeFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch (failure) {
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
     * This method is called from the TrainControllerGUI when a failure is
     * deactivated and sent through the failure simulation radio buttons. This
     * method will flip the brakes on the train controller and model.
     *
     * @author Tyler Protivnak
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void fixFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch (failure) {
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
        if (this.powerStatus && this.serviceBrakeStatus && this.antennaStatus && this.gps.getAuthority() > 0) {
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
    public int getFailureType() {  //get from train model
        if (!this.powerStatus) {
            if (!this.antennaStatus) {
                if (!this.serviceBrakeStatus) {
                    return 7; //All 3 are failed
                }
                return 4; //Power and antenna are out
            } else if (!this.serviceBrakeStatus) {
                return 5;//Power and service brake are out
            }
            return 1; //Only power has failed
        } else if (!this.antennaStatus) {
            if (!this.serviceBrakeStatus) {
                return 6;
            }
            return 2;
        } else if (!this.serviceBrakeStatus) {
            return 3;
        }
        return 0; //default, all clear
    }

    /**
     * This function should be called by the train model to find out the next
     * power command for the engine.
     *
     * @author Tyler Protivnak
     * @param actualSpeed The current speed from the Train Model
     * @param samplePeriod The sampling period defined by the Train Model
     * @param manualMode the operation mode of the train controller
     * @return power after calculations
     */
    public double calculatePower(double actualSpeed, double samplePeriod, boolean manualMode) { //should pull speed limit information from
        //loaded track xlx after calculating location.

        this.eK = (this.speedControl.getSetPoint(manualMode) - actualSpeed);   //Calc error difference

        this.uK = this.uK_1 + ((samplePeriod / 2) * (this.eK + this.eK_1));

        this.powerCommand = (this.kP * this.eK) + (this.kI * this.uK);
        if (this.powerCommand > this.maxPower) {
            this.uK = this.uK_1;
            //this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
            this.powerCommand = this.maxPower;
        }

        this.eK_1 = this.eK;
        this.uK_1 = this.uK;
        if (this.powerCommand < 0) {
            this.powerCommand = 0;
        }

        if (this.serviceBrakeActivated || this.emergencyBrakeActivated || this.speedControl.getSetPoint(manualMode) <= 0.0 || (this.stationStopTimer > 0)) {
            this.powerCommand = 0.0;
        }
        return this.powerCommand;
    }

    /**
     * Reset the power calculation parameters so we don't overshoot set speed
     * 
     * @author Tyler Protivnak
     */
    private void resetPower() {
        this.eK = 0;
        this.eK_1 = 0;
        this.uK = 0;
        this.uK_1 = 0;
    }

    /**
     * Sets the Kp as passed by the train controller gui
     *
     * @author Tyler Protivnak
     * @param Kp the new kp from the engineer
     */
    public void setKP(double Kp) {
        this.kP = Kp;
    }

    /**
     * Sets the Ki as passed by the train controller gui
     *
     * @author Tyler Protivnak
     * @param Ki the ki from the engineer
     */
    public void setKI(double Ki) {
        this.kI = Ki;
    }

    /**
     * Return the current kp value
     *
     * @author Tyler Protivnak
     * @return the set KP value
     */
    public double getKP() {
        return this.kP;
    }

    /**
     * Return the current ki value
     * 
     * @author Tyler Protivnak
     * @return the set KI value
     */
    public double getKI() {
        return this.kI;
    }

    /**
     * Is the service brake activated or not
     * 
     * @author Tyler Protivnak
     * @return boolean value true if activated, else false
     */
    public boolean isServiceBrakeActivated() {
        return serviceBrakeActivated;
    }

    /**
     * Set the service brake on the controller and model
     *
     * @author Tyler Protivnak
     * @param serviceBrakeActivated Boolean to set the status of the service
     * brake. True = on. Also updates train model.
     */
    public void setServiceBrakeActivated(boolean serviceBrakeActivated) {
        this.serviceBrakeActivated = serviceBrakeActivated;
        this.trainModel.setServiceBrakeActivated(serviceBrakeActivated);
    }

    /**
     * Is the emergency brake activated
     * 
     * @author Tyler Protivnak
     * @return true if E brake is activated, else false
     */
    public boolean isEmergencyBrakeActivated() {
        return emergencyBrakeActivated;
    }

    /**
     * Set the emergency brake on the controller and model
     *
     * @author Tyler Protivnak
     * @param emergencyBrakeActivated Boolean to set the status of the e brake.
     * True = on. Also updates train model.
     */
    public void setEmergencyBrakeActivated(boolean emergencyBrakeActivated) {
        this.emergencyBrakeActivated = emergencyBrakeActivated;
        this.trainModel.setEmergencyBrakeActivated(emergencyBrakeActivated);
    }

    /**
     * What is the current power command we are outputting?
     *
     * @author Tyler Protivnak
     * @return power command that was passed to the train model
     */
    public double getPowerCommand() {
        return powerCommand;
    }

    /**
     * What is the maximum power for the train?
     *
     * @author Tyler Protivnak
     * @return the max power for the given train
     */
    public double getMaxPower() {
        return maxPower;
    }

    /**
     * Is the emergency brake override activated?
     *
     * @author Tyler Protivnak
     * @return true if override is triggered, else false
     */
    public boolean isEmergencyBrakeOverride() {
        return emergencyBrakeOverride;
    }

    /**
     * Set the value of the service brake override
     * 
     * @author Tyler Protivnak
     * @param set the boolean value we want to set the service brake override to
     */
    public void setServiceBrakeOverride(boolean set) {
        this.serviceBrakeOverride = set;
    }

    /**
     * Set the value of the emergency brake override
     * 
     * @author Tyler Protivnak
     * @param set the boolean value we want to set the emergency brake override to
     */
    public void setEmergencyBrakeOverride(boolean set) {
        if(!set && this.emergencyBrakeOverride && this.gps.getCurrSpeed() != 0.0){
            return;
        }
        this.emergencyBrakeOverride = set;
    }

    /**
     * get a reference to the speed controller for this vitals system
     * 
     * @author Tyler Protivnak
     * @return reference to the speed controller for this vital system
     */
    public SpeedControl getSpeedControl() {
        return this.speedControl;
    }

    /**
     * get a reference to the gps for this vitals system
     * 
     * @author Tyler Protivnak
     * @return reference to the gps for this vital system
     */
    public GPS getGPS() {
        return this.gps;
    }

    /**
     * Use the beacon passed in to figure out the station logic and announcements
     * 
     * @author Tyler Protivnak
     * @param beacon the beacon we will decode and use for station logic
     */
    public void receieveBeacon(Beacon beacon) {
        boolean skipped = true;
        if (beacon.getStation() != null) {
            if(!this.approachingStation){
                this.station = beacon.getStation().getName();
            }
            if (beacon.getID() == 34) { //Special case for the green line
                specialCase = !this.specialCase;
                skipped = false;
            }
            if (!this.previousStation.equals(this.station) && (this.specialCase || skipped) && this.line.equals("GREEN")) {      
                this.approachingStation = true;
                this.doorSide = beacon.isOnRight();
                this.distanceToStation = beacon.getDistance() + 50;
            }
            
            else if(!this.previousStation.equals(this.station) && (this.specialCase || skipped) && this.line.equals("RED") && !this.approachingStation) {
                this.approachingStation = true;
                this.doorSide = beacon.isOnRight();
                this.distanceToStation = beacon.getDistance() + 50;
            }
        }
    }
}