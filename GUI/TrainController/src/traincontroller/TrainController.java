/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traincontroller;

/**
 *
 * @author Tyler
 */


public class TrainController {

    public int systemMode; // 0 == Manual and 1 == Automatic
    public int airConditioning; // 0 == Off and 1 == On and 2 == Failure
    public int heat; // 0 == Off and 1 == On and 2 == Failure
    public int lights; // 0 == Off and 1 == On and 2 == Failure
    public int leftDoor; // 0 == Closed and 1 == Open and 2 == Failure
    public int rightDoor; // 0 == Closed and 1 == Open and 2 == Failure
    public int serviceBrake; // 0 == Closed and 1 == Open and 2 == Failure
    public int emergencyBrake; // 0 == Closed and 1 == Open and 2 == Failure
    
    public float requestedSetSpeed;
    public float actualSetSpeed; 
    public float powerCommand;
        
    
    public int trainSelection; //Value comes from drop down menu
    
    /**
     * @param args the command line arguments
     */
    public void main(String[] args) {
        // TODO code application logic here
        
        if(GetSystemMode()==1){ //Automatic Mode
            
        }
        
        else{ //Manual Mode i.e. take inputs from conductor.
            
        }
    }
    
   /**
    * This method is used to obtain the current state of
    * the system.
    *
    * @author Tyler Protivnak
    * @return systemMode Return 0 for manual and 1 for automatic
    */
    public int GetSystemMode(){
        return systemMode;
    }
    
   /**
    * This method is used to obtain the current state of
    * the system.
    *
    * @author Tyler Protivnak
    * @param newSetSpeed speed to be set
    * @return systemMode 0 for failure and 1 for true
    */
    public int setSetSpeed(float newSetSpeed){
        
        return 0;
    }
    
    public int systemFailureDetection(){
        
        return 0;
    }
    
}
