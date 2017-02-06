/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackmodel;

/**
 *
 * @author Dan
 */
public class Switch extends Block {
    
    private int centralSwitchID;
    private Block portC;
    private boolean activated;
    
    public Switch(int newCentralSwitchID) {
        //intialize block-specific fields, most are not used by switch 
        super(null, null, -1, -1, false, null, -1, -1, -1, -1, false, false, false, false);
        //initialize switch-specific fields
        centralSwitchID = newCentralSwitchID;
        portA = null;   //static port
        portB = null;   //default dependent port
        portC = null;   //alternate dependent port
        activated = false;
    }
    
    public void setPortA(Block port){
        portA = port;
    }
    
    public void setPortB(Block port){
        portB = port;
    }
    
    public void setPortC(Block port){
        portC = port;
    }
    
    public void toggleSwitch() {
        activated = !activated;
    }
    
    public void setSwitch(boolean activate) {
        activated = activate;
    }
    
    public int getID() {
        return centralSwitchID;
    }
    
    public Block getNextBlock(Block previousBlock) {
        //entering from a dependent block, exiting the static port
        if (previousBlock.equals(portB) || previousBlock.equals(portC)) {
            return portA;
        }
        //entering from the static port, exiting the default dependent port
        else if (previousBlock.equals(portA) && !activated) {
            return portB;
        }
        //entering from the static port, exiting the alternate dependent port
        else if (previousBlock.equals(portA) && !activated) {
            return portC;
        }
        //invalid block
        else {
            return null;
        }
    }
    
}