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
    private int maxPower;
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
        this.maxPower = getMaxPower();
        this.passengers = updatePassengers();
        this.temperature = updateTemp();

        //Failures
        this.antennaStatus = false;
        this.powerStatus = false;
        this.serviceBrakeStatus = false;
    }    
    
    public TrainControllerGUI CreateGUIObject(TrainController trainControllerObject){
        //Create GUI object
        TrainControllerGUI trainControllerGUI = new TrainControllerGUI(trainControllerObject);
    
        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trainControllerGUI);
        frame.pack();
        frame.setVisible(true);     //Make sure to set it visible
        
        //Initialize the GUI
        trainControllerObject.InitializeInputPanel(trainControllerGUI);
        
        return  trainControllerGUI;  //Return the GUI object
        
    }
    
    /**
     * 
     * @return 
     */
    private int getSpeedLimit(){ //should pull speed limit information from
        return 0;                //loaded track xlx after calculating location.
    }
    
    /**
     * 
     * @return 
     */
    private double calculatePower(){ //should pull speed limit information from
        return 0.0;                //loaded track xlx after calculating location.
    }
    
    /**
     * 
     * @return 
     */
    private int updatePassengers(){ //should pull passenger information from train model
        return 0; 
    }    
    
    /**
     * 
     * @return 
     */
    private int updateTemp(){ //should pull temp information from train model
        return 0; 
    }
    
    /**
     * 
     * @return 
     */
    private int getMaxPower() { //should pull temp information from train model
        return 0;
    }
    
    /**
     * 
     * @param gui 
     */
    private void InitializeInputPanel(TrainControllerGUI gui) {
        
        gui.TrainInfoText.append("Train ID: " + this.trainID + "\nLine: " + 
        this.line + "\nSection: " + this.section + "\nBlock: " + this.block + 
        "\nPassengers: " + this.passengers + "\nTemp: " + this.temperature);
        gui.TrainInfoText.setEditable(false);
        
        gui.ActualSpeedLabel.setText(String.valueOf(this.currSpeed));
        gui.SpeedLimitLabel.setText(String.valueOf(this.speedLimit));
        gui.AuthorityLabel.setText(String.valueOf(this.authority));
        gui.PowerUsedLabel.setText(String.valueOf(this.powerCommand));
        gui.MaxPowerLabel.setText(String.valueOf(this.maxPower));
        gui.NotificationsDisplay.append(this.announcement);
        gui.NotificationsDisplay.setEditable(false);
        gui.KiInput.setValue(this.kI);
        gui.KpInput.setValue(this.kP);
        
        
        
    }
        
}
