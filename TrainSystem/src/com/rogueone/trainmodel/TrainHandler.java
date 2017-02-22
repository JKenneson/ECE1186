/**
 * The train handler class will hold a hashmap of all trains hashed by TrainID for easy lookup
 * This class will handle all communication between outside modules to the trains in the handler
 *
 * @author Jonathan Kenneson
 * @Creation 2/13/17
 * @Modification 2/13/17
 */
package com.rogueone.trainmodel;

import com.rogueone.trainmodel.gui.TrainModelGUI;
import java.util.ArrayList;

/**
 * Class declaration for TrainHandler
 *
 * @author Jon Kenneson
 */
public class TrainHandler {
    
    private static ArrayList<TrainModel> trains = new ArrayList<TrainModel>();
    
    /**
     * Main function currently tests the functionality of the Train Model class independent from the other modules
     * The user may perform extensive testing of this module through the module's GUI
     * Also, small functionality from the TrainController may be implemented here
     * 
     * @author Jonathan Kenneson
     * @param args 
     */
    public static void main(String[] args) throws InterruptedException  { 
        
        //trainModelInit();
        
        trainModelAndControllerInit();
        
    }
    
    /**
     * Dispatch a new train with a set point speed, authority, number of cars, and the line (Red or Green)
     * 
     * @author Jonathan Kenneson
     * @param setPoint The initial CTC Set Point Speed
     * @param authority The initial CTC authority
     * @param numCars The number of cars on the train (1 or 2)
     * @param line  The line the train will be traveling on (Red or Green)
     */
    public void dispatchNewTrain(int setPoint, int authority, int numCars, String line) {
        trains.add(new TrainModel(setPoint, authority, numCars, line));
    }
    
    /**
     * Update all the trains along the track, this will calculate all new speeds and distances for all trains
     * This function also updates all the train controllers attached to the trains
     */
    public void updateTrains() {
        for (TrainModel train : TrainHandler.trains) {
            train.updateTrain();
            train.UpdateGUI(train.getTrainModelGUI());
            train.updateTrainControllerGUI();
        }
    }
    
    public static void trainModelAndControllerInit() throws InterruptedException {
        TrainModel trainModelTest1 = new TrainModel(0, 40000, 1, "Green");                              //Create a new TrainModel object with a set point speed of 40, authority of 40000, and 1 car
        TrainModelGUI trainModelGUITest1 = trainModelTest1.CreateGUIObject(trainModelTest1);    //Instantiate a GUI for this train
        
        trainModelTest1.createTrainController();                                                //Create and attach a TrainController to this train
        trainModelTest1.createTrainControllerGUI();                                             //Create a TrainControllerGUI
        
        
        //Constantly update velocity then the GUI
        while(true){
            trainModelTest1.updateTrain();
            trainModelTest1.UpdateGUI(trainModelGUITest1);
            trainModelTest1.updateTrainControllerGUI();
            if(trainModelGUITest1.isDisplayable() == false) {
                System.exit(0);
            }
            Thread.sleep(1000);
        }
    }
    
    
    public static void trainModelInit () throws InterruptedException {
        //Create a new TrainModel object with a set point speed of 40, authority of 40000, and 1 car
        TrainModel trainModelTest1 = new TrainModel(40, 40000, 1, "Green");
        //Instantiate a GUI for this train
        TrainModelGUI trainModelGUITest1 = trainModelTest1.CreateGUIObject(trainModelTest1);
        
        //Constantly update velocity then the GUI
        while(true){
            trainModelTest1.updateTrain();
            trainModelTest1.UpdateGUI(trainModelGUITest1);
            if(trainModelGUITest1.isDisplayable() == false) {
                System.exit(0);
            }
            Thread.sleep(1000);
        }
    }


    public TrainHandler() {
        
    }
    
    
    
    
}
