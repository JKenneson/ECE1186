/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import java.util.Random;

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
    private final Random random;
    private static final int MAX_TEMPURATURE = 100;
    private static final int MIN_TEMPURATURE = 0;
    private static final int MAX_TEMPURATURE_CHANGE = 5;
    private static final int MAX_PASSENGER_CHANGE = 3;
    private static final int HEATER_THRESHOLD = 32;
    
    //Contructor
    public Station(int newStationID, String newStationName, Line newLine, Block newBlockA, Section newSectionA, 
            Block newBlockB, Section newSectionB) {
        stationID = newStationID;
        name = newStationName;
        line = newLine;
        blockA = newBlockA;
        sectionA = newSectionA;
        blockB = newBlockB;
        sectionB = newSectionB;
        waitingPassengers = 0;
        temperature = 70;
        heaterOn = false;
        random = new Random();
    }
    public int updateTemperature() {
        //Adjust temperature
        int difference = MAX_TEMPURATURE_CHANGE - random.nextInt(2 * MAX_TEMPURATURE_CHANGE + 1);
        temperature += difference;
        //Adjust heater
        if(temperature < HEATER_THRESHOLD) {
            heaterOn = true;
        }
        else {
            heaterOn = false;
        }
        //Enforce boundaries
        if(temperature < MIN_TEMPURATURE) {
            temperature = MIN_TEMPURATURE;
        }
        if(temperature > MAX_TEMPURATURE) {
            temperature = MAX_TEMPURATURE;
        }
        return temperature;
    }
    public int updatePassengers() {
        int difference = (MAX_PASSENGER_CHANGE - 1) - random.nextInt(MAX_PASSENGER_CHANGE + 1);
        waitingPassengers += difference;
        return waitingPassengers;
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
    public boolean isHeaterOn() {
        return heaterOn;
    }
    public int getTemperature() {
        return temperature;
    }
    public void queuePassengers(int newPassengers) {
        waitingPassengers =+ newPassengers;
    }
    /**
     * Request passengers from the station to board the train
     * @param boardingPassengers the number of passengers requested
     * @return the deficit of requested passengers, otherwise 0
     */
    public int boardPassengers(int boardingPassengers) {
        int surplusPassengers = waitingPassengers - boardingPassengers;
        //Enough waiting passengers
        if (surplusPassengers >= 0) {
            waitingPassengers = surplusPassengers;
            return 0;
        }
        //Not enough waiting passengers
        else {
            waitingPassengers = 0;
            return -surplusPassengers;
        }
    }
    public int getWaitingPassengers() {
        return waitingPassengers;
    }
    public boolean equals(Station otherStation) {
        return this.stationID == otherStation.getID();
    }
    @Override
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