/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

/**
 *
 * @author Tyler
 */
public class PowerSystems implements Updateable{
    
    private TrainController trainController;
    
    private boolean leftDoorOpen;
    private boolean rightDoorOpen;
    private boolean lightsOn;
    private boolean airConditioningOn;
    private boolean heaterOn;
    private int temperature;
    
    public PowerSystems(TrainController tc){
        
        this.trainController = tc;
        this.leftDoorOpen = this.trainController.trainModel.isLeftDoorOpen();
        this.rightDoorOpen = this.trainController.trainModel.isRightDoorOpen();
        this.lightsOn = this.trainController.trainModel.isLightsOn();
        
        this.temperature = this.trainController.trainModel.getTemperature();
        
        if(this.temperature>72){
            this.airConditioningOn = true;
            this.heaterOn = false;
        }
        else if(this.temperature<39){
            this.airConditioningOn = false;
            this.heaterOn = true;
        }
        else{
            this.airConditioningOn = false;
            this.heaterOn = false;            
        }
    }
    
    /**
     * This method pulls the internal temperature from the train model and decides 
     * how to adjust the climate control systems
     * 
     * @author Tyler Protivnak
     */
    public void update(){ 
        this.temperature = this.trainController.trainModel.getTemperature();
        
        if(this.temperature>72){
            this.airConditioningOn = true;
            this.heaterOn = false;
        }
        else if(this.temperature<39){
            this.airConditioningOn = false;
            this.heaterOn = true;
        }
        else{
            this.airConditioningOn = false;
            this.heaterOn = false;            
        }
    }
    
    /**
     * 
     * @param airConditioningOn Boolean to set the status of the A/C. True = on.
     */
    public void setAirConditioningOn(boolean airConditioningOn) {
        this.airConditioningOn = airConditioningOn;
    }

    /**
     * 
     * @param heaterOn Boolean to set the status of the heater. True = on.
     */
    public void setHeaterOn(boolean heaterOn) {
        this.heaterOn = heaterOn;
    }
    
    /**
     * 
     * @param lightsOn Boolean to set the status of the lights. True = on. Also updates train model.
     */
    public void setLightsOn(boolean lightsOn) {
        this.lightsOn = lightsOn;
        this.trainController.trainModel.setLightsOn(lightsOn);
    }
    
    /**
     * 
     * @param leftDoorOpen Boolean to set the status of the left door. True = open. Also updates train model.
     */
    public void setLeftDoorOpen(boolean leftDoorOpen) {
        this.leftDoorOpen = leftDoorOpen;
        this.trainController.trainModel.setLeftDoorOpen(leftDoorOpen);
    }
    
    /**
     * 
     * @param rightDoorOpen Boolean to set the status of the right door. True = open. Also updates train model.
     */
    public void setRightDoorOpen(boolean rightDoorOpen) {
        this.rightDoorOpen = rightDoorOpen;
        this.trainController.trainModel.setRightDoorOpen(rightDoorOpen);
    }

    /**
     * 
     * @return 
     */
    public boolean isLeftDoorOpen() {
        return leftDoorOpen;
    }

    /**
     * 
     * @return 
     */
    public boolean isRightDoorOpen() {
        return rightDoorOpen;
    }

    /**
     * 
     * @return 
     */
    public boolean isLightsOn() {
        return lightsOn;
    }

    /**
     * 
     * @return 
     */
    public boolean isAirConditioningOn() {
        return airConditioningOn;
    }

    /**
     * 
     * @return 
     */
    public boolean isHeaterOn() {
        return heaterOn;
    }

    /**
     * 
     * @return 
     */
    public int getTemperature() {
        return temperature;
    }
    
    
}
