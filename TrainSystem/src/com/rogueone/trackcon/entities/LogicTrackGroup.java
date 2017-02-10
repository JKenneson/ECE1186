/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author kylemonto
 */
public class LogicTrackGroup {
    
    private ArrayList<Switch> switches;
    private StateSet currentTrackState;
    private HashMap<StateSet, UserSwitchState> stateMapping;
    
    public LogicTrackGroup(){
        this.switches = new ArrayList<Switch>();
        this.stateMapping = new HashMap<StateSet, UserSwitchState>();
    }

    public ArrayList<Switch> getSwitches() {
        return switches;
    }

    public void addSwitches(Switch switches) {
        this.switches.add(switches);
    }

    public void setCurrentTrackState(StateSet currentTrackState) {
        this.currentTrackState = currentTrackState;
    }

    public StateSet getCurrentTrackState() {
        return currentTrackState;
    }
    

    public void addTrackState(StateSet stateSet, UserSwitchState switchState){
        stateMapping.put(stateSet, switchState);
    }
    
    public UserSwitchState getSwitchState(StateSet stateSet){
        return stateMapping.get(stateSet);
    }
    
    public HashMap<StateSet, UserSwitchState> getStateMapping(){
        return stateMapping;
    }
    
    
}
