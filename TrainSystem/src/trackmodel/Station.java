/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackmodel;

/**
 *
 * @author Dan
 */
public class Station {
    
    private String name;
    private boolean rightSide;
    private int temperature;
    private boolean heaterOn;
    private int waitingPassengers;
    
    public Station(String tempName, boolean tempRightSide) {
        name = tempName;
        rightSide = tempRightSide;
        //Mock passenger and temperature data for now, until the random simulators are finished.
        waitingPassengers = 0;
        temperature = -1;
        heaterOn = false;
    }
    
    //NOTE: I don't think we'll need all these methods, but they're here for now for early prototyping purposes.
 
    public void setHeater(boolean on)
    {
        if (on) {
            heaterOn = true;
        }    
        else {
            heaterOn = false;
        }    
    }
    
    public boolean isHeaterOn() {
        return heaterOn;
    }
    
    public void setTemperature(int newTemperature) {
        temperature = newTemperature;
    }
    
    public int getTemperature() {
        return temperature;
    }
    
    public void setRightSide(boolean isOnRightSide) {
        rightSide = isOnRightSide;
    }
    
    public boolean isOnRightSide() {
        return rightSide;
    }
    
    public void setName(String newName) {
        name = newName;
    }
    
    public String getName() {
        return name;
    }
    
    public void queuePassengers(int newPassengers) {
        waitingPassengers =+ newPassengers;
    }
    
    public int boardPassengers(int boardingPassengers) {
        waitingPassengers =- boardingPassengers;
        return waitingPassengers;
        //No support for negative passengers YET
    }
    
    
}