/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import java.util.ArrayList;

/**
 * SwitchState class holds and maps information for a particular switch to the default and 
 * alternate connections and lights
 * @author kylemonto
 */
public class SwitchState {
    
    private Global.SwitchState switchState;
    private TrackConnection defaultConnection1;
    private TrackConnection defaultConnection2;
    private ArrayList<Light> lightsDefault1;
    private ArrayList<Light> lightsDefault2;
    private ArrayList<Light> lightsAlternate;
    private TrackConnection alternateConnection;

    public SwitchState(TrackConnection trackConnectionDefault,  TrackConnection trackConnectionAlternate) {
        this.defaultConnection1 = trackConnectionDefault;
        this.alternateConnection = trackConnectionAlternate;
    }


    public SwitchState(Global.SwitchState switchState, TrackConnection trackConnectionDefault, ArrayList<Light> lightsDefault, TrackConnection trackConnectionAlternate, ArrayList<Light> lightsAlternate) {
        this.switchState = switchState;
        this.defaultConnection1 = trackConnectionDefault;
        this.defaultConnection2 = null;
        this.lightsDefault1 = lightsDefault;
        this.lightsDefault2 = null;
        this.alternateConnection = trackConnectionAlternate;
        this.lightsAlternate = lightsAlternate;
    }
    
    public SwitchState(Global.SwitchState switchState, TrackConnection trackConnectionDefault1, ArrayList<Light> lightsDefault1, TrackConnection trackConnectionDefault2, ArrayList<Light> lightsDefault2, TrackConnection trackConnectionAlternate, ArrayList<Light> lightsAlternate) {
        this.switchState = switchState;
        this.defaultConnection1 = trackConnectionDefault1;
        this.defaultConnection2 = trackConnectionDefault2;
        this.lightsDefault1 = lightsDefault1;
        this.lightsDefault2 = lightsDefault2;
        this.alternateConnection = trackConnectionAlternate;
        this.lightsAlternate = lightsAlternate;
    }

    public Global.SwitchState getSwitchState() {
        return switchState;
    }

    public void setSwitchState(Global.SwitchState switchState) {
        this.switchState = switchState;
    }

    public TrackConnection getDefaultConnection() {
        return defaultConnection1;
    }

    public void setDefaultConnection(TrackConnection defaultConnection) {
        this.defaultConnection1 = defaultConnection;
    }


    public TrackConnection getAlternateConnection() {
        return alternateConnection;
    }

    public void setAlternateConnection(TrackConnection alternateConnection) {
        this.alternateConnection = alternateConnection;
    }

    public ArrayList<Light> getLightsDefault() {
        return lightsDefault1;
    }

    public ArrayList<Light> getLightsAlternate() {
        return lightsAlternate;
    }
    
    


    
    


    
}
