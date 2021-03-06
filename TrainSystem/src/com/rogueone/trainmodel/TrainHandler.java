/**
 * The train handler class will hold a hashmap of all trains hashed by TrainID for easy lookup
 * This class will handle all communication between outside modules to the trains in the handler
 *
 * @author Jonathan Kenneson
 * @Creation 2/13/17
 * @Modification 3/27/17
 */
package com.rogueone.trainmodel;

import com.rogueone.trainmodel.gui.TrainHandlerGUI;
import com.rogueone.trainmodel.gui.TrainModelGUI;
import com.rogueone.trainsystem.TrainSystem;
import java.util.ArrayList;

/**
 * Class declaration for TrainHandler
 *
 * @author Jon Kenneson
 */
public class TrainHandler {
    
    private TrainSystem trainSystem;
    private static ArrayList<TrainModel> trains = new ArrayList<TrainModel>();
    private TrainHandlerGUI trainHandlerGUI;
    
    
    public TrainHandler(TrainSystem trainSystem) {
        this.trainSystem = trainSystem;
        this.trainHandlerGUI = new TrainHandlerGUI(this);
    }
    
    /**
     * Dispatch a new train with a set point speed, authority, number of cars, and the line (Red or Green)
     * 
     * @author Jonathan Kenneson
     * @param setPoint The initial CTC Set Point Speed
     * @param authority The initial CTC authority
     * @param numCars The number of cars on the train (1 or 2)
     * @param line  The line the train will be traveling on (Red or Green)
     * @param trainID The ID of the train
     */
    public void dispatchNewTrain(int setPoint, int authority, int numCars, String line, int trainID) {
        TrainModel trainToAdd = new TrainModel(setPoint, authority, numCars, line, this.trainSystem, trainID);
        trainToAdd.CreateGUIObject(trainToAdd);
        trainToAdd.createTrainController();
        trainToAdd.createTrainControllerGUI();
        
        trains.add(trainToAdd);
    }
    
    /**
     * Update all the trains along the track, this will calculate all new speeds and distances for all trains
     * This function also updates all the train controllers attached to the trains
     * 
     * @author Jonathan Kenneson
     */
    public void updateTrains() {
        //Check to see if we need to delete any trains
        ArrayList<TrainModel> trainsToDelete = new ArrayList<TrainModel>();
        
        int arraySize = trains.size();
        
        for(int i = 0 ; i < arraySize ; i++) {
            trains.get(i).updateTrain();
            trains.get(i).UpdateGUI(trains.get(i).getTrainModelGUI());
            trains.get(i).trainController.updateController();
            trains.get(i).updateTrainControllerGUI();
            
            if(trains.get(i).getReachedYard()) {
                trainsToDelete.add(trains.get(i));
            }
        }
        
        //Remove the selected trains and tell the CTC
        for (TrainModel trainToRemove : trainsToDelete) {
            this.trainSystem.getCTC().incrementTrainsToYard(trainToRemove.getTotalDistanceTraveledFeet(), trainToRemove.getLine());
            this.trains.remove(trainToRemove);
            this.trainSystem.getCTC().removeTrainFromTable(trainToRemove.trainID);
        }
    }
    
    
    
    /**
     * Returns the Train Handler GUI
     * @author Jonathan Kenneson
     * @return The TrainHandlerGUI that is attached to this handler
     */
    public TrainHandlerGUI getGUI() {
        return this.trainHandlerGUI;
    }

    /**
     * Displays the GUIs based on trainID passed in
     * @author Jonathan Kenneson
     * @param trainID The ID of the train to display
     */
    public void showObjectsFromCTC(int trainID) {
        for (TrainModel train : TrainHandler.trains) {
            if(train.trainID == trainID) {  //If we find the trainID, display the GUIs
                train.showGUIObject();
                train.trainController.showGUIObject();
            }
        }
    }
    
    /**
     * Returns the ArrayList of trains
     * @author Jonathan Kenneson
     * @return ArrayList of trains
     */
    public ArrayList<TrainModel> getTrains() {
        return trains;
    }
    
    /**
     * Returns the current block of a train specified by the ID passed in
     * 
     * @author Jonathan Kenneson
     * @param trainID The ID of the train
     * @return The formatted current block output, or an empty string if the trainID is not here
     */
    public String getBlockForTrain(int trainID) {
        for (TrainModel train : TrainHandler.trains) {
            if(train.trainID == trainID) {  //If we find the trainID, display the GUIs
                StringBuilder sb = new StringBuilder();
                sb.append(train.getCurrBlock().getSection().getSectionID());
                sb.append(":");
                sb.append(train.getCurrBlock().getID());
                return sb.toString();
            }
        }
        
        return "null";
    }
    
    
    /**
     * Main function currently tests the functionality of the Train Model class independent from the other modules
     * The user may perform extensive testing of this module through the module's GUI
     * Also, small functionality from the TrainController may be implemented here
     * 
     * @author Jonathan Kenneson
     * @param args 
     */
    /*
    public static void main(String[] args) throws InterruptedException  { 
        
        //trainModelInit();
        
        //trainModelAndControllerInit();
        
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
    */

    
    
    
    
    
}
