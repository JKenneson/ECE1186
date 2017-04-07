/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trainmodel;

/**
 *
 * @author Tyler
 */
public class TemperatureModel{
    private double temperature = 60;
    private boolean ACOn;
    private boolean HeaterOn;
    private final double tempDelta = 0.5;
    private int updateCount = 0;
        
    public TemperatureModel(boolean acOn, boolean heaterOn){
        this.ACOn = acOn;
        this.HeaterOn = heaterOn;
    }
        
    public void update(){
        if(this.updateCount == 5){
            this.updateCount = 0;
            System.out.println("AC: " + this.ACOn + " Heater: " + this.HeaterOn);
            if(this.ACOn){
                this.temperature -= this.tempDelta;
            }
            else if(this.HeaterOn){
                this.temperature += this.tempDelta;
            }
        }
        //this.temperature += .05;
        this.updateCount++;
    }
    
    public double getTemperature(){
        return this.temperature;
    }
    
    
    public void setACOn(boolean value){
        this.ACOn = value;
    }
    
    public void setHeaterOn(boolean value){
        this.HeaterOn = value;
    }
}
