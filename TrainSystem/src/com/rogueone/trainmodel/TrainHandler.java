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

/**
 * Class declaration for TrainHandler
 *
 * @author Jon Kenneson
 */
public class TrainHandler {
    
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
    
    public static void trainModelAndControllerInit() throws InterruptedException {
        //Create a new TrainModel object with a set point speed of 40, authority of 40000, and 1 car
        TrainModel trainModelTest1 = new TrainModel(40, 40000, 1);
        //Instantiate a GUI for this train
        TrainModelGUI trainModelGUITest1 = trainModelTest1.CreateGUIObject(trainModelTest1);
        
        //Constantly update velocity then the GUI
        while(true){
            trainModelTest1.updateTrain();
            trainModelTest1.UpdateGUI(trainModelGUITest1);
            if(trainModelGUITest1.isDisplayable() == false) {
                break;
            }
            Thread.sleep(1000);
        }
    }
    
    
    public static void trainModelInit () throws InterruptedException {
        //Create a new TrainModel object with a set point speed of 40, authority of 40000, and 1 car
        TrainModel trainModelTest1 = new TrainModel(40, 40000, 1);
        //Instantiate a GUI for this train
        TrainModelGUI trainModelGUITest1 = trainModelTest1.CreateGUIObject(trainModelTest1);
        
        //Constantly update velocity then the GUI
        while(true){
            trainModelTest1.updateTrain();
            trainModelTest1.UpdateGUI(trainModelGUITest1);
            if(trainModelGUITest1.isDisplayable() == false) {
                break;
            }
            Thread.sleep(1000);
        }
    }
    
    
    
    
}
