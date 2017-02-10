/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.traincon.gui.TrainControllerGUI;
import javax.swing.*;


/**
 *
 * Class declaration for Train Controller
 * 
 * @author Tyler
 */
public class TrainController {
    
    //Variable declaration for the class
    //Train Operations
    private boolean leftDoorOpen;
    private boolean rightDoorOpen;
    private boolean lightsOn;
    private int temperature;
    private boolean serviceBrakeActivated;
    private boolean emergencyBrakeActivated;
    //Speed and Authority
    private int currSpeed;
    private int speedLimit;
    private int driverSetPoint;
    private int ctcSetPoint;
    private int authority;
    private double powerReceived;
    //Station and Passengers
    private String approachingStation;
    private int passengersOnBaord;
    private int passengersDisembarking;
    private int passengersEmbarking;
    private int passengerMaxCapacity;
    //Physical Characteristics
    private int trainWeight;
    private int trainLength;
    private int numCars;
    private boolean trackAntennaActivated;
    private boolean mboAntennaActivated;
    
    public TrainController(){
        
    }    
}
