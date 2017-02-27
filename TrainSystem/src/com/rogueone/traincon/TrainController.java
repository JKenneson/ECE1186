/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.global.Clock;
import com.rogueone.traincon.gui.TrainControllerGUI;
import com.rogueone.trainmodel.TrainModel; //Should I it this way or how???
import com.rogueone.trainmodel.entities.TrainFailures;
import com.rogueone.trainsystem.TrainSystem;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;


/**
 *
 * Class declaration for Train Controller
 * 
 * @author Tyler Protivnak
 */
public class TrainController {
    
    NumberFormat commaFormatter = NumberFormat.getInstance(Locale.US);
    DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
    
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    
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
    
    //private int distanceTraveled; //Distance traveled since last auth command
    private byte driverSetPoint;
    private double authority;
    private byte recommendedSetPoint;
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
    public TrainControllerGUI gui;
    public TrainSystem ts;
    private String trainID;
    private String line;
    private String section;
    private String block;
    private double maxPower;
    private int passengers;
    private int temperature;
    
    //Failures
    private boolean antennaStatus;
    private boolean powerStatus;
    private boolean serviceBrakeStatus;
        
    /**
     * This method is the constructor that should be used to make a new Train controller.
     * This should only be called when a new train model is created and thus, only called
     * by the train model. If a gui is not needed for the controller, pass "null" for the 
     * gui.
     * 
     * @author Tyler Protivnak 
     * @param tm should be a "this" reference to the Train model using this controller
     * @param gui trainController GUI object if it exists, else pass null
     * @param setPointSpeed suggested Speed passed through track
     * @param authority given authority passed through track
     * @param maxPow maximum power allowed by the train
     * @param trainID train ID from CTC
     * @param line line ID from CTC
     * @param section initial section, should be from yard
     * @param block initial block, should be from yard
     */
    public TrainController(TrainModel tm, TrainControllerGUI gui, byte setPointSpeed, short authority, double maxPow, String trainID,
           String line, String section, String block, TrainSystem ts){
        
        this.ts = ts;
        this.trainModel = tm; //Should come from passed (this) reference
        this.gui = gui;
        
        //Initialize Train Controller Object
        this.manualMode = false;
        this.leftDoorOpen = this.trainModel.isLeftDoorOpen();
        this.rightDoorOpen = this.trainModel.isRightDoorOpen();
        this.lightsOn = this.trainModel.isLightsOn();
        
        this.temperature = this.trainModel.getTemperature();
        
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
        
        this.serviceBrakeActivated = false;
        this.emergencyBrakeActivated = false;
        this.emergencyBrakeOverride = false;

        //Speed and Authority
        this.currSpeed = this.trainModel.getCurrSpeed();
        this.speedLimit = getSpeedLimit();
        this.authority = (double)authority*this.FEET_IN_A_METER;
        this.driverSetPoint = setPointSpeed;
        this.recommendedSetPoint = setPointSpeed;
        this.kP = 100; //Seem to +=6 .5; +=12 1; +=17 1.5; +=19 2.0 assuming no passengers
        this.kI = 2;
        this.eK = 0;
        this.eK_1 = 0;
        this.uK = 0;
        this.uK_1 = 0;

        //Announcements
        this.announcement = this.trainID + "Departing Yard";
        //System.out.println(this.announcement);

        //Train Information
        this.trainID = trainID;
        this.line = line;
        this.section = section;
        this.block = block;
        this.maxPower = maxPow;
//        this.passengers = updatePassengers();
  //      updateClimateControl(this.trainModel);

        //Failures
        this.antennaStatus = true;
        this.powerStatus = true;
        this.serviceBrakeStatus = true;
        
        if(gui != null){
            this.updateGUI(gui);
        }
        
    }    
    
    
    /**
     * This method makes the train controller gui and initializes it's view for
     * the user. It also sets the gui object for the train controller.
     *      * 
     * @author Tyler Protivnak
     * @param trainControllerObject 
     * @return the created train controller object
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
        
        this.gui = trainControllerGUI;  //Return the GUI object
        return trainControllerGUI;  //Return the GUI object
    }
    
    /**
     * This method initializes the gui's visual attributes
     * 
     * @author Tyler Protivnak
     * @param gui the TrainControllerGUI that will be initialized
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
        gui.SpeedInput.setValue(this.driverSetPoint);
        gui.KiInput.setValue(this.kI);
        gui.KpInput.setValue(this.kP);
        gui.ClockText.append(this.ts.getClock().printClock());//Get value from global clock value (EST)
        
        for(int i = 0; i < getNumberOfTrains(); i++){
            //gui.TrainSelectorDropDown.addItem(getTrainArray().get(i));
            //snag train array and add them to the drop down list
        }        
        //Add more functionality in future    
    }
        
    /**
     * This method is called from the TrainControllerGUI when a failure is activated and sent through
     * the failure simulation radio buttons. This method will flip the brakes on the 
     * train controller and model.
     * 
     * @author Tyler Protivnak
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void causeFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch(failure) {
            //A power failure will prevent the doors, lights, and temp setting from working.  Activate emergency brake
            case Power:
                this.powerStatus = false;               //In any setter methods below, the value will not be updated if there is a power failure
                this.emergencyBrakeOverride = true;
                this.trainModel.causeFailure(TrainFailures.Power);
                break;
            //A brake failure will prevent the service brake from being activated. Activate the emergency brake
            case Brake:
                this.serviceBrakeStatus = false;
                this.emergencyBrakeOverride = true;
                this.trainModel.causeFailure(TrainFailures.Brake);
                break;
            //Deactivate the track and mbo antenna.  Activate emergency brake
            case Antenna:
                this.antennaStatus = false;
                this.emergencyBrakeOverride = true;
                this.trainModel.causeFailure(TrainFailures.Antenna);
                break;
        }
    }
    
    /**
     * This method is called from the TrainControllerGUI when a failure is deactivated
     * and sent through the failure simulation radio buttons. This method will flip the brakes on the 
     * train controller and model.
     * 
     * @author Tyler Protivnak
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void fixFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch(failure) {
            //Undo the power failure
            case Power:
                this.powerStatus = true;
                this.trainModel.fixFailure(TrainFailures.Power);
                break;
            //Undo the brake failure
            case Brake:
                this.serviceBrakeStatus = true;
                this.trainModel.fixFailure(TrainFailures.Brake);
                break;
            //Undo the antenna failure
            case Antenna:
                this.antennaStatus = true;
                this.trainModel.fixFailure(TrainFailures.Antenna);
                break;
        }
        //If there are no failures, de-activate the emergencyBrakeOverride
        if(this.powerStatus && this.serviceBrakeStatus && this.antennaStatus) {
            this.emergencyBrakeOverride = false;
        }
    }
    
    /**
     * This function should be called by the train model to find out the next power
     * command for the engine.
     * 
     * @author Tyler Protivnak
     * @param actualSpeed The current speed from the Train Model
     * @param samplePeriod The sampling period defined by the Train Model
     * @return power after calculations
     */
    public double calculatePower(double actualSpeed, double samplePeriod){ //should pull speed limit information from
                        //loaded track xlx after calculating location.
                        
        if(this.serviceBrakeActivated || this.emergencyBrakeActivated || this.trainModel.getGrade()<0 || this.getSetPoint()==0.0){
            this.currSpeed = actualSpeed;
            this.powerCommand = 0.0;
            return 0.0;
        }
        
        this.eK = getSetPoint()-actualSpeed;   //Calc error difference
        this.currSpeed = actualSpeed;           //Save actual speed
        
        this.uK = this.uK_1 + ((samplePeriod/2)*(this.eK+this.eK_1));
        
        //System.out.println("Setpt = " + this.getSetPoint());
        //System.out.println("eK = " + this.eK);
        //System.out.println("kP*eK = " + this.kP*this.eK);
        //System.out.println("kI*uK = " + this.kI*this.uK);
        this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
        if(this.powerCommand > this.maxPower){
            this.uK = this.uK_1;
            this.powerCommand = (this.kP*this.eK) + (this.kI*this.uK);
            this.powerCommand = this.maxPower;
            
        }
        
        this.eK_1 = this.eK;
        this.uK_1 = this.uK;
        if(this.powerCommand<0){
            this.powerCommand = 0;
        }
        return this.powerCommand;
    }
    
    /**
     * Sets the Kp as passed by the train controller gui
     * 
     * @author Tyler Protivnak
     * @param Kp the new kp from the engineer
     */
    public void setKP(int Kp){
        this.kP = Kp;
    }
    
    /**
     * Sets the Ki as passed by the train controller gui
     * 
     * @author Tyler Protivnak
     * @param Ki 
     */
    public void setKI(int Ki){
        this.kI = Ki;
    }
        
    /**
     * This method pulls the number of passengers from the train model and sets
     * the train controllers passengers variable to the most updated value.
     * 
     * @author Tyler Protivnak
     * @return the number of passengers on train
     */
    private void updatePassengers(){ //should pull passenger information from train model
        this.passengers = this.trainModel.getPassengersOnBaord();
    }    
    
    /**
     * This method pulls the internal temperature from the train model and decides 
     * how to adjust the climate control systems
     * 
     * @author Tyler Protivnak
     */
    private void updateClimateControl(){ //should pull temp information from train model
        this.temperature = this.trainModel.getTemperature();
        
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
     * This function pulls the time and date of the system from a global class
     * 
     * @author Tyler Protivnak
     * @return the current date and time of the system
     */
    private String getTime(){
        return "7:00:00 PM April 20, 2017"; //Get value from global time class
    }
    
    
    private int getNumberOfTrains(){
        return 0; //Get value from ?????
    }
    
    private ArrayList getTrainArray(){
        return null;
    }
            
    
    /**
     * This function figures out how to set the gui visual aids for the failures
     * on the train controller
     * 
     * @author Tyler Protivnak
     * @return value cooresponding to failure type
     */
    private int getFailureType(){  //get from train model
        if(!this.powerStatus){
            if(!this.antennaStatus){
                if(!this.serviceBrakeStatus){
                    return 7; //All 3 are failed
                }
                return 4; //Power and antenna are out
            }
            else if(!this.serviceBrakeStatus){
                return 5;//Power and service brake are out
            }
            return 1; //Only power has failed
        }
        else if(!this.antennaStatus){
            if(!this.serviceBrakeStatus){
                return 6;
            }
            return 2;
        }
        else if(!this.serviceBrakeStatus){
            return 3;
        }
        return 0; //default, all clear
    }                                       
    
    /**
     * The update function for the full controller
     * 
     * @author Tyler Protivnak
     */
    public void updateController(){
        this.authority -= this.trainModel.getDistanceTraveled();
        if(!this.manualMode)
            this.updateClimateControl();
        this.updatePassengers();
        if(this.authority<0)
            this.setEmergencyBrakeActivated(true);
        
        updateSafeSpeed();
        
        
    }
    
    
    private void updateSafeSpeed(){
        if(this.currSpeed>this.getSetPoint() && !this.manualMode){
            this.setServiceBrakeActivated(true);
        }
        else{
            this.setServiceBrakeActivated(false);
        }
    }
        
    public void updateGUI(TrainControllerGUI gui){
        
        this.updateController();
        
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
        
        if(this.airConditioningOn && this.heaterOn){
            //System.out.println("Both climate control systems activated at the same time, disabling both.");
            this.airConditioningOn = false;
            this.heaterOn = false;
            gui.ACOff.setSelected(true);
            gui.HeatOff.setSelected(true);
        }
        else if(this.airConditioningOn){
            gui.ACOn.setSelected(true);
            gui.HeatOff.setSelected(true);
        }
        else if(this.heaterOn){
            gui.ACOff.setSelected(true);
            gui.HeatOn.setSelected(true);
        }
        else{
            gui.ACOff.setSelected(true);
            gui.HeatOff.setSelected(true);
        }
               
        gui.TrainInfoText.setText(null);
        gui.TrainInfoText.append("Train ID: " + this.trainID + "\nLine: " + 
        this.line + "\nSection: " + this.section + "\nBlock: " + this.block + 
        "\nPassengers: " + this.passengers + "\nTemp: " + this.temperature + " F");
        
        if(this.emergencyBrakeOverride || this.emergencyBrakeActivated){      //Default to always print emergency brake if both emergency and service are activated
            
            gui.EmergencyBrakeToggleButton.setSelected(true);

            //System.out.println(getFailureType());
            switch(getFailureType()){
                
                case 1://Power Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.PowerFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    
                case 2://Antenna Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    
                case 3://Brake Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                case 4://Power and Antenna Failure
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    
                case 5://Power and Brake Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                case 6://Antenna and Brake Failure
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                case 7://Power, Antenna, and Brake Failure
                    gui.StatusPowerLabel.setText("FAILURE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusAntennaLabel.setText("FAILURE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    gui.StatusBrakeLabel.setText("FAILURE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/SQUARE_98.png")));
                    
                    //Update simulation boxes
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.AntennaFailureCheck.setSelected(true);
                    gui.ServiceBrakeFailureCheck.setSelected(true);
                    break;
                    
                default:
                    //Update status panel
                    gui.StatusPowerLabel.setText("ACTIVE");
                    gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusAntennaLabel.setText("ACTIVE");
                    gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    gui.StatusBrakeLabel.setText("ACTIVE");
                    gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
                    
                    //Update simulation boxes
                    gui.PowerFailureCheck.setSelected(false);
                    gui.AntennaFailureCheck.setSelected(false);
                    gui.ServiceBrakeFailureCheck.setSelected(false);
                    break;
                    //must have been sent by passenger or driver
            }
        }
        else{
            gui.EmergencyBrakeToggleButton.setSelected(false);
            //Update status panel
            gui.StatusPowerLabel.setText("ACTIVE");
            gui.StatusPowerImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
            gui.StatusAntennaLabel.setText("ACTIVE");
            gui.StatusAntennaImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
            gui.StatusBrakeLabel.setText("ACTIVE");
            gui.StatusBrakeImage.setIcon(new ImageIcon(getClass().getResource("../images/CIRC_98.png")));
        }

        if(this.serviceBrakeActivated) {
            gui.ServiceBrakeToggleButton.setSelected(true);
        }
        else{
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
        
        gui.AuthorityLabel.setText(this.decimalFormatter.format(this.authority));
        gui.SpeedLimitLabel.setText(Integer.toString(this.speedLimit));
        gui.MaxPowerLabel.setText(decimalFormatter.format(this.maxPower));
        gui.PowerUsedLabel.setText(decimalFormatter.format(this.powerCommand));
        
        //gui.ClockText.setText(Clock.printClock());
        
        //Will add more as we move forward.
    }
    
    
    public void activateServiceBrake(){
        this.trainModel.setServiceBrakeActivated(true);
        this.serviceBrakeActivated = true;
    }
    
    public void activateEmergencyBrake(){
        this.trainModel.setEmergencyBrakeActivated(true);
        this.emergencyBrakeActivated = true;
    }
    
    private double getRemainingAuthority(){
        return this.authority - this.getDistanceTraveled();
    }
    
    private int getDistanceTraveled(){
        return 0;//Calculate the distance traveled using speed and time
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
     * @param manualMode Boolean to set mode. Pass "True" for manual and "False" for automatic
     */
    public void setManualMode(boolean manualMode) {
        this.manualMode = manualMode;
    }
    
    /**
     * 
     * @param lightsOn Boolean to set the status of the lights. True = on. Also updates train model.
     */
    public void setLightsOn(boolean lightsOn) {
        this.lightsOn = lightsOn;
        this.trainModel.setLightsOn(lightsOn);
    }
    
    
    /**
     * 
     * @param leftDoorOpen Boolean to set the status of the left door. True = open. Also updates train model.
     */
    public void setLeftDoorOpen(boolean leftDoorOpen) {
        this.leftDoorOpen = leftDoorOpen;
        this.trainModel.setLeftDoorOpen(leftDoorOpen);
    }

    /**
     * 
     * @param rightDoorOpen Boolean to set the status of the right door. True = open. Also updates train model.
     */
    public void setRightDoorOpen(boolean rightDoorOpen) {
        this.rightDoorOpen = rightDoorOpen;
        this.trainModel.setRightDoorOpen(rightDoorOpen);
    }

    /**
     * 
     * @param serviceBrakeActivated Boolean to set the status of the service brake. True = on. Also updates train model.
     */
    public void setServiceBrakeActivated(boolean serviceBrakeActivated) {
        this.serviceBrakeActivated = serviceBrakeActivated;
        this.trainModel.setServiceBrakeActivated(serviceBrakeActivated);
    }

    /**
     * 
     * @param emergencyBrakeActivated Boolean to set the status of the e brake. True = on. Also updates train model.
     */
    public void setEmergencyBrakeActivated(boolean emergencyBrakeActivated) {
        this.emergencyBrakeActivated = emergencyBrakeActivated;
        this.trainModel.setEmergencyBrakeActivated(emergencyBrakeActivated);
    }

    /**
     * 
     * @param driverSetPoint Sets the set point of the driver
     */
    public void setDriverSetPoint(byte driverSetPoint) {
        this.driverSetPoint = driverSetPoint;
        this.trainModel.setDriverSetPoint(driverSetPoint);
    }
    
    /**
     * 
     * @return Reference to the controllers train model
     */
    public TrainModel getTrainModel() {
        return this.trainModel;
    }

    /**
     * 
     * @return The current speed of the train
     */
    public double getCurrSpeed() {
        return currSpeed;
    } 
    
    /**
     * 
     * @param authority Sets a new authority for the train control to follow 
     */
    public void setAuthority(short authority) {
        this.authority = (double)authority*this.FEET_IN_A_METER;
        //System.out.println(this.authority);
    }

    /**
     * 
     * @param recommendedSetPoint Set point sent by ctc or mbo
     */
    public void setRecommendedSetPoint(byte recommendedSetPoint) {
        this.recommendedSetPoint = recommendedSetPoint;
    }
    
    /**
     * 
     * @return the setpoint that the driver had set
     */
    public byte getDriverSetPoint() {
        return driverSetPoint;
    }
    
    /**
     * 
     * @return the set KP value
     */
    public int getkP() {
        return kP;
    }

    /**
     * 
     * @return the set KI value
     */
    public int getkI() {
        return kI;
    }
    
    /**
     * 
     * @return the speed limit of the current block
     */
    private int getSpeedLimit(){ //should pull speed limit information from
        return 55;                //loaded track xlx after calculating location.
    }
    
    /**
     * 
     * @return the set point based on the mode (man or auto) of the train
     */
    private double getSetPoint(){
        if(this.manualMode){
            if(this.trainModel.getDriverSetPoint() > this.getSpeedLimit()){
                this.driverSetPoint = (byte) this.getSpeedLimit();
            }
            else{
                this.driverSetPoint = (byte) this.trainModel.getDriverSetPoint();
            }
            return this.driverSetPoint;
        }
        else{
            if(this.trainModel.getCtcSetPoint() > this.getSpeedLimit()){
                this.recommendedSetPoint = (byte) this.getSpeedLimit();
            }
            else{
                this.recommendedSetPoint = (byte) this.trainModel.getCtcSetPoint();//Is this legal?
            }
            return this.recommendedSetPoint; //////////////////////////////////////////////////////////////NEED TO GET CTC OR MBO!!!
        }
    }
}
