/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.global;

/**
 *
 * @author Dan
 */
public class UnitConversion {
    
    public static final double METERS_PER_FOOT = 0.3048;
    public static final double KILOMETERS_PER_MILE = 1.60934;
    public static final double FEET_PER_METER = 3.28084;
    public static final double MILES_PER_KILOMETER = 0.621371;
    
    public static double metersToFeet(double meters) {
        return meters * FEET_PER_METER;
    }
    
    public static double metersToYards(double meters) {
        return (meters * FEET_PER_METER) / 3;
    }
    
    public static double kilometersPerHourToMilesPerHour(double kilometers) {
        return kilometers * MILES_PER_KILOMETER;
    }
    
}
