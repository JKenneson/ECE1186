/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon;

import com.rogueone.global.Global;
import com.rogueone.trainsystem.TrainSystem;
import java.util.ArrayList;

public class TrackControllerHandler {
    
    TrainSystem trainSystem = null;
    TrackHandlerGUI trackHandlerGUI = null;
    private ArrayList<TrackController> trackControllers = new ArrayList<TrackController>();
    
    public TrackControllerHandler(TrainSystem ts) {
        trainSystem = ts;
        TrackController greenTrackController = new TrackController(Global.Line.GREEN, this.trainSystem);
        TrackController redTrackController = new TrackController(Global.Line.RED, this.trainSystem);
        trackControllers.add(greenTrackController);
        trackControllers.add(redTrackController);
        trackHandlerGUI = new TrackHandlerGUI(this);
    }

    public TrackHandlerGUI getTrackHandlerGUI() {
        return trackHandlerGUI;
    }

    public ArrayList<TrackController> getTrackControllers() {
        return trackControllers;
    }
    
    
    
    
    
}
