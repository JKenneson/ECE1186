/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;

/**
 *
 * @author kylemonto
 */
public class SwitchState {
    
    private Global.SwitchState switchState;
    private TrackConnection defaultConnection;
    private Light light1Default;
    private Light light2Default;
    private TrackConnection alternateConnection;
    private Light light1Alternate;
    private Light light2Alternate;

    public SwitchState(TrackConnection trackConnectionDefault, Light light1Default, Light light2Default, TrackConnection trackConnectionAlternate, Light light1Alternate, Light light2Alternate) {
        this.defaultConnection = trackConnectionDefault;
        this.light1Default = light1Default;
        this.light2Default = light2Default;
        this.alternateConnection = trackConnectionAlternate;
        this.light1Alternate = light1Alternate;
        this.light2Alternate = light2Alternate;
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

    public Light getLight1Default() {
        return light1Default;
    }

    public void setLight1Default(Light light1Default) {
        this.light1Default = light1Default;
    }

    public Light getLight2Default() {
        return light2Default;
    }

    public void setLight2Default(Light light2Default) {
        this.light2Default = light2Default;
    }

    public TrackConnection getAlternateConnection() {
        return alternateConnection;
    }

    public void setAlternateConnection(TrackConnection alternateConnection) {
        this.alternateConnection = alternateConnection;
    }

    public Light getLight1Alternate() {
        return light1Alternate;
    }

    public void setLight1Alternate(Light light1Alternate) {
        this.light1Alternate = light1Alternate;
    }

    public Light getLight2Alternate() {
        return light2Alternate;
    }

    public void setLight2Alternate(Light light2Alternate) {
        this.light2Alternate = light2Alternate;
    }
    
    


    
}
