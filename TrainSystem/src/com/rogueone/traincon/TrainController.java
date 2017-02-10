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
    private boolean manualMode;
    private boolean leftDoorOpen;
    private boolean rightDoorOpen;
    private boolean lightsOn;
    private boolean airConditioningOn;
    private boolean headerOn;
    private boolean serviceBrakeActivated;
    private boolean emergencyBrakeActivated;
    
    //Speed and Authority
    private int currSpeed;
    private int speedLimit;
    private int authority;
    private int driverSetPoint;
    private int recommendedSetPoint;
    private double powerCommand;
    private double kP;
    private double kI;
    private int eK;
    private int eK_1;
    private int uK;
    private int uK_1;
    
    
    //Announcements
    private String announcement;
    
    //Train Information
    private String trainID;
    private String line;
    private String section;
    private String block;
    private int passengers;
    private int temperature;
    
    //Failures
    private boolean antennaStatus;
    private boolean powerStatus;
    private boolean serviceBrakeStatus;
    
    /**
     * 
     * @param setPointSpeed
     * @param authority
     * @param trainID
     * @param line
     * @param section
     * @param block 
     */
    public TrainController(int setPointSpeed, int authority, String trainID,
           String line, String section, String block){
        //Initialize Train Controller Object
        this.manualMode = false;
        this.leftDoorOpen = false;
        this.rightDoorOpen = false;
        this.lightsOn = false;
        this.airConditioningOn = false;
        this.headerOn = false;
        this.serviceBrakeActivated = false;
        this.emergencyBrakeActivated = false;

        //Speed and Authority
        this.currSpeed = 0;
        this.speedLimit = getSpeedLimit();
        this.authority = authority;
        this.driverSetPoint = 0;
        this.recommendedSetPoint = setPointSpeed;
        this.powerCommand = calculatePower();
        this.kP = 0;
        this.kI = 0;
        this.eK = 0;
        this.eK_1 = 0;
        this.uK = 0;
        this.uK_1 = 0;


        //Announcements
        this.announcement = "Departing Yard";

        //Train Information
        this.trainID = trainID;
        this.line = line;
        this.section = section;
        this.block = block;
        this.passengers = updatePassengers();
        this.temperature = updateTemp();

        //Failures
        this.antennaStatus = false;
        this.powerStatus = false;
        this.serviceBrakeStatus = false;
    }    
    
    private int getSpeedLimit(){ //should pull speed limit information from
        return 0;                //loaded track xlx after calculating location.
    }
    
    private double calculatePower(){ //should pull speed limit information from
        return 0.0;                //loaded track xlx after calculating location.
    }
    
    private int updatePassengers(){ //should pull passenger information from train model
        return 0; 
    }    
    private int updateTemp(){ //should pull temp information from train model
        return 0; 
    }
}
