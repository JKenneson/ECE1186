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
    
    private final int stationID;
    private final String name;
    private final Line line;
    private final Block blockA;
    private final Section sectionA;
    private final Block blockB;
    private final Section sectionB;
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
    
    /**
     * Update temperature at station randomly
     * @return the new temperature
     */
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
    
    /**
     * Update the number of passengers at station randomly
     * @return the new number of passengers
     */
    public int updatePassengers() {
        int difference = (MAX_PASSENGER_CHANGE - 1) - random.nextInt(MAX_PASSENGER_CHANGE + 1);
        waitingPassengers += difference;
        if (waitingPassengers < 0) {
            waitingPassengers = 0;
        }
        return waitingPassengers;
    }
    
    //Getters and Setters
    
    /**
     * Get ID of the Station
     * @return the Station ID
     */
    public int getID() {
        return stationID;
    }
    
    /**
     * Get name of the Station
     * @return the Station name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get line of the Station
     * @return the Station line
     */
    public Line getLine() {
        return line;
    }
    
    /**
     * Get primary block of the Station
     * @return the primary Station block
     */
    public Block getBlockA() {
        return blockA;
    }
    
    /**
     * Get primary section of the station
     * @return the primary station section
     */
    public Section getSectionA() {
        return sectionA;
    }
    
    /**
     * Get secondary block of the Station
     * @return the secondary Station block
     */
    public Block getBlockB() {
        return blockB;
    }
    
    /**
     * Get secondary section of the station
     * @return the secondary station section
     */
    public Section getSectionB() {
        return sectionB;
    }
    
    /**
     * Get state of the heater
     * @return boolean true if on, false if off
     */
    public boolean isHeaterOn() {
        return heaterOn;
    }
    
    /**
     * Get temperature at the station
     * @return int temperature of the station
     */
    public int getTemperature() {
        return temperature;
    }
    
    /**
     * Add passengers to waiting queue
     * @param int newPassengers number of passengers to add
     */
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
    
    /**
     * Get number of passengers waiting at the station
     * @return the int specifying number of passengers waiting at the station
     */
    public int getWaitingPassengers() {
        return waitingPassengers;
    }
    
    public boolean equals(Station otherStation) {
        return this.stationID == otherStation.getID();
    }
    
    /**
     * Get string of the station name and ID
     * @return string containing station name and ID
     */
    @Override
    public String toString() {
        return name + " (" + stationID + ")";
    }
    
    /**
     * Get string of the detailed station info
     * @return formatted string containing all station info
     */
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
        return sb.toString();
    }
    
}