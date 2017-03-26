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
    private TrackConnection defaultConnection;
    private ArrayList<Light> lightsDefault;
    private ArrayList<Light> lightsAlternate;
    private TrackConnection alternateConnection;

    public SwitchState(TrackConnection trackConnectionDefault,  TrackConnection trackConnectionAlternate) {
        this.defaultConnection = trackConnectionDefault;
        this.alternateConnection = trackConnectionAlternate;
    }


    public SwitchState(Global.SwitchState switchState, TrackConnection trackConnectionDefault, ArrayList<Light> lightsDefault, TrackConnection trackConnectionAlternate, ArrayList<Light> lightsAlternate) {
        this.switchState = switchState;
        this.defaultConnection = trackConnectionDefault;
        this.lightsDefault = lightsDefault;
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
        return defaultConnection;
    }

    public void setDefaultConnection(TrackConnection defaultConnection) {
        this.defaultConnection = defaultConnection;
    }


    public TrackConnection getAlternateConnection() {
        return alternateConnection;
    }

    public void setAlternateConnection(TrackConnection alternateConnection) {
        this.alternateConnection = alternateConnection;
    }

    public ArrayList<Light> getLightsDefault() {
        return lightsDefault;
    }

    public ArrayList<Light> getLightsAlternate() {
        return lightsAlternate;
    }
    
    


    
    


    
}
