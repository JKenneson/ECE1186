//Comment out the following package statement to compile separately.
package com.rogueone.trackview;

import com.rogueone.global.Global;
import com.rogueone.trackcon.entities.Light;
import com.rogueone.trackcon.entities.PresenceBlock;
import com.rogueone.trackcon.entities.TrackConnection;
import com.rogueone.trackcon.entities.UserSwitchState;
import com.rogueone.trackmodel.Block;
import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trainsystem.TrainSystem;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.jws.WebParam;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TrackView extends Frame {

    ShapePanel sp;
    float shiftAmountX = 30;
    float shiftAmountY = 50;
    ArrayList<MyShape> shapeList;
    HashMap<Integer, Switch> switchList;
    HashMap<String, TrackLight> trackLightList;
    public HashMap<String, Section> sectionList;
    Yard yardStart;
    Crossing crossing;
    double scale = 5;
    public int trainID = 0;
    public Global.Line line;
    private JFrame theWindow;

    public TrainSystem trainSystem;

    /**
     * Track View constructor
     *
     * @param trainSystem
     * @param line
     */
    public TrackView(TrainSystem trainSystem, Global.Line line) {
        //Title our frame.
        this.trainSystem = trainSystem;
        if (line == Global.Line.GREEN) {
            this.line = line;
            shapeList = new ArrayList<MyShape>(); // create empty ArrayList
            switchList = new HashMap<Integer, Switch>();
            trackLightList = new HashMap<String, TrackLight>();
            sectionList = new HashMap<String, Section>();
            sp = new ShapePanel(1000, 305, this.trainSystem, this.line);
            theWindow = new JFrame("Track View - " + line);
            theWindow.setSize(1000, 325);
            Container c = theWindow.getContentPane();
            sp.setBackground(Color.BLACK);
            c.add(sp);
            initializeGreenLine();
            sp.repaint();
            theWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            theWindow.setResizable(false);
        } else if (line == Global.Line.RED) {
            this.line = line;
            shapeList = new ArrayList<MyShape>(); // create empty ArrayList
            switchList = new HashMap<Integer, Switch>();
            trackLightList = new HashMap<String, TrackLight>();
            sectionList = new HashMap<String, Section>();
            sp = new ShapePanel(1000, 300, this.trainSystem, this.line);
            theWindow = new JFrame("Track View - " + line);
            theWindow.setSize(1000, 325);
            Container c = theWindow.getContentPane();
            sp.setBackground(Color.BLACK);
            c.add(sp);
            initializeRedLine2();
            sp.repaint();
            theWindow.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            theWindow.setResizable(false);
        }
    }

    /**
     * Method to toggle the display for the track view
     *
     * @param show - boolean value to toggle or on and off
     */
    public void displayTrackView(boolean show) {
        this.theWindow.setVisible(show);
    }

    /**
     * Method that will update the track view called every clock tick. displays
     * everything listed below
     *
     * @param occupiedBlocks - pseudo trains - from TC
     * @param switchStates - switch states from the - from TC
     * @param switchArray - switch information - from TC
     * @param crossing - crossing status - from TC
     * @param offSwitches - offSwitches - from TC
     * @param trackFailures - track failures - from TC
     */
    public void updateTrackView(LinkedList<PresenceBlock> occupiedBlocks, LinkedList<UserSwitchState> switchStates, HashMap<Integer, com.rogueone.trackcon.entities.Switch> switchArray, com.rogueone.trackcon.entities.Crossing crossing, LinkedList<Integer> offSwitches, LinkedList<String> trackFailures) {

        if (this.line == Global.Line.GREEN) {
            if (switchStates != null) {
                Iterator listIterator = switchStates.iterator();
                while (listIterator.hasNext()) {
                    UserSwitchState listEntry = (UserSwitchState) listIterator.next();
                    Iterator switchIterator = listEntry.getUserSwitchStates().iterator();
                    while (switchIterator.hasNext()) {
                        AbstractMap.SimpleEntry<Integer, Global.SwitchState> switchEntry = (AbstractMap.SimpleEntry<Integer, Global.SwitchState>) switchIterator.next();
                        Integer switchID = switchEntry.getKey();
                        com.rogueone.trackcon.entities.Switch switchInfo = switchArray.get(switchID);
                        ArrayList<Light> currentLights = null;
                        boolean lightsOut = false;
                        if (!offSwitches.contains(switchID)) {
                            if (switchEntry.getValue() == Global.SwitchState.DEFAULT) {
                                updateSwitch(switchID, true);
                                currentLights = switchInfo.getSwitchState().getLightsDefault();
                            } else {
                                updateSwitch(switchID, false);
                                currentLights = switchInfo.getSwitchState().getLightsAlternate();
                            }
                        } else {
                            currentLights = switchInfo.getSwitchState().getLightsDefault();
                            lightsOut = true;
                        }
                        Iterator lightsIterator = currentLights.iterator();
                        while (lightsIterator.hasNext()) {
                            Light lightIteration = (Light) lightsIterator.next();
                            if (lightsOut) {
                                updateTrackLight(lightIteration.getSection(), lightIteration.getBlockID(), Global.LightState.OFF);
                            } else {
                                updateTrackLight(lightIteration.getSection(), lightIteration.getBlockID(), lightIteration.getLightState());
                            }

                        }
                    }
                }
            }
            if (occupiedBlocks != null) {
                updateSection(Global.Section.ZZ, 151, false, (short) 0, 1, 1);
                Iterator blockIterator = occupiedBlocks.iterator();
                while (blockIterator.hasNext()) {
                    PresenceBlock pb = (PresenceBlock) blockIterator.next();
                    double remainingAuthority = 1;
                    double speed = 1;
                    for (TrainModel tm : this.trainSystem.getTrainHandler().getTrains()) {
                        Block trainBlock = tm.getCurrBlock();
                        if (trainBlock.getID() == pb.getCurrBlock().getID()) {
                            remainingAuthority = tm.getAuthority();
                            speed = tm.getCurrSpeedMPH();
                        }
                    }
                    if (pb.getPrevBlock().getType() == Global.PieceType.YARD) {
                        updateSection(pb.getCurrBlock().getSection().getSectionID(), pb.getCurrBlock().getID(), true, pb.getCurrBlock().getTrackCircuit().authority, remainingAuthority, speed);

                    } else {
                        Block prevBlock = (Block) pb.getPrevBlock();
                        updateSection(prevBlock.getSection().getSectionID(), prevBlock.getID(), false, (short) 0, 1, 1);
                        updateSection(pb.getCurrBlock().getSection().getSectionID(), pb.getCurrBlock().getID(), true, pb.getCurrBlock().getTrackCircuit().authority, remainingAuthority, speed);
                    }

                }
            }
            if (crossing != null) {
                updateCrossing(crossing);
            }
            if (trackFailures != null) {
                updateTrackFailures(trackFailures);
            }
        } else {
            if (switchStates != null) {
                Iterator listIterator = switchStates.iterator();
                while (listIterator.hasNext()) {
                    UserSwitchState listEntry = (UserSwitchState) listIterator.next();
                    Iterator switchIterator = listEntry.getUserSwitchStates().iterator();
                    while (switchIterator.hasNext()) {
                        AbstractMap.SimpleEntry<Integer, Global.SwitchState> switchEntry = (AbstractMap.SimpleEntry<Integer, Global.SwitchState>) switchIterator.next();
                        Integer switchID = switchEntry.getKey();
                        com.rogueone.trackcon.entities.Switch switchInfo = switchArray.get(switchID);
                        ArrayList<Light> currentLights = null;
                        boolean lightsOut = false;
                        if (!offSwitches.contains(switchID)) {
                            if (switchEntry.getValue() == Global.SwitchState.DEFAULT) {
                                updateSwitch(switchID, true);
                                currentLights = switchInfo.getSwitchState().getLightsDefault();
                            } else {
                                updateSwitch(switchID, false);
                                currentLights = switchInfo.getSwitchState().getLightsAlternate();
                            }
                        } else {
                            currentLights = switchInfo.getSwitchState().getLightsDefault();
                            lightsOut = true;
                        }
                        Iterator lightsIterator = currentLights.iterator();
                        while (lightsIterator.hasNext()) {
                            Light lightIteration = (Light) lightsIterator.next();
                            if (lightsOut) {
                                updateTrackLight(lightIteration.getSection(), lightIteration.getBlockID(), Global.LightState.OFF);
                            } else {
                                updateTrackLight(lightIteration.getSection(), lightIteration.getBlockID(), lightIteration.getLightState());
                            }
                        }
                    }
                }
            }
            if (occupiedBlocks != null) {
                updateSection(Global.Section.U, 77, false, (short) 0, 1, 1);
                Iterator blockIterator = occupiedBlocks.iterator();
                while (blockIterator.hasNext()) {
                    PresenceBlock pb = (PresenceBlock) blockIterator.next();
                    double remainingAuthority = 1;
                    double speed = 1;
                    for (TrainModel tm : this.trainSystem.getTrainHandler().getTrains()) {
                        Block trainBlock = tm.getCurrBlock();
                        if (trainBlock.getID() == pb.getCurrBlock().getID()) {
                            remainingAuthority = tm.getAuthority();
                            speed = tm.getCurrSpeedMPH();
                        }
                    }
                    if (pb.getPrevBlock().getType() == Global.PieceType.YARD) {
                        updateSection(pb.getCurrBlock().getSection().getSectionID(), pb.getCurrBlock().getID(), true, pb.getCurrBlock().getTrackCircuit().authority, remainingAuthority, speed);

                    } else {
                        Block prevBlock = (Block) pb.getPrevBlock();
                        updateSection(prevBlock.getSection().getSectionID(), prevBlock.getID(), false, (short) 0, 1, 1);
                        updateSection(pb.getCurrBlock().getSection().getSectionID(), pb.getCurrBlock().getID(), true, pb.getCurrBlock().getTrackCircuit().authority, remainingAuthority, speed);
                    }
                }
            }
            if (crossing != null) {
                updateCrossing(crossing);
            }
            if (trackFailures != null) {
                updateTrackFailures(trackFailures);
            }
        }
        sp.repaint();

    }

    /**
     * Method that places and initializes the green line layout
     */
    private void initializeGreenLine() {
        Switch switch0 = new Switch(35 + shiftAmountX + 3, 51, 35 + shiftAmountX + 3, 59, 15, 5, 45, -45, 0);
        sp.addShape(switch0);
        switch0.setIsDefault(true);
        Switch switch4 = new Switch(190 + shiftAmountX + 15, 57, 190 + shiftAmountX + 23, 57, 12, 5, 45, -45, 4);
        sp.addShape(switch4);
        switch4.setIsDefault(true);
        Switch switch5 = new Switch(190 + shiftAmountX + 17, 115, 190 + shiftAmountX + 23, 115, 12, 5, -55, 55, 5);
        sp.addShape(switch5);
        switch5.setIsDefault(true);
        Switch switch1 = new Switch(705 + shiftAmountX, 225, 705 + shiftAmountX + 8, 225, 14, 5, -55, 55, 1);
        sp.addShape(switch1);
        switch1.setIsDefault(true);
        Switch switch2 = new Switch(705 + shiftAmountX, 57, 705 + shiftAmountX + 8, 57, 12, 5, 45, -45, 2);
        sp.addShape(switch2);
        switch2.setIsDefault(true);
        Switch switch3 = new Switch(870 + shiftAmountX + 9, 51, 870 + shiftAmountX + 6, 59, 15, 5, -45, 45, 3);
        sp.addShape(switch3);
        switch3.setIsDefault(true);

        switchList.put(0, switch0);
        switchList.put(1, switch1);
        switchList.put(2, switch2);
        switchList.put(3, switch3);
        switchList.put(4, switch4);
        switchList.put(5, switch5);

        Section YY = new Section(0 + shiftAmountX, 30, 50, 5, 45, "YY", -15, 16, this.trainSystem, Global.Line.GREEN);
        sp.addShape(YY);
        sectionList.put("YY", YY);
        Section J1 = new Section(0 + shiftAmountX, 80, 50, 5, -45, "J", 4, 13, this.trainSystem, Global.Line.GREEN);
        sp.addShape(J1);
        sectionList.put("J1", J1);
        Section K = new Section(50 + shiftAmountX, 55, 50, 5, 0, "K", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(K);
        sectionList.put("K", K);

        Section L = new Section(105 + shiftAmountX, 55, 50, 5, 0, "L", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(L);
        sectionList.put("L", L);
        Section M = new Section(160 + shiftAmountX, 55, 50, 5, 0, "M", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(M);
        sectionList.put("M", M);
        //first vertical section
        Section N = new Section(190 + shiftAmountX, 85, 50, 5, 90, "N", -13, 0, this.trainSystem, Global.Line.GREEN);  //192
        sp.addShape(N);
        sectionList.put("N", N);
        //first loop
        Section O = new Section(175 + shiftAmountX, 135 + 2, 50, 5, -60, "O", -15, 4, this.trainSystem, Global.Line.GREEN);
        sp.addShape(O);
        sectionList.put("O", O);
        Section P = new Section(190 + shiftAmountX + 2, 160 + 2, 50, 5, 0, "P", -5, -5, this.trainSystem, Global.Line.GREEN);
        sp.addShape(P);
        sectionList.put("P", P);
        Section Q = new Section(210 + shiftAmountX - 2, 135 + 2, 50, 5, 60, "Q", 5, 5, this.trainSystem, Global.Line.GREEN);
        sp.addShape(Q);
        sectionList.put("Q", Q);

        Section R = new Section(220 + shiftAmountX, 55, 50, 5, 0, "R", -3, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(R);
        sectionList.put("R", R);

        Section S = new Section(270 + shiftAmountX + 5, 55, 50, 5, 0, "S", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(S);
        sectionList.put("S", S);
        Section T = new Section(325 + shiftAmountX + 5, 55, 50, 5, 0, "T", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(T);
        sectionList.put("T", T);
        Section U = new Section(380 + shiftAmountX + 5, 55, 50, 5, 0, "U", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(U);
        sectionList.put("U", U);
        Section V = new Section(435 + shiftAmountX + 5, 55, 50, 5, 0, "V", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(V);
        sectionList.put("V", V);
        Section W = new Section(490 + shiftAmountX + 5, 55, 50, 5, 0, "W", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(W);
        sectionList.put("W", W);
        Section X = new Section(545 + shiftAmountX + 5, 55, 50, 5, 0, "X", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(X);
        sectionList.put("X", X);
        Section Y = new Section(600 + shiftAmountX + 5, 55, 50, 5, 0, "Y", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(Y);
        sectionList.put("Y", Y);
        Section Z = new Section(655 + shiftAmountX + 5, 55, 50, 5, 0, "Z", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(Z);
        sectionList.put("Z", Z);
        //second vertical section
        Section F = new Section(685 + shiftAmountX + 5, 85, 50, 5, 90, "F", -13, 0, this.trainSystem, Global.Line.GREEN);
        sp.addShape(F);
        sectionList.put("F", F);
        Section E = new Section(685 + shiftAmountX + 5, 140, 50, 5, 90, "E", -13, 0, this.trainSystem, Global.Line.GREEN);
        sp.addShape(E);
        sectionList.put("E", E);
        Section D = new Section(685 + shiftAmountX + 5, 195, 50, 5, 90, "D", -13, 0, this.trainSystem, Global.Line.GREEN);
        sp.addShape(D);
        sectionList.put("D", D);
        //second loop
        Section C = new Section(665 + shiftAmountX + 9, 245, 50, 5, -60, "C", -22, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(C);
        sectionList.put("C", C);
        Section B = new Section(685 + shiftAmountX + 5, 270, 50, 5, 0, "B", -5, -5, this.trainSystem, Global.Line.GREEN);
        sp.addShape(B);
        sectionList.put("B", B);
        Section A = new Section(705 + shiftAmountX + 2, 245, 50, 5, 60, "A", 14, 16, this.trainSystem, Global.Line.GREEN);
        sp.addShape(A);
        sectionList.put("A", A);

        Section G = new Section(710 + shiftAmountX + 10, 55, 50, 5, 0, "G", -3, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(G);
        sectionList.put("G", G);

        Section H = new Section(765 + shiftAmountX + 10, 55, 50, 5, 0, "H", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(H);
        sectionList.put("H", H);
        Section I = new Section(820 + shiftAmountX + 10, 55, 50, 5, 0, "I", -5, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(I);
        sectionList.put("I", I);

        Section ZZ = new Section(870 + shiftAmountX + 12, 30, 50, 5, -45, "ZZ", 3, 15, this.trainSystem, Global.Line.GREEN);
        sp.addShape(ZZ);
        sectionList.put("ZZ", ZZ);
        Section J2 = new Section(870 + shiftAmountX + 10, 80, 50, 5, 45, "J", -4, 13, this.trainSystem, Global.Line.GREEN);
        sp.addShape(J2);
        sectionList.put("J2", J2);

        TrackLight lightYY = new TrackLight(85, 35, 225, false);
        sp.addShape(lightYY);
        trackLightList.put("YY", lightYY);
        TrackLight lightJ1 = new TrackLight(50, 50, 135, false);
        sp.addShape(lightJ1);
        trackLightList.put("J1", lightJ1);
        TrackLight lightJ2 = new TrackLight(870 + shiftAmountX + 60, 80, 225, false);
        sp.addShape(lightJ2);
        trackLightList.put("J2", lightJ2);
        TrackLight lightM = new TrackLight(160 + shiftAmountX + 40, 32, 180, false);
        sp.addShape(lightM);
        trackLightList.put("M", lightM);
        TrackLight lightN77 = new TrackLight(160 + shiftAmountX + 70, 60, 90, true);
        sp.addShape(lightN77);
        trackLightList.put("N77", lightN77);
        TrackLight lightN85 = new TrackLight(160 + shiftAmountX + 30, 105, -90, true);
        sp.addShape(lightN85);
        trackLightList.put("N85", lightN85);
        TrackLight lightQ = new TrackLight(160 + shiftAmountX + 73, 105, 55, true);
        sp.addShape(lightQ);
        trackLightList.put("Q", lightQ);
        TrackLight lightZ = new TrackLight(655 + shiftAmountX + 45, 32, 180, false);
        sp.addShape(lightZ);
        trackLightList.put("Z", lightZ);
        TrackLight lightF = new TrackLight(655 + shiftAmountX + 75, 60, 90, true);
        sp.addShape(lightF);
        trackLightList.put("F", lightF);
        TrackLight lightD = new TrackLight(655 + shiftAmountX + 35, 215, -90, true);
        sp.addShape(lightD);
        trackLightList.put("D", lightD);
        TrackLight lightA = new TrackLight(655 + shiftAmountX + 78, 215, 55, true);
        sp.addShape(lightA);
        trackLightList.put("A", lightA);
        TrackLight lightI = new TrackLight(820 + shiftAmountX + 50, 32, 180, false);
        sp.addShape(lightI);
        trackLightList.put("I", lightI);
        TrackLight lightZZ = new TrackLight(860 + shiftAmountX + 38, 2, 135, false);
        sp.addShape(lightZZ);
        trackLightList.put("ZZ", lightZZ);

        Station stationGlenbury = new Station(110, 36, 8);
        sp.addShape(stationGlenbury);
        Station stationDormont2 = new Station(369, 36, 8);
        sp.addShape(stationDormont2);
        Station stationDormont = new Station(186, 36, 8);
        sp.addShape(stationDormont);
        Station stationGlenbury2 = new Station(453, 36, 8);
        sp.addShape(stationGlenbury2);
        Station stationMtLebanon = new Station(235, 63, 7);
        sp.addShape(stationMtLebanon);
        Station stationPoplar = new Station(212, 150, 8);
        sp.addShape(stationPoplar);
        Station stationOverbrook1 = new Station(534, 36, 8);
        sp.addShape(stationOverbrook1);
        Station stationInglewood1 = new Station(555, 36, 8);
        sp.addShape(stationInglewood1);
        Station stationCentral1 = new Station(573, 36, 8);
        sp.addShape(stationCentral1);
        Station stationCastleShannon = new Station(267, 170, 8);
        sp.addShape(stationCastleShannon);
        Station stationSouthBank = new Station(785, 36, 8);
        sp.addShape(stationSouthBank);
        Station stationWhited = new Station(763, 95, 8);
        sp.addShape(stationWhited);
        Station stationUniOfPitt = new Station(763, 170, 8);
        sp.addShape(stationUniOfPitt);

        Station stationEdgebrook = new Station(720, 236, 8);
        sp.addShape(stationEdgebrook);
        Station stationPioneer = new Station(781, 236, 8);
        sp.addShape(stationPioneer);

        Station stationCentral2 = new Station(875, 36, 8);
        sp.addShape(stationCentral2);
        Station stationInglewood2 = new Station(895, 36, 8);
        sp.addShape(stationInglewood2);
        Station stationOverbrook2 = new Station(913, 64, 8);
        sp.addShape(stationOverbrook2);

        crossing = new Crossing(750, 140);
        sp.addShape(crossing);

        yardStart = new Yard(0, 0);
        sp.addShape(yardStart);
        Yard yardEnd = new Yard(950, 0);
        sp.addShape(yardEnd);

        LegendBox lb = new LegendBox(50, 240, trainSystem);
        sp.addShape(lb);

    }

    /**
     * first idea for red line layout
     */
    private void initializeRedLine() {

        // Branch to Yard
        Section U = new Section(0 + shiftAmountX, 30, 50, 5, 45, "U", -15, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(U);
        //sectionList.put("U", U);

        // First loop
        Section A = new Section(120 + shiftAmountX, 80, 50, 5, -45, "A", 5, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(A);
        //sectionList.put("A", A);
        Section B = new Section(90 + shiftAmountX, 100, 30, 5, 0, "B", -5, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(B);
        //sectionList.put("B", B);
        Section C = new Section(40 + shiftAmountX, 80, 50, 5, 45, "C", -15, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(C);
        //sectionList.put("C", C);
        Section D = new Section(50 + shiftAmountX, 55, 50, 5, 0, "D", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(D);
        //sectionList.put("D", D);
        Section E = new Section(105 + shiftAmountX, 55, 50, 5, 0, "E", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(E);
        //sectionList.put("E", E);

        // Main strech
        Section F = new Section(160 + shiftAmountX, 55, 50, 5, 0, "F", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(F);
        //sectionList.put("F", F);
        Section G = new Section(215 + shiftAmountX, 55, 50, 5, 0, "G", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(G);
        //sectionList.put("G", G);
        Section H = new Section(270 + shiftAmountX, 55, 350, 5, 0, "H", -5, -15, this.trainSystem, Global.Line.RED);
        sp.addShape(H);
        //sectionList.put("H", H);
        Section I = new Section(625 + shiftAmountX, 55, 50, 5, 0, "I", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(I);
        //sectionList.put("I", I);

        // Second loop
        Section J = new Section(680 + shiftAmountX, 55, 50, 5, 0, "J", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(J);
        //sectionList.put("J", J);
        Section K = new Section(735 + shiftAmountX, 55, 50, 5, 0, "K", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(K);
        //sectionList.put("K", K);
        Section L = new Section(785 + shiftAmountX, 75, 50, 5, 45, "L", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(L);
        //sectionList.put("L", L);
        Section M = new Section(755 + shiftAmountX, 100, 50, 5, 0, "M", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(M);
        //sectionList.put("M", M);
        Section N = new Section(705 + shiftAmountX, 80, 50, 5, 45, "N", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(N);
        //sectionList.put("N", N);

        // First wayside
        Section O = new Section(380 + shiftAmountX, 80, 50, 5, -45, "O", 5, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(O);
        //sectionList.put("O", O);
        Section P = new Section(350 + shiftAmountX, 100, 30, 5, 0, "P", -5, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(P);
        //sectionList.put("P", P);
        Section Q = new Section(300 + shiftAmountX, 80, 50, 5, 45, "Q", -15, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(Q);
        //sectionList.put("Q", Q);

        // Second wayside
        Section R = new Section(580 + shiftAmountX, 80, 50, 5, -45, "R", 5, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(R);
        //sectionList.put("R", R);   
        Section S = new Section(550 + shiftAmountX, 100, 30, 5, 0, "S", -5, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(S);
        //sectionList.put("S", S);  
        Section T = new Section(500 + shiftAmountX, 80, 50, 5, 45, "T", -15, 16, this.trainSystem, Global.Line.RED);
        sp.addShape(T);
        //sectionList.put("T", T);  

        // Switches
        Switch switch1 = new Switch(25 + shiftAmountX + 15, 50, 30 + shiftAmountX + 23, 57, 12, 5, 45, 0, 12);
        sp.addShape(switch1);
        switch1.setIsDefault(true);
        Switch switch2 = new Switch(150 + shiftAmountX + 15, 57, 150 + shiftAmountX + 23, 57, 12, 5, -45, 0, 6);
        sp.addShape(switch2);
        switch2.setIsDefault(true);
        Switch switch3 = new Switch(285 + shiftAmountX + 15, 57, 290 + shiftAmountX + 23, 57, 12, 5, 45, 0, 7);
        sp.addShape(switch3);
        switch3.setIsDefault(true);
        Switch switch4 = new Switch(405 + shiftAmountX + 15, 57, 405 + shiftAmountX + 23, 57, 12, 5, -45, 0, 8);
        sp.addShape(switch4);
        switch4.setIsDefault(true);
        Switch switch5 = new Switch(485 + shiftAmountX + 15, 57, 490 + shiftAmountX + 23, 57, 12, 5, 45, 0, 9);
        sp.addShape(switch5);
        switch5.setIsDefault(true);
        Switch switch6 = new Switch(605 + shiftAmountX + 15, 57, 605 + shiftAmountX + 23, 57, 12, 5, -45, 0, 10);
        sp.addShape(switch6);
        switch6.setIsDefault(true);
        Switch switch7 = new Switch(690 + shiftAmountX + 15, 57, 690 + shiftAmountX + 23, 57, 12, 5, 45, 0, 11);
        sp.addShape(switch7);
        switch7.setIsDefault(true);

        //TrackLights
        TrackLight light1 = new TrackLight(25 + shiftAmountX, 45, 0, false);
        sp.addShape(light1);
        //trackLightList.put("?", light1);
        TrackLight light2 = new TrackLight(25 + shiftAmountX, 65, 0, true);
        sp.addShape(light2);
        //trackLightList.put("?", light2);
        TrackLight light3 = new TrackLight(150 + shiftAmountX, 45, 0, false);
        sp.addShape(light3);
        //trackLightList.put("?", light3);
        TrackLight light4 = new TrackLight(150 + shiftAmountX, 65, 0, true);
        sp.addShape(light4);
        //trackLightList.put("?", light4);
        TrackLight light5 = new TrackLight(285 + shiftAmountX, 45, 0, false);
        sp.addShape(light5);
        //trackLightList.put("?", light5);
        TrackLight light6 = new TrackLight(285 + shiftAmountX, 65, 0, true);
        sp.addShape(light6);
        //trackLightList.put("?", light6);
        TrackLight light7 = new TrackLight(405 + shiftAmountX, 45, 0, false);
        sp.addShape(light7);
        //trackLightList.put("?", light7);
        TrackLight light8 = new TrackLight(405 + shiftAmountX, 65, 0, true);
        sp.addShape(light8);
        //trackLightList.put("?", light8);
        TrackLight light9 = new TrackLight(485 + shiftAmountX, 45, 0, false);
        sp.addShape(light9);
        //trackLightList.put("?", light9);
        TrackLight light10 = new TrackLight(485 + shiftAmountX, 65, 0, true);
        sp.addShape(light10);
        //trackLightList.put("?", light10);
        TrackLight light11 = new TrackLight(605 + shiftAmountX, 45, 0, false);
        sp.addShape(light11);
        //trackLightList.put("?", light11);
        TrackLight light12 = new TrackLight(605 + shiftAmountX, 65, 0, true);
        sp.addShape(light12);
        //trackLightList.put("?", light12);
        TrackLight light13 = new TrackLight(690 + shiftAmountX, 45, 0, false);
        sp.addShape(light13);
        //trackLightList.put("?", light13);
        TrackLight light14 = new TrackLight(690 + shiftAmountX, 65, 0, true);
        sp.addShape(light14);
        //trackLightList.put("?", light14);

        //Stations
        Station stationShadyside = new Station(70, 100, 8);
        sp.addShape(stationShadyside);
        Station stationHerron = new Station(210, 36, 8);
        sp.addShape(stationHerron);
        Station stationSwissvale = new Station(265, 36, 8);
        sp.addShape(stationSwissvale);
        Station stationPenn = new Station(320, 36, 8);
        sp.addShape(stationPenn);
        Station stationSteelPlaza = new Station(450, 36, 8);
        sp.addShape(stationSteelPlaza);
        Station stationFirstAve = new Station(650, 36, 8);
        sp.addShape(stationFirstAve);
        Station stationStationSquare = new Station(715, 36, 8);
        sp.addShape(stationStationSquare);
        Station stationSouthHills = new Station(860, 100, 8);
        sp.addShape(stationSouthHills);

        //Crossings
        crossing = new Crossing(670, 30);
        sp.addShape(crossing);

        //Yards
        Yard yardStart = new Yard(0, 0);
        sp.addShape(yardStart);

        LegendBox lb = new LegendBox(1, 200, trainSystem);
        sp.addShape(lb);

    }

    /**
     * second idea for red line layout
     */
    private void initializeRedLine2() {
        Switch switch6 = new Switch(140 + shiftAmountX + 50, 44 + shiftAmountY, 139 + shiftAmountX + 50, 51 + shiftAmountY, 13, 5, 45, -30, 6);
        switch6.setIsDefault(true);
        sp.addShape(switch6);
        Switch switch7 = new Switch(310 + shiftAmountX + 50, 48 + shiftAmountY, 308 + shiftAmountX + 50, 45 + shiftAmountY, 8, 5, 0, -45, 7);
        switch7.setIsDefault(true);
        sp.addShape(switch7);
        Switch switch8 = new Switch(485 + shiftAmountX, 48 + shiftAmountY, 487 + shiftAmountX, 45 + shiftAmountY, 8, 5, 0, 45, 8);
        switch8.setIsDefault(true);
        sp.addShape(switch8);
        Switch switch9 = new Switch(542 + shiftAmountX, 48 + shiftAmountY, 540 + shiftAmountX, 45 + shiftAmountY, 8, 5, 0, -45, 9);
        switch9.setIsDefault(true);
        sp.addShape(switch9);
        Switch switch10 = new Switch(668 + shiftAmountX, 48 + shiftAmountY, 669 + shiftAmountX, 45 + shiftAmountY, 8, 5, 0, 45, 10);
        switch10.setIsDefault(true);
        sp.addShape(switch10);
        Switch switch11 = new Switch(831 + shiftAmountX, 44 + shiftAmountY, 831 + shiftAmountX, 51 + shiftAmountY, 12, 5, -45, 30, 11);
        switch11.setIsDefault(false);
        sp.addShape(switch11);
        Switch switch12 = new Switch(10 + shiftAmountX + 50, 46 + shiftAmountY, 14 + shiftAmountX + 50, 48 + shiftAmountY, 12, 5, -45, 90, 12);
        switch12.setIsDefault(true);
        sp.addShape(switch12);
        switchList.put(6, switch6);
        switchList.put(7, switch7);
        switchList.put(8, switch8);
        switchList.put(9, switch9);
        switchList.put(10, switch10);
        switchList.put(11, switch11);
        switchList.put(12, switch12);

        Section U = new Section(13 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "U", -5, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(U);
        sectionList.put("U", U);

        Section C = new Section(63 + shiftAmountX, 25 + shiftAmountY, 50, 5, -45, "C", 10, 5, this.trainSystem, Global.Line.RED);
        sp.addShape(C);
        sectionList.put("C", C);
        Section B = new Section(108 + shiftAmountX, 7 + shiftAmountY, 50, 5, 0, "B", -5, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(B);
        sectionList.put("B", B);
        Section A = new Section(152 + shiftAmountX, 25 + shiftAmountY, 50, 5, 45, "A", -15, 5, this.trainSystem, Global.Line.RED);
        sp.addShape(A);
        sectionList.put("A", A);

        Section D = new Section(65 + shiftAmountX, 70 + shiftAmountY, 70, 5, 30, "D", 0, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(D);
        sectionList.put("D", D);
        Section E = new Section(128 + shiftAmountX, 70 + shiftAmountY, 71, 5, -30, "E", -7, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(E);
        sectionList.put("E", E);

        Section F = new Section(200 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "F", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(F);
        sectionList.put("F", F);

        Section G = new Section(255 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "G", -5, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(G);
        sectionList.put("G", G);

        Section H24_27 = new Section(310 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "H24_27", -23, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(H24_27);
        sectionList.put("H24_27", H24_27);

        Section T = new Section(357 + shiftAmountX, 25 + shiftAmountY, 50, 5, -45, "T", -5, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(T);
        sectionList.put("T", T);
        Section S = new Section(402 + shiftAmountX, 7 + shiftAmountY, 50, 5, 0, "S", -5, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(S);
        sectionList.put("S", S);
        Section R = new Section(446 + shiftAmountX, 25 + shiftAmountY, 50, 5, 45, "R", 5, 0, this.trainSystem, Global.Line.RED);
        sp.addShape(R);
        sectionList.put("R", R);

        Section H28_32 = new Section(368 + shiftAmountX, 48 + shiftAmountY, 117, 5, 0, "H28_32", -20, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(H28_32);
        sectionList.put("H28_32", H28_32);

        Section H33_38 = new Section(492 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "H33_38", -22, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(H33_38);
        sectionList.put("H33_38", H33_38);

        Section Q = new Section(539 + shiftAmountX, 25 + shiftAmountY, 50, 5, -45, "Q", -10, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(Q);
        sectionList.put("Q", Q);
        Section P = new Section(584 + shiftAmountX, 7 + shiftAmountY, 50, 5, 0, "P", -5, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(P);
        sectionList.put("P", P);
        Section O = new Section(628 + shiftAmountX, 25 + shiftAmountY, 50, 5, 45, "O", 5, 0, this.trainSystem, Global.Line.RED);
        sp.addShape(O);
        sectionList.put("O", O);

        Section H39_43 = new Section(550 + shiftAmountX, 48 + shiftAmountY, 117, 5, 0, "H39_43", -20, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(H39_43);
        sectionList.put("H39_43", H39_43);

        Section H44_45 = new Section(674 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "H44_45", -20, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(H44_45);
        sectionList.put("H44_45", H44_45);

        Section I = new Section(729 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "I", 0, 15, this.trainSystem, Global.Line.RED);
        sp.addShape(I);
        sectionList.put("I", I);
        Section J49_52 = new Section(784 + shiftAmountX, 48 + shiftAmountY, 50, 5, 0, "J49_52", -22, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(J49_52);
        sectionList.put("J49_52", J49_52);

        Section J53_54 = new Section(831 + shiftAmountX, 25 + shiftAmountY, 50, 5, -45, "J53_54", 12, 5, this.trainSystem, Global.Line.RED);
        sp.addShape(J53_54);
        sectionList.put("J53_54", J53_54);
        Section K = new Section(876 + shiftAmountX, 7 + shiftAmountY, 50, 5, 0, "K", -5, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(K);
        sectionList.put("K", K);
        Section L = new Section(920 + shiftAmountX, 25 + shiftAmountY, 50, 5, 45, "L", -10, 10, this.trainSystem, Global.Line.RED);
        sp.addShape(L);
        sectionList.put("L", L);

        Section M = new Section(895 + shiftAmountX, 67 + shiftAmountY, 73, 5, -33, "M", -10, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(M);
        sectionList.put("M", M);
        Section N = new Section(834 + shiftAmountX, 70 + shiftAmountY, 70, 5, 30, "N", 0, -5, this.trainSystem, Global.Line.RED);
        sp.addShape(N);
        sectionList.put("N", N);

        TrackLight lightU = new TrackLight(48 + shiftAmountX, 65 + shiftAmountY, 180, true);
        sp.addShape(lightU);
        trackLightList.put("U", lightU);
        TrackLight lightF = new TrackLight(205 + shiftAmountX, 65 + shiftAmountY, 0, false);
        sp.addShape(lightF);
        trackLightList.put("F", lightF);

        TrackLight lightH27 = new TrackLight(345 + shiftAmountX, 65 + shiftAmountY, 180, true);
        sp.addShape(lightH27);
        trackLightList.put("H27", lightH27);
        TrackLight lightH28 = new TrackLight(387 + shiftAmountX, 25 + shiftAmountY, 0, true);
        sp.addShape(lightH28);
        trackLightList.put("H28", lightH28);
        TrackLight lightH32 = new TrackLight(458 + shiftAmountX, 25 + shiftAmountY, 180, false);
        sp.addShape(lightH32);
        trackLightList.put("H32", lightH32);
        TrackLight lightH33 = new TrackLight(490 + shiftAmountX, 65 + shiftAmountY, 0, false);
        sp.addShape(lightH33);
        trackLightList.put("H33", lightH33);
        TrackLight lightH38 = new TrackLight(535 + shiftAmountX, 65 + shiftAmountY, 180, true);
        sp.addShape(lightH38);
        trackLightList.put("H38", lightH38);
        TrackLight lightH39 = new TrackLight(570 + shiftAmountX, 25 + shiftAmountY, 0, true);
        sp.addShape(lightH39);
        trackLightList.put("H39", lightH39);
        TrackLight lightH43 = new TrackLight(640 + shiftAmountX, 25 + shiftAmountY, 180, false);
        sp.addShape(lightH43);
        trackLightList.put("H43", lightH43);
        TrackLight lightH44 = new TrackLight(675 + shiftAmountX, 65 + shiftAmountY, 0, false);
        sp.addShape(lightH44);
        trackLightList.put("H44", lightH44);

        TrackLight lightJ52 = new TrackLight(820 + shiftAmountX, 65 + shiftAmountY, 180, true);
        sp.addShape(lightJ52);
        trackLightList.put("J52", lightJ52);

        TrackLight lightC = new TrackLight(55 + shiftAmountX, 23 + shiftAmountY, -45, true);
        sp.addShape(lightC);
        trackLightList.put("C", lightC);
        TrackLight lightT = new TrackLight(350 + shiftAmountX, 23 + shiftAmountY, -45, true);
        sp.addShape(lightT);
        trackLightList.put("T", lightT);
        TrackLight lightQ = new TrackLight(532 + shiftAmountX, 23 + shiftAmountY, -45, true);
        sp.addShape(lightQ);
        trackLightList.put("Q", lightQ);
        TrackLight lightJ53 = new TrackLight(823 + shiftAmountX, 23 + shiftAmountY, -45, true);
        sp.addShape(lightJ53);
        trackLightList.put("J53", lightJ53);

        TrackLight lightA = new TrackLight(200 + shiftAmountX, 23 + shiftAmountY, 225, false);
        sp.addShape(lightA);
        trackLightList.put("A", lightA);
        TrackLight lightR = new TrackLight(495 + shiftAmountX, 23 + shiftAmountY, 225, false);
        sp.addShape(lightR);
        trackLightList.put("R", lightR);
        TrackLight lightO = new TrackLight(677 + shiftAmountX, 23 + shiftAmountY, 225, false);
        sp.addShape(lightO);
        trackLightList.put("O", lightO);

        TrackLight lightD = new TrackLight(82 + shiftAmountX, 37 + shiftAmountY, 40, true);
        sp.addShape(lightD);
        trackLightList.put("D", lightD);
        TrackLight lightN = new TrackLight(850 + shiftAmountX, 37 + shiftAmountY, 40, true);
        sp.addShape(lightN);
        trackLightList.put("N", lightN);

        TrackLight lightE = new TrackLight(170 + shiftAmountX, 37 + shiftAmountY, -220, false);
        sp.addShape(lightE);
        trackLightList.put("E", lightE);

        yardStart = new Yard(5, 42 + shiftAmountY);
        sp.addShape(yardStart);

        LegendBox legend = new LegendBox(50, 240, trainSystem);
        sp.addShape(legend);

        Station shadysideStation = new Station((int) (100 + shiftAmountX), (int) 42, (int) (8));
        sp.addShape(shadysideStation);
        Station herronAveStation = new Station((int) (216 + shiftAmountX), (int) (31 + shiftAmountY), (int) (8));
        sp.addShape(herronAveStation);
        Station swissvilleStation = new Station((int) (269 + shiftAmountX), (int) (31 + shiftAmountY), (int) (8));
        sp.addShape(swissvilleStation);
        Station pennStationStation = new Station((int) (333 + shiftAmountX), (int) (54 + shiftAmountY), (int) (8));
        sp.addShape(pennStationStation);
        Station steelPlazaStation = new Station((int) (518 + shiftAmountX), (int) (54 + shiftAmountY), (int) (8));
        sp.addShape(steelPlazaStation);
        Station firstAveStation = new Station((int) (718 + shiftAmountX), (int) (54 + shiftAmountY), (int) (8));
        sp.addShape(firstAveStation);
        Station stationSquareStation = new Station((int) (775 + shiftAmountX), (int) (54 + shiftAmountY), (int) (8));
        sp.addShape(stationSquareStation);
        Station southHillsStation = new Station((int) (967 + shiftAmountX), (int) (17 + shiftAmountY), (int) (8));
        sp.addShape(southHillsStation);

        crossing = new Crossing(775, 25 + shiftAmountY);
        sp.addShape(crossing);
    }

    /**
     * update a switch visually
     * @param switchID
     * @param defaultOrAlternate 
     */
    private void updateSwitch(Integer switchID, boolean defaultOrAlternate) {
        Switch s = switchList.get(switchID);
        if (s != null) {
            s.setIsDefault(defaultOrAlternate);
        }
    }

    /**
     * update track light visually
     * @param section
     * @param blockID
     * @param lightState 
     */
    private void updateTrackLight(Global.Section section, int blockID, Global.LightState lightState) {
        if (this.line == Global.Line.GREEN) {
            if (section.toString() == "N") {
                TrackLight tl = trackLightList.get(section.toString() + String.valueOf(blockID));
                if (tl != null) {
                    if (lightState == Global.LightState.GO) {
                        tl.setIsGo(true);
                        tl.setIsStop(false);
                    } else if (lightState == Global.LightState.STOP) {
                        tl.setIsGo(false);
                        tl.setIsStop(true);
                    } else if (lightState == Global.LightState.OFF) {
                        tl.setIsGo(false);
                        tl.setIsStop(false);
                    }
                }
            } else if (section.toString() == "J") {
                TrackLight tl1 = trackLightList.get(section.toString() + "1");
                TrackLight tl2 = trackLightList.get(section.toString() + "2");
                if (tl1 != null && tl2 != null) {
                    if (lightState == Global.LightState.GO) {
                        tl1.setIsGo(true);
                        tl1.setIsStop(false);
                        tl2.setIsGo(true);
                        tl2.setIsStop(false);
                    } else if (lightState == Global.LightState.STOP) {
                        tl1.setIsGo(false);
                        tl1.setIsStop(true);
                        tl2.setIsGo(false);
                        tl2.setIsStop(true);
                    } else if (lightState == Global.LightState.OFF) {
                        tl1.setIsGo(false);
                        tl1.setIsStop(false);
                        tl2.setIsGo(false);
                        tl2.setIsStop(false);
                    }
                }
            } else {
                TrackLight tl = trackLightList.get(section.toString());
                if (tl != null) {
                    if (lightState == Global.LightState.GO) {
                        tl.setIsGo(true);
                        tl.setIsStop(false);
                    } else if (lightState == Global.LightState.STOP) {
                        tl.setIsGo(false);
                        tl.setIsStop(true);
                    } else if (lightState == Global.LightState.OFF) {
                        tl.setIsGo(false);
                        tl.setIsStop(false);
                    }
                }
            }
        } else {
            if (section.toString() == "H" || section.toString() == "J") {
                TrackLight tl = trackLightList.get(section.toString() + String.valueOf(blockID));
                if (tl != null) {
                    if (lightState == Global.LightState.GO) {
                        tl.setIsGo(true);
                        tl.setIsStop(false);
                    } else if (lightState == Global.LightState.STOP) {
                        tl.setIsGo(false);
                        tl.setIsStop(true);
                    } else if (lightState == Global.LightState.OFF) {
                        tl.setIsGo(false);
                        tl.setIsStop(false);
                    }
                }
            } else {
                TrackLight tl = trackLightList.get(section.toString());
                if (tl != null) {
                    if (lightState == Global.LightState.GO) {
                        tl.setIsGo(true);
                        tl.setIsStop(false);
                    } else if (lightState == Global.LightState.STOP) {
                        tl.setIsGo(false);
                        tl.setIsStop(true);
                    } else if (lightState == Global.LightState.OFF) {
                        tl.setIsGo(false);
                        tl.setIsStop(false);
                    }
                }
            }
        }

    }

    /**
     * update section with train occupancy or failures visually
     * @param sectionID
     * @param id
     * @param occupied
     * @param authority
     * @param remainingAuthority
     * @param speed 
     */
    private void updateSection(Global.Section sectionID, int id, boolean occupied, short authority, double remainingAuthority, double speed) {
        if (this.line == Global.Line.GREEN) {
            Section s1, s2 = null;
            if (sectionID.toString().equals("J") && id == 62) {
                //light up K
                s1 = sectionList.get("K");
                s1.setIsOccupied(occupied);
            } else if (sectionID.toString().equals("J") && id != 62) {
                s1 = sectionList.get("J1");
                s1.setIsOccupied(occupied);
                s2 = sectionList.get("J2");
                s2.setIsOccupied(occupied);
            } else {
                s1 = sectionList.get(sectionID.toString());
                s1.setIsOccupied(occupied);
            }
            if (occupied) {
                s1.addBlockToCurrentBlocks(id);
                if (authority == -1) {
                    s1.setIsHalted(true);
                } else {
                    s1.setIsHalted(false);
                }
                if (remainingAuthority <= 0 || speed <= 0) {
                    s1.setIsStopped(true);
                } else {
                    s1.setIsStopped(false);
                }
                if (s2 != null) {
                    s2.addBlockToCurrentBlocks(id);
                    if (authority == -1) {
                        s2.setIsHalted(true);
                    } else {
                        s2.setIsHalted(false);
                    }
                    if (remainingAuthority <= 0 || speed <= 0) {
                        s2.setIsStopped(true);
                    } else {
                        s2.setIsStopped(false);
                    }
                }
            } else {
                s1.removeBlockFromCurrentBlocks(id);
                if (s2 != null) {
                    s2.removeBlockFromCurrentBlocks(id);
                }
            }
        } else {
            Section s = null;
            if (sectionID.toString() == "H" && id >= 24 && id <= 27) {
                s = sectionList.get("H24_27");
            } else if (sectionID.toString() == "H" && id >= 28 && id <= 32) {
                s = sectionList.get("H28_32");
            } else if (sectionID.toString() == "H" && id >= 33 && id <= 38) {
                s = sectionList.get("H33_38");
            } else if (sectionID.toString() == "H" && id >= 39 && id <= 43) {
                s = sectionList.get("H39_43");
            } else if (sectionID.toString() == "H" && id >= 44 && id <= 45) {
                s = sectionList.get("H44_45");
            } else if (sectionID.toString() == "J" && id >= 49 && id <= 52) {
                s = sectionList.get("J49_52");
            } else if (sectionID.toString() == "J" && id >= 53 && id <= 54) {
                s = sectionList.get("J53_54");
            } else {
                s = sectionList.get(sectionID.toString());
            }

            s.setIsOccupied(occupied);
            if (occupied) {
                s.addBlockToCurrentBlocks(id);
                if (authority == -1) {
                    s.setIsHalted(true);
                } else {
                    s.setIsHalted(false);
                }
                if (remainingAuthority <= 0 || speed <= 0) {
                    s.setIsStopped(true);
                } else {
                    s.setIsStopped(false);
                }
            } else {
                s.removeBlockFromCurrentBlocks(id);
            }
        }
    }

    /**
     * update crossing visually
     * @param crossing 
     */
    private void updateCrossing(com.rogueone.trackcon.entities.Crossing crossing) {
        if (crossing.getCurrentCrossingState() == Global.CrossingState.ACTIVE) {
            this.crossing.setIsActive(true);
            this.crossing.setToggleLights(this.trainSystem.getClock().getSecond() % 2);
        } else {
            this.crossing.setIsActive(false);
        }
    }

    /**
     * Update the track view for displaying the block status
     *
     * @param line - current line
     * @param section - current section
     * @param blockID - block to close or open
     * @param openOrClose - true for open block, false for close block
     */
    public void setBlockStatus(Global.Line line, String section, int blockID, boolean openOrClose) {
        //open block
        for (Section s : sectionList.values()) {
            if (s.getSectionID().equals(section)) {
                if (openOrClose) {
                    s.removeBlockFromClosedBlocks(blockID);
                } else {
                    s.addBlockToClosedBlocks(blockID);
                }
            }
        }
        this.trainSystem.getTrackControllerHandler().updateTrack(line);
    }

    /**
     * update view with track failures visually
     * @param trackFailures 
     */
    private void updateTrackFailures(LinkedList<String> trackFailures) {
        if (!trackFailures.isEmpty()) {
            for (Section s : sectionList.values()) {
                s.clearFailedBlocks();
            }
            Iterator failureIter = trackFailures.iterator();
            while (failureIter.hasNext()) {
                String[] failString = ((String) failureIter.next()).split(",");
                String section = failString[0];
                int blockID = Integer.parseInt(failString[1]);
                if (section.equals("J") && blockID == 62) {
                    section = "K";
                }
                for (Section s : sectionList.values()) {
                    if (s.getSectionID().equals(section)) {
                        s.addBlockToFailedBlocks(blockID);
                    }
                }
            }
        } else {
            for (Section s : sectionList.values()) {
                s.clearFailedBlocks();
            }
        }
    }

    /**
     * shape panel that track view uses to draw objects and paint
     */
    class ShapePanel extends JPanel {

        // These instance variables are used to store the desired size
        // of the panel.  See method getPreferredSize() below.
        private int prefwid, prefht;

        // Store index of the selected MyShape.  This allows the Shape
        // to be moved and updated.
        private int selindex;

        // Keep track of positions where mouse is moved on the display.
        // This is used by mouse event handlers when moving the shapes.
        private int x1, y1, x2, y2;

        private boolean popped; // has popup menu been activated?

        public ShapePanel(int pwid, int pht, TrainSystem trainSystem, Global.Line line) {
            selindex = -1;

            prefwid = pwid;	// values used by getPreferredSize method below
            prefht = pht;   // (which is called implicitly).  This enables
            // the JPanel to request the room that it needs.
            // However, the JFrame is not required to honor
            // that request.

            setOpaque(true);// Paint all pixels here (See API)

            setBackground(Color.lightGray);

            addMouseListener(new MyMouseListener(trainSystem, line));
//        addMouseMotionListener(new MyMover());
            popped = false;

        }  // end of constructor

        void addShape(MyShape s) {
            shapeList.add(s);
            repaint();	// repaint so we can see new shape
        }

        public void paintComponent(Graphics g) {
            ShapePanel.super.paintComponent(g);         // don't forget this line!
            Graphics2D g2d = (Graphics2D) g;
            for (int i = 0; i < shapeList.size(); i++) {
                shapeList.get(i).draw(g2d);
            }
        }

        // This class is extending MouseAdapter.  MouseAdapter is a predefined
        // class that implements MouseListener in a trivial way (i.e. none of
        // the methods actually do anything).  Extending MouseAdapter allows
        // a programmer to implement only the MouseListener methods that
        // he/she needs but still satisfy the interface (recall that to
        // implement an interface one must implement ALL of the methods in the
        // interface -- in this case I do not need 3 of the 5 MouseListener
        // methods)
        // Note that there is a lot of logic in this class to test for the
        // different state conditions of the program.  The idea is that clicking
        // on and releasing the mouse will do different things at different 
        // times in the program execution.  As an alternative, you could in fact
        // have MouseListeners for different circumstances (ex: for being
        // in DRAW mode vs. being in NONE mode).  In this case, you could
        // actually swap the listeners in and out as appropriate using the 
        // removeMouseListener method in addition to the addMouseListener method
        // for the JPanel.
        public class MyMouseListener extends MouseAdapter {

            private TrainSystem trainSystem;
            private Global.Line line;

            private MyMouseListener(TrainSystem trainSystem, Global.Line line) {
                this.trainSystem = trainSystem;
                this.line = line;
            }

            /**
             * Main method that determines what type of action should be taken when the track view panel is clicked
             * @param e 
             */
            public void mousePressed(MouseEvent e) {
                x1 = e.getX();  // store where mouse is when clicked
                y1 = e.getY();
                //check if one of the switches was clicked
                boolean switchClicked = false;
                for (Switch s : switchList.values()) {
                    if (s.contains(x1, y1)) {
                        boolean result = this.trainSystem.getTrackControllerHandler().toggleSwitch(line, s.getSwitchID());
                        if (result) {
                            if (s.isIsDefault() == true) {
                                s.setIsDefault(false);
                            } else {
                                s.setIsDefault(true);
                            }
                            this.trainSystem.getTrackControllerHandler().updateTrack(line);
                            sp.repaint();
                            switchClicked = true;
                        }
                    }
                }
                if (yardStart.contains(x1, y1)) {
                    //check if you can dispatch the train
                    if (this.trainSystem.getTrackControllerHandler().requestDispatch(line)) {
                        JFrame f = new JFrame();
                        DispatchPanel dp = new DispatchPanel(this.trainSystem, line);
                        f.add(dp);
                        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        f.pack();
                        f.setVisible(true);
                    }
                }
                if (!switchClicked) {
                    for (Section s : sectionList.values()) {
                        boolean containsSection = s.contains(x1, y1);
                        if (containsSection && s.isIsOccupied()) {
//                            System.out.println("Section : " + s.getSectionID() + " selected");
                            int blockID = s.getBlockIDUpdate(x1, y1);
                            if (blockID != -1) {
//                                System.out.println("Block : " + blockID + " selected");
                                JFrame f = new JFrame();
                                UpdatePanel up = new UpdatePanel(this.trainSystem, line, blockID);
                                f.add(up);
                                f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                                f.pack();
                                f.setVisible(true);
                            }
                        } else if (containsSection && !s.isIsOccupied()) {
//                            System.out.println("Section : " + s.getSectionID() + " selected");
                            int blockID = s.getBlockID(x1, y1);
                            if (blockID != -1) {
//                                System.out.println("Block : " + blockID + " selected");
                                //check to see if block is opened or closed
                                if (!s.isBlockFailed(blockID)) {
                                    if (!s.isBlockClosed(blockID)) {
                                        //check to see if you can close that block
                                        int response = JOptionPane.showOptionDialog(null,
                                                "Would you like to close block " + blockID + " in section " + s.getSectionID() + "?",
                                                "Close Block " + blockID + "",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE, null, null, null
                                        );
                                        if (response == JOptionPane.YES_OPTION) {
                                            if (this.trainSystem.getTrackControllerHandler().requestMaintenance(line, blockID)) {
                                                //if true result from call to close track, then update block to close
                                                s.addBlockToClosedBlocks(blockID);
                                            } else {
                                                JOptionPane.showMessageDialog(null,
                                                        "Block " + blockID + " in section " + s.getSectionID() + " was not closed",
                                                        "Block " + blockID + " not closed",
                                                        JOptionPane.WARNING_MESSAGE);
                                            }
                                        }
                                        this.trainSystem.getTrackControllerHandler().updateTrack(this.line);
                                        break;
                                    } else {
                                        //check to see if you can open that block
                                        int response = JOptionPane.showOptionDialog(null,
                                                "Would you like to open block " + blockID + " in section " + s.getSectionID() + "?",
                                                "Open Block " + blockID + "",
                                                JOptionPane.YES_NO_OPTION,
                                                JOptionPane.QUESTION_MESSAGE, null, null, null
                                        );
                                        if (response == JOptionPane.YES_OPTION) {
                                            if (this.trainSystem.getTrackControllerHandler().requestOpen(line, blockID)) {
                                                //if true result from open track call, the update the block to open
                                                s.removeBlockFromClosedBlocks(blockID);
                                            } else {
                                                JOptionPane.showMessageDialog(null,
                                                        "Block " + blockID + " in section " + s.getSectionID() + " was not opened",
                                                        "Block " + blockID + " not opened",
                                                        JOptionPane.WARNING_MESSAGE);
                                            }
                                        }
                                        this.trainSystem.getTrackControllerHandler().updateTrack(this.line);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            public void mouseReleased(MouseEvent e) {
//            if (mode == Mode.DRAW) // in DRAW mode, create the new Shape
//            {					   // and add it to the list of Shapes.  In this
//                // case we need to distinguish between the
//                // shapes since we are calling constructors
//                if (currShape == Figures.TREE) {
//                    newShape = new Tree(x1, y1, 50);
//                } else if (currShape == Figures.SNOWFLAKE) {
//                    newShape = new Snowflake(x1, y1, 10);
//                } else if (currShape == Figures.GREETING) {
//                    newShape = new Greeting(x1, y1, 30);
//                } else if (currShape == Figures.CABIN) {
//                    newShape = new Cabin(x1, y1, 60);
//                } else if (currShape == Figures.CLOUD) {
//                    newShape = new Cloud(x1, y1, 25);
//                } else if (currShape == Figures.CLAUS) {
//                    newShape = new Claus(x1, y1, 30);
//                }
//                addShape(newShape);
//                edit = Editted.YES;
//            } // In MOVING mode, set mode back to NONE and unselect shape (since 
//            // the move is finished when we release the mouse).
//            else if (mode == Mode.MOVING) {
//                mode = Mode.NONE;
//                unSelect();
//                makeShape.setEnabled(true);
//                repaint();
//                cutOption.setEnabled(false);
//                copyOption.setEnabled(false);
//            } else if (e.isPopupTrigger() && selindex >= 0) // if button is
//            {							// the popup menu trigger, show the
//                popper.show(ShapePanel.this, x1, y1); // popup menu
//            }
//            popped = false;  // unset popped since mouse is being released
//        }
            }

//logic to rotate through the shapes that the user clicks on
            public int getSelected(double x, double y) {
//        int slSize = shapeList.size();
//        int totalSelectedShapes = 0;
//        ArrayList<Integer> selectShape = new ArrayList<Integer>();
//        for (int i = 0; i < slSize; i++) {
//            if (shapeList.get(i).contains(x, y)) {
//                Integer addInt = new Integer(i);
//                selectShape.add(addInt);
//                totalSelectedShapes = selectShape.size();
//            }
//        }
//        if (selectShape.size() == 1) {
//            shapeList.get((selectShape.get(0)).intValue()).highlight(true);
//            return (selectShape.get(0)).intValue();
//        }
//        if (selectShape.size() > 1) {
//            while (startIter < totalSelectedShapes) {
//                if (startIter > 0) {
//                    shapeList.get((selectShape.get(startIter - 1)).intValue()).highlight(false);
//                } else if (highlightCount == 1) {
//                    shapeList.get((selectShape.get(totalSelectedShapes - 1)).intValue()).highlight(false);
//                    highlightCount = 0;
//                }
//                shapeList.get((selectShape.get(startIter)).intValue()).highlight(true);
//                startIter++;
//                if (startIter >= totalSelectedShapes) {
//                    startIter = 0;
//                    highlightCount++;
//                    return (selectShape.get(totalSelectedShapes - 1)).intValue();
//                } else {
//                    return (selectShape.get(startIter - 1)).intValue();
//                }
//            }
//        }
//        startIter = 0;
                return -1;
            }

            public void unSelect() {
//        if (selindex >= 0) {
//            shapeList.get(selindex).highlight(false);
//            selindex = -1;
//        }
            }

//    public boolean deleteSelected() {
//        if (selindex >= 0) {
//            shapeList.remove(selindex);
//            selindex = -1;
//            edit = Editted.YES;
//            return true;
//        } else {
//            return false;
//        }
//    }
            public void setMode(WebParam.Mode newMode) // set Mode
            {
//        mode = newMode;
            }

            public void addShape(MyShape newshape) // Add shape
            {
                shapeList.add(newshape);
                repaint();	// repaint so we can see new shape
            }

            // Method called implicitly by the JFrame to determine how much
            // space this JPanel wants.  Be sure to include this method in
            // your program so that your panel will be sized correctly.
            public Dimension getPreferredSize() {
                return new Dimension(prefwid, prefht);
            }

            // This method enables the shapes to be seen.  Note the parameter,
            // which is implicitly passed.  To draw the shapes, we in turn
            // call the draw() method for each shape.  The real work is in the draw()
            // method for each MyShape
            public void paintComponent(Graphics g) {
                ShapePanel.super.paintComponent(g);         // don't forget this line!
                Graphics2D g2d = (Graphics2D) g;
                for (int i = 0; i < shapeList.size(); i++) {
                    shapeList.get(i).draw(g2d);
                }
            }
        }
    } // end of ShapePanel

}
