/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.traincon;

import com.rogueone.traincon.gui.TrainControllerGUI;
import com.rogueone.trainmodel.TrainModel; //Should I it this way or how???
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
        
    //Announcements
    private String announcement;
    
    //Train Information
    public TrainModel trainModel;
    //public GPS gps;
    public PowerSystems powerSystem;
    //public SpeedControl speedControl;
    public TMR vitals;
    public TrainControllerGUI gui;
    public TrainSystem trainSystem;
    private String trainID;
    private String line;
    private String section;
    private String block;
    private int passengers;
    
    
        
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
     * @param ts
     */
    public TrainController(TrainModel tm, TrainControllerGUI gui, byte setPointSpeed, short authority, double maxPow, TrainSystem ts){
        
        this.trainSystem = ts;
        this.trainModel = tm; //Should come from passed (this) reference
        this.powerSystem = new PowerSystems(this);
        this.vitals = new TMR(ts, tm, this, maxPow, setPointSpeed, authority, trainID);
        this.gui = gui;
        
        
        
        
        //Initialize Train Controller Object
        this.manualMode = false;        
              
        //Announcements
        this.announcement = this.trainID + "Departing Yard";
        //System.out.println(this.announcement);

        //Train Information
        this.trainID = trainID;
        this.line = line;
        this.section = section;
        this.block = block;
        
//        this.passengers = updatePassengers();
        
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
        
        //Initialize the GUI
        trainControllerObject.InitializeInputPanel(trainControllerGUI);
        
        this.gui = trainControllerGUI;  //Return the GUI object
        return trainControllerGUI;  //Return the GUI object
    } 
    
    public void showGUIObject() {
        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this.gui);
        frame.pack();
        frame.setVisible(true);     //Make sure to set it visible
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
        "\nPassengers: " + this.passengers + "\nTemp: " + this.powerSystem.getTemperature());
        gui.TrainInfoText.setEditable(false);
        
        gui.ActualSpeedLabel.setText(String.valueOf(this.vitals.getPrimary().getGPS().getCurrSpeed()));
        gui.SetSpeedLabel.setText(String.valueOf(this.vitals.getPrimary().getGPS().getSpeedLimit()));
        gui.AuthorityLabel.setText(String.valueOf(this.vitals.getPrimary().getGPS().getAuthority()));
        gui.PowerUsedLabel.setText(String.valueOf(this.vitals.getPrimary().getPowerCommand()));
        gui.MaxPowerLabel.setText(String.valueOf(this.vitals.getPrimary().getMaxPower()));
        gui.NotificationsDisplay.append(this.announcement);
        gui.NotificationsDisplay.setEditable(false);
        gui.SpeedInput.setValue(this.vitals.getPrimary().getSpeedControl().getDriverSetPoint());
        gui.KiInput.setValue(this.vitals.getPrimary().getKI());
        gui.KpInput.setValue(this.vitals.getPrimary().getKP());
        gui.ClockText.append(this.trainSystem.getClock().printClock());//Get value from global clock value (EST)
        
        for(int i = 0; i < getNumberOfTrains(); i++){
            //gui.TrainSelectorDropDown.addItem(getTrainArray().get(i));
            //snag train array and add them to the drop down list
        }        
        //Add more functionality in future    
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
    
    private int getNumberOfTrains(){
        return 0; //Get value from ?????
    }
    
    private ArrayList getTrainArray(){
        return null;
    }
    
    public void approachingStation(){
        //1. We know we are at least
    }

    /**
     * The update function for the full controller
     * 
     * @author Tyler Protivnak
     */
    public void updateController(){
       
        this.vitals.update(this.manualMode);     
        if(!this.manualMode){
            this.powerSystem.update(this.vitals.getPrimary().getGPS().setLights());
        }
        this.updatePassengers();
    }
        
    public void updateGUI(TrainControllerGUI gui){
        //this.updateController();
        
        for(int i = 0; i < getNumberOfTrains(); i++){
            //gui.TrainSelectorDropDown.addItem(getTrainArray().get(i));
            //snag train array and add them to the drop down list
        }
        
        if(this.powerSystem.isLeftDoorOpen()) {
            gui.LeftDoorOpened.setSelected(true);
        }
        else {
            gui.LeftDoorClosed.setSelected(true);
        }
        
        if(this.powerSystem.isRightDoorOpen()) {
            gui.RightDoorOpened.setSelected(true);
        }
        else {
            gui.RightDoorClosed.setSelected(true);
        }
        
        if(this.powerSystem.isLightsOn()) {
            gui.LightsOn.setSelected(true);
        }
        else {
            gui.LightsOff.setSelected(true);
        }
        
        if(this.powerSystem.isAirConditioningOn() && this.powerSystem.isHeaterOn()){
            //System.out.println("Both climate control systems activated at the same time, disabling both.");
            this.powerSystem.setAirConditioningOn(false);
            this.powerSystem.setHeaterOn(false);
            gui.ACOff.setSelected(true);
            gui.HeatOff.setSelected(true);
        }
        else if(this.powerSystem.isAirConditioningOn()){
            gui.ACOn.setSelected(true);
            gui.HeatOff.setSelected(true);
        }
        else if(this.powerSystem.isHeaterOn()){
            gui.ACOff.setSelected(true);
            gui.HeatOn.setSelected(true);
        }
        else{
            gui.ACOff.setSelected(true);
            gui.HeatOff.setSelected(true);
        }
               
        gui.TrainInfoText.setText(null);
        gui.TrainInfoText.append("Train ID: " + this.vitals.getPrimary().getGPS().trainID + "\nLine: " + 
        this.vitals.getPrimary().getGPS().getCurrBlock().getLine() + "\nSection: " + this.vitals.getPrimary().getGPS().getCurrBlock().getSection() + "\nBlock: " + this.vitals.getPrimary().getGPS().getCurrBlock() + 
        "\nPassengers: " + this.passengers + "\nTemp: " + this.powerSystem.getTemperature() + " F");
        
        if(this.vitals.getPrimary().isEmergencyBrakeOverride() || this.vitals.getPrimary().isEmergencyBrakeActivated()){      //Default to always print emergency brake if both emergency and service are activated
            
            gui.EmergencyBrakeToggleButton.setSelected(true);

            //System.out.println(getFailureType());
            switch(this.vitals.getPrimary().getFailureType()){
                
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
        
        if(this.vitals.getPrimary().isServiceBrakeActivated()) {
            gui.ServiceBrakeToggleButton.setSelected(true);
        }
        else{
            gui.ServiceBrakeToggleButton.setSelected(false);
        }        
        
        //Speed and Authority
        gui.ActualSpeedLabel.setText(decimalFormatter.format(this.trainModel.getCurrSpeedMPH()));
        gui.SetSpeedLabel.setText(decimalFormatter.format(this.vitals.getPrimary().getGPS().getSpeedLimit()));
        
        if(this.manualMode){
            gui.ManualModeSelect.setSelected(true);
            gui.AutoModeSelect.setSelected(false);
            gui.SetSpeedLabel.setText(Integer.toString((int) (this.vitals.getPrimary().getSpeedControl().getDriverSetPoint())));
        }
        else{
            gui.ManualModeSelect.setSelected(false);
            gui.AutoModeSelect.setSelected(true);
            gui.SetSpeedLabel.setText(Integer.toString((int) (this.vitals.getPrimary().getSpeedControl().getRecommendedSetPoint())));
        }
        
        gui.AuthorityLabel.setText(this.decimalFormatter.format(this.vitals.getPrimary().getGPS().getAuthority()));
        gui.SpeedLimitLabel.setText(decimalFormatter.format(this.vitals.getPrimary().getGPS().getSpeedLimit()));
        gui.MaxPowerLabel.setText(decimalFormatter.format(this.vitals.getPrimary().getMaxPower()));
        gui.PowerUsedLabel.setText(decimalFormatter.format(this.vitals.getPrimary().getPowerCommand()));
        
        gui.ClockText.setText(this.trainSystem.getClock().printClock());
        
        //Will add more as we move forward.
    }     
    
    public double calculatePower(double actualSpeed, double samplePeriod){
        return this.vitals.calculatePower(actualSpeed, samplePeriod, this.manualMode);
    }
    
    /**
     * 
     * @param manualMode Boolean to set mode. Pass "True" for manual and "False" for automatic
     */
    public void setManualMode(boolean manualMode) {
        this.manualMode = manualMode;
    }
   
    public boolean isManualMode() {
        return manualMode;
    }
    
    public void setAuthority(short authority){
        this.vitals.getPrimary().getGPS().setAuthority(authority);
    }
    
    public void setSpeed(byte speed){
        this.vitals.getPrimary().getSpeedControl().setRecommendedSetPoint(speed);
    }
    
    private void setServiceBrake(boolean brake){
        this.vitals.getPrimary().setServiceBrakeActivated(brake);
    }
    
    public void safeToProceed(boolean safe){
        this.setServiceBrake(!safe || this.vitals.getPrimary().isServiceBrakeActivated());
    }
    
}
