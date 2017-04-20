/**
 * TMR system used for redundant operation of the train controller vitals
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
 * Class declaration for TMR
 *
 * @author Tyler Protivnak
 */

public class TMR {
    
    public Vitals [] vitals = new Vitals[3];
    public Vitals primaryVital;
   
    /**
     * Constructor that creates 3 copies of vitals that are used in a redundant manner
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
    public TMR(TrainSystem ts, TrainModel tm, double maxPow, byte setPointSpeed, short authority, String trainID, PowerSystems ps, String line){
        for(int i = 0; i < 3; i++){
            vitals[i] = new Vitals(ts, tm, maxPow, setPointSpeed, authority, trainID, ps, line);
        }
        this.primaryVital = vitals[0];
    }
    
    /**
     * Calculate the power that the engine should output based on previous calculations
     * used for power. The TMR system decides on a safe power command by passing the
     * output of at least 2 of 3 agreed values
     * 
     * @author Tyler Protivnak
     * @param actualSpeed the speed we are moving at
     * @param samplePeriod the sample period for power calculations
     * @param manualMode the operation of the train controller at the moment. Used for setpoint decisions
     * @return the power value the train model should set it's engine at
     */
    public double calculatePower(double actualSpeed, double samplePeriod, boolean manualMode){
        double [] powers = new double[3];
        for(int i = 0; i < 3; i++){
            powers[i] = vitals[i].calculatePower(actualSpeed, samplePeriod, manualMode);
        }
        
        if(powers[0] == powers[1] || powers[0] == powers[2]){
            return powers[0];
        }
        else if(powers[1] == powers[2]){
            return powers[1];
        }
        else{
            System.out.println("We should not get here... Probably stop train");
            return this.calculatePower(actualSpeed, samplePeriod, manualMode);
        }
    }
    
    /**
     * Return a reference to the primary vital if doing no critical checks
     * 
     * @author Tyler Protivnak
     * @return reference to primary system vital
     */
    public Vitals getPrimary(){
        return this.primaryVital;
    }
    
    /**
     * Update all three vitals in the TMR system
     * 
     * @author Tyler Protivnak
     * @param manualMode the operation mode of the train controller
     */
    public void update(boolean manualMode){
        for(int i = 0; i < 3; i++){
            this.vitals[i].update(manualMode);
        }
    }
    
    /**
     * Update the recommended (non driver) set point on all the vitals
     * 
     * @author Tyler Protivnak
     * @param input the new recommended set point
     */
    public void updateRecommendedSetPoint(byte input){
        for(int i = 0; i < 3; i++){
            this.vitals[i].getSpeedControl().setRecommendedSetPoint(input);
        }
    }
    
    /**
     * Update the driver's set point on all the vitals
     * 
     * @author Tyler Protivnak
     * @param input the new driver set point
     */
    public void updateDriverSetPoint(byte input){
        for(int i = 0; i < 3; i++){
            this.vitals[i].getSpeedControl().setDriverSetPoint(input);
        }
    }
    
    /**
     * Update the authority value on all the vitals
     * 
     * @author Tyler Protivnak
     * @param input the new set point
     */
    public void updateAuthority(short input){
        for(int i = 0; i < 3; i++){
            this.vitals[i].getGPS().setAuthority(input);
        }
    }
    
    /**
     * Give me a beacon message that we are to use to make decisions about stopping and updating
     * 
     * @author Tyler Protivnak
     * @param b the beacon we are looking to make decisions with
     */
    public void receiveBeacon(Beacon b){ //update for TMR work
        this.primaryVital.receieveBeacon(b);
    }
    
    /**
     * Set the service brake override on all vitals in system
     * 
     * @author Tyler Protivnak
     * @param set new brake status boolean
     */
    public void setServiceBrakeOverride(boolean set){
        for(int i = 0; i < 3; i++){
            this.vitals[i].setServiceBrakeOverride(set);
        }
    }
    
    /**
     * Set the service brake on all vitals in system
     * 
     * @author Tyler Protivnak
     * @param set new brake status boolean
     */
    public void setServiceBrakeActivated(boolean set){
        for(int i = 0; i < 3; i++){
            this.vitals[i].setServiceBrakeActivated(set);
        }
    }
    
    /**
     * Set the emergency brake override on all vitals in system
     * 
     * @author Tyler Protivnak
     * @param set new brake status boolean
     */
    public void setEmergencyBrakeOverride(boolean set) {
        for(int i = 0; i < 3; i++){
            this.vitals[i].setEmergencyBrakeOverride(set);
        }
    }

    /**
     * Set the emergency brake on all vitals in system
     * 
     * @author Tyler Protivnak
     * @param set new brake status boolean
     */
    public void setEmergencyBrakeActivated(boolean set) {
        for(int i = 0; i < 3; i++){
            this.vitals[i].setEmergencyBrakeActivated(set);
        }
    }
    
    /**
     * Pass failure signal to Vital
     * 
     * @author Tyler Protivnak
     * @param failure 
     */
    public void causeFailure(TrainFailures failure, boolean trainModel) {
        for(int i = 0; i < 3; i++){
            this.vitals[i].causeFailure(failure, trainModel);
        }
    }
    
    /**
     * Pass fix signal to Vital
     * 
     * @author Tyler Protivnak
     * @param failure 
     */
    public void fixFailure(TrainFailures failure, boolean trainModel) {
        for(int i = 0; i < 3; i++){
            this.vitals[i].fixFailure(failure, trainModel);
        }
    }
}
