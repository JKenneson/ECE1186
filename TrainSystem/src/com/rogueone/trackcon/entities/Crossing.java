/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import java.util.HashMap;

/**
 * Crossing class that stores information pertinent to crossings in the system
 * @author kylemonto
 */
public class Crossing {

    private Global.Line line;
    private Global.Section section;
    private int blockID;
    private HashMap<Global.CrossingState, Global.LightState> crossingState;
    private Global.CrossingState currentCrossingState;

    public Crossing(Global.Line line, Global.Section section, int block, HashMap<Global.CrossingState, Global.LightState> crossingStates, Global.CrossingState crossingState) {
        this.line = line;
        this.section = section;
        this.blockID = block;
        this.crossingState = crossingStates;
        this.currentCrossingState = crossingState;
    }

    public Global.Line getLine() {
        return line;
    }

    public Global.Section getSection() {
        return section;
    }

    public int getBlockID() {
        return blockID;
    }

    public HashMap<Global.CrossingState, Global.LightState> getCrossingState() {
        return crossingState;
    }

    public Global.CrossingState getCurrentCrossingState() {
        return currentCrossingState;
    }

    public void setCurrentCrossingState(Global.CrossingState currentCrossingState) {
        this.currentCrossingState = currentCrossingState;
    }

}
