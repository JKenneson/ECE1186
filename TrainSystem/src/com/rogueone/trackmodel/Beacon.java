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
public class Beacon {
    
    private String message;
    private boolean rightSide;
    
    public Beacon(String newMessage, boolean newRightSide) {
        message = newMessage;
        rightSide = newRightSide;
    }
    
    public String getMessage() {
        return message;
    }
    
    public boolean onRight() {
        return rightSide;
    }
    
    public boolean onLeft() {
        return !rightSide;
    }
    
}
