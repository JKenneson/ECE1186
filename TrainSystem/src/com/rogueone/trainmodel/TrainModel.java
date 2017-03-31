/**
 * The train model class is an accurate representation of a physical train, equipped with sliding doors, lights, and temperature control
 * The train model can be created to simulate the physics of a train traveling along the track
 * All necessary calculations for force, mass, acceleration, velocity, and distance traveled will be calculated here
 *
 * @author Jonathan Kenneson
 * @Creation 2/3/17
 * @Modification 3/27/17
 */
package com.rogueone.trainmodel;

import com.rogueone.trainmodel.gui.TrainModelGUI;
import com.rogueone.trainmodel.entities.TrainFailures;
import com.rogueone.global.Global;
import com.rogueone.trackmodel.Block;
import com.rogueone.trackmodel.Station;
import com.rogueone.trackmodel.TrackPiece;
import com.rogueone.traincon.TrainController;
import com.rogueone.traincon.gui.TrainControllerGUI;
import com.rogueone.trainsystem.TrainSystem;
import com.sun.javafx.image.impl.IntArgb;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;
import javax.swing.*;

/**
 * Class declaration for TrainModel
 * 
 * @author Jonathan Kenneson
 */
public class TrainModel {
    
    public final double MAX_ACCELERATION = 0.5;         //Max acceleration of 0.5 m/s^2
    public final double SERVICE_BRAKE_DECEL = -1.2;     //Service brake decelerates at 1.2 m/s^2
    public final double EMERGENCY_BRAKE_DECEL = -2.73;  //Emergency brake decelerates at 2.73 m/s^2
    public final double COEFFICIENT_OF_FRICTION = 0.16; //Assuming the coefficient of friction between train wheel and track is 0.16 (steel on steel - lubricated)
    public final double NUMBER_OF_WHEELS = 12;          //Assuming there are 12 wheels per car to distribute the force to the track
    public final double GRAVITY = 9.8;                  //9.8 m/s^2 for acceleration due to gravity
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double KG_IN_A_POUND = 0.454;          //0.454 kg = 1 pound
    public final double CAR_LENGTH = 105.64;            //1 car's train length (in feet)
    public final double CAR_WEIGHT = 81800;             //1 car's train weight (in lbs)
    public final int CAR_CAPACITY = 222;                //1 car's passenger max capacity 
    public final double PASS_WEIGHT = 165.35;           //1 passenger's weight (in lbs)
    public final double TRAIN_HEIGHT = 11.22;           //Train height (in feet)
    public final double TRAIN_WIDTH = 8.70;             //Train width (in feet)
    
    //Variable declaration for the class
    //Train System reference
    private TrainSystem trainSystem;
    //Reference to TrainController
    public TrainController trainController;
    private TrainControllerGUI trainControllerGUI;
    //Reference to GUI
    private TrainModelGUI trainModelGUI;
    //Failures
    private boolean powerFailure;
    private boolean brakeFailure;
    private boolean antennaFailure;
    private boolean emergencyBrakeOverride;     //This is activated whenever a failure occurs and overrides any brake activated
    //Train Operations
    private boolean leftDoorOpen;
    private boolean rightDoorOpen;
    private boolean lightsOn;
    private int temperature;
    private boolean serviceBrakeActivated;
    private boolean emergencyBrakeActivated;
    //Speed and Authority
    private double currSpeed;       //Vf in m/s
    private double currSpeedMPH;    //Vf in mph for printing to the screen
    private double lastSpeed;       //Vi in m/s
    private int speedLimit;
    private int driverSetPoint;
    private int ctcSetPoint;
    private double authority;       //feet remaining
    private double distanceIntoBlock;//The distance we have traveled into the block
    private double distanceTraveledMeters;//meters traveled this cycle
    private double distanceTraveledFeet;//feet traveled this cycle
    private double powerReceived;   //kW
    private double grade;
    private double angleOfSlope;
    private double force;           //N
    private double frictionForce;   //N -> (uk * m)
    private double acceleration;    //m/s^2
    //Station and Passengers
    private String approachingStation;
    private int passengersAtStation;
    private int passengersOnBaord;
    private int passengersDisembarking;
    private int passengerMaxCapacity;
    private int crewOnBoard;                //For now, there will always be only 1 crew member on board
    //Physical Characteristics
    private double trainWeight;
    private double trainLength;
    private double trainHeight;
    private double trainWidth;
    private int numCars;
    private boolean trackAntennaActivated;
    private boolean mboAntennaActivated;
    private String line;
    public int trainID;
    
    //Blocks
    private Block currBlock;
    private Block currTempBlock;
    private TrackPiece prevBlock;
    private TrackPiece nextBlock;
    
    
    /**
     * Initializer for the TrainModel class, sets all variables to a default state
     * 
     * @author Jonathan Kenneson
     * @param setPointSpeed The desired speed set by the CTC
     * @param authority The desired authority set by the CTC
     * @param numCars How many cars are to be created (1 or 2)
     * @param line The line the train will be on (Red or Green)
     * @param trainSystem A back reference to the over-arching train system
     */
    public TrainModel(int setPointSpeed, int authority, int numCars, String line, TrainSystem trainSystem, int trainID) {
        //Failures
        this.powerFailure = false;
        this.brakeFailure = false;
        this.antennaFailure = false;
        this.emergencyBrakeOverride = false;
        //Train Operations
        this.leftDoorOpen = false;
        this.rightDoorOpen = false;
        this.lightsOn = false;
        this.temperature = 72;
        this.serviceBrakeActivated = false;
        this.emergencyBrakeActivated = false;
        //Speed and Authority
        this.currSpeed = 0;
        this.currSpeedMPH = 0;
        this.lastSpeed = 0;
        this.speedLimit = 0;
        this.driverSetPoint = 0;
        this.ctcSetPoint = setPointSpeed;
        this.authority = authority;
        this.distanceIntoBlock = 0;
        this.distanceTraveledMeters = 0;
        this.distanceTraveledFeet = 0;
        this.powerReceived = 0;
        this.grade = 0;
        this.angleOfSlope = 0;
        this.force = 0;
        this.frictionForce = 0;
        this.acceleration = 0;
        //Station and Passengers
        this.approachingStation = "";
        this.passengersAtStation = 0;
        this.passengersOnBaord = 0;
        this.passengersDisembarking = 0;
        this.passengerMaxCapacity = this.CAR_CAPACITY * numCars;
        this.crewOnBoard = 1;
        //Physical Characteristics
        this.trainWeight = this.CAR_WEIGHT * numCars;
        this.trainLength = this.CAR_LENGTH * numCars;
        this.trainHeight = this.TRAIN_HEIGHT;               //Height and width are similar for all trains
        this.trainWidth = this.TRAIN_WIDTH;
        this.numCars = numCars;
        this.trackAntennaActivated = true;
        this.mboAntennaActivated = true;
        this.line = line;
        this.trainID = trainID;
        
        this.trainController = null;
        this.trainControllerGUI = null;
        
        this.trainModelGUI = null;
        
        this.trainSystem = trainSystem;
        //Block setting
        this.prevBlock = trainSystem.getTrackModel().getYard();
        this.currBlock = trainSystem.getTrackModel().enterTrack(Global.Line.GREEN);
        this.nextBlock = null;
        this.currTempBlock = null;
        this.trainSystem.getTrackControllerHandler().updateTrack();
    }
    
    /**
     * This method creates a new TrainController object for this train, with a back-reference to this specific TrainModel
     * @author Jonathan Kenneson
     */
    public void createTrainController() {
        //Create a new TrainController object
        this.trainController = new TrainController(this, null, (byte)this.ctcSetPoint, (short)(this.authority/this.FEET_IN_A_METER), 300, this.trainSystem);
    }
    
    /**
     * This method creates a new TrainControllerGUI object for this train, with a back-reference to the specific TrainController
     * @author Jonathan Kenneson
     */
    public void createTrainControllerGUI() {
        this.trainControllerGUI = this.trainController.CreateGUIObject(trainController);
    }
    
    /**
     * This method updates the TrainControllerGUI object for this train
     * @author Jonathan Kenneson
     */    
    public void updateTrainControllerGUI() {
        this.trainController.updateGUI(this.trainControllerGUI);
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
        
        //Initialize the GUI
        trainModelObject.InitializeInputPanel(trainModelGUI);
        
        this.trainModelGUI = trainModelGUI;
        return  trainModelGUI;  //Return the GUI object
    }
    
    /**
     * This function displays the gui attached to the TrainModel object
     * 
     * @author Jonathan Kenneson
     */
    public void showGUIObject() {
        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(this.trainModelGUI);
        frame.pack();
        frame.setVisible(true);     //Make sure to set it visible
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
        gui.gradeInputSpinner.setValue(this.grade);
        gui.speedLimitSpinner.setValue(this.speedLimit);
        
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
        if(this.emergencyBrakeOverride) {           //Check first if the override has been enabled -> a failure has occured
            gui.brakesState.setText("Emergency");
        }
        else if(this.emergencyBrakeActivated) {      //Default to always print emergency brake if both emergency and service are activated
            gui.brakesState.setText("Emergency");
        }
        else if(this.serviceBrakeActivated) {
            gui.brakesState.setText("Service");
        }
        else {
            gui.brakesState.setText("None");
        }        
        
        //Speed and Authority
        gui.currSpeedState.setText(Global.decimalFormatter.format(this.currSpeedMPH));
        gui.speedLimitState.setText(Integer.toString(this.speedLimit));
        gui.driverSetPointState.setText(Integer.toString(this.driverSetPoint));
        gui.ctcSetPointState.setText(Integer.toString(this.ctcSetPoint));
        gui.authorityState.setText(Global.decimalFormatter.format(this.authority));
        gui.powerState.setText(Global.commaFormatter.format(this.powerReceived));
        gui.gradeState.setText(Double.toString(this.grade));
        
        //Station and Passengers
        gui.nextStationState.setText(this.approachingStation);
        gui.passAtStationState.setText(Integer.toString(this.passengersAtStation));
        gui.passOnBoardState.setText(Integer.toString(this.passengersOnBaord));
        gui.passDisembarkState.setText(Integer.toString(this.passengersDisembarking));
        gui.maxCapacityState.setText(Integer.toString(this.passengerMaxCapacity));
        gui.crewOnBoardState.setText(Integer.toString(this.crewOnBoard));
        
        //Physical Characteristics
        gui.trainWeightState.setText(Global.commaFormatter.format(this.trainWeight));
        gui.trainLengthState.setText(Global.commaFormatter.format(this.trainLength));
        gui.trainHeightState.setText(Global.commaFormatter.format(this.trainHeight));
        gui.trainWidthState.setText(Global.commaFormatter.format(this.trainWidth));
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
    public void causeFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch(failure) {
            //A power failure will prevent the doors, lights, and temp setting from working.  Activate emergency brake
            case Power:
                this.powerFailure = true;               //In any setter methods below, the value will not be updated if there is a power failure
                this.emergencyBrakeOverride = true;
                break;
            //A brake failure will prevent the service brake from being activated. Activate the emergency brake
            case Brake:
                this.brakeFailure = true;
                this.emergencyBrakeOverride = true;
                break;
            //Deactivate the track and mbo antenna.  Activate emergency brake
            case Antenna:
                this.antennaFailure = true;
                this.trackAntennaActivated = false;
                this.mboAntennaActivated = false;
                this.emergencyBrakeOverride = true;
                break;
        }
    }
    
    /**
     * This method is called from the TrainModelGUI when a failure is de-activated, or "fixed"
     * Should undo everything that the causeFailure() function did
     * 
     * @author Jonathan Kenneson
     * @param failure One of 3 TrainFailures as found in TrainFailures.java
     */
    public void fixFailure(TrainFailures failure) {
        //Switch on the failure passed in
        switch(failure) {
            //Undo the power failure
            case Power:
                this.powerFailure = false;
                break;
            //Undo the brake failure
            case Brake:
                this.brakeFailure = false;
                break;
            //Undo the antenna failure
            case Antenna:
                this.antennaFailure = false;
                this.trackAntennaActivated = true;
                this.mboAntennaActivated = true;
                break;
        }
        //If there are no failures, de-activate the emergencyBrakeOverride
        if(!this.powerFailure && !this.brakeFailure && !this.antennaFailure) {
            this.emergencyBrakeOverride = false;
        }
    }
    
    /**
     * This method should be called every clock cycle and will update all important characteristics of the TrainModel, such as:
     * 1)   Length, weight, pass max capacity based on number of cars and passengers in train     
     * 2)   Its velocity based on the power sent in from the TrainController
     * 3)   Adjust for friction.  Assuming the coefficient of kinetic friction between the train and track is 0.16 (steel on steel -> lubricated)
     * 4)   Take grade into account and sum the new forces
     * 5)   If the brakes are activated, this takes precedent and the train slows down according the which brake is activated
     * 6)   Calculate distance traveled since last cycle and subtract that from remaining authority
     * 7)   Calculate distance into block and ask for new block if we've gone the length of the block
     * 
     * @author Jonathan Kenneson
     */
    public void updateTrain() {
        //1)   Length, weight, pass max capacity based on number of cars and passengers in train     
        //Not time efficient, but easy to calculate this way: reset weight and length each cycle and re-calculate based on new values
        this.trainWeight = 0;
        this.trainLength = 0;
        //First, update the train length and weight based on number of cars
        this.trainLength = CAR_LENGTH * this.numCars;
        this.trainWeight = CAR_WEIGHT * this.numCars;
        //Then, add in based on how many passengers and crew there are
        this.trainWeight += (PASS_WEIGHT * (this.passengersOnBaord + this.crewOnBoard));
        this.passengerMaxCapacity = CAR_CAPACITY * this.numCars;    //Passenger max capacity
        
        //Check the current block if there is an updated S&A to send to the controller
        checkTrackCircuit();
        
        //2)   Its velocity based on the power sent in from the TrainController
        //First, ask the TrainController for a new power
        if(this.trainController != null) {
            this.powerReceived = this.trainController.calculatePower(this.currSpeedMPH, 1); //Ask for a new power
        }        
        //System.out.println("Power: " + this.powerReceived);
        
        //P=F*v  -> Calculate a force by deviding that power by the velocity
        if(this.currSpeed == 0) {    //Can't devide by zero
            this.force = (this.powerReceived * 1000) / 1;                    //Arbitrary so we don't divide by zero
        }
        else {
            this.force = (this.powerReceived * 1000) / (double)this.currSpeed;
        }
        
        //3)    Adjust for friction.  uk = 0.16   Friction = uk * FN = uk * m * g
        this.frictionForce = COEFFICIENT_OF_FRICTION * (this.trainWeight * KG_IN_A_POUND)/NUMBER_OF_WHEELS * GRAVITY;        //Convert lbs to kg
        //4)   Take grade into account and sum the new forces.  (m*g*sin(theta)) + (Ff * cos(theta))
        this.angleOfSlope = Math.toDegrees(Math.atan2(this.grade, 100));            //Taking the arctan(%slope/100) will give the angle of the track
        this.frictionForce = (this.trainWeight * KG_IN_A_POUND/NUMBER_OF_WHEELS * GRAVITY * Math.sin(this.angleOfSlope)) + (this.frictionForce * Math.cos(this.angleOfSlope));
        
        //System.out.println("Angle: " + angleOfSlope);
        //System.out.println("Ff: " + frictionForce);
        
        //Sum the forces
        this.force = this.force - this.frictionForce;
        
        //F=m*a  -> Calculate the acceleration by dividing the force by the mass
        this.acceleration = this.force / (this.trainWeight * KG_IN_A_POUND);        //Convert lbs to kg
        
        //Maximum acceleration of 0.5 m/s^2
        if(this.acceleration > MAX_ACCELERATION * 1) {      //Always have 1 second intervals
            this.acceleration = MAX_ACCELERATION * 1;
        }
        
        //5)   If the brakes are activated, this takes precedent and the train slows down according to which brake is activated
        if(this.emergencyBrakeActivated || this.emergencyBrakeOverride) {
            this.acceleration = EMERGENCY_BRAKE_DECEL * 1;
        }
        else if (this.serviceBrakeActivated) {
            this.acceleration = SERVICE_BRAKE_DECEL * 1;
        }        
        //System.out.println("Accel: " + this.acceleration);
        
        //vf = vi + a*t  -> Final velocity (speed) equals initial velocity + acceleration * time
        //Calculate current speed in m/s
        this.currSpeed = this.lastSpeed + this.acceleration * 1;    //Find seconds by dividing elapsed time by a billion
        //Check for negative speed (impossible)
        if(this.currSpeed < 0) {
            this.currSpeed = 0;
        }
        
        //6)   Calculate distance traveled since last cycle and subtract that from remaining authority  -> s = vi*t + 1/2*a*t^2
        this.distanceTraveledMeters = this.lastSpeed * 1 + (1/2 * this.acceleration * Math.pow(1, 2));    //In meters
        this.distanceTraveledFeet = this.distanceTraveledMeters * FEET_IN_A_METER;        //Convert to feet
        //System.out.println("Distance: " + this.distanceTraveled + "\n");
        
        //Subtract the distance traveled from remaining authority
        this.authority -= this.distanceTraveledFeet;
        
        //Assign the last speed to the current speed for the next cycle
        this.lastSpeed = this.currSpeed;                                                                
                
        //Current speed is in m/s, convert to mph to print out
        this.currSpeedMPH = this.currSpeed * SECONDS_IN_AN_HOUR / METERS_IN_A_MILE;
        
        //7)   Calculate distance into block and ask for new block if we've gone the length of the block
        this.distanceIntoBlock += this.distanceTraveledFeet;
        
        //Set the occupancy to true each update
        this.currBlock.setOccupancy(true);                                
        
        //If we've passsed into the next block, get the next block
        if(this.distanceIntoBlock > currBlock.getLength()) {
            
            //Set new distnace into block
            this.distanceIntoBlock -= currBlock.getLength();
            //Set the occupancy to false as we leave
            this.currBlock.setOccupancy(false); 
            //Get next TrackPiece. Should be Block or Yard
            this.nextBlock = this.currBlock.getNext(this.prevBlock);
            
            System.out.println("Train " + this.trainID + ": Line " + currBlock.getLine() + ", Section " + currBlock.getSection() + ", Block: " + this.currBlock + " (prev: " + this.prevBlock + ", next: " + this.nextBlock + ")");
            
            if(this.nextBlock != null) {

                //Normal execution
                if(this.nextBlock.getType() == Global.PieceType.BLOCK) {
                    this.currTempBlock = this.currBlock;
                    this.currBlock = (Block) this.nextBlock;
                    this.prevBlock = this.currTempBlock;
                    this.currBlock.setOccupancy(true);
                    this.trainSystem.getTrackControllerHandler().updateTrack();
                    this.trainSystem.getTrackModel().updateGUI();
                    
                    //Check for beacon and send it to the Track Controller
                    if(this.currBlock.getBeacon() != null) {
                        this.trainController.receieveBeacon(this.currBlock.getBeacon());
                    }
                }
                //Train has reached end of track
                else if (this.nextBlock.getType() == Global.PieceType.YARD) {
                    System.out.println("Train has reached the yard");
                }
            }
            else {
               System.err.println("Train cannot get TrackPiece following Block " + currBlock);    
            }
            
        }
        
//        if(this.authority <= 0)
//            this.trainSystem.getTrackControllerHandler().updateTrack();
        //System.out.println("Safe stopping distance: " + this.safeStoppingDistance());
    }
    
    /**
     * This method checks the track circuit if there is a new S&A to pass to the Train Controller
     * @author Jonathan Kenneson
     */
    private void checkTrackCircuit() {
        byte newSetSpeed = this.currBlock.getTrackCircuit().speed;
        short newAuthority = this.currBlock.getTrackCircuit().authority;
        
        System.out.println("Track Circuit for Train " + this.trainID + " -> Speed: " + newSetSpeed + " Authority: " + newAuthority);
        
        //Safe to proceed, tell Train Controller we can proceed safely
        if(newSetSpeed == 0 && newAuthority == 0) {
            this.trainController.safeToProceed(true);
        }
        //Tell Train Controller to full stop
        else if(newSetSpeed == -1 && newAuthority == -1) {
            this.trainController.safeToProceed(false);
        }
        //Give Train Controller updated speed and authority
        else {
            this.trainController.setAuthority((short)(newAuthority/this.FEET_IN_A_METER));
            this.trainController.setSpeed(newSetSpeed);
            
            //Update the Train Model's authority and ctcSetSpoint
            this.authority = newAuthority;
            this.ctcSetPoint = newSetSpeed;
            
            //Reset the speed and authority of the track circuit
            this.currBlock.getTrackCircuit().speed = 0;
            this.currBlock.getTrackCircuit().authority = 0;
        }
    }
    
    /**
     * This method is called from the Track Controller when the train is at a station block with 0 speed
     * Will return if there is no station
     * @author Jonathan Kenneson
     * @param doorSide false - left side, true - right side
     */
    public void boardPassengers(boolean doorSide) {
        //Return if we are not on a station block
        Station currStation = this.currBlock.getStation();
        if(currStation == null) {
            return;
        }
        
        //First, open the doors
        if(doorSide) {
            this.setRightDoorOpen(true);
        }
        else {
            this.setLeftDoorOpen(true);
        }
        
        //Pick a random number of passengers on board to exit
        Random randDepart = new Random();
        int numToDepart = randDepart.nextInt(this.passengersOnBaord);
        this.passengersOnBaord -= numToDepart;
        
        //Ask the station for a number of passengers to enter
        int passengersToEnter = currStation.boardPassengers(this.passengersOnBaord);
        this.passengersOnBaord += passengersToEnter;
        
        //To wrap up, close the doors and return
        if(doorSide) {
            this.setRightDoorOpen(false);
        }
        else {
            this.setLeftDoorOpen(false);
        }
    }
            
    /**
     * This method will return the stopping distance of the train based on the current speed and the service brake activating
     * @author Jonathan Kenneson
     * @return The distance (in feet) it will take to fully stop the train with the service brake applied
     */
    public double safeStoppingDistance() {
        /* Some nice equations:
         * vf = vi + a*t
         * s = vi*t + 1/2*a*t^2
        */
        
        //Declare variables
        double vf = 0.0;
        double vi = this.currSpeed;
        double a = this.SERVICE_BRAKE_DECEL;
        double t, s;
        
        //Find the time to stop
        t = (vf - vi) / a;
        
        //Find the safe stopping distance in meters
        s = vi*t + (1/2)*a*(Math.pow(t, 2));
        
        double safeStoppingDistanceFeet = s * this.FEET_IN_A_METER;
        return safeStoppingDistanceFeet;
    }
    
    /**
     * Returns the StringID
     * @author Jonathan Kenneson
     * @return The TrainID
     */
    public String toString() {
        return String.valueOf(this.trainID);
    }
    
    
    
    
    
    
    
    
    
    /**
     * Following are all getters and setters for the TrainModel class
     * 
     * @author Jonathan Kenneson
     * @return 
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
        //Check to make sure there is no power failure
        if(!this.powerFailure) {
            this.leftDoorOpen = leftDoorOpen;
        }
    }

    public boolean isRightDoorOpen() {
        return rightDoorOpen;
    }

    public void setRightDoorOpen(boolean rightDoorOpen) {
        //Check to make sure there is no power failure
        if(!this.powerFailure) {
            this.rightDoorOpen = rightDoorOpen;
        }
    }

    public boolean isLightsOn() {
        return lightsOn;
    }

    public void setLightsOn(boolean lightsOn) {
        //Check to make sure there is no power failure
        if(!this.powerFailure) {
            this.lightsOn = lightsOn;
        }
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        //Check to make sure there is no power failure
        if(!this.powerFailure) {
            this.temperature = temperature;
        }
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

    public double getCurrSpeed() {
        return currSpeed;
    }

    public void setCurrSpeed(int currSpeed) {
        this.currSpeed = currSpeed;
    }

    public double getCurrSpeedMPH() {
        return currSpeedMPH;
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

    public double getAuthority() {
        return authority;
    }

    public void setAuthority(double authority) {
        this.authority = authority;
        this.trainController.setAuthority((short)(authority/FEET_IN_A_METER));
    }
    
    public double getDistanceTraveledMeters() {
        return this.distanceTraveledMeters;
    }
    
    public void setDistanceTraveledMeters(double distanceTravled) {
        this.distanceTraveledMeters = distanceTravled;
    }

    public double getDistanceTraveledFeet() {
        return this.distanceTraveledFeet;
    }
    
    public void setDistanceTraveledFeet(double distanceTravled) {
        this.distanceTraveledFeet = distanceTravled;
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

    public double getTrainWeight() {
        return trainWeight;
    }

    public void setTrainWeight(int trainWeight) {
        this.trainWeight = trainWeight;
    }

    public double getTrainLength() {
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
    
    public TrainModelGUI getTrainModelGUI() {
        return this.trainModelGUI;
    }
    
    public Block getCurrBlock() {
        return this.currBlock;
    }
}
