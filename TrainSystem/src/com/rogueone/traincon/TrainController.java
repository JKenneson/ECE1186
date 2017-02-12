/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.traincon.gui.TrainControllerGUI;
import com.rogueone.trainmodel.TrainModel; //Should I it this way or how???
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;


/**
 *
 * Class declaration for Train Controller
 * 
 * @author Tyler
 */
public class TrainController {
    
    NumberFormat commaFormatter = NumberFormat.getInstance(Locale.US);
    DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
    
    
    public enum failureModes{
        POWER_FAILURE, ANTENNA_FAILURE, BRAKE_FAILURE
    }
    
    //Variable declaration for the class
    //Train Operations
    private boolean manualMode;
    private boolean leftDoorOpen;
    private boolean rightDoorOpen;
    private boolean lightsOn;
    private boolean airConditioningOn;
    private boolean heaterOn;
    private boolean serviceBrakeActivated;
    private boolean emergencyBrakeActivated;
    private boolean emergencyBrakeOverride;
    
    //Speed and Authority
    private double currSpeed;
    private int speedLimit;
    private int authority;
    //private int distanceTraveled; //Distance traveled since last auth command
    private int driverSetPoint;
    private int recommendedSetPoint;
    private double powerCommand;
    private int kP;
    private int kI;
    private double eK;
    private double eK_1;
    private double uK;
    private double uK_1;
    
    //Announcements
    private String announcement;
    
    //Train Information
    private TrainModel trainModel;
    private String trainID;
    private String line;
    private String section;
    private String block;
    private double maxPower;
    private int passengers;
    private int temperature;
    
    //Failures
    private int failureType;
    private boolean antennaStatus;
    private boolean powerStatus;
    private boolean serviceBrakeStatus;
    
    /**
     * 
     * @param tm
     * @param setPointSpeed
     * @param authority
     * @param maxPow
     * @param trainID
     * @param line
     * @param section
     * @param block 
     */
    public TrainController(TrainModel tm, int setPointSpeed, int authority, double maxPow, String trainID,
           String line, String section, String block){
        
        this.trainModel = tm; //Should come from passed (this) reference
        
        //Initialize Train Controller Object
        this.manualMode = false;
        this.leftDoorOpen = false;
        this.rightDoorOpen = false;
        this.lightsOn = false;
        this.airConditioningOn = false;
        this.heaterOn = false;
        this.serviceBrakeActivated = false;
        this.emergencyBrakeActivated = false;
        this.emergencyBrakeOverride = false;

        //Speed and Authority
        this.currSpeed = 0;
        this.speedLimit = getSpeedLimit();
        this.authority = authority;
        this.driverSetPoint = 0;
        this.recommendedSetPoint = setPointSpeed;
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
        this.maxPower = maxPow;
        this.passengers = updatePassengers();
        updateClimateControl(this.trainModel);

        //Failures
        this.failureType = getFailureType();
        this.antennaStatus = false;
        this.powerStatus = false;
        this.serviceBrakeStatus = false;
    }    
    
    /**
     *
     * @param trainControllerObject
     * @return
     */
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
     * @param gui 
     */
    private void InitializeInputPanel(TrainControllerGUI gui) {
        
        gui.TrainInfoText.append("Train ID: " + this.trainID + "\nLine: " + 
        this.line + "\nSection: " + this.section + "\nBlock: " + this.block + 
        "\nPassengers: " + this.passengers + "\nTemp: " + this.temperature);
        gui.TrainInfoText.setEditable(false);
        
        gui.ActualSpeedLabel.setText(String.valueOf(this.currSpeed));
        gui.SetSpeedLabel.setText(String.valueOf(this.speedLimit));
        gui.AuthorityLabel.setText(String.valueOf(this.authority));
        gui.PowerUsedLabel.setText(String.valueOf(this.powerCommand));
        gui.MaxPowerLabel.setText(String.valueOf(this.maxPower));
        gui.NotificationsDisplay.append(this.announcement);
        gui.NotificationsDisplay.setEditable(false);
        gui.KiInput.setValue(this.kI);
        gui.KpInput.setValue(this.kP);
        gui.ClockText.append(getTime());//Get value from global clock value (EST)
        
        for(int i = 0; i < getNumberOfTrains(); i++){
            //gui.TrainSelectorDropDown.addItem(getTrainArray().get(i));
            //snag train array and add them to the drop down list
        }        
        //Add more functionality in future    
    }
    
    /**
     * 
     * @return 
     */
    private int getSpeedLimit(){ //should pull speed limit information from
        return 0;                //loaded track xlx after calculating location.
    }
    
    private double getSetPoint(){
        if(this.manualMode){
            return this.driverSetPoint;
        }
        else{
            return this.recommendedSetPoint;
        }
    }
    
    /**
     * 
     * @param actualSpeed
     * @param samplePeriod
     * @return 
     */
    public double calculatePower(double actualSpeed, double samplePeriod){ //should pull speed limit information from
                        //loaded track xlx after calculating location.
        
        this.eK = getSetPoint()-actualSpeed;   //Calc error difference
        this.currSpeed = actualSpeed;           //Save actual speed
        
        this.uK = this.uK_1 + ((samplePeriod/2)*(this.eK+this.eK_1));
        
        this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
        if(this.powerCommand > this.maxPower){
            this.uK = this.uK_1;
            this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
        }
        
        this.eK_1 = this.eK;
        this.uK_1 = this.uK;
        
        return this.powerCommand;
    }
    
    public void setKP(int Kp){
        this.kP = Kp;
    }
    
    public void setKI(int Ki){
        this.kI = Ki;
    }
    
    /**
     * 
     * @return 
     */
    private int updatePassengers(){ //should pull passenger information from train model
        return 0; 
    }    
    
    
    
    //*************CAN I IMPORT TRAINMODEL OR HOW DO I DO THIS?************//
    
    /**
     * 
     * @return 
     */
    private void updateClimateControl(TrainModel tm){ //should pull temp information from train model
        this.temperature = tm.getTemperature();
    }
    
    //*************CAN I IMPORT TRAINMODEL OR HOW DO I DO THIS?************//
    
    private String getTime(){
        return "4:20:00 PM April 20, 420!"; //Get value from global time class
    }
    
    private int getNumberOfTrains(){
        return 0; //Get value from ?????
    }
    
    private ArrayList getTrainArray(){
        return null;
    }
            
    private int getFailureType(){  //get from train model
        return 0;                        //Set values for different
    }                                       //Failure combos?
    
    private void updateValues(){
        
    }
        
    private void updateGUI(TrainControllerGUI gui){
    
        for(int i = 0; i < getNumberOfTrains(); i++){
            //gui.TrainSelectorDropDown.addItem(getTrainArray().get(i));
            //snag train array and add them to the drop down list
        }
        
        if(this.leftDoorOpen) {
            gui.LeftDoorOpened.setSelected(true);
        }
        else {
            gui.LeftDoorClosed.setSelected(true);
        }
        
        if(this.rightDoorOpen) {
            gui.RightDoorOpened.setSelected(true);
        }
        else {
            gui.RightDoorClosed.setSelected(true);
        }
        
        if(this.lightsOn) {
            gui.LightsOn.setSelected(true);
        }
        else {
            gui.LightsOff.setSelected(true);
        }

        if(this.airConditioningOn){
            gui.ACOn.setSelected(true);
            gui.ACOff.setSelected(false);
            gui.HeatOff.setSelected(true);
            gui.HeatOn.setSelected(false);
        }
        else if(this.heaterOn){
            gui.ACOn.setSelected(false);
            gui.ACOff.setSelected(true);
            gui.HeatOff.setSelected(false);
            gui.HeatOn.setSelected(true);
        }
        else{
            gui.ACOn.setSelected(false);
            gui.ACOff.setSelected(true);
            gui.HeatOff.setSelected(true);
            gui.HeatOn.setSelected(false);
        }
        
        gui.TrainInfoText.setText(null);
        gui.TrainInfoText.append("Train ID: " + this.trainID + "\nLine: " + 
        this.line + "\nSection: " + this.section + "\nBlock: " + this.block + 
        "\nPassengers: " + this.passengers + "\nTemp: " + this.temperature);
        
        if(!this.emergencyBrakeOverride || !this.emergencyBrakeActivated){      //Default to always print emergency brake if both emergency and service are activated
            gui.EmergencyBrakeToggleButton.setSelected(true);

            switch(getFailureType()){
                case 1://Power Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.PowerFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    
                case 2://Antenna Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    
                case 3://Brake Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                case 4://Power and Antenna Failure
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    
                case 5://Power and Brake Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                case 6://Antenna and Brake Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                case 7://Power, Antenna, and Brake Failure
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                default:
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("TrainSystem/src/com/rougeone/images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.PowerFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    //must have been sent by passenger or driver
                }
            }

        if(!this.serviceBrakeActivated) {
            gui.ServiceBrakeToggleButton.setSelected(true);
        }
        else {
            gui.ServiceBrakeToggleButton.setSelected(false);
        }        
        
        //Speed and Authority
        gui.ActualSpeedLabel.setText(decimalFormatter.format(this.currSpeed));
        gui.SetSpeedLabel.setText(Integer.toString(this.speedLimit));
        
        if(this.manualMode){
            gui.ManualModeSelect.setSelected(true);
            gui.AutoModeSelect.setSelected(false);
            gui.SetSpeedLabel.setText(Integer.toString(this.driverSetPoint));
        }
        else{
            gui.ManualModeSelect.setSelected(false);
            gui.AutoModeSelect.setSelected(true);
            gui.SetSpeedLabel.setText(Integer.toString(this.recommendedSetPoint));
        }
        
        gui.AuthorityLabel.setText(Integer.toString(getRemainingAuthority()));
        gui.SpeedLimitLabel.setText(Integer.toString(this.speedLimit));
        gui.MaxPowerLabel.setText(decimalFormatter.format(this.maxPower));
        gui.PowerUsedLabel.setText(decimalFormatter.format(this.powerCommand));
        
        gui.ClockText.setText(getTime());
        
        //Will add more as we move forward.
    }
    
    public void activateServiceBrake(){
        //trainMode.setServiceBrakeActivated(true);
        this.serviceBrakeActivated = true;
    }
    
    public void activateEmergencyBrake(){
        //trainMode.setEmergencyBrakeActivated(true);
        this.emergencyBrakeActivated = true;
    }
    
    private int getRemainingAuthority(){
        return this.authority - this.getDistanceTraveled();
    }
    
    private int getDistanceTraveled(){
        return 0;//Calculate the distance traveled using speed and time
    }        
}
