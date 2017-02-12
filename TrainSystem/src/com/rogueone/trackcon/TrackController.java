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

    private HashMap<Global.LogicGroups, LogicTrackGroup> logicGroupsGreenArray;
    private HashMap<Global.LogicGroups, LogicTrackGroup> logicGroupsRedArray;
    private HashMap<Integer, Switch> switchGreenArray;
    private HashMap<Integer, Switch> switchRedArray;
    private Crossing greenCrossing;
    private Crossing redCrossing;

    private TrackModel trackModelTest1;

    public TrackController() {

        this.trackModelTest1 = new TrackModel();
        try {
            trackModelTest1.parseDataFile(new File("src/com/rogueone/assets/TrackData.xlsx"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (InvalidFormatException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {

        TrackController trackControllerTest1 = new TrackController();

        TrackControllerGUI trackControllerGUITest1 = trackControllerTest1.createGUIObject(trackControllerTest1);
        File defaultPLC = new File("src/com/rogueone/assets/wayside_fun.xlsx");
        trackControllerTest1.loadPLC(defaultPLC);
        trackControllerGUITest1.plcProgramTextField.setText(defaultPLC.getName());
        trackControllerTest1.updateSummaryTab(trackControllerGUITest1);
        while (true) {
            trackControllerTest1.updateGUI(trackControllerGUITest1);
            if (trackControllerGUITest1.isDisplayable() == false) {
                System.out.println("Window Closed");
                break;
            }
        }

    }

    /**
     * Function to import the data from an excel document that contains track
     * state information, such as switch, lights, crossings, connections, etc.
     *
     * @param plcFile - excel document that contains information regarding track
     * states
     */
    public void loadPLC(File plcFile) {
        //Testing in reads, if the file is there
        XSSFWorkbook plcWorkbook;
        try {
            plcWorkbook = new XSSFWorkbook(plcFile);
            //import the plc data sheet from the excel document
            XSSFSheet sheet = plcWorkbook.getSheetAt(0);
            switchGreenArray = new HashMap<Integer, Switch>();
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
                switchGreenArray.put(trackSwitch.getSwitchID(), trackSwitch);
            }
            System.out.println("SwitchArray: " + switchGreenArray.toString());

            //initialize the green logical groups array and import the logical
            //groups from the remaining sheets from the excel document
            logicGroupsGreenArray = new HashMap<Global.LogicGroups, LogicTrackGroup>();

            for (int s = 1; s < 5; s++) {
                sheet = plcWorkbook.getSheetAt(s);

                int numRows = 0;
                Global.TrackGroups trackGroup1 = null;
                Global.TrackGroups trackGroup2 = null;
                Global.TrackGroups trackGroup3 = null;

                LogicTrackGroup logicTrackGroup = new LogicTrackGroup();
                Row trackGroupRow = sheet.getRow(1);

                if (s == 3 || s == 4) {
                    String[] switchIDs = trackGroupRow.getCell(0).getStringCellValue().split(",");
                    for (String ID : switchIDs) {
                        logicTrackGroup.addSwitches(switchGreenArray.get(Integer.parseInt(ID)));
                    }
                    numRows = 9;
                } else {
                    int switchID = (int) trackGroupRow.getCell(0).getNumericCellValue();
                    logicTrackGroup.addSwitches(switchGreenArray.get(switchID));
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

                logicGroupsGreenArray.put(Global.LogicGroups.valueOf(sheet.getSheetName()), logicTrackGroup);
            }

            //Start of Red line import
            sheet = plcWorkbook.getSheet("RED_SWITCHES");
            switchRedArray = new HashMap<Integer, Switch>();
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
                switchRedArray.put(trackSwitch.getSwitchID(), trackSwitch);
            }

            logicGroupsRedArray = new HashMap<Global.LogicGroups, LogicTrackGroup>();

            for (int s = 6; s < 8; s++) {
                sheet = plcWorkbook.getSheetAt(s);

                int numRows = 0;
                Global.TrackGroups trackGroup1 = null;
                Global.TrackGroups trackGroup2 = null;
                Global.TrackGroups trackGroup3 = null;

                LogicTrackGroup logicTrackGroup = new LogicTrackGroup();
                Row trackGroupRow = sheet.getRow(1);

                if (s == 7) {
                    String[] switchIDs = trackGroupRow.getCell(0).getStringCellValue().split(",");
                    for (String ID : switchIDs) {
                        logicTrackGroup.addSwitches(switchRedArray.get(Integer.parseInt(ID)));
                    }
                    numRows = 9;
                } else {
                    int switchID = (int) trackGroupRow.getCell(0).getNumericCellValue();
                    logicTrackGroup.addSwitches(switchRedArray.get(switchID));
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

                logicGroupsRedArray.put(Global.LogicGroups.valueOf(sheet.getSheetName()), logicTrackGroup);
            }

            for (int i = 8; i < 10; i++) {
                if (i == 8) {
                    sheet = plcWorkbook.getSheet("GREEN_CROSSING");
                } else {
                    sheet = plcWorkbook.getSheet("RED_CROSSING");
                }
                Row greenRow = sheet.getRow(1);
                Global.Line line = Global.Line.valueOf(greenRow.getCell(0).getStringCellValue());
                Global.Section section = Global.Section.valueOf(greenRow.getCell(1).getStringCellValue());
                int block = (int) greenRow.getCell(2).getNumericCellValue();
                HashMap<Global.CrossingState, Global.LightState> crossingStates = new HashMap<Global.CrossingState, Global.LightState>();
                crossingStates.put(Global.CrossingState.ACTIVE, Global.LightState.valueOf(greenRow.getCell(3).getStringCellValue()));
                crossingStates.put(Global.CrossingState.INACTIVE, Global.LightState.valueOf(greenRow.getCell(4).getStringCellValue()));
                if (i == 8) {
                    this.greenCrossing = new Crossing(line, section, block, crossingStates, Global.CrossingState.INACTIVE);
                } else if (i == 9) {
                    this.redCrossing = new Crossing(line, section, block, crossingStates, Global.CrossingState.INACTIVE);
                }

            }

            LogicTrackGroup ltg = logicGroupsGreenArray.get(Global.LogicGroups.GREEN_0);
            StateSet currentState = ltg.getCurrentTrackState();

            System.out.println("Set = " + currentState.toString());

            HashMap<StateSet, UserSwitchState> mappings = ltg.getStateMapping();

            System.out.println("Mappings = " + mappings);
            //how to search for current state mapping        
            for (Map.Entry<StateSet, UserSwitchState> entry : mappings.entrySet()) {
                StateSet key = entry.getKey();
                UserSwitchState value = entry.getValue();
                if (key.equals(currentState)) {
                    System.out.println("Mapping contains current State");
                    //return UserSwitchState
                } else {
                    System.out.println("Mapping does not contain current State");
                }
                // do stuff
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private TrackControllerGUI createGUIObject(TrackController trackControllerTest1) {
        //Create a GUI object
        TrackControllerGUI trackControllerGUI = new TrackControllerGUI(trackControllerTest1);

        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        MainFrame mf = new MainFrame();
        mf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mf.getContentPane().add(trackControllerGUI);
        mf.pack();
        mf.setVisible(true);     //Make sure to set it visible

        return trackControllerGUI;  //Return the GUI
    }

    private void updateGUI(TrackControllerGUI trackControllerGUITest1) {
        try {
            TimeUnit.SECONDS.sleep(5);
            System.out.println("5 Seconds Passed");

        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void updateSummaryTab(TrackControllerGUI trackControllerGUITest1) {
        String switchColumnNames[] = {"Section", "Block ID", "Switch ID", "State", "Connection"};
        DefaultTableModel switchModel = new DefaultTableModel(switchColumnNames, 0);

        String lightColumnNames[] = {"Section", "Block ID", "Light State"};
        DefaultTableModel lightModel = new DefaultTableModel(lightColumnNames, 0);

        String crossingColumnNames[] = {"Section", "BlockID", "State"};
        DefaultTableModel crossingModel = new DefaultTableModel(crossingColumnNames, 0);

        HashMap<Integer, Switch> displaySwitchArray = null;
        Crossing displayCrossing = null;
        Global.Line displayLine = null;
        String selectedController = (String) trackControllerGUITest1.currentTrackControllerComboBox.getSelectedItem();
        if (selectedController.contains("Green")) {
            displaySwitchArray = this.switchGreenArray;
            displayCrossing = greenCrossing;
            displayLine = Global.Line.GREEN;
        } else {
            displaySwitchArray = this.switchRedArray;
            displayCrossing = redCrossing;
            displayLine = Global.Line.RED;
        }

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
        loadBlocks.addAll(this.trackModelTest1.getBlocks());
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

        trackControllerGUITest1.currentSwitchTable.setModel(switchModel);
        trackControllerGUITest1.currentBlockTable.setModel(blockModel);
        trackControllerGUITest1.currentTrackSignalsTable.setModel(lightModel);
        trackControllerGUITest1.currentCrossingTable.setModel(crossingModel);
    }

    public void setupSimulateTab(Global.LogicGroups logicGroup, TrackControllerGUI gui) {
        LogicTrackGroup selectedLogicGroup = logicGroupsGreenArray.get(logicGroup);
        if(selectedLogicGroup == null){
            selectedLogicGroup = logicGroupsRedArray.get(logicGroup);
        }
        
        ArrayList<Switch> selectedGroupSwitches = selectedLogicGroup.getSwitches();
        gui.enableInputs(selectedLogicGroup);
    }
}
