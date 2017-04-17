/**
 * The power systems control the electrical systems for doors, lights and climate control
 *
 * @author Tyler Protivnak
 * @Creation 3/4/17
 * @Modification 4/17/2017
 */
package com.rogueone.traincon;

/**
 * Class declaration for PowerSystem
 * 
 * @author Tyler Protivnak
 */
public class PowerSystems{
    
    private final TrainController trainController;
    private boolean leftDoorOpen;
    private boolean rightDoorOpen;
    private boolean lightsOn;
    private boolean airConditioningOn;
    private boolean heaterOn;
    private double temperature;
    
    /**
     * Constructor for Power Systems 
     * 
     * @author Tyler Protivnak 
     * @param tc reference to the train controller for a train model reference
     */
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
        else if(this.temperature<72){
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
     * @param lights boolean for whether or not the lights should be on
     * @param manualMode the current operation mode of the train controller
     */
    public void update(boolean lights, boolean manualMode){ 
        this.temperature = this.trainController.trainModel.getTemperature();
        
        if(!manualMode){
            if(this.temperature>72){
                this.setAirConditioningOn(true);
                this.setHeaterOn(false);
            }
            else if(this.temperature<72){
                this.setAirConditioningOn(false);
                this.setHeaterOn(true);
            }
            else{
                this.setAirConditioningOn(false);
                this.setHeaterOn(false);         
            }

            this.setLightsOn(lights);
        }
        
        else{
            if(this.airConditioningOn && this.heaterOn){
                this.trainController.gui.appendNotificationsDisplay("Only one climate control system should be active\n");
            }
            else if(this.temperature>72 && !this.airConditioningOn){
                this.trainController.gui.appendNotificationsDisplay("Only AC should be on\n");
            }
            else if(this.temperature<72 && !this.heaterOn){
                this.trainController.gui.appendNotificationsDisplay("Only Heater should be on\n");
            }

            if(lights != this.lightsOn){
                if(lights){
                    this.trainController.gui.appendNotificationsDisplay("Lights should be on\n");
                }
                else{
                    this.trainController.gui.appendNotificationsDisplay("Lights should be off\n");
                }
            }
        }
    }
    
    /**
     * Set the air conditioner on or off
     * 
     * @author Tyler Protivnak
     * @param airConditioningOn Boolean to set the status of the A/C. True = on.
     */
    public void setAirConditioningOn(boolean airConditioningOn) {
        this.airConditioningOn = airConditioningOn;
        this.trainController.trainModel.setACOn(airConditioningOn);
    }

    /**
     * set the heater on or off
     * 
     * @author Tyler Protivnak
     * @param heaterOn Boolean to set the status of the heater. True = on.
     */
    public void setHeaterOn(boolean heaterOn) {
        this.heaterOn = heaterOn;
        this.trainController.trainModel.setHeaterOn(heaterOn);
    }
    
    /**
     * turn the lights on or off
     * 
     * @author Tyler Protivnak
     * @param lightsOn Boolean to set the status of the lights. True = on. Also updates train model.
     */
    public void setLightsOn(boolean lightsOn) {
        this.lightsOn = lightsOn;
        this.trainController.trainModel.setLightsOn(lightsOn);
    }
    
    /**
     * open or close the left door
     * 
     * @author Tyler Protivnak
     * @param leftDoorOpen Boolean to set the status of the left door. True = open. Also updates train model.
     */
    public void setLeftDoorOpen(boolean leftDoorOpen) {
        this.leftDoorOpen = leftDoorOpen;
        this.trainController.trainModel.setLeftDoorOpen(leftDoorOpen);
    }
    
    /**
     * open or close the right door
     * 
     * @author Tyler Protivnak
     * @param rightDoorOpen Boolean to set the status of the right door. True = open. Also updates train model.
     */
    public void setRightDoorOpen(boolean rightDoorOpen) {
        this.rightDoorOpen = rightDoorOpen;
        this.trainController.trainModel.setRightDoorOpen(rightDoorOpen);
    }

    /**
     * is the left door open or closed?
     * 
     * @author Tyler Protivnak
     * @return true for open or false for closed
     */
    public boolean isLeftDoorOpen() {
        return leftDoorOpen;
    }

    /**
     * is the right door open or closed?
     * 
     * @author Tyler Protivnak
     * @return true for open or false for closed
     */
    public boolean isRightDoorOpen() {
        return rightDoorOpen;
    }

    /**
     * Are the lights on or off?
     * 
     * @author Tyler Protivnak
     * @return true for on or false for off
     */
    public boolean isLightsOn() {
        return lightsOn;
    }

    /**
     * is the air conditioning on or off?
     * 
     * @author Tyler Protivnak
     * @return true for on or false for off
     */
    public boolean isAirConditioningOn() {
        return airConditioningOn;
    }

    /**
     * is the heater on or off?
     * 
     * @author Tyler Protivnak
     * @return true for on or false for off
     */
    public boolean isHeaterOn() {
        return heaterOn;
    }

    /**
     * what is the cabin temperature?
     * 
     * @author Tyler Protivnak
     * @return temperature of the cabin in degrees F
     */
    public double getTemperature() {
        return temperature;
    }   
}