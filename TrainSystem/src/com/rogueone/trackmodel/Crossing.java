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
    
    private boolean state = false;
    
    public boolean getState() {
        return state;
    }
    
    public void setState(boolean newState) {
        state = newState;
    }
    
    public void raise() {
        state = false;
    }
    
    public void lower() {
        state = true;
    }
    
}
