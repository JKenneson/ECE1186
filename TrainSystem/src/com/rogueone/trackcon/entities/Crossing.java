/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
    private LinkedList<Integer> activeBlocks;

    public Crossing(Global.Line line, Global.Section section, int block, HashMap<Global.CrossingState, Global.LightState> crossingStates, Global.CrossingState crossingState, String activeBlocks) {
        this.line = line;
        this.section = section;
        this.blockID = block;
        this.crossingState = crossingStates;
        this.currentCrossingState = crossingState;
        this.activeBlocks = new LinkedList<Integer>();
        List<String> blockList = Arrays.asList(activeBlocks.split(","));
        for(String s : blockList){
            this.activeBlocks.add(Integer.parseInt(s));
        }
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

    public LinkedList<Integer> getActiveBlocks() {
        return activeBlocks;
    }
    
    

}
