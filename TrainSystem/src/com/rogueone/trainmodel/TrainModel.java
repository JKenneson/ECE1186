/**
 * The train model class is an accurate representation of a physical train, equipped with sliding doors, lights, and temperature control
 * The train model can be created to simulate the physics of a train traveling along the track
 * All necessary calculations for force, mass, acceleration, velocity, and distance traveled will be calculated here
 *
 * @author Jonathan Kenneson
 * @Creation 2/3/17
 * @Modification 2/6/17
 */
package com.rogueone.trainmodel;

import com.rogueone.trainmodel.gui.TrainModelGUI;
import com.rogueone.trainmodel.entities.TrainFailures;
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
    private double grade;
    //Station and Passengers
    private String approachingStation;
    private int passengersAtStation;
    private int passengersOnBaord;
    private int passengersDisembarking;
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
     * @param setPointSpeed The desired speed set by the CTC
     * @param authority The desired authority set by the CTC
     * @param numCars How many cars are to be created (1 or 2)
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
        this.grade = 0;
        //Station and Passengers
        this.approachingStation = "";
        this.passengersAtStation = 0;
        this.passengersOnBaord = 0;
        this.passengersDisembarking = 0;
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
     * @param trainModelObject The TrainModel to create the GUI off of
     * @return TrainModelGUI A GUI object for this TrainModel
     */
    public TrainModelGUI CreateGUIObject(TrainModel trainModelObject) {
        //Create a GUI object
        TrainModelGUI trainModelGUI = new TrainModelGUI(trainModelObject);
        
        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trainModelGUI);
        frame.pack();
        frame.setVisible(true);     //Make sure to set it visible
        
        //Initialize the GUI
        trainModelObject.InitializeInputPanel(trainModelGUI);
        
        return  trainModelGUI;  //Return the GUI object
    }
    
    
    /**
     * Initialize some properties and variables in the train model window's input panel
     * 
     * @author Jonathan Kenneson
     * @param gui A TrainModelGUI object that will get initialized with values for the input panel
     */
    public void InitializeInputPanel(TrainModelGUI gui) {
        //All spinner values initialized just once
        gui.temperatureInputSpinner.setValue(this.temperature);
        
        gui.ctcSetPointSpinner.setValue(this.ctcSetPoint);
        gui.ctcAuthoritySpinner.setValue(this.authority);
        
        gui.driverSetPointSpinner.setValue(this.driverSetPoint);
        gui.driverPowerSpinner.setValue(this.powerReceived);
        
        gui.passengersInputSpinner.setValue(this.passengersAtStation);
        gui.numCarsInputSpinner.setValue(this.numCars);
    }
    
    
    /**
     * Update all properties and variables in the train model window
     * 
     * @author Jonathan Kenneson
     * @param gui A TrainModelGUI object that will get updated with values from the class
     */
    public void UpdateGUI(TrainModelGUI gui) {
        //Train Operations
        if(this.leftDoorOpen) {
            gui.leftDoorState.setText("Open");
        }
        else {
            gui.leftDoorState.setText("Closed");
        }
        if(this.rightDoorOpen) {
            gui.rightDoorState.setText("Open");
        }
        else {
            gui.rightDoorState.setText("Closed");
        }
        if(this.lightsOn) {
            gui.lightsState.setText("On");
        }
        else {
            gui.lightsState.setText("Off");
        }
        gui.temperatureState.setText(Integer.toString(this.temperature));
        if(this.emergencyBrakeActivated) {      //Default to always print emergency brake if both emergency and service are activated
            gui.brakesState.setText("Emergency");
        }
        else if(this.serviceBrakeActivated) {
            gui.brakesState.setText("Service");
        }
        else {
            gui.brakesState.setText("None");
        }        
        
        //Speed and Authority
        gui.currSpeedState.setText(Integer.toString(this.currSpeed));
        gui.speedLimitState.setText(Integer.toString(this.speedLimit));
        gui.driverSetPointState.setText(Integer.toString(this.driverSetPoint));
        gui.ctcSetPointState.setText(Integer.toString(this.ctcSetPoint));
        gui.authorityState.setText(Integer.toString(this.authority));
        gui.powerState.setText(Double.toString(this.powerReceived));
        gui.gradeState.setText(Double.toString(this.grade));
        
        //Station and Passengers
        gui.nextStationState.setText(this.approachingStation);
        gui.passAtStationState.setText(Integer.toString(this.passengersAtStation));
        gui.passOnBoardState.setText(Integer.toString(this.passengersOnBaord));
        gui.passDisembarkState.setText(Integer.toString(this.passengersDisembarking));
        gui.maxCapacityState.setText(Integer.toString(this.passengerMaxCapacity));
        
        //Physical Characteristics
        gui.trainWeightState.setText(Integer.toString(this.trainWeight));
        gui.trainLengthState.setText(Integer.toString(this.trainLength));
        gui.numCarsState.setText(Integer.toString(this.numCars));
        
        if(this.trackAntennaActivated) {
            gui.trackAntennaState.setText("Activated");
        }
        else {
            gui.trackAntennaState.setText("Failed");
        }
        if(this.mboAntennaActivated) {
            gui.mboAntennaState.setText("Activated");
        }
        else {
            gui.mboAntennaState.setText("Failed");
        }
    }

    /**
     * This method is called from the TrainModelGUI when a failure is activated and sent through
     * the "Send New Failure" button. For now, the response will be to activate the emergency brake. 
     * Later, the TrainModel will alert the TrainController to handle these
     * 
     * @author Jonathan Kenneson
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void handleFailure(TrainFailures failure) {
        
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
        TrainModel trainModelTest1 = new TrainModel(40, 500, 1);
        //Instantiate a GUI for this train
        TrainModelGUI trainModelGUITest1 = trainModelTest1.CreateGUIObject(trainModelTest1);
        

        //Constantly update velocity then the GUI
        while(true){
            
            trainModelTest1.UpdateGUI(trainModelGUITest1);
            if(trainModelGUITest1.isDisplayable() == false) {
                break;
            }
        }
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Following are all getters and setters for the TrainModel class
     * 
     * @author Jonathan Kenneson
     */

    public double getGrade() {
        return this.grade;
    }
    
    public void setGrade(double grade) {
        this.grade = grade;
    }
    
    public boolean isLeftDoorOpen() {
        return leftDoorOpen;
    }

    public void setLeftDoorOpen(boolean leftDoorOpen) {
        this.leftDoorOpen = leftDoorOpen;
    }

    public boolean isRightDoorOpen() {
        return rightDoorOpen;
    }

    public void setRightDoorOpen(boolean rightDoorOpen) {
        this.rightDoorOpen = rightDoorOpen;
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public void setLightsOn(boolean lightsOn) {
        this.lightsOn = lightsOn;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public boolean isServiceBrakeActivated() {
        return serviceBrakeActivated;
    }

    public void setServiceBrakeActivated(boolean serviceBrakeActivated) {
        this.serviceBrakeActivated = serviceBrakeActivated;
    }

    public boolean isEmergencyBrakeActivated() {
        return emergencyBrakeActivated;
    }

    public void setEmergencyBrakeActivated(boolean emergencyBrakeActivated) {
        this.emergencyBrakeActivated = emergencyBrakeActivated;
    }

    public int getCurrSpeed() {
        return currSpeed;
    }

    public void setCurrSpeed(int currSpeed) {
        this.currSpeed = currSpeed;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getDriverSetPoint() {
        return driverSetPoint;
    }

    public void setDriverSetPoint(int driverSetPoint) {
        this.driverSetPoint = driverSetPoint;
    }

    public int getCtcSetPoint() {
        return ctcSetPoint;
    }

    public void setCtcSetPoint(int ctcSetPoint) {
        this.ctcSetPoint = ctcSetPoint;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public double getPowerReceived() {
        return powerReceived;
    }

    public void setPowerReceived(double powerReceived) {
        this.powerReceived = powerReceived;
    }

    public String getApproachingStation() {
        return approachingStation;
    }

    public void setApproachingStation(String approachingStation) {
        this.approachingStation = approachingStation;
    }

    public int getPassengersOnBaord() {
        return passengersOnBaord;
    }

    public void setPassengersOnBaord(int passengersOnBaord) {
        this.passengersOnBaord = passengersOnBaord;
    }

    public int getPassengersDisembarking() {
        return passengersDisembarking;
    }

    public void setPassengersDisembarking(int passengersDisembarking) {
        this.passengersDisembarking = passengersDisembarking;
    }

    public int getPassengersAtStation() {
        return passengersAtStation;
    }

    public void setPassengersAtStation(int passengersAtStation) {
        this.passengersAtStation = passengersAtStation;
    }

    public int getPassengerMaxCapacity() {
        return passengerMaxCapacity;
    }

    public void setPassengerMaxCapacity(int passengerMaxCapacity) {
        this.passengerMaxCapacity = passengerMaxCapacity;
    }

    public int getTrainWeight() {
        return trainWeight;
    }

    public void setTrainWeight(int trainWeight) {
        this.trainWeight = trainWeight;
    }

    public int getTrainLength() {
        return trainLength;
    }

    public void setTrainLength(int trainLength) {
        this.trainLength = trainLength;
    }

    public int getNumCars() {
        return numCars;
    }

    public void setNumCars(int numCars) {
        this.numCars = numCars;
    }

    public boolean isTrackAntennaActivated() {
        return trackAntennaActivated;
    }

    public void setTrackAntennaActivated(boolean trackAntennaActivated) {
        this.trackAntennaActivated = trackAntennaActivated;
    }

    public boolean isMboAntennaActivated() {
        return mboAntennaActivated;
    }

    public void setMboAntennaActivated(boolean mboAntennaActivated) {
        this.mboAntennaActivated = mboAntennaActivated;
    }
    
}
