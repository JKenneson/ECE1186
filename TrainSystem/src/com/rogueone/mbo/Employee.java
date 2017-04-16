/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rogueone.mbo;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import java.io.*;
import org.apache.poi.ss.usermodel.Row;
import java.util.ArrayList;
import java.io.IOException;
import com.rogueone.mbo.gui.MovingBlockGUI;
import com.rogueone.mbo.gui.TrainScheduleGUI;
import com.rogueone.trainsystem.TrainSystem;
import javax.swing.table.*;
/**
 *
 * @author Brian
 */
public class Employee {
    public String name;
    public String shiftStart;
    public String shiftBreak;
    public String shiftEnd;
    public int drivingTrain;
    public boolean onBreak;
    
    /**
     * Gets employee name
     * @return employee name
     * @author Brian Stevenson
     */
    public String getName(){
        return this.name;
    }
    /**
     * Gets shift start time
     * @return shift start time
     * @author Brian Stevenson
     */
    public String getStart(){
        return this.shiftStart;
    }
    /**
     * Gets break start time
     * @return break start time
     * @author Brian Stevenson
     */
    public String getBreak(){
        return this.shiftBreak;
    }
    /**
     * Gets shift end time
     * @return shift end time
     * @author Brian Stevenson
     */
    public String getEnd(){
        return this.shiftEnd;
    }
    /**
     * Gets train employee is driving
     * @return current train
     * @author Brian Stevenson
     */
    public int getTrain(){
        return drivingTrain;
    }
    /**
     * Sets employee name
     * @param name 
     * @author Brian Stevenson
     */
    public void setName(String name){
        this.name = name;
    }
    /**
     * Sets shift start
     * @param start 
     * @author Brian Stevenson
     */
    public void setStart(String start){
        this.shiftStart = start;
    }
    /**
     * Sets break start
     * @param newBreak 
     * @author Brian Stevenson
     */
    public void setBreak(String newBreak){
        this.shiftBreak = newBreak;
    }
    /**
     * Sets shift end
     * @param end
     * @author Brian Stevenson
     */
    public void setEnd(String end){
        this.shiftEnd = end;
    }
    /**
     * Gets weather employee is on break or not
     * @return break status
     * @author Brian Stevenson
     */
    public boolean isOnBreak(){
        return this.onBreak;
    }
}
