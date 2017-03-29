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
    
    public String getName(){
        return this.name;
    }
    public String getStart(){
        return this.shiftStart;
    }
    public String getBreak(){
        return this.shiftBreak;
    }
    public String getEnd(){
        return this.shiftEnd;
    }
    public int getTrain(){
        return drivingTrain;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setStart(String start){
        this.shiftStart = start;
    }
    public void setBreak(String newBreak){
        this.shiftBreak = newBreak;
    }
    public void setEnd(String end){
        this.shiftEnd = end;
    }
    public boolean isOnBreak(){
        return this.onBreak;
    }
}
