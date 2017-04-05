/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

/**
 *
 * @author Dan
 */
public class Crossing {
    
    //Default state is raised
    private boolean state = false;
    
    /**
    * Get state of crossing
    * @author Dan Bednarczyk
    * @return boolean true for lowered, false for raised
    */
    public boolean getState() {
        return state;
    }
    
    /**
    * Set state of crossing
    * @author Dan Bednarczyk
    * @param newState boolean true for lowered, false for raised
    */
    public void setState(boolean newState) {
        state = newState;
    }
    
    /**
    * Raise crossing (set state to false)
    * @author Dan Bednarczyk
    */
    public void raise() {
        state = false;
    }
    
    /**
    * Lower crossing (set state to true)
    * @author Dan Bednarczyk
    */
    public void lower() {
        state = true;
    }
    
}
