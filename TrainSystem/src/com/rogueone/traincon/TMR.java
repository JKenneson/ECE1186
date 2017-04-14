/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.trackmodel.Beacon;
import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trainsystem.TrainSystem;

/**
 *
 * @author Tyler
 */

public class TMR {
    
    public Vitals [] vitals = new Vitals[3];
    
    
    //Need to implement fail over from primary
    public Vitals primaryVital;
    
    public TMR(TrainSystem ts, TrainModel tm, TrainController tc, double maxPow, byte setPointSpeed, short authority, String trainID, PowerSystems ps, String line){
        for(int i = 0; i < 3; i++){
            vitals[i] = new Vitals(ts, tm, tc, maxPow, setPointSpeed, authority, trainID, ps, line);
        }
        this.primaryVital = vitals[0];
    }
    
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
        //return this.primaryVital.calculatePower(actualSpeed, samplePeriod, manualMode);
    }
    
    public Vitals getPrimary(){
        return this.primaryVital;
    }
    
    public void update(boolean manualMode){
        for(int i = 0; i < 3; i++){
            this.vitals[i].update(manualMode);
        }
        //this.primaryVital.update(manualMode);
    }
    
    public void receiveBeacon(Beacon b){ //update for TMR work
        this.primaryVital.receieveBeacon(b);
    }
    
    public void setServiceBrakeOverride(boolean set){
        for(int i = 0; i < 3; i++){
            this.vitals[i].setServiceBrakeOverride(set);
        }
    }
    
    public void setServiceBrakeActivated(boolean set){
        for(int i = 0; i < 3; i++){
            this.vitals[i].setServiceBrakeActivated(set);
        }
    }
    
    public void setEmergencyBrakeOverride(boolean set) {
        for(int i = 0; i < 3; i++){
            this.vitals[i].setEmergencyBrakeOverride(set);
        }
    }

    public void setEmergencyBrakeActivated(boolean set) {
        for(int i = 0; i < 3; i++){
            this.vitals[i].setEmergencyBrakeActivated(set);
        }
    }
    
}
