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
    private TrainModel trainModel;
    private GPS gps;
    private PowerSystems powerSystem;
    private SpeedControl speedControl;
    private Vitals vitals;
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
     */
    public TrainController(TrainModel tm, TrainControllerGUI gui, byte setPointSpeed, short authority, double maxPow, String trainID,
           String line, String section, String block, TrainSystem ts){
        
        this.trainSystem = ts;
        this.trainModel = tm; //Should come from passed (this) reference
        this.gui = gui;
        
        this.gps = new GPS(authority, this.trainSystem, this.trainID);
        this.powerSystem = new PowerSystems(this.trainModel);
        this.speedControl = new SpeedControl(driverSetPoint, recommendedSetPoint, tm, this, this.gps);
        
        
        
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
              
                                           
    
    /**
     * The update function for the full controller
     * 
     * @author Tyler Protivnak
     */
    public void updateController(){
        this.gps.updatePosition();
        this.authority -= this.trainModel.getDistanceTraveled();
        if(!this.manualMode)
            this.updateClimateControl();
        this.updatePassengers();
        if(this.authority<0)
            this.setEmergencyBrakeActivated(true);
        else if(!this.emergencyBrakeOverride){
            this.setEmergencyBrakeActivated(false);
        }
            
        updateSafeSpeed();
        //this.currSpeed = this.trainModel.getCurrSpeed();
        
        
    }
    
    
    private void updateSafeSpeed(){
        if((this.currSpeed>this.getSetPoint() && !this.manualMode) || this.serviceBrakeActivated){
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
        
        gui.ClockText.setText(this.trainSystem.getClock().printClock());
        
        //Will add more as we move forward.
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
        this.powerSystem.setAirConditioningOn(airConditioningOn);
    }

    /**
     * 
     * @param heaterOn Boolean to set the status of the heater. True = on.
     */
    public void setHeaterOn(boolean heaterOn) {
        this.setHeaterOn(heaterOn);
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
        this.powerSystem.setLightsOn(lightsOn);
        this.trainModel.setLightsOn(lightsOn);
    }
    
    
    /**
     * 
     * @param leftDoorOpen Boolean to set the status of the left door. True = open. Also updates train model.
     */
    public void setLeftDoorOpen(boolean leftDoorOpen) {
        this.powerSystem.setLeftDoorOpen(leftDoorOpen);
        this.trainModel.setLeftDoorOpen(leftDoorOpen);
    }

    /**
     * 
     * @param rightDoorOpen Boolean to set the status of the right door. True = open. Also updates train model.
     */
    public void setRightDoorOpen(boolean rightDoorOpen) {
        this.powerSystem.setRightDoorOpen(rightDoorOpen);
        this.trainModel.setRightDoorOpen(rightDoorOpen);
    }

    /**
     * 
     * @param serviceBrakeActivated Boolean to set the status of the service brake. True = on. Also updates train model.
     */
    public void setServiceBrakeActivated(boolean serviceBrakeActivated) {
        this.vitals.setServiceBrakeActivated(serviceBrakeActivated);
        this.trainModel.setServiceBrakeActivated(serviceBrakeActivated);
    }

    /**
     * 
     * @param emergencyBrakeActivated Boolean to set the status of the e brake. True = on. Also updates train model.
     */
    public void setEmergencyBrakeActivated(boolean emergencyBrakeActivated) {
        this.vitals.setEmergencyBrakeActivated(emergencyBrakeActivated);
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
    

   
    public boolean isManualMode() {
        return manualMode;
    }

    
    
    
}
