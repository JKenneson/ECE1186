//Comment out the following package statement to compile separately.
package com.rogueone.trackview;

/**
 * Example01 illustrates some basics of Java 2D. This version is compliant with
 * Java 1.2 Beta 3, Jun 1998. Please refer to: <BR>
 * http://www.javaworld.com/javaworld/jw-07-1998/jw-07-media.html
 * <P>
 * @author Bill Day <bill.day@javaworld.com>
 * @version 1.0
 * @see java.awt.Graphics2D
 *
 */
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
import java.awt.geom.Rectangle2D;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.jws.WebParam;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class TrackView extends Frame {

    /**
     * Instantiates an Example01 object.
     *
     */
    public static void main(String args[]) {
//        new TrackView(Global.Line.GREEN);
    }
    ShapePanel sp;
    float shiftAmount = 30;
    ArrayList<MyShape> shapeList;
    HashMap<Integer, Switch> switchList;
    HashMap<String, TrackLight> trackLightList;
    HashMap<String, Section> sectionList;
    Crossing crossing;
    double scale = 5;

    private TrainSystem trainSystem;

    /**
     * Our Example01 constructor sets the frame's size, adds the visual
     * components, and then makes them visible to the user. It uses an adapter
     * class to deal with the user closing the frame.
     *
     * @param trainSystem
     * @param line
     */
    public TrackView(TrainSystem trainSystem, Global.Line line) {
        //Title our frame.
        this.trainSystem = trainSystem;
        if (line == Global.Line.GREEN) {
            shapeList = new ArrayList<MyShape>(); // create empty ArrayList
            switchList = new HashMap<Integer, Switch>();
            trackLightList = new HashMap<String, TrackLight>();
            sectionList = new HashMap<String, Section>();
            sp = new ShapePanel(1000, 300);
            JFrame theWindow = new JFrame("Track View - " + line);
            theWindow.setSize(1000, 300);
            Container c = theWindow.getContentPane();
            sp.setBackground(Color.BLACK);
            c.add(sp);
            initializeGreenLine();
            sp.repaint();
            theWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            theWindow.setResizable(false);
            theWindow.setVisible(true);
        }
        else if (line == Global.Line.RED) {
            shapeList = new ArrayList<MyShape>(); // create empty ArrayList
            switchList = new HashMap<Integer, Switch>();
            trackLightList = new HashMap<String, TrackLight>();
            sectionList = new HashMap<String, Section>();
            sp = new ShapePanel(1000, 300);
            JFrame theWindow = new JFrame("Track View - " + line);
            theWindow.setSize(1000, 300);
            Container c = theWindow.getContentPane();
            sp.setBackground(Color.BLACK);
            c.add(sp);
            initializeRedLine();
            sp.repaint();
            theWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            theWindow.setResizable(false);
            theWindow.setVisible(true);
        }
    }

    public void updateTrackView(LinkedList<PresenceBlock> occupiedBlocks, LinkedList<UserSwitchState> switchStates, HashMap<Integer, com.rogueone.trackcon.entities.Switch> switchArray, com.rogueone.trackcon.entities.Crossing crossing) {

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
                    if (switchEntry.getValue() == Global.SwitchState.DEFAULT) {
                        updateSwitch(switchID, true);
                        currentLights = switchInfo.getSwitchState().getLightsDefault();
                    } else {
                        updateSwitch(switchID, false);
                        currentLights = switchInfo.getSwitchState().getLightsAlternate();
                    }
                    Iterator lightsIterator = currentLights.iterator();
                    while (lightsIterator.hasNext()) {
                        Light lightIteration = (Light) lightsIterator.next();
                        updateTrackLight(lightIteration.getSection(), lightIteration.getBlockID(), lightIteration.getLightState());
                    }
                }
            }
        }
        if (occupiedBlocks != null) {
            Iterator blockIterator = occupiedBlocks.iterator();
            while (blockIterator.hasNext()) {
                PresenceBlock pb = (PresenceBlock) blockIterator.next();
                double remainingAuthority = 1;
                for (TrainModel tm : this.trainSystem.getTrainHandler().getTrains()) {
                    Block trainBlock = tm.getCurrBlock();
                    if (trainBlock.getID() == pb.getCurrBlock().getID()) {
                        //System.out.println("ID's Match authority = " + tm.getAuthority());
                        remainingAuthority = tm.getAuthority();
                    }
                }
                if (pb.getPrevBlock().getType() == Global.PieceType.YARD) {
                    updateSection(pb.getCurrBlock().getSection().getSectionID(), pb.getCurrBlock().getID(), true, pb.getCurrBlock().getTrackCircuit().authority, remainingAuthority);

                } else {
                    Block prevBlock = (Block) pb.getPrevBlock();
                    updateSection(prevBlock.getSection().getSectionID(), prevBlock.getID(), false, (short) 0, 1);
                    updateSection(pb.getCurrBlock().getSection().getSectionID(), pb.getCurrBlock().getID(), true, pb.getCurrBlock().getTrackCircuit().authority, remainingAuthority);
                }

            }
        }
        if (crossing != null) {
            updateCrossing(crossing);
        }
        sp.repaint();
        
    }

    private void initializeGreenLine() {
        Section YY = new Section(0 + shiftAmount, 30, 50, 5, 45, "YY", -15, 16, this.trainSystem);
        sp.addShape(YY);
        sectionList.put("YY", YY);
        Section J1 = new Section(0 + shiftAmount, 80, 50, 5, -45, "J", 4, 13, this.trainSystem);
        sp.addShape(J1);
        sectionList.put("J1", J1);
        Section K = new Section(50 + shiftAmount, 55, 50, 5, 0, "K", -5, 15, this.trainSystem);
        sp.addShape(K);
        sectionList.put("K", K);
        Switch switch3 = new Switch(35 + shiftAmount + 3, 51, 35 + shiftAmount + 3, 59, 15, 5, 45, -45, 3);
        sp.addShape(switch3);
        switch3.setIsDefault(true);
        Section L = new Section(105 + shiftAmount, 55, 50, 5, 0, "L", -5, 15, this.trainSystem);
        sp.addShape(L);
        sectionList.put("L", L);
        Section M = new Section(160 + shiftAmount, 55, 50, 5, 0, "M", -5, 15, this.trainSystem);
        sp.addShape(M);
        sectionList.put("M", M);
        //first vertical section
        Section N = new Section(190 + shiftAmount, 85, 50, 5, 90, "N", -13, 0, this.trainSystem);  //192
        sp.addShape(N);
        sectionList.put("N", N);
        //first loop
        Section O = new Section(175 + shiftAmount, 135 + 2, 50, 5, -60, "O", -15, 4, this.trainSystem);
        sp.addShape(O);
        sectionList.put("O", O);
        Section P = new Section(190 + shiftAmount + 2, 160 + 2, 50, 5, 0, "P", -5, -5, this.trainSystem);
        sp.addShape(P);
        sectionList.put("P", P);
        Section Q = new Section(210 + shiftAmount - 2, 135 + 2, 50, 5, 60, "Q", 5, 5, this.trainSystem);
        sp.addShape(Q);
        sectionList.put("Q", Q);

        Section R = new Section(220 + shiftAmount, 55, 50, 5, 0, "R", -3, 15, this.trainSystem);
        sp.addShape(R);
        sectionList.put("R", R);

        Switch switch4 = new Switch(190 + shiftAmount + 15, 57, 190 + shiftAmount + 23, 57, 12, 5, 45, -45, 4);
        sp.addShape(switch4);
        switch4.setIsDefault(true);
        Switch switch5 = new Switch(190 + shiftAmount + 17, 115, 190 + shiftAmount + 23, 115, 12, 5, -55, 55, 5);
        sp.addShape(switch5);
        switch5.setIsDefault(true);

        Section S = new Section(270 + shiftAmount + 5, 55, 50, 5, 0, "S", -5, 15, this.trainSystem);
        sp.addShape(S);
        sectionList.put("S", S);
        Section T = new Section(325 + shiftAmount + 5, 55, 50, 5, 0, "T", -5, 15, this.trainSystem);
        sp.addShape(T);
        sectionList.put("T", T);
        Section U = new Section(380 + shiftAmount + 5, 55, 50, 5, 0, "U", -5, 15, this.trainSystem);
        sp.addShape(U);
        sectionList.put("U", U);
        Section V = new Section(435 + shiftAmount + 5, 55, 50, 5, 0, "V", -5, 15, this.trainSystem);
        sp.addShape(V);
        sectionList.put("V", V);
        Section W = new Section(490 + shiftAmount + 5, 55, 50, 5, 0, "W", -5, 15, this.trainSystem);
        sp.addShape(W);
        sectionList.put("W", W);
        Section X = new Section(545 + shiftAmount + 5, 55, 50, 5, 0, "X", -5, 15, this.trainSystem);
        sp.addShape(X);
        sectionList.put("X", X);
        Section Y = new Section(600 + shiftAmount + 5, 55, 50, 5, 0, "Y", -5, 15, this.trainSystem);
        sp.addShape(Y);
        sectionList.put("Y", Y);
        Section Z = new Section(655 + shiftAmount + 5, 55, 50, 5, 0, "Z", -5, 15, this.trainSystem);
        sp.addShape(Z);
        sectionList.put("Z", Z);
        //second vertical section
        Section F = new Section(685 + shiftAmount + 5, 85, 50, 5, 90, "F", -13, 0, this.trainSystem);
        sp.addShape(F);
        sectionList.put("F", F);
        Section E = new Section(685 + shiftAmount + 5, 140, 50, 5, 90, "E", -13, 0, this.trainSystem);
        sp.addShape(E);
        sectionList.put("E", E);
        Section D = new Section(685 + shiftAmount + 5, 195, 50, 5, 90, "D", -13, 0, this.trainSystem);
        sp.addShape(D);
        sectionList.put("D", D);
        //second loop
        Section C = new Section(665 + shiftAmount + 9, 245, 50, 5, -60, "C", -15, 4, this.trainSystem);
        sp.addShape(C);
        sectionList.put("C", C);
        Section B = new Section(685 + shiftAmount + 5, 270, 50, 5, 0, "B", -5, -5, this.trainSystem);
        sp.addShape(B);
        sectionList.put("B", B);
        Section A = new Section(705 + shiftAmount + 2, 245, 50, 5, 60, "A", 7, 5, this.trainSystem);
        sp.addShape(A);
        sectionList.put("A", A);

        Section G = new Section(710 + shiftAmount + 10, 55, 50, 5, 0, "G", -3, 15, this.trainSystem);
        sp.addShape(G);
        sectionList.put("G", G);
        Switch switch1 = new Switch(705 + shiftAmount, 225, 705 + shiftAmount + 8, 225, 14, 5, -55, 55, 1);
        sp.addShape(switch1);
        switch1.setIsDefault(true);
        Switch switch2 = new Switch(705 + shiftAmount, 57, 705 + shiftAmount + 8, 57, 12, 5, 45, -45, 2);
        sp.addShape(switch2);
        switch2.setIsDefault(true);

        Section H = new Section(765 + shiftAmount + 10, 55, 50, 5, 0, "H", -5, 15, this.trainSystem);
        sp.addShape(H);
        sectionList.put("H", H);
        Section I = new Section(820 + shiftAmount + 10, 55, 50, 5, 0, "I", -5, 15, this.trainSystem);
        sp.addShape(I);
        sectionList.put("I", I);

        Section ZZ = new Section(870 + shiftAmount + 10, 30, 50, 5, -45, "ZZ", 3, 15, this.trainSystem);
        sp.addShape(ZZ);
        sectionList.put("ZZ", ZZ);
        Section J2 = new Section(870 + shiftAmount + 10, 80, 50, 5, 45, "J", -4, 13, this.trainSystem);
        sp.addShape(J2);
        sectionList.put("J2", J2);
        Switch switch0 = new Switch(870 + shiftAmount + 6, 51, 870 + shiftAmount + 6, 59, 15, 5, -45, 45, 0);
        sp.addShape(switch0);
        switch0.setIsDefault(true);

        switchList.put(0, switch0);
        switchList.put(1, switch1);
        switchList.put(2, switch2);
        switchList.put(3, switch3);
        switchList.put(4, switch4);
        switchList.put(5, switch5);

        TrackLight lightYY = new TrackLight(85, 35, 225, false);
        sp.addShape(lightYY);
        trackLightList.put("YY", lightYY);
        TrackLight lightJ1 = new TrackLight(50, 50, 135, false);
        sp.addShape(lightJ1);
        trackLightList.put("J1", lightJ1);
        TrackLight lightJ2 = new TrackLight(870 + shiftAmount + 60, 80, 225, false);
        sp.addShape(lightJ2);
        trackLightList.put("J2", lightJ2);
        TrackLight lightM = new TrackLight(160 + shiftAmount + 40, 32, 180, false);
        sp.addShape(lightM);
        trackLightList.put("M", lightM);
        TrackLight lightN77 = new TrackLight(160 + shiftAmount + 70, 60, 90, true);
        sp.addShape(lightN77);
        trackLightList.put("N77", lightN77);
        TrackLight lightN85 = new TrackLight(160 + shiftAmount + 30, 105, -90, true);
        sp.addShape(lightN85);
        trackLightList.put("N85", lightN85);
        TrackLight lightQ = new TrackLight(160 + shiftAmount + 73, 105, 55, true);
        sp.addShape(lightQ);
        trackLightList.put("Q", lightQ);
        TrackLight lightZ = new TrackLight(655 + shiftAmount + 45, 32, 180, false);
        sp.addShape(lightZ);
        trackLightList.put("Z", lightZ);
        TrackLight lightF = new TrackLight(655 + shiftAmount + 75, 60, 90, true);
        sp.addShape(lightF);
        trackLightList.put("F", lightF);
        TrackLight lightD = new TrackLight(655 + shiftAmount + 35, 215, -90, true);
        sp.addShape(lightD);
        trackLightList.put("D", lightD);
        TrackLight lightA = new TrackLight(655 + shiftAmount + 78, 215, 55, true);
        sp.addShape(lightA);
        trackLightList.put("A", lightA);
        TrackLight lightI = new TrackLight(820 + shiftAmount + 50, 32, 180, false);
        sp.addShape(lightI);
        trackLightList.put("I", lightI);
        TrackLight lightZZ = new TrackLight(860 + shiftAmount + 38, 2, 135, false);
        sp.addShape(lightZZ);
        trackLightList.put("ZZ", lightZZ);

        Station stationGlenbury = new Station(108, 36, 8);
        sp.addShape(stationGlenbury);
        Station stationDormont = new Station(178, 36, 8);
        sp.addShape(stationDormont);
        Station stationMtLebanon = new Station(235, 63, 7);
        sp.addShape(stationMtLebanon);
        Station stationPoplar = new Station(210, 155, 8);
        sp.addShape(stationPoplar);
        Station stationOverbrook1 = new Station(530, 36, 8);
        sp.addShape(stationOverbrook1);
        Station stationInglewood1 = new Station(580, 36, 8);
        sp.addShape(stationInglewood1);
        Station stationCastleShannon = new Station(252, 170, 8);
        sp.addShape(stationCastleShannon);
        Station stationSouthBank = new Station(765, 36, 8);
        sp.addShape(stationSouthBank);
        Station stationWhited = new Station(762, 100, 8);
        sp.addShape(stationWhited);
        Station stationUniOfPitt = new Station(762, 175, 8);
        sp.addShape(stationUniOfPitt);
        Station stationEdgebrook = new Station(710, 245, 8);
        sp.addShape(stationEdgebrook);
        Station stationPioneer = new Station(792, 245, 8);
        sp.addShape(stationPioneer);
        Station stationOverbrook2 = new Station(865, 36, 8);
        sp.addShape(stationOverbrook2);
        Station stationInglewood2 = new Station(910, 62, 8);
        sp.addShape(stationInglewood2);

        crossing = new Crossing(750, 140);
        sp.addShape(crossing);

        Yard yardStart = new Yard(0, 0);
        sp.addShape(yardStart);
        Yard yardEnd = new Yard(950, 0);
        sp.addShape(yardEnd);

        LegendBox lb = new LegendBox(1, 200);
        sp.addShape(lb);

    }
    
    private void initializeRedLine() {
        
        // Branch to Yard
        Section U = new Section(0 + shiftAmount, 30, 50, 5, 45, "U", -15, 16, this.trainSystem);
        sp.addShape(U);
        //sectionList.put("U", U);
        
        // First loop
        Section A = new Section(120 + shiftAmount, 80, 50, 5, -45, "A", 5, 16, this.trainSystem);
        sp.addShape(A);
        //sectionList.put("A", A);
        Section B = new Section(90 + shiftAmount, 100, 30, 5, 0, "B", -5, 16, this.trainSystem);
        sp.addShape(B);
        //sectionList.put("B", B);
        Section C = new Section(40 + shiftAmount, 80, 50, 5, 45, "C", -15, 16, this.trainSystem);
        sp.addShape(C);
        //sectionList.put("C", C);
        Section D = new Section(50 + shiftAmount, 55, 50, 5, 0, "D", -5, 15, this.trainSystem);
        sp.addShape(D);
        //sectionList.put("D", D);
        Section E = new Section(105 + shiftAmount, 55, 50, 5, 0, "E", -5, 15, this.trainSystem);
        sp.addShape(E);
        //sectionList.put("E", E);
        
        // Main strech
        Section F = new Section(160 + shiftAmount, 55, 50, 5, 0, "F", -5, 15, this.trainSystem);
        sp.addShape(F);
        //sectionList.put("F", F);
        Section G = new Section(215 + shiftAmount, 55, 50, 5, 0, "G", -5, 15, this.trainSystem);
        sp.addShape(G);
        //sectionList.put("G", G);
        Section H = new Section(270 + shiftAmount, 55, 350, 5, 0, "H", -5, -15, this.trainSystem);
        sp.addShape(H);
        //sectionList.put("H", H);
        Section I = new Section(625 + shiftAmount, 55, 50, 5, 0, "I", -5, 15, this.trainSystem);
        sp.addShape(I);
        //sectionList.put("I", I);
        
        // Second loop
        Section J = new Section(680 + shiftAmount, 55, 50, 5, 0, "J", -5, 15, this.trainSystem);
        sp.addShape(J);
        //sectionList.put("J", J);
        Section K = new Section(735 + shiftAmount, 55, 50, 5, 0, "K", -5, 15, this.trainSystem);
        sp.addShape(K);
        //sectionList.put("K", K);
        Section L = new Section(785 + shiftAmount, 75, 50, 5, 45, "L", -5, 15, this.trainSystem);
        sp.addShape(L);
        //sectionList.put("L", L);
        Section M = new Section(755 + shiftAmount, 100, 50, 5, 0, "M", -5, 15, this.trainSystem);
        sp.addShape(M);
        //sectionList.put("M", M);
        Section N = new Section(705 + shiftAmount, 80, 50, 5, 45, "N", -5, 15, this.trainSystem);
        sp.addShape(N);
        //sectionList.put("N", N);
        
        // First wayside
        Section O = new Section(380 + shiftAmount, 80, 50, 5, -45, "O", 5, 16, this.trainSystem);
        sp.addShape(O);
        //sectionList.put("O", O);
        Section P = new Section(350 + shiftAmount, 100, 30, 5, 0, "P", -5, 16, this.trainSystem);
        sp.addShape(P);
        //sectionList.put("P", P);
        Section Q = new Section(300 + shiftAmount, 80, 50, 5, 45, "Q", -15, 16, this.trainSystem);
        sp.addShape(Q);
        //sectionList.put("Q", Q);
        
        // Second wayside
        Section R = new Section(580 + shiftAmount, 80, 50, 5, -45, "R", 5, 16, this.trainSystem);
        sp.addShape(R);
        //sectionList.put("R", R);   
        Section S = new Section(550 + shiftAmount, 100, 30, 5, 0, "S", -5, 16, this.trainSystem);
        sp.addShape(S);
        //sectionList.put("S", S);  
        Section T = new Section(500 + shiftAmount, 80, 50, 5, 45, "T", -15, 16, this.trainSystem);
        sp.addShape(T);
        //sectionList.put("T", T);  
        
        // Switches
        Switch switch1 = new Switch(25 + shiftAmount + 15, 50, 30 + shiftAmount + 23, 57, 12, 5, 45, 0, 12);
        sp.addShape(switch1);
        switch1.setIsDefault(true);
        Switch switch2 = new Switch(150 + shiftAmount + 15, 57, 150 + shiftAmount + 23, 57, 12, 5, -45, 0, 6);
        sp.addShape(switch2);
        switch2.setIsDefault(true);
        Switch switch3 = new Switch(285 + shiftAmount + 15, 57, 290 + shiftAmount + 23, 57, 12, 5, 45, 0, 7);
        sp.addShape(switch3);
        switch3.setIsDefault(true);
        Switch switch4 = new Switch(405 + shiftAmount + 15, 57, 405 + shiftAmount + 23, 57, 12, 5, -45, 0, 8);
        sp.addShape(switch4);
        switch4.setIsDefault(true);
        Switch switch5 = new Switch(485 + shiftAmount + 15, 57, 490 + shiftAmount + 23, 57, 12, 5, 45, 0, 9);
        sp.addShape(switch5);
        switch5.setIsDefault(true);
        Switch switch6 = new Switch(605 + shiftAmount + 15, 57, 605 + shiftAmount + 23, 57, 12, 5, -45, 0, 10);
        sp.addShape(switch6);
        switch6.setIsDefault(true);
        Switch switch7 = new Switch(690 + shiftAmount + 15, 57, 690 + shiftAmount + 23, 57, 12, 5, 45, 0, 11);
        sp.addShape(switch7);
        switch7.setIsDefault(true);
        
        //TrackLights
        TrackLight light1 = new TrackLight(25 + shiftAmount, 45, 0, false);
        sp.addShape(light1);
        //trackLightList.put("?", light1);
        TrackLight light2 = new TrackLight(25 + shiftAmount, 65, 0, true);
        sp.addShape(light2);
        //trackLightList.put("?", light2);
        TrackLight light3 = new TrackLight(150 + shiftAmount, 45, 0, false);
        sp.addShape(light3);
        //trackLightList.put("?", light3);
        TrackLight light4 = new TrackLight(150 + shiftAmount, 65, 0, true);
        sp.addShape(light4);
        //trackLightList.put("?", light4);
        TrackLight light5 = new TrackLight(285 + shiftAmount, 45, 0, false);
        sp.addShape(light5);
        //trackLightList.put("?", light5);
        TrackLight light6 = new TrackLight(285 + shiftAmount, 65, 0, true);
        sp.addShape(light6);
        //trackLightList.put("?", light6);
        TrackLight light7 = new TrackLight(405 + shiftAmount, 45, 0, false);
        sp.addShape(light7);
        //trackLightList.put("?", light7);
        TrackLight light8 = new TrackLight(405 + shiftAmount, 65, 0, true);
        sp.addShape(light8);
        //trackLightList.put("?", light8);
        TrackLight light9 = new TrackLight(485 + shiftAmount, 45, 0, false);
        sp.addShape(light9);
        //trackLightList.put("?", light9);
        TrackLight light10 = new TrackLight(485 + shiftAmount, 65, 0, true);
        sp.addShape(light10);
        //trackLightList.put("?", light10);
        TrackLight light11 = new TrackLight(605 + shiftAmount, 45, 0, false);
        sp.addShape(light11);
        //trackLightList.put("?", light11);
        TrackLight light12 = new TrackLight(605 + shiftAmount, 65, 0, true);
        sp.addShape(light12);
        //trackLightList.put("?", light12);
        TrackLight light13 = new TrackLight(690 + shiftAmount, 45, 0, false);
        sp.addShape(light13);
        //trackLightList.put("?", light13);
        TrackLight light14 = new TrackLight(690 + shiftAmount, 65, 0, true);
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

        LegendBox lb = new LegendBox(1, 200);
        sp.addShape(lb);

    }

    private void updateSwitch(Integer switchID, boolean defaultOrAlternate) {
        Switch s = switchList.get(switchID);
        if(s != null) {
           s.setIsDefault(defaultOrAlternate); 
        }
    }

    private void updateTrackLight(Global.Section section, int blockID, Global.LightState lightState) {
        if (section.toString() == "N") {
            TrackLight tl = trackLightList.get(section.toString() + String.valueOf(blockID));
            if (tl != null) {
                if (lightState == Global.LightState.GO) {
                    tl.setIsGo(true);
                    tl.setIsStop(false);
                } else {
                    tl.setIsGo(false);
                    tl.setIsStop(true);
                }
            }
        } else if (section.toString() == "J") {
            TrackLight tl1 = trackLightList.get(section.toString() + "1");
            TrackLight tl2 = trackLightList.get(section.toString() + "2");
            if(tl1 != null && tl2 != null) {
                if (lightState == Global.LightState.GO) {
                    tl1.setIsGo(true);
                    tl1.setIsStop(false);
                    tl2.setIsGo(true);
                    tl2.setIsStop(false);
                } else {
                    tl1.setIsGo(false);
                    tl1.setIsStop(true);
                    tl2.setIsGo(false);
                    tl2.setIsStop(true);
                }
            }          
        } else {
            TrackLight tl = trackLightList.get(section.toString());
            if (tl != null) {
                if (lightState == Global.LightState.GO) {
                    tl.setIsGo(true);
                    tl.setIsStop(false);
                } else {
                    tl.setIsGo(false);
                    tl.setIsStop(true);
                }
            }
        }

    }

    private void updateSection(Global.Section sectionID, int id, boolean occupied, short authority, double remainingAuthority) {
        Section s;
        if (sectionID.toString().equals("J") && id == 62) {
            //light up K
            s = sectionList.get("K");
            s.setIsOccupied(occupied);
        } else {
            s = sectionList.get(sectionID.toString());
            s.setIsOccupied(occupied);
        }
        if (occupied) {
            s.addBlockToCurrentBlocks(id);
            if (authority < 0) {
                s.setIsStopped(true);
            } else {
                s.setIsStopped(false);
            }
            if (remainingAuthority <= 0) {
                s.setHasAuthority(false);
            } else {
                s.setHasAuthority(true);
            }
        } else {
            s.removeBlockFromCurrentBlocks(id);
        }
    }

    private void updateCrossing(com.rogueone.trackcon.entities.Crossing crossing) {
        if (crossing.getCurrentCrossingState() == Global.CrossingState.ACTIVE) {
            this.crossing.setIsActive(true);
            this.crossing.setToggleLights(this.trainSystem.getClock().getSecond() % 2);
        } else {
            this.crossing.setIsActive(false);
        }
    }

    /**
     * The paint method provides the real magic. Here we cast the Graphics
     * object to Graphics2D to illustrate that we may use the same old graphics
     * capabilities with Graphics2D that we are used to using with Graphics.
     *
     */
//  public void paint(Graphics g) {
//    //Here is how we used to draw a square with width
//    //of 200, height of 200, and starting at x=50, y=50.
//    g.setColor(Color.red);
////    g.drawRect(50,50,200,200);
//    Cabin c = new Cabin(500, 150, 50);
////    Rectangle2D mainPost = new Rectangle2D.Double(0, 0, 5 * scale, 25 * scale);
//    Arc2D redLight = new Arc2D.Double((5 * scale)/(double)2, 0 , 5 * scale, 5 * scale, 90, -180, Arc2D.PIE);
//    Arc2D greenLight = new Arc2D.Double(5 * scale, 2* ((5 * scale) /(double)2), 5 * scale, 5 * scale,  90, -180, Arc2D.PIE);
//    
// 
//    //Let's set the Color to blue and then use the Graphics2D
//    //object to draw a rectangle, offset from the square.
//    //So far, we've not done anything using Graphics2D that
//    //we could not also do using Graphics.  (We are actually
//    //using Graphics2D methods inherited from Graphics.)
//    Graphics2D g2d = (Graphics2D)g;
//    g2d.draw((Shape) c);
////    g2d.setColor(Color.BLACK);
////    g2d.fill(mainPost);
//////    g2d.drawRect(75,75,300,200);
////    g2d.draw(mainPost);
////    g2d.setColor(Color.RED);
////    g2d.fill(redLight);
////    g2d.draw(redLight);
////    g2d.setColor(Color.GREEN);
////    g2d.fill(greenLight);
////    g2d.draw(greenLight);
//  }
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

        public ShapePanel(int pwid, int pht) {

            selindex = -1;

            prefwid = pwid;	// values used by getPreferredSize method below
            prefht = pht;   // (which is called implicitly).  This enables
            // the JPanel to request the room that it needs.
            // However, the JFrame is not required to honor
            // that request.

            setOpaque(true);// Paint all pixels here (See API)

            setBackground(Color.lightGray);

            addMouseListener(new MyMouseListener());
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

            public void mousePressed(MouseEvent e) {
                x1 = e.getX();  // store where mouse is when clicked
                y1 = e.getY();

//            if (!e.isPopupTrigger() && (mode == Mode.NONE
//                    || mode == Mode.SELECTED)) // left click and
//            {												    // either NONE or
//                if (selindex >= 0) // SELECTED mode
//                {
//                    unSelect();			// unselect previous shape
//                    mode = Mode.NONE;
//                    cutOption.setEnabled(false);	//addition of disabled cut/copy buttons
//                    copyOption.setEnabled(false);
//                }
//                selindex = getSelected(x1, y1);  // find shape mouse is
//                // clicked on
//                if (selindex >= 0) {
//                    mode = Mode.SELECTED;  	// Now in SELECTED mode for shape
//                    cutOption.setEnabled(true); 	//selected item enabled the copy/paste
//                    copyOption.setEnabled(true);
//                    // Check for double-click.  If so, show dialog to update text of
//                    // the current text shape (will do nothing if shape is not a MyText)
//                    MyShape curr = shapeList.get(selindex);
//                    if (curr instanceof MyText && e.getClickCount() == 2) {
//                        String newText = JOptionPane.showInputDialog(theWindow,
//                                "Enter new text [Cancel for no change]");
//                        if (newText != null) {
//                            ((MyText) curr).setText(newText);
//                        }
//                    }
//                }
//                repaint();	//  Make sure updates are redrawn
//            } else if (e.isPopupTrigger() && selindex >= 0) // if button is
//            {								               // the popup menu
//                popper.show(ShapePanel.this, x1, y1);      // trigger, show
//                popped = true;							   // popup menu
//                cutOption.setEnabled(false);
//                copyOption.setEnabled(false);
//            }
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
