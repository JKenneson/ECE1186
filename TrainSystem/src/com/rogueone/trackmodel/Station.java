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
    private String stationName;
    private Line line;
    private TrackPiece blockA;
    private Section sectionA;
    private TrackPiece blockB;
    private Section sectionB;
    private boolean rightSide;
    private boolean leftSide;
    private int waitingPassengers = 0;
    private int temperature = -1;
    private boolean heaterOn = false;
    
    //Contructor
    public Station(int newStationID, String newStationName, Line newLine, TrackPiece newBlockA, Section newSectionA, 
            TrackPiece newBlockB, Section newSectionB, boolean newRightSide, boolean newLeftSide) {
        stationID = newStationID;
        stationName = newStationName;
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
    public void setID(int stationID) {
        this.stationID = stationID;
    }
    public String getStationName() {
        return stationName;
    }
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    public Line getLine() {
        return line;
    }
    public void setLine(Line line) {
        this.line = line;
    }
    public TrackPiece getBlockA() {
        return blockA;
    }
    public void setBlockA(TrackPiece blockA) {
        this.blockA = blockA;
    }
    public Section getSectionA() {
        return sectionA;
    }
    public void setSectionA(Section newSectionA) {
        this.sectionA = newSectionA;
    }
    public TrackPiece getBlockB() {
        return blockB;
    }
    public void setBlockB(TrackPiece blockB) {
        this.blockB = blockB;
    }
    public Section getSectionB() {
        return sectionB;
    }
    public void setSectionB(Section newSectionB) {
        this.sectionB = newSectionB;
    }
    public boolean isRightSide() {
        return rightSide;
    }
    public void setRightSide(boolean rightSide) {
        this.rightSide = rightSide;
    }
    public boolean isLeftSide() {
        return leftSide;
    }
    public void setLeftSide(boolean leftSide) {
        this.leftSide = leftSide;
    }
    public void setHeater(boolean on)
    {
        heaterOn = on;  
    }
    public boolean isHeaterOn() {
        return heaterOn;
    }
    public void setTemperature(int newTemperature) {
        temperature = newTemperature;
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
        return stationName + " (" + stationID + ")";
    }
    public String toStringDetail() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ");
        sb.append(stationName);
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