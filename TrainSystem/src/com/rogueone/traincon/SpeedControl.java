/**
 * The speed controller decides the safe speed at which the train should travel
 * at any given time
 *
 * @author Tyler Protivnak
 * @Creation 3/4/17
 * @Modification 4/17/2017
 */
package com.rogueone.traincon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class declaration for SpeedControl
 *
 * @author Tyler Protivnak
 */
public class SpeedControl{
    
    NumberFormat commaFormatter = NumberFormat.getInstance(Locale.US);
    DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");
    
    public final double METERS_IN_A_MILE = 1609.34;     //1609.34 meters in a mile
    public final double FEET_IN_A_METER = 3.28;         //3.28 feet = 1 meter
    public final double SECONDS_IN_AN_HOUR = 3600;      //3600 seconds in an hour
    private final GPS gps;
    private byte driverSetPoint;
    private byte recommendedSetPoint;
    
    /**
     * Constructor for speed controller
     * 
     * @author Tyler Protivnak
     * @param driverSetPoint the driver set point given upon creation
     * @param recommendedSetPoint the ctc or mbo set point given upon creation
     * @param gps 
     */
    public SpeedControl(byte driverSetPoint, byte recommendedSetPoint, GPS gps){
        this.driverSetPoint = driverSetPoint;
        this.recommendedSetPoint = recommendedSetPoint;
        this.gps = gps;
    }

    /**
     * Should the service brake be activated given the current speed?
     * 
     * @author Tyler Protivnak
     * @param manualMode the current operation mode of the train controller
     * @return boolean as to weather or not to activate the service brake
     */
    public boolean update(boolean manualMode){
        double currSpeed = this.gps.getCurrSpeed();
        if((currSpeed>this.getSetPoint(manualMode))){
            return true;
        }
        else{
            return false;
        }
    }
       
    /**
     * Given the operational mode, what should the setpoint speed be for calculations?
     * 
     * @author Tyler Protivnak
     * @param manualMode the current operation mode of the train controller
     * @return the set point based on the mode (man or auto) of the train
     */
    public double getSetPoint(boolean manualMode){
        if(manualMode){
            if(this.driverSetPoint > this.gps.getSpeedLimit()){
                return this.gps.getSpeedLimit();
            }
            return this.driverSetPoint;
        }
        else{
            if(this.recommendedSetPoint > this.gps.getSpeedLimit()){
                return this.gps.getSpeedLimit();
            }
            return this.recommendedSetPoint;
        }
    }

    /**
     * What is the driver set point?
     * 
     * @author Tyler Protivnak
     * @return driver set point in mph
     */
    public byte getDriverSetPoint() {
        return this.driverSetPoint;
    }

    /**
     * Set the setpoint for the driver
     * 
     * @author Tyler Protivnak
     * @param driverSetPoint new set point value 
     */
    public void setDriverSetPoint(byte driverSetPoint) {
        this.driverSetPoint = driverSetPoint;
    }

    /**
     * what is the recommended setpoint we have?
     * 
     * @author Tyler Protivnak
     * @return rec set point in mph
     */
    public byte getRecommendedSetPoint() {
        return this.recommendedSetPoint;
    }

    /**
     * Set the recommended setpoint
     * 
     * @author Tyler Protivnak
     * @param recommendedSetPoint new set point value 
     */
    public void setRecommendedSetPoint(byte recommendedSetPoint) {
        this.recommendedSetPoint = recommendedSetPoint;
    }
}
