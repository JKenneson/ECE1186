/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.mbo;
import static com.rogueone.mbo.Mbo.convertTime;
import static com.rogueone.mbo.Mbo.incrementTime;
import static com.rogueone.mbo.Mbo.mboGui;
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
public class Scheduler {
     private static File file = new File("src/com/rogueone/assets/schedule.xlsx");
     private static ArrayList<String> redDispatchTimes = new ArrayList<String>();
     private static ArrayList<String> greenDispatchTimes = new ArrayList<String>();
     public static MovingBlockGUI mboGui = Mbo.mboGui;
     public static TrainScheduleGUI scheduleGUI = new TrainScheduleGUI();
     public TrainSystem trainSystem;
    
      public Scheduler(TrainSystem ts) throws IOException, InvalidFormatException {
       trainSystem = ts;
       readRedSchedule(file);
       readPersonnelSchedule(file);
   }
    /**
     * Reads th excel file for the personnel schedule, then outputs it to the GUI
     * @param gui MBO GUI to be updated with personnel schedule information
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static void readPersonnelSchedule(File file) throws IOException, InvalidFormatException{
         String tableInfo="";
        //Openning Excel Sheet
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet personnelSchedule = workbook.getSheetAt(0);
        int numEmployees = personnelSchedule.getLastRowNum();
        
        
        Object[][] data = new Object[numEmployees+1][8];
        int i,j;
        for(i = 1; i < numEmployees+1; i++){
            Row currRow = personnelSchedule.getRow(i);
            
            for(j = 0; j < 8; j++){
                if(currRow.getCell(j) != null){
                    String info = currRow.getCell(j).toString();
                    data[i][j] = info;
                }
                //System.out.println(tableInfo);
            }
        }
        
      Object[] columnNames = new Object[8];
      columnNames[0]="NAME";
      columnNames[1]="SUN";
      columnNames[2]="MON";
      columnNames[3]="TUES";
      columnNames[4]="WED";
      columnNames[5]="THURS";
      columnNames[6]="FRI";
      columnNames[7]="SAT";
      
      DefaultTableModel model = new DefaultTableModel(data, columnNames);
      mboGui.pScheduleTable.setModel(model);
     
    }
    public static void setScheduleVisible(){
        scheduleGUI.setVisible(true);
    }
    /**
     * Reads information from excel sheet regarding train schedules, then outputs them to the GUI
     * @param gui GUI to be updated with train schedule information 
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static void readRedSchedule( File file) throws IOException, InvalidFormatException{
        //scheduleGUI.setVisible(true);
        redDispatchTimes.clear();
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet redSchedule = workbook.getSheetAt(1);
        int numTrains = redSchedule.getLastRowNum();
        String[] redIncrements = {"3.7", "2.3", "1.5", "1.8" , "2.1" , "2.1" , "1.7" , "2.3"};
         Object[] columnNames = new Object[11];
        
        Object[][] data = new Object[numTrains+1][11];
        int i,j;
        String oldInfo = "";
        String info = "";
        //odds
        for(i = 0; i < numTrains+1; i++){
            if((i+1)%2==0){
            Row currRow = redSchedule.getRow(i);
            if(currRow != null){
            for(j = 0; j < 11; j++){
                if(currRow.getCell(j) != null){
                    info = currRow.getCell(j).toString();
                    
                    //System.out.println(i);
                    if(i==0){     
                        //System.out.println("HERE");
                        columnNames[j] = info;
                    }
                    else if(j==0){
                        String[] infos = info.split("\\.");
                        info = infos[0];
                    }
                    else if(j==2){
                            oldInfo = info;
                            info = convertTime(info);
                            redDispatchTimes.add(info);
                            //System.out.println(info);
                        }
                    
                }
                else if(j>2){
                    if(oldInfo.compareTo("")!=0){
                    oldInfo = incrementTime(oldInfo, redIncrements[j-3]);
                    info = convertTime(oldInfo);
                    }
                }
                if((i>0)){
                data[i][j] = info;
                }
            }
            }
            }
        }
        
        //evens only
        for(i = 0; i < numTrains+1; i++){
            if((i)%2 == 0){
            Row currRow = redSchedule.getRow(i);
            if(currRow != null){
            for(j = 0; j < 11; j++){
                if(currRow.getCell(j) != null){
                    info = currRow.getCell(j).toString();
                    
                    //System.out.println(i);
                    if(i==0){     
                        //System.out.println("HERE");
                        columnNames[j] = info;
                    }
                    else if(j==0){
                        String[] infos = info.split("\\.");
                        info = infos[0];
                    }
                    else if(j==2){
                            oldInfo = info;
                            info = convertTime(info);
                            redDispatchTimes.add(info);
                            //System.out.println(info);
                        }
                    
                }
                else if(j>2){
                    if(oldInfo.compareTo("")!=0){
                    oldInfo = incrementTime(oldInfo, redIncrements[j-3]);
                    info = convertTime(oldInfo);
                    }
                }
                if((i>0)){
                data[i][j] = info;
                }
            }
            }
            }
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        scheduleGUI.redTable.setModel(model);
    }
    
    public ArrayList<String> getDispatchTimes(){
        return this.redDispatchTimes;
    }
    
    
    
    /**
     * Function to generate a new employee schedule in excel
     * @param file excel file to be written to
     */
    public static void generateSchedule(int numTrains) throws IOException, InvalidFormatException{
        Workbook workbook = new XSSFWorkbook();
       // System.out.println("There are: "+numTrains+" trains!");
        
        
        numTrains = numTrains * 2;
        int i=0,j=0,k=0;
        String[] personnelHeaders = {"Name","SUN","MON","TUES","WED","THURS","FRI","SAT"};
        String[] trainHeaders = {"Train ID","Driver","Departure","SHADYSIDE","HERRON","SWISSVILLE","PENN STATION","STEEL PLAZA","FIRST AVE","ST SQUARE","STH HILLS"};

        //Sheet redSchedule = workbook.getSheetAt(1);
        Sheet peopleSheet = workbook.createSheet("Personnel");
        Sheet redSheet = workbook.createSheet("Red");
        Sheet greenSheet = workbook.createSheet("Green");
        int employeeNumber = 0;
        int trainID = 100;
        int dayOff1 = 0;
        int dayOff2 = dayOff1+1;
        int dayOffInterval = 7/numTrains;
        String startTimeDay = "06.00.00";
        String startTimeEvening = "14.00.00";
        int currentDay = 1;
        int testTrigger = 0;
        int lastUnused = 0;
        int numberMissed = 0;
        int isOff = 0;
        ArrayList<Integer> workingEmployees = new ArrayList<Integer>();
        if(dayOffInterval==0){
        dayOffInterval =1;
        }
        //generate red
        for(j=0;j<numTrains+1;j++){
            isOff = 0;
            testTrigger = 0;
            Row peopleRow = peopleSheet.createRow(j);
            Row redRow = redSheet.createRow(j);
            Row greenRow = greenSheet.createRow(j);
            if(j==0){
                for(i=0;i<8;i++){
                    Cell cell = peopleRow.createCell(i);
                    cell.setCellValue(personnelHeaders[i]);
                }
                for(i=0;i<11;i++){
                    Cell redCell = redRow.createCell(i);
                    Cell greenCell = greenRow.createCell(i);
                    if(j==0){
                    redCell.setCellValue(trainHeaders[i]);
                    greenCell.setCellValue(trainHeaders[i]);
                    }
                    else{
                        redCell.setCellValue("");
                    }
                }
            }
            else{
                String[] driverNames = new String[numTrains+1];
                for(i=0;i<8;i++){
                    Cell cell = peopleRow.createCell(i);
                    if(i==0){
                        driverNames[j]="Employee "+employeeNumber;
                        cell.setCellValue("Employee "+employeeNumber);
                    }
                    else if(i==dayOff1 || i==dayOff2){
                        if(currentDay == i){
                            isOff = 1;
                        }
                        cell.setCellValue("OFF");
                    }
                    else{
                        
                        if((j &1)==0){
                            cell.setCellValue("6am - 2:30pm");
                        }
                        else{
                            cell.setCellValue("2pm - 10:30pm");
                        }
                    }
                    
                    
                }
                   if(isOff == 0){
                for(i=0;i<3;i++){
                    Cell cell = redRow.createCell(i);
                    if(i==0){
                        cell.setCellValue(trainID);
                    }
                    else if(i==1){
                        cell.setCellValue(driverNames[j]);
                    }
                    else if(i==2){
                        if((j &1)==0){
                        cell.setCellValue(startTimeDay);
                        startTimeDay = incrementTime(startTimeDay,"7.0");
                       //startTimeDay  = convertTime(startTimeDay);
                    }
                        else{
                           cell.setCellValue(startTimeEvening);
                            startTimeEvening = incrementTime(startTimeEvening,"7.0"); 
                           // startTimeEvening  = convertTime(startTimeEvening);
                        }
                    }
                }
                            
                   }
                   else{
                       Row deleted = redSheet.getRow(j);
                       redSheet.removeRow(deleted);
                       /*
                       System.out.println("J+1: "+(j+1)+" Last row: "+numTrains);
                       if(j+1<numTrains){
                           System.out.println("SHIFTING");
                        redSheet.shiftRows(j, j, 1);
                       
                       }
                       */
                   }
            }
            employeeNumber++;
            dayOff1=dayOff1+dayOffInterval;
            dayOff2=dayOff1+1;
            if(dayOff1>7){
                dayOff1=0;
            }
            if(dayOff2>7){
                dayOff2=0;
            }
            trainID++;
        }// end generate red
        
        FileOutputStream output = new FileOutputStream("src\\com\\rogueone\\assets\\altSchedule.xlsx");
        workbook.write(output);
        output.close();
        /*
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        workbook.createSheet("Personnel");
        XSSFSheet personnelSchedule = workbook.getSheetAt(1);
        
        ArrayList<String> names = new ArrayList<String>();
        
            //creating an array of all employee names
            for(int i = 0; i <= personnelSchedule.getLastRowNum(); i++){
                Row tempRow = personnelSchedule.getRow(i);
                String tempString = tempRow.getCell(0).toString();
                names.add(tempString);
            }
        
        numTrains = names.size();
        
        //for(int i=0; i<numEmployees; i=i+3){
        //}
        */
    }
}
