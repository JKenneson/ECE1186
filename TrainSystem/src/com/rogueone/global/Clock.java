/**
 * The clock class holds all information about the current time and day of the system
 * The clock will run Monday-Sunday from 6:00AM - 11:00PM every day
 *
 * @author Jonathan Kenneson
 * @Creation 2/25/17
 * @Modification 2/25/17
 */
package com.rogueone.global;

/**
 * The clock will run Monday-Sunday from 6:00AM - 11:00PM every day
 *
 * @author Jon Kenneson
 */
public class Clock {
    
    //Enum declaration
    /**
     * Holds each of the 7 days of the week as an enumeration
     */
    private enum DayOfTheWeek {
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    }
    /**
     * Holds the AM or PM modifier to the time of the day to recognize if it is in the morning or evening
     */
    private enum TimeOfDay {
        AM, PM;
    }
    
    //Variable declaration
    private DayOfTheWeek dayOfTheWeek;
    private TimeOfDay timeOfDay;
    private int hour;
    private int minute;
    private int second;
    
    /**
     * Initializer for the clock class sets the hour to 6, the time of day to AM, and the day of the week to Monday
     * @author Jonathan Kenneson
     */
    public Clock() {
        this.dayOfTheWeek = DayOfTheWeek.Monday;
        this.timeOfDay = TimeOfDay.AM;
        this.hour = 6;
        this.minute = 0;
        this.second = 0;
    }
    
    /**
     * This method will update the clock 1 second, and will update to the next day if we are at 11PM
     * @author Jonathan Kenneson
     */
    public void updateClock() {
        this.second ++;             //Increment the seconds
        if(this.second == 60) {     //If we move into a new minute, increment minute and reset second
            this.minute ++;
            this.second = 0;
        }
        if(this.minute == 60) {     //If we move into a new hour, increment hour and reset minute
            this.hour ++;
            this.minute = 0;
        }
        if(this.hour == 12 && this.timeOfDay == TimeOfDay.AM) { //If we move into the afternoon, set the time of day to PM
            this.timeOfDay = TimeOfDay.PM;
        }
        if(this.hour == 13) {       //Wrapping around (really 1:00 PM)
            this.hour = 1;
        }
        if(this.hour == 11 && this.timeOfDay == TimeOfDay.PM) { //If it's 11:00 PM, move into the next day and reset the clock back to 6:00AM
            this.dayOfTheWeek = getNextDay(this.dayOfTheWeek);
            this.timeOfDay = TimeOfDay.AM;
            this.hour = 6;
            this.minute = 0;
            this.second = 0;
        }
    }
    
    /**
     * Print the clock in a nice format ->   Day HH:MM:SS AM
     * 
     * @author Jonathan Kenneson
     * @return A nicely formatted clock -> Day HH:MM:SS AM
     */
    public String printClock() {
        StringBuilder stringToReturn = new StringBuilder();
        
        stringToReturn.append(this.dayOfTheWeek);
        stringToReturn.append(" ");
        if(this.hour < 10) {
            stringToReturn.append("0");
        }
        stringToReturn.append(this.hour);
        stringToReturn.append(":");
        if(this.minute < 10) {
            stringToReturn.append("0");
        }
        stringToReturn.append(this.minute);
        stringToReturn.append(":");
        if(this.second < 10) {
            stringToReturn.append("0");
        }
        stringToReturn.append(this.second);
        stringToReturn.append(" ");
        stringToReturn.append(this.timeOfDay);
        
        return stringToReturn.toString();
    }
    
    
    
    /**
     * Helper function to return the next day of the week based on the current day
     * 
     * @author Jonathan Kenneson
     * @param currentDayOfTheWeek   The current day of the week
     * @return The next day in logical sequence
     */
    private DayOfTheWeek getNextDay(DayOfTheWeek currentDayOfTheWeek) {
        if(currentDayOfTheWeek == DayOfTheWeek.Monday) {
            return DayOfTheWeek.Tuesday;
        }
        else if(currentDayOfTheWeek == DayOfTheWeek.Tuesday) {
            return DayOfTheWeek.Wednesday;
        }
        else if(currentDayOfTheWeek == DayOfTheWeek.Wednesday) {
            return DayOfTheWeek.Thursday;
        }
        else if(currentDayOfTheWeek == DayOfTheWeek.Thursday) {
            return DayOfTheWeek.Friday;
        }
        else if(currentDayOfTheWeek == DayOfTheWeek.Friday) {
            return DayOfTheWeek.Saturday;
        }
        else if(currentDayOfTheWeek == DayOfTheWeek.Saturday) {
            return DayOfTheWeek.Sunday;
        }
        else {
            return DayOfTheWeek.Monday;
        }
    }
}
