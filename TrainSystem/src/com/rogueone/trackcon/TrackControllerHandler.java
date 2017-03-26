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

/**
 * TrackControllerHandler will manage the track controllers across the system
 * @author kylemonto
 */
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

    /**
     * Getter for the Track Handler's GUI
     * @return TrackHandlerGUI object
     */
    public TrackHandlerGUI getTrackHandlerGUI() {
        return trackHandlerGUI;
    }

    /**
     * Getter for the list of Track Controllers in within the Track Handler
     * @return 
     */
    public ArrayList<TrackController> getTrackControllers() {
        return trackControllers;
    }

    /**
     * Getter that returns a HashMap that maps Track Sections to Track Groups
     * @return HashMap of Track Sections to TrackGroups
     */
    public HashMap<Global.Section, Global.TrackGroups> getSectionToGroupMapping() {
        return sectionToGroupMapping;
    }

    /**
     * Getter that returns a HashMap that maps Track Groups to Logic Groups
     * @return HashMap of TrackGroups to LogicGroups
     */
    public HashMap<Global.TrackGroups, Global.LogicGroups> getGroupToLogicMapping() {
        return groupToLogicMapping;
    }
    
    /**
     * Method the CTC will call to dispatch a train
     * @param line - line that a train should be dispatched to
     * @return 
     */
    public boolean requestDispatch(Global.Line line){
        boolean returnValue = false;
        if(line == Global.Line.GREEN){
            returnValue = trackControllers.get(0).canDispatchProceed();
        } else {
//            returnValue = trackControllers.get(1).canDispatchProceed();
        }
        return returnValue;
    }
    
    /**
     * Method the CTC will call to request Maintenance
     * @param line - line that maintenance request should service
     * @param blockID - blockID that needs maintenance
     * @return 
     */
    public boolean requestMaintenance(Global.Line line, int blockID){
        boolean returnValue = false;
        if(line == Global.Line.GREEN){
            returnValue = trackControllers.get(0).canClose(blockID);
        } else {
//            returnValue = trackControllers.get(1).canClose(blockID);
        }
        return returnValue;
    }
    
    /**
     * Method the CTC will call to request Maintenance
     * @param line - requested line that needs to be opened
     * @param blockID - block that is requesting to be opened
     * @return 
     */
    public boolean requestOpen(Global.Line line, int blockID){
        boolean returnValue = false;
        if(line == Global.Line.GREEN){
            returnValue = trackControllers.get(0).canOpen(blockID);
        } else {
//            returnValue = trackControllers.get(1).canOpen(blockID);
        }
        return returnValue;
    }

    /**
     * Function that will update the track controllers when called
     * as of now, this function is called whenever a train moves from
     * 1 track block to another
     */
    public void updateTrack() {
        TrackController trackController = trackControllers.get(0);
//        trackController.evaluateProceed();
        trackController.evaluateSwitches();
        trackController.updateSummaryTab();
//        trackController.evaluateCrossing();
    }
    
    
    
    
    
    
    
    
}
