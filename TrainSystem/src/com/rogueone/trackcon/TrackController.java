/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon;

import com.rogueone.mainframe.MainFrame;
import com.rogueone.global.Global;
import com.rogueone.trackcon.entities.Crossing;
import com.rogueone.trackcon.entities.Light;
import com.rogueone.trackcon.entities.LogicTrackGroup;
import com.rogueone.trackcon.entities.State;
import com.rogueone.trackcon.entities.StateSet;
import com.rogueone.trackcon.entities.SwitchState;
import com.rogueone.trackcon.entities.Switch;
import com.rogueone.trackcon.entities.TrackConnection;
import com.rogueone.trackcon.entities.UserSwitchState;
import com.rogueone.trackcon.gui.TrackControllerGUI;
import com.rogueone.trackmodel.Block;
import com.rogueone.trackmodel.TrackModel;
import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trackmodel.Section;
import com.rogueone.trainsystem.TrainSystem;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.sl.draw.geom.Guide;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author kylemonto
 */
public class TrackController {

    private Global.Line controllerLine;
    private HashMap<Global.LogicGroups, LogicTrackGroup> logicGroupsArray;
    private HashMap<Integer, Switch> switchArray;
    private Crossing crossing;
    
    private TrackControllerGUI trackControllerGUI = null;

    private TrackModel trackModel;
    private HashMap<Integer, TrainModel> trainArray;

    public TrackController(TrainSystem ts) {

//        this.trackModel = new TrackModel(new TrainSystem(), new File("src/com/rogueone/assets/TrackData.xlsx"));//temp
        this.trackModel = ts.getTrackModel(); 

    }
    
    public TrackController(){
        this.trackModel = new TrackModel(new TrainSystem(), new File("src/com/rogueone/assets/TrackData.xlsx"));//temp
    }

    public TrackController(JFrame mf) {
        TrackController trackControllerTest1 = new TrackController();
        //initialize a new track controller GUI
        this.trackControllerGUI = trackControllerTest1.createGUIObject(trackControllerTest1, mf);
        //set the default PLC file
        File defaultPLC = new File("src/com/rogueone/assets/wayside_fun.xlsx");
        //load the plc file into track controller
        trackControllerTest1.loadPLC(defaultPLC, Global.Line.GREEN);
        //set the plc program field on the gui
        this.trackControllerGUI.plcProgramTextField.setText(defaultPLC.getName());
        //load the plc and track model data into the summary tab of track controller
        trackControllerTest1.updateSummaryTab();
//        while (true) {
//            trackControllerTest1.updateGUI(trackControllerGUITest1);
//            if (trackControllerGUITest1.isDisplayable() == false) {
//                System.out.println("Window Closed");
//                break;
//            }
//        }
    }

    public static void main(String[] args) {
        //initialize a new track controller
        TrackController trackControllerTest1 = new TrackController();
        //initialize a new track controller GUI
        TrackControllerGUI trackControllerGUITest1 = trackControllerTest1.createGUIObject(trackControllerTest1);
        //set the default PLC file
        File defaultPLC = new File("src/com/rogueone/assets/wayside_fun.xlsx");
        //load the plc file into track controller
        trackControllerTest1.loadPLC(defaultPLC, Global.Line.GREEN);
        //set the plc program field on the gui
        trackControllerGUITest1.plcProgramTextField.setText(defaultPLC.getName());
        //load the plc and track model data into the summary tab of track controller
        trackControllerTest1.updateSummaryTab();
//        while (true) {
//            trackControllerTest1.updateGUI(trackControllerGUITest1);
//            if (trackControllerGUITest1.isDisplayable() == false) {
//                System.out.println("Window Closed");
//                break;
//            }
//        }

    }

    public TrackController(Global.Line line, TrainSystem trainSystem) {
        this.controllerLine = line;
        //initialize a new track controller
//        TrackController trackController = new TrackController(trainSystem);
        //load the track model
        this.trackModel = trainSystem.getTrackModel();
        //initialize a new track controller GUI
        this.trackControllerGUI = this.createGUIObject(this);
        //set the default PLC file
        File defaultPLC = new File("src/com/rogueone/assets/wayside_fun.xlsx");
        //load the plc file into track controller
        this.loadPLC(defaultPLC, line);
        //set the plc program field on the gui
        this.trackControllerGUI.plcProgramTextField.setText(defaultPLC.getName());
        //load the plc and track model data into the summary tab of track controller
        this.updateSummaryTab();
    }

    /**
     * Function to import the data from an excel document that contains track
     * state information, such as switch, lights, crossings, connections, etc.
     *
     * @param plcFile - excel document that contains information regarding track
     * states
     */
    public void loadPLC(File plcFile, Global.Line line) {
        //Testing in reads, if the file is there
        XSSFWorkbook plcWorkbook;
        try {
            plcWorkbook = new XSSFWorkbook(plcFile);
            //import the plc data sheet from the excel document
            XSSFSheet sheet = null;
            if (line == Global.Line.GREEN) {
                //load in the green switch information
                sheet = plcWorkbook.getSheetAt(0);
                this.switchArray = new HashMap<Integer, Switch>();
                for (int i = 1; i <= 6; i++) {
                    Row tempRow = sheet.getRow(i);
                    Light light1Default = new Light(tempRow.getCell(3).getStringCellValue());
                    Light light2Default = new Light(tempRow.getCell(4).getStringCellValue());
                    ArrayList<Light> lightsDefault = new ArrayList<Light>();
                    lightsDefault.add(light1Default);
                    lightsDefault.add(light2Default);
                    TrackConnection trackConnectionDefault = new TrackConnection(tempRow.getCell(2).getStringCellValue());
                    Light light1Alternate = new Light(tempRow.getCell(6).getStringCellValue());
                    Light light2Alternate = new Light(tempRow.getCell(7).getStringCellValue());
                    ArrayList<Light> lightsAlternate = new ArrayList<Light>();
                    lightsAlternate.add(light1Alternate);
                    lightsAlternate.add(light2Alternate);
                    TrackConnection trackConnectionAlternate = new TrackConnection(tempRow.getCell(5).getStringCellValue());
                    SwitchState switchState = new SwitchState(Global.SwitchState.DEFAULT, trackConnectionDefault, lightsDefault, trackConnectionAlternate, lightsAlternate);
                    Switch trackSwitch = new Switch((int) tempRow.getCell(0).getNumericCellValue(), tempRow.getCell(1).getStringCellValue(), switchState);
                    this.switchArray.put(trackSwitch.getSwitchID(), trackSwitch);
                }
                System.out.println("SwitchArray: " + this.switchArray.toString());

                //initialize the green logical groups array and import the logical
                //groups from the remaining sheets from the excel document
                this.logicGroupsArray = new HashMap<Global.LogicGroups, LogicTrackGroup>();

                //load in each sheet for the green line
                for (int s = 1; s < 5; s++) {
                    sheet = plcWorkbook.getSheetAt(s);

                    int numRows = 0;
                    Global.TrackGroups trackGroup1 = null;
                    Global.TrackGroups trackGroup2 = null;
                    Global.TrackGroups trackGroup3 = null;

                    LogicTrackGroup logicTrackGroup = new LogicTrackGroup(Global.LogicGroups.valueOf(sheet.getSheetName()));
                    Row trackGroupRow = sheet.getRow(1);

                    if (s == 3 || s == 4) {
                        String[] switchIDs = trackGroupRow.getCell(0).getStringCellValue().split(",");
                        for (String ID : switchIDs) {
                            logicTrackGroup.addSwitches(this.switchArray.get(Integer.parseInt(ID)));
                        }
                        numRows = 9;
                    } else {
                        int switchID = (int) trackGroupRow.getCell(0).getNumericCellValue();
                        logicTrackGroup.addSwitches(this.switchArray.get(switchID));
                        numRows = 5;
                    }

                    StateSet currentState = new StateSet();
                    trackGroup1 = Global.TrackGroups.valueOf(trackGroupRow.getCell(3).getStringCellValue());
                    State current1 = new State(trackGroup1, Global.Presence.UNOCCUPIED);
                    currentState.addState(current1);
                    trackGroup2 = Global.TrackGroups.valueOf(trackGroupRow.getCell(4).getStringCellValue());
                    State current2 = new State(trackGroup2, Global.Presence.UNOCCUPIED);
                    currentState.addState(current2);
                    if (s == 3 || s == 4) {
                        trackGroup3 = Global.TrackGroups.valueOf(trackGroupRow.getCell(5).getStringCellValue());
                        State current3 = new State(trackGroup3, Global.Presence.UNOCCUPIED);
                        currentState.addState(current3);
                    }
                    logicTrackGroup.setCurrentTrackState(currentState);
                    for (int i = 2; i <= numRows; i++) {
                        Row tempRow = sheet.getRow(i);
                        StateSet set1 = new StateSet();
                        UserSwitchState userSwitchState = new UserSwitchState();
                        State s1 = new State(trackGroup1, tempRow.getCell(3).getNumericCellValue() == 1 ? Global.Presence.OCCUPIED : Global.Presence.UNOCCUPIED);
                        State s2 = new State(trackGroup2, tempRow.getCell(4).getNumericCellValue() == 1 ? Global.Presence.OCCUPIED : Global.Presence.UNOCCUPIED);
                        set1.addState(s1);
                        set1.addState(s2);
                        if (s == 3 || s == 4 && trackGroup3 != null) {
                            State s3 = new State(trackGroup3, tempRow.getCell(5).getNumericCellValue() == 1 ? Global.Presence.OCCUPIED : Global.Presence.UNOCCUPIED);
                            set1.addState(s3);
                            userSwitchState.addUserSwitchState(new AbstractMap.SimpleEntry<Integer, Global.SwitchState>((int) trackGroupRow.getCell(6).getNumericCellValue(), Global.SwitchState.valueOf(tempRow.getCell(6).getStringCellValue())));
                            userSwitchState.addUserSwitchState(new AbstractMap.SimpleEntry<Integer, Global.SwitchState>((int) trackGroupRow.getCell(7).getNumericCellValue(), Global.SwitchState.valueOf(tempRow.getCell(7).getStringCellValue())));
                            logicTrackGroup.addTrackState(set1, userSwitchState);
                        } else {
                            userSwitchState.addUserSwitchState(new AbstractMap.SimpleEntry<Integer, Global.SwitchState>((int) trackGroupRow.getCell(5).getNumericCellValue(), Global.SwitchState.valueOf(tempRow.getCell(5).getStringCellValue())));
                            logicTrackGroup.addTrackState(set1, userSwitchState);
                        }
                    }

                    this.logicGroupsArray.put(Global.LogicGroups.valueOf(sheet.getSheetName()), logicTrackGroup);
                }

                //load the crossing into the trackController
                sheet = plcWorkbook.getSheet("GREEN_CROSSING");
                Row greenRow = sheet.getRow(1);
                Global.Line sheetLine = Global.Line.valueOf(greenRow.getCell(0).getStringCellValue());
                Global.Section section = Global.Section.valueOf(greenRow.getCell(1).getStringCellValue());
                int block = (int) greenRow.getCell(2).getNumericCellValue();
                HashMap<Global.CrossingState, Global.LightState> crossingStates = new HashMap<Global.CrossingState, Global.LightState>();
                crossingStates.put(Global.CrossingState.ACTIVE, Global.LightState.valueOf(greenRow.getCell(3).getStringCellValue()));
                crossingStates.put(Global.CrossingState.INACTIVE, Global.LightState.valueOf(greenRow.getCell(4).getStringCellValue()));
                this.crossing = new Crossing(sheetLine, section, block, crossingStates, Global.CrossingState.INACTIVE);

            } else if (line == Global.Line.RED) {
                //Start of Red line import
                sheet = plcWorkbook.getSheet("RED_SWITCHES");
                this.switchArray = new HashMap<Integer, Switch>();
                for (int i = 1; i <= 3; i++) {
                    Row tempRow = sheet.getRow(i);
                    Light light1Default = new Light(tempRow.getCell(3).getStringCellValue());
                    Light light2Default = new Light(tempRow.getCell(4).getStringCellValue());
                    Light light3Default = new Light(tempRow.getCell(5).getStringCellValue());
                    ArrayList<Light> lightsDefault = new ArrayList<Light>();
                    lightsDefault.add(light1Default);
                    lightsDefault.add(light2Default);
                    lightsDefault.add(light3Default);
                    TrackConnection trackConnectionDefault = new TrackConnection(tempRow.getCell(2).getStringCellValue());
                    Light light1Alternate = new Light(tempRow.getCell(7).getStringCellValue());
                    Light light2Alternate = new Light(tempRow.getCell(8).getStringCellValue());
                    Light light3Alternate = new Light(tempRow.getCell(9).getStringCellValue());
                    ArrayList<Light> lightsAlternate = new ArrayList<Light>();
                    lightsAlternate.add(light1Alternate);
                    lightsAlternate.add(light2Alternate);
                    lightsAlternate.add(light3Alternate);
                    TrackConnection trackConnectionAlternate = new TrackConnection(tempRow.getCell(6).getStringCellValue());
                    SwitchState switchState = new SwitchState(Global.SwitchState.DEFAULT, trackConnectionDefault, lightsDefault, trackConnectionAlternate, lightsAlternate);
                    Switch trackSwitch = new Switch((int) tempRow.getCell(0).getNumericCellValue(), tempRow.getCell(1).getStringCellValue(), switchState);
                    this.switchArray.put(trackSwitch.getSwitchID(), trackSwitch);
                }

                this.logicGroupsArray = new HashMap<Global.LogicGroups, LogicTrackGroup>();

                for (int s = 6; s < 8; s++) {
                    sheet = plcWorkbook.getSheetAt(s);

                    int numRows = 0;
                    Global.TrackGroups trackGroup1 = null;
                    Global.TrackGroups trackGroup2 = null;
                    Global.TrackGroups trackGroup3 = null;

                    LogicTrackGroup logicTrackGroup = new LogicTrackGroup(Global.LogicGroups.valueOf(sheet.getSheetName()));
                    Row trackGroupRow = sheet.getRow(1);

                    if (s == 7) {
                        String[] switchIDs = trackGroupRow.getCell(0).getStringCellValue().split(",");
                        for (String ID : switchIDs) {
                            logicTrackGroup.addSwitches(this.switchArray.get(Integer.parseInt(ID)));
                        }
                        numRows = 9;
                    } else {
                        int switchID = (int) trackGroupRow.getCell(0).getNumericCellValue();
                        logicTrackGroup.addSwitches(this.switchArray.get(switchID));
                        numRows = 5;
                    }

                    StateSet currentState = new StateSet();
                    trackGroup1 = Global.TrackGroups.valueOf(trackGroupRow.getCell(3).getStringCellValue());
                    State current1 = new State(trackGroup1, Global.Presence.UNOCCUPIED);
                    currentState.addState(current1);
                    trackGroup2 = Global.TrackGroups.valueOf(trackGroupRow.getCell(4).getStringCellValue());
                    State current2 = new State(trackGroup2, Global.Presence.UNOCCUPIED);
                    currentState.addState(current2);
                    if (s == 7) {
                        trackGroup3 = Global.TrackGroups.valueOf(trackGroupRow.getCell(5).getStringCellValue());
                        State current3 = new State(trackGroup3, Global.Presence.UNOCCUPIED);
                        currentState.addState(current3);
                    }
                    logicTrackGroup.setCurrentTrackState(currentState);
                    for (int i = 2; i <= numRows; i++) {
                        Row tempRow = sheet.getRow(i);
                        StateSet set1 = new StateSet();
                        UserSwitchState userSwitchState = new UserSwitchState();
                        State s1 = new State(trackGroup1, tempRow.getCell(3).getNumericCellValue() == 1 ? Global.Presence.OCCUPIED : Global.Presence.UNOCCUPIED);
                        State s2 = new State(trackGroup2, tempRow.getCell(4).getNumericCellValue() == 1 ? Global.Presence.OCCUPIED : Global.Presence.UNOCCUPIED);
                        set1.addState(s1);
                        set1.addState(s2);
                        if (s == 7 && trackGroup3 != null) {
                            State s3 = new State(trackGroup3, tempRow.getCell(5).getNumericCellValue() == 1 ? Global.Presence.OCCUPIED : Global.Presence.UNOCCUPIED);
                            set1.addState(s3);
                            userSwitchState.addUserSwitchState(new AbstractMap.SimpleEntry<Integer, Global.SwitchState>((int) trackGroupRow.getCell(6).getNumericCellValue(), Global.SwitchState.valueOf(tempRow.getCell(6).getStringCellValue())));
                            userSwitchState.addUserSwitchState(new AbstractMap.SimpleEntry<Integer, Global.SwitchState>((int) trackGroupRow.getCell(7).getNumericCellValue(), Global.SwitchState.valueOf(tempRow.getCell(7).getStringCellValue())));
                            logicTrackGroup.addTrackState(set1, userSwitchState);
                        } else {
                            userSwitchState.addUserSwitchState(new AbstractMap.SimpleEntry<Integer, Global.SwitchState>((int) trackGroupRow.getCell(5).getNumericCellValue(), Global.SwitchState.valueOf(tempRow.getCell(5).getStringCellValue())));
                            logicTrackGroup.addTrackState(set1, userSwitchState);
                        }
                    }

                    this.logicGroupsArray.put(Global.LogicGroups.valueOf(sheet.getSheetName()), logicTrackGroup);
                }
                //load the red crossing into the track controller
                sheet = plcWorkbook.getSheet("RED_CROSSING");
                Row greenRow = sheet.getRow(1);
                Global.Line sheetLine = Global.Line.valueOf(greenRow.getCell(0).getStringCellValue());
                Global.Section section = Global.Section.valueOf(greenRow.getCell(1).getStringCellValue());
                int block = (int) greenRow.getCell(2).getNumericCellValue();
                HashMap<Global.CrossingState, Global.LightState> crossingStates = new HashMap<Global.CrossingState, Global.LightState>();
                crossingStates.put(Global.CrossingState.ACTIVE, Global.LightState.valueOf(greenRow.getCell(3).getStringCellValue()));
                crossingStates.put(Global.CrossingState.INACTIVE, Global.LightState.valueOf(greenRow.getCell(4).getStringCellValue()));
                this.crossing = new Crossing(sheetLine, section, block, crossingStates, Global.CrossingState.INACTIVE);
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public TrackControllerGUI createGUIObject(TrackController trackController) {
        //Create a GUI object
        TrackControllerGUI trackControllerGUI = new TrackControllerGUI(trackController);

        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
//        MainFrame mf = new MainFrame();
//        mf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        mf.getContentPane().add(trackControllerGUI);
//        mf.pack();
//        mf.setVisible(true);     //Make sure to set it visible
        return trackControllerGUI;  //Return the GUI
    }

    public TrackControllerGUI createGUIObject(TrackController trackControllerTest1, JFrame mf) {
        //Create a GUI object
        TrackControllerGUI trackControllerGUI = new TrackControllerGUI(trackControllerTest1);

        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
//        MainFrame mf = new MainFrame();
//        mf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        mf.getContentPane().add(trackControllerGUI);
//        mf.pack();
//        mf.setVisible(true);     //Make sure to set it visible
        return trackControllerGUI;  //Return the GUI
    }

    public void updateGUI(TrackControllerGUI trackControllerGUITest1) {
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("5 Seconds Passed");

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void updateSummaryTab() {
        String switchColumnNames[] = {"Section", "Block ID", "Switch ID", "State", "Connection"};
        DefaultTableModel switchModel = new DefaultTableModel(switchColumnNames, 0);

        String lightColumnNames[] = {"Section", "Block ID", "Light State"};
        DefaultTableModel lightModel = new DefaultTableModel(lightColumnNames, 0);

        String crossingColumnNames[] = {"Section", "BlockID", "State"};
        DefaultTableModel crossingModel = new DefaultTableModel(crossingColumnNames, 0);

        HashMap<Integer, Switch> displaySwitchArray = null;
        Crossing displayCrossing = null;
        Global.Line displayLine = null;
        displaySwitchArray = this.switchArray;
        displayCrossing = crossing;
        displayLine = this.controllerLine;
            
        Set<Entry<Integer, Switch>> switchSet = displaySwitchArray.entrySet();
        Iterator switchIterator = switchSet.iterator();
        while (switchIterator.hasNext()) {
            Entry<Integer, Switch> switchIteration = (Entry<Integer, Switch>) switchIterator.next();
            Integer switchID = switchIteration.getKey();
            Switch switchObject = switchIteration.getValue();
            SwitchState switchState = switchObject.getSwitchState();
            TrackConnection currentTrackConnection = null;
            ArrayList<Light> currentLights = null;
            if (switchState.getSwitchState() == Global.SwitchState.DEFAULT) {
                currentTrackConnection = switchState.getDefaultConnection();
                currentLights = switchState.getLightsDefault();
            } else {
                currentTrackConnection = switchState.getAlternateConnection();
                currentLights = switchState.getLightsAlternate();
            }
            //add switch to table model
            String switchRowData[] = {switchObject.getSection() + "", switchObject.getBlockID() + "", switchObject.getSwitchID() + "", switchState.getSwitchState() + "", currentTrackConnection + ""};
            switchModel.addRow(switchRowData);
            //add lights to table model
            Iterator lightsIterator = currentLights.iterator();
            while (lightsIterator.hasNext()) {
                Light lightIteration = (Light) lightsIterator.next();
                String lightRowData[] = {lightIteration.getSection() + "", lightIteration.getBlockID() + "", lightIteration.getLightState() + ""};
                lightModel.addRow(lightRowData);
            }
        }

        //add crossings to table model - with associated lights
        String crossingRowData[] = {displayCrossing.getSection() + "", displayCrossing.getBlockID() + "", displayCrossing.getCurrentCrossingState() + ""};
        crossingModel.addRow(crossingRowData);
        Set<Entry<Global.CrossingState, Global.LightState>> lights = displayCrossing.getCrossingState().entrySet();
        Iterator crossingLightsIterator = lights.iterator();
        while (crossingLightsIterator.hasNext()) {
            Entry<Global.CrossingState, Global.LightState> lightState = (Entry<Global.CrossingState, Global.LightState>) crossingLightsIterator.next();
            if (displayCrossing.getCurrentCrossingState() == lightState.getKey()) {
                String lightRowData[] = {displayCrossing.getSection() + "", displayCrossing.getBlockID() + "", lightState.getValue() + ""};
                lightModel.addRow(lightRowData);
            }
        }

        ArrayList<Block> loadBlocks = new ArrayList<Block>();
        loadBlocks.addAll(this.trackModel.getBlockArray());
        ArrayList<Block> currentBlocks = new ArrayList<Block>();
        for (int i = 0; i < loadBlocks.size(); i++) {
            if (loadBlocks.get(i).getLine().getLineID() == displayLine) {
                currentBlocks.add(loadBlocks.get(i));
            }
        }

        String blockColumnNames[] = {"Section", "BlockID", "Occupied", "Status"};
        DefaultTableModel blockModel = new DefaultTableModel(blockColumnNames, 0);
        for (int i = 0; i < currentBlocks.size(); i++) {
            String status = "Active";
            if (currentBlocks.get(i).getFailureBrokenRail() || currentBlocks.get(i).getFailureBrokenRail() || currentBlocks.get(i).getFailureBrokenRail()) {
                status = "Inactive";
            }
            String blockRowData[] = {currentBlocks.get(i).getSection() + "", currentBlocks.get(i).getID() + "", currentBlocks.get(i).isOccupied() + "", status + ""};
            blockModel.addRow(blockRowData);
        }

        this.trackControllerGUI.currentSwitchTable.setModel(switchModel);
        this.trackControllerGUI.currentBlockTable.setModel(blockModel);
        this.trackControllerGUI.currentTrackSignalsTable.setModel(lightModel);
        this.trackControllerGUI.currentCrossingTable.setModel(crossingModel);
    }

    public String setupSimulateTabSwitch(Global.LogicGroups logicGroup) {
        LogicTrackGroup selectedLogicGroup = logicGroupsArray.get(logicGroup);
        if (selectedLogicGroup == null) {
            System.out.println("Logic group is not in selected Track Controller");
            String message = "Logic group is not in selected Track Controller";
            return message;
        }
        this.trackControllerGUI.enableInputs(selectedLogicGroup);
        this.trackControllerGUI.setImage(logicGroup);
        return null;
    }

    public UserSwitchState updateStateMapping(LogicTrackGroup selectedLogicTrackGroup, StateSet guiStateSet) {
        String lineSelected = null;
        LogicTrackGroup ltg = logicGroupsArray.get(selectedLogicTrackGroup.getLogicGroup());
        if (ltg == null) {
            System.out.println("No valid logic group found in current track controller");
            return null;
        } 
        lineSelected = this.controllerLine.toString();
        
        StateSet currentState = ltg.getCurrentTrackState();

        System.out.println("Current Set = " + currentState.toString());

        System.out.println("GUI Set = " + currentState.toString());

        HashMap<StateSet, UserSwitchState> mappings = ltg.getStateMapping();

        System.out.println("Mappings = " + mappings);
        //how to search for current state mapping        
        for (Map.Entry<StateSet, UserSwitchState> entry : mappings.entrySet()) {
            StateSet key = entry.getKey();
            UserSwitchState userSwitchState = entry.getValue();
            if (key.equals(guiStateSet)) {
                System.out.println("Mapping contains GUI State");
                System.out.println("Previous State is now Current state");
                ltg.setPreviousTrackState(currentState);
                System.out.println("Current State is now GUI state");
                ltg.setCurrentTrackState(guiStateSet);
                this.trackControllerGUI.setSelectedLogicTrackGroup(ltg);
                logicGroupsArray.replace(selectedLogicTrackGroup.getLogicGroup(), ltg);
                return userSwitchState;
                //return UserSwitchState
            } else {
                System.out.println("Mapping does not contain current State");
            }
            // do stuff
        }
        return null;
    }

    public void addTrain(String trainID, String suggestedSpeed, String suggestedAuthority, TrackControllerGUI gui) {
//        TrainModel train = new TrainModel(Integer.parseInt(suggestedSpeed), Integer.parseInt(suggestedAuthority), 1);
//        this.trainArray.put(Integer.parseInt(trainID), train);

        String trainColumnNames[] = {"Train ID", "Suggested Speed", "Suggested Authority", "Commanded Speed", "Commanded Authority"};
        DefaultTableModel trainModel = new DefaultTableModel(trainColumnNames, 0);

        Set<Entry<Integer, TrainModel>> trainSet = this.trainArray.entrySet();
        Iterator trainIterator = trainSet.iterator();
        while (trainIterator.hasNext()) {
            Entry<Integer, TrainModel> currentTrain = (Entry<Integer, TrainModel>) trainIterator.next();
            TrainModel currentTrainVal = currentTrain.getValue();
            String trainRowData[] = {currentTrain.getKey() + "", currentTrainVal.getCtcSetPoint() + "", currentTrainVal.getAuthority() + "", currentTrainVal.getCtcSetPoint() + "", currentTrainVal.getAuthority() + ""};
            trainModel.addRow(trainRowData);
        }

        gui.currentTrainsTable.setModel(trainModel);

    }

    public String setupSimulateTabCrossing(Global.LogicGroups logicGroup) {
        Crossing crossing = null;
        LogicTrackGroup selectedLogicGroup = logicGroupsArray.get(logicGroup);
        if (selectedLogicGroup == null) {
            System.out.println("Logic group is not in selected Track Controller cannot get crossing");
            String message = "Logic group is not in selected Track Controller cannot get crossing";
            return message;
        } 
        this.trackControllerGUI.enableInputs(crossing);
        this.trackControllerGUI.setImage(crossing);
        return null;
    }

    public void updateCrossing(Crossing selectedCrossing, Global.CrossingState selectedCrossState) {
        selectedCrossing.setCurrentCrossingState(selectedCrossState);
        if (selectedCrossing.getLine() == Global.Line.GREEN) {
            this.crossing = selectedCrossing;
            this.trackControllerGUI.setSelectedCrossing(this.crossing);
        } else {
            this.crossing = selectedCrossing;
            this.trackControllerGUI.setSelectedCrossing(this.crossing);
        }

    }
    
    

//    public updatePresence(){
//        ArrayList<Section> sectionsArray = trackModelTest1.getLine(Global.Line.GREEN).getSections();
//    }

    public TrackControllerGUI getTrackControllerGUI() {
        return trackControllerGUI;
    }

    public Global.Line getControllerLine() {
        return controllerLine;
    }

    
    
}
