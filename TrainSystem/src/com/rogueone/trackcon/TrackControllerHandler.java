/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon;

import com.rogueone.global.Global;
import com.rogueone.trainsystem.TrainSystem;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * TrackControllerHandler will manage the track controllers across the system
 *
 * @author kylemonto
 */
public class TrackControllerHandler {

    private TrainSystem trainSystem = null;
    private TrackHandlerGUI trackHandlerGUI = null;
    private ArrayList<TrackController> trackControllers = new ArrayList<TrackController>();
    private HashMap<Global.Section, Global.TrackGroupsGreen> sectionToGroupMapping;
    private HashMap<Global.TrackGroupsGreen, Global.LogicGroups> groupToLogicMapping;

    public TrackControllerHandler(TrainSystem ts) {
        trainSystem = ts;

        //create the section to group mapping object
        sectionToGroupMapping = new HashMap<Global.Section, Global.TrackGroupsGreen>();
        for (Global.Section s : Global.Section.values()) {
            for (Global.TrackGroupsGreen tg : Global.TrackGroupsGreen.values()) {
                if (tg.toString().contains(s.toString())) {
                    sectionToGroupMapping.put(s, tg);
                    break;
                }
            }
        }

        //create the group to logic group mapping
        groupToLogicMapping = new HashMap<Global.TrackGroupsGreen, Global.LogicGroups>();
        for (Global.TrackGroupsGreen tg : Global.TrackGroupsGreen.values()) {
            if (tg.equals(Global.TrackGroupsGreen.ABC) || tg.equals(Global.TrackGroupsGreen.DEF) || tg.equals(Global.TrackGroupsGreen.RSTUVWXYZ)) {
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_1_2);
            } else if (tg.equals(Global.TrackGroupsGreen.KLM) || tg.equals(Global.TrackGroupsGreen.N) || tg.equals(Global.TrackGroupsGreen.OPQ)) {
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_4_5);
            } else if (tg.equals(Global.TrackGroupsGreen.GHI) || tg.equals(Global.TrackGroupsGreen.ZZ)) {
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_3);
            } else if (tg.equals(Global.TrackGroupsGreen.YY) || tg.equals(Global.TrackGroupsGreen.J)) {
                groupToLogicMapping.put(tg, Global.LogicGroups.GREEN_0);
            }
        }
        
        File altFile = null;
//        int response = JOptionPane.showConfirmDialog(null, "Do you want to load an alternate PLC file?");
//        if (response == JOptionPane.YES_OPTION) {
//            JFileChooser fc = new JFileChooser("src/com/rogueone/assets");
//            int returnVal = fc.showOpenDialog(null);
//
//            if (returnVal == JFileChooser.APPROVE_OPTION) {
//                altFile = fc.getSelectedFile();
//                //This is where a real application would open the file.
//                System.out.println("Opening: " + altFile.getName() + ".");
//            } else {
//                System.out.println("No file selected/using default");
//            }
//        } else {
//            System.out.println("Using default file");
//        }

        TrackController greenTrackController = new TrackController(Global.Line.GREEN, this.trainSystem, altFile);
        TrackController redTrackController = new TrackController(Global.Line.RED, this.trainSystem, altFile);
        trackControllers.add(greenTrackController);     //0
        trackControllers.add(redTrackController);       //1

        trackHandlerGUI = new TrackHandlerGUI(this);
    }

    /**
     * Getter for the Track Handler's GUI
     *
     * @return TrackHandlerGUI object
     */
    public TrackHandlerGUI getTrackHandlerGUI() {
        return trackHandlerGUI;
    }

    /**
     * Getter for the list of Track Controllers in within the Track Handler
     *
     * @return
     */
    public ArrayList<TrackController> getTrackControllers() {
        return trackControllers;
    }

    /**
     * Getter that returns a HashMap that maps Track Sections to Track Groups
     *
     * @return HashMap of Track Sections to TrackGroups
     */
    public HashMap<Global.Section, Global.TrackGroupsGreen> getSectionToGroupMapping() {
        return sectionToGroupMapping;
    }

    /**
     * Getter that returns a HashMap that maps Track Groups to Logic Groups
     *
     * @return HashMap of TrackGroups to LogicGroups
     */
    public HashMap<Global.TrackGroupsGreen, Global.LogicGroups> getGroupToLogicMapping() {
        return groupToLogicMapping;
    }

    /**
     * Method the CTC will call to dispatch a train
     *
     * @param line - line that a train should be dispatched to
     * @return
     */
    public boolean requestDispatch(Global.Line line) {
        boolean returnValue = false;
        if (line == Global.Line.GREEN) {
            returnValue = trackControllers.get(0).canDispatchProceed();
        } else {
            returnValue = trackControllers.get(1).canDispatchProceed();
        }
        return returnValue;
    }

    /**
     * Method the CTC will call to request Maintenance
     *
     * @param line - line that maintenance request should service
     * @param blockID - blockID that needs maintenance
     * @return
     */
    public boolean requestMaintenance(Global.Line line, int blockID) {
        boolean returnValue = false;
        if (line == Global.Line.GREEN) {
            returnValue = trackControllers.get(0).canClose(blockID);
        } else {
            returnValue = trackControllers.get(1).canClose(blockID);
        }
        return returnValue;
    }

    /**
     * Method the CTC will call to request Maintenance
     *
     * @param line - requested line that needs to be opened
     * @param blockID - block that is requesting to be opened
     * @return
     */
    public boolean requestOpen(Global.Line line, int blockID) {
        boolean returnValue = false;
        if (line == Global.Line.GREEN) {
            returnValue = trackControllers.get(0).canOpen(blockID);
        } else {
            returnValue = trackControllers.get(1).canOpen(blockID);
        }
        return returnValue;
    }

    /**
     * Method the CTC will call to update speed and authority to a block
     * @param line - line to update
     * @param blockID - block to update
     * @param speed - speed to send
     * @param authority - authority to send
     */
    public void requestUpdateSpeedAuthority(Global.Line line, int blockID, double speed, double authority) {
        if (line == Global.Line.GREEN) {
            trackControllers.get(0).updateSpeedAuthority(blockID, speed, authority);
        } else {
            trackControllers.get(1).updateSpeedAuthority(blockID, speed, authority);
        }
    }

    /**
     * Method the CTC will call to gather switch information
     * @param line
     * @return 
     */
    public String getSwitchInformation(Global.Line line) {
        String returnString = null;
        if (line == Global.Line.GREEN) {
            returnString = trackControllers.get(0).getSwitches();
        } else {
            returnString = trackControllers.get(1).getSwitches();
        }
        return returnString;
    }

    /**
     * Getter to return the manual switches
     * @param line - line to get switches from
     * @return 
     */
    public LinkedList<String> getManualSwitches(Global.Line line) {
        if (line == Global.Line.GREEN) {
            LinkedList<String> manualSwitches = trackControllers.get(0).getManualSwitches();
            if (manualSwitches != null) {
                return manualSwitches;
            }
        } else {
            LinkedList<String> manualSwitches = trackControllers.get(1).getManualSwitches();
            if (manualSwitches != null) {
                return manualSwitches;
            }
        }
        return null;
    }

    /**
     * method to toggle a switch on a line
     * @param line - line the switch is on
     * @param switchID - switch ID to toggle
     * @return 
     */
    public boolean toggleSwitch(Global.Line line, Integer switchID) {
        if (line == Global.Line.GREEN) {
            return trackControllers.get(0).toggleSwitch(switchID);
        } else {
            return trackControllers.get(1).toggleSwitch(switchID);
        }
    }

    /**
     * Function that will update the track controllers when called as of now,
     * this function is called whenever a train moves from 1 track block to
     * another
     */
    public void updateTrack(Global.Line line) {
        if (line == Global.Line.GREEN) {
            TrackController trackController = trackControllers.get(0);
            trackController.evaluateProceed();
            trackController.evaluateSwitches();
            trackController.evaluateCrossing();
            trackController.updateSummaryTab();
        } else {
            TrackController trackController = trackControllers.get(1);
            trackController.evaluateProceed();
            trackController.evaluateSwitches();
            trackController.evaluateCrossing();
            trackController.updateSummaryTab();
        }
    }

    /**
     * track view to update
     * @param line - line to update
     */
    public void updateTrackView(Global.Line line) {
        if(line == Global.Line.GREEN){
            TrackController trackController = trackControllers.get(0);
            trainSystem.getTrackViewGreen().updateTrackView(trackController.getOccupiedBlocks(), trackController.getSwitchStates(), trackController.getSwitchArray(), trackController.getCrossing(), trackController.getOffSwitches(), trackController.getTrackFailures());
        } else {
            TrackController trackController = trackControllers.get(1);
            trainSystem.getTrackViewRed().updateTrackView(trackController.getOccupiedBlocks(), trackController.getSwitchStates(), trackController.getSwitchArray(), trackController.getCrossing(), trackController.getOffSwitches(), trackController.getTrackFailures());
        }
        
    }

}
