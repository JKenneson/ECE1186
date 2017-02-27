/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon;

import com.rogueone.global.Global;
import com.rogueone.trainsystem.TrainSystem;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackControllerHandler {
    
    private TrainSystem trainSystem = null;
    private TrackHandlerGUI trackHandlerGUI = null;
    private ArrayList<TrackController> trackControllers = new ArrayList<TrackController>();
    private HashMap<Global.Section, Global.TrackGroups> sectionToGroupMapping;
    private HashMap<Global.TrackGroups, Global.LogicGroups> groupToLogicMapping;
    
    public TrackControllerHandler(TrainSystem ts) {
        trainSystem = ts;
        TrackController greenTrackController = new TrackController(Global.Line.GREEN, this.trainSystem);
        TrackController redTrackController = new TrackController(Global.Line.RED, this.trainSystem);
        trackControllers.add(greenTrackController);
        trackControllers.add(redTrackController);
        
        sectionToGroupMapping = new HashMap<Global.Section, Global.TrackGroups>();
        for(Global.Section s : Global.Section.values()){
            for(Global.TrackGroups tg : Global.TrackGroups.values()){
                if(tg.toString().contains(s.toString())){
                    sectionToGroupMapping.put(s, tg);
                    break;
                }
            }
        }
        
        groupToLogicMapping = new HashMap<Global.TrackGroups, Global.LogicGroups>();
        for(Global.TrackGroups tg : Global.TrackGroups.values()){
            if(tg.equals(Global.TrackGroups.ABC) || tg.equals(Global.TrackGroups.DEF) || tg.equals(Global.TrackGroups.RSTUVWXYZ)){
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_1_2 );
            } else if(tg.equals(Global.TrackGroups.KLM) || tg.equals(Global.TrackGroups.N) || tg.equals(Global.TrackGroups.OPQ)){
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_4_5 );
            } else if(tg.equals(Global.TrackGroups.GHI) || tg.equals(Global.TrackGroups.ZZ) ){
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_3 );
            } else if(tg.equals(Global.TrackGroups.YY) || tg.equals(Global.TrackGroups.J) ){
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_0 );
            }
        }
        
        trackHandlerGUI = new TrackHandlerGUI(this);
    }

    public TrackHandlerGUI getTrackHandlerGUI() {
        return trackHandlerGUI;
    }

    public ArrayList<TrackController> getTrackControllers() {
        return trackControllers;
    }

    public HashMap<Global.Section, Global.TrackGroups> getSectionToGroupMapping() {
        return sectionToGroupMapping;
    }

    public HashMap<Global.TrackGroups, Global.LogicGroups> getGroupToLogicMapping() {
        return groupToLogicMapping;
    }

    public void updateTrack() {
        TrackController trackController = trackControllers.get(0);
        trackController.updatePresence();
    }
    
    
    
    
    
    
    
    
}
