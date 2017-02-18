/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.Global;

/**
 *
 * @author Dan
 */
public class Station {
    
    private int stationID;
    private String name;
    private Line line;
    private Block blockA;
    private Section sectionA;
    private Block blockB;
    private Section sectionB;
    private boolean rightSide;
    private boolean leftSide;
    private int waitingPassengers = 0;
    private int temperature = -1;
    private boolean heaterOn = false;
    
    //Contructor
    public Station(int newStationID, String newStationName, Line newLine, Block newBlockA, Section newSectionA, 
            Block newBlockB, Section newSectionB, boolean newRightSide, boolean newLeftSide) {
        stationID = newStationID;
        name = newStationName;
        line = newLine;
        blockA = newBlockA;
        sectionA = newSectionA;
        blockB = newBlockB;
        sectionB = newSectionB;
        rightSide = newRightSide;
        leftSide = newLeftSide;
        //Mock passenger and temperature data for now, until the random simulators are finished.
        waitingPassengers = 0;
        temperature = -1;
        heaterOn = false;
    }
    //Getters and Setters
    public int getID() {
        return stationID;
    }
    public String getName() {
        return name;
    }
    public Line getLine() {
        return line;
    }
    public Block getBlockA() {
        return blockA;
    }
    public Section getSectionA() {
        return sectionA;
    }
    public Block getBlockB() {
        return blockB;
    }
    public Section getSectionB() {
        return sectionB;
    }
    public boolean isRightSide() {
        return rightSide;
    }
    public boolean isLeftSide() {
        return leftSide;
    }
    public void setHeater(boolean on)
    {
        heaterOn = on;  
    }
    public boolean isHeaterOn() {
        return heaterOn;
    }
    public int getTemperature() {
        return temperature;
    }
    public void queuePassengers(int newPassengers) {
        waitingPassengers =+ newPassengers;
    }
    public int boardPassengers(int boardingPassengers) {
        waitingPassengers =- boardingPassengers;
        return waitingPassengers;
        //No support for negative passengers YET
    }
    public boolean equals(Station otherStation) {
        return this.line.equals(otherStation.getLine()) && this.stationID == otherStation.getID();
    }
    public String toString() {
        return name + " (" + stationID + ")";
    }
    public String toStringDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(name);
        sb.append(", ID: ");
        sb.append(stationID);
        sb.append(", line: ");
        sb.append(line);
        if(blockA != null) {
            sb.append(", Block A: ");
            sb.append(blockA.getID());
            sb.append(", Block A Section: ");
            sb.append(sectionA);
        }
        if(blockB != null) {
            sb.append(", Block B: ");
            sb.append(blockB.getID());
            sb.append(", Block B Section: ");
            sb.append(sectionB);
        }
        sb.append(", Right Side: ");
        sb.append(rightSide);
        sb.append(", Left Side: ");
        sb.append(leftSide);
        return sb.toString();
    }
    
}