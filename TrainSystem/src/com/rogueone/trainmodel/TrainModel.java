/**
 * The train model class is an accurate representation of a physical train, equipped with sliding doors, lights, and temperature control
 * The train model can be created to simulate the physics of a train traveling along the track
 * All necessary calculations for force, mass, acceleration, velocity, and distance traveled will be calculated here
 *
 * @author Jonathan Kenneson
 * @creation date 2/3/17
 * @modification date 2/6/17
 */
package com.rogueone.trainmodel;

import com.rogueone.trainmodel.gui.TrainModelGUI;
import javax.swing.*;

/**
 * Class declaration for TrainModel
 * 
 * @author Jonathan Kenneson
 */
public class TrainModel {
    
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
    
    /**
     * Initializer for the TrainModel class, sets all variables to a default state
     * 
     * @author Jonathan Kenneson
     * @param setPointSpeed the desired speed set by the CTC
     * @param authority the desired authority set by the CTC
     * @param numCars how many cars are to be created (1 or 2)
     */
    public TrainModel(int setPointSpeed, int authority, int numCars) {
        //Train Operations
        this.leftDoorOpen = false;
        this.rightDoorOpen = false;
        this.lightsOn = false;
        this.temperature = 72;
        this.serviceBrakeActivated = false;
        this.emergencyBrakeActivated = false;
        //Speed and Authority
        this.currSpeed = 0;
        this.speedLimit = 0;
        this.driverSetPoint = 0;
        this.ctcSetPoint = setPointSpeed;
        this.authority = authority;
        this.powerReceived = 0;
        //Station and Passengers
        this.approachingStation = "";
        this.passengersOnBaord = 0;
        this.passengersDisembarking = 0;
        this.passengersEmbarking = 0;
        this.passengerMaxCapacity = 0;                                                                          //TODO: Set initial max capacity based off of number of cars
        //Physical Characteristics
        this.trainWeight = 0;                                                                                   //TODO: Set train weight and length based off numCars
        this.trainLength = 0;
        this.numCars = numCars;
        this.trackAntennaActivated = true;
        this.mboAntennaActivated = true;
    }
    
    /**
     * This function will create, display, and return a GUI object for the Train Model class that can be interacted with
     * 
     * @author Jonathan Kenneson
     * @return TrainModelGUI a GUI object for this TrainModel
     */
    public TrainModelGUI CreateGUIObject() {
        //Create a GUI object
        TrainModelGUI trainModelGUI = new TrainModelGUI();
        
        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trainModelGUI);
        frame.pack();
        frame.setVisible(true);     //Make sure to set it visible
        
        return  trainModelGUI;  //Return the GUI
    }
    
    /**
     * Set some initial properties to variables in the train model window
     * 
     * @author Jonathan Kenneson
     * @param gui A TrainModelGUI object that will get updated with initial values
     */
    public void InitializeGUI(TrainModelGUI gui) {
        //Train Operations
        gui.leftDoorState.setText("Closed");
        gui.rightDoorState.setText("Closed");
        gui.lightsState.setText("Off");
        gui.tempState.setText(Integer.toString(this.temperature));
        gui.tempInputSpinner.setValue(this.temperature);
        gui.brakesState.setText("None");
        //Speed and Authority
        gui.currSpeedState.setText(Integer.toString(this.currSpeed));
        gui.speedLimitState.setText(Integer.toString(this.speedLimit));
        gui.driverSetPointState.setText(Integer.toString(this.driverSetPoint));
        gui.driverSetPointSpinner.setValue(this.driverSetPoint);
        gui.ctcSetPointState.setText(Integer.toString(this.ctcSetPoint));
        gui.ctcSetPointSpinner.setValue(this.ctcSetPoint);
        gui.authorityState.setText(Integer.toString(this.authority));
        gui.ctcAuthoritySpinner.setValue(this.authority);
        gui.driverPowerSpinner.setValue(this.powerReceived);
        //Station and Passengers
        gui.nextStationState.setText(this.approachingStation);
        gui.passOnBoardState.setText(Integer.toString(this.passengersOnBaord));
        gui.passDisembarkState.setText(Integer.toString(this.passengersDisembarking));
        gui.passengersInputSpinner.setValue(this.passengersEmbarking);
        gui.maxCapacityState.setText(Integer.toString(this.passengerMaxCapacity));
        //Physical Characteristics
        gui.trainWeightState.setText(Integer.toString(this.trainWeight));
        gui.trainLengthState.setText(Integer.toString(this.trainLength));
        gui.numCarsState.setText(Integer.toString(this.numCars));
        gui.numCarsInputSpinner.setValue(this.numCars);
        gui.trackAntennaState.setText("");
        gui.mboAntennaState.setText("");
    }

    public static void printMe() {
        System.out.println("Print Me!");
    }
    
    
    /**
     * Main function tests the functionality of the Train Model class independent from the other modules
     * The user may perform extensive testing of this module through the module's GUI
     * 
     * @author Jonathan Kenneson
     * @param args 
     */
    public static void main(String[] args) {
        //Create a new TrainModel object with a set point speed of 40, authority of 500, and 1 car
        TrainModel trainModelTest = new TrainModel(40, 500, 1);
        //Instantiate a GUI for this train
        TrainModelGUI trainModelGUITest = trainModelTest.CreateGUIObject();
        //Initialize the GUI with defualt values after it is first created
        trainModelTest.InitializeGUI(trainModelGUITest);
        
    }
    
}
