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
     private static int numGreenTrains = 0;
     private static int numRedTrains = 0;
     public TrainSystem trainSystem;
     private static String startTime = "6.00.00";
     private static Object[][] greenData;
     private static Object[][] redData;
     private static int employeeNumber = 0;
     public  String[] redIncrements = {"3.7", "2.3", "1.5", "1.8" , "2.1" , "2.1" , "1.7" , "2.3"};
     public  String[] greenIncrements = {"2.3", "2.3","2.4", "2.7", "2.6", "1.9", "2.0", "2.0", "2.2", "2.5", "2.2", "2.5", "2.2", "4.4", "2.2", "2.3", "2.4", "2.1", "2.0", "2.0"};
     private static Object[] redCols = new Object[11];
     private static Object[] greenCols = new Object[21];
     
     
    
      public Scheduler(TrainSystem ts) throws IOException, InvalidFormatException {
       trainSystem = ts;
       readRedSchedule(file);
       readGreenSchedule(file);
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
        
        
        Object[][] data = new Object[numEmployees+1][5];
        int i,j;
        for(i = 1; i < numEmployees+1; i++){
            Row currRow = personnelSchedule.getRow(i);
            
            for(j = 0; j < 5; j++){
                if(currRow.getCell(j) != null){
                    String info = currRow.getCell(j).toString();
                    data[i][j] = info;
                }
                //System.out.println(tableInfo);
            }
        }
        
      Object[] columnNames = new Object[5];
//      columnNames[0]="NAME";
//      columnNames[1]="SUN";
//      columnNames[2]="MON";
//      columnNames[3]="TUES";
//      columnNames[4]="WED";
//      columnNames[5]="THURS";
//      columnNames[6]="FRI";
//      columnNames[7]="SAT";

        columnNames[0]="NAME";
        columnNames[1]="SHIFT START";
        columnNames[2]="BREAK START";
        columnNames[3]="BREAK END";
        columnNames[4]="SHIFT END";
      
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
        String[] greenIncrements = {"2.3", "2.3","2.4", "2.7", "2.6", "1.9", "2.0", "2.0", "2.2", "2.5", "2.2", "2.5", "2.2", "4.4", "2.2", "2.3", "2.4", "2.1", "2.0", "2.0"};
        
         Object[] redColumnNames = new Object[11];
        
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
                        redColumnNames[j] = info;
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
        
        //System.out.println(redData[1][3].toString());
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
                        redColumnNames[j] = info;
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
        redData = data;
        numRedTrains = numTrains;
        //System.out.println(data[1][3].toString());
        //System.out.println(data[2][3].toString());
        DefaultTableModel model = new DefaultTableModel(data, redColumnNames);
        redCols = redColumnNames;
        scheduleGUI.redTable.setModel(model);
    }
    
    
     public static void readGreenSchedule( File file) throws IOException, InvalidFormatException{
        //scheduleGUI.setVisible(true);
        String[] redIncrements = {"3.7", "2.3", "1.5", "1.8" , "2.1" , "2.1" , "1.7" , "2.3"};
        String[] greenIncrements = {"2.3", "2.3","2.4", "2.7", "2.6", "1.9", "2.0", "2.0", "2.2", "2.5", "2.2", "4.4", "2.2", "2.3", "2.4", "2.1", "2.0", "2.0"};
        greenDispatchTimes.clear();
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet greenSchedule = workbook.getSheetAt(2);
        int numTrains = greenSchedule.getLastRowNum();
         Object[] columnNames = new Object[21];
        
        Object[][] data = new Object[numTrains+1][21];
        int i,j;
        String oldInfo = "";
        String info = "";
        //odds
        for(i = 0; i < numTrains+1; i++){
            if((i+1)%2==0){
            Row currRow = greenSchedule.getRow(i);
            if(currRow != null){
            for(j = 0; j < 21; j++){
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
                            greenDispatchTimes.add(info);
                            //System.out.println(info);
                        }
                    
                }
                else if(j>2){
                    if(oldInfo.compareTo("")!=0){
                    oldInfo = incrementTime(oldInfo, greenIncrements[j-3]);
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
            Row currRow = greenSchedule.getRow(i);
            if(currRow != null){
            for(j = 0; j < 21; j++){
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
                            greenDispatchTimes.add(info);
                            //System.out.println(info);
                        }
                    
                }
                else if(j>2){
                    if(oldInfo.compareTo("")!=0){
                    oldInfo = incrementTime(oldInfo, greenIncrements[j-3]);
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
        
        greenData = data;
        numGreenTrains = numTrains;
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        greenCols = columnNames;
        scheduleGUI.jTable3.setModel(model);
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
        
        
        //numTrains = numTrains * 2;
        int i=0,j=0,k=0;
        String[] personnelHeaders = {"Name","SHIFT START","BREAK START","BREAK END","SHIFT END"};
        String[] trainHeaders = {"Train ID","Driver","Departure","SHADYSIDE","HERRON","SWISSVILLE","PENN STATION","STEEL PLAZA","FIRST AVE","ST SQUARE","STH HILLS"};
        String[] greenHeaders = {"Train ID", "PIONEER", "EDGEBROOK", "PITT", "WHITED", "SOUTH BANK", "CENTRAL", "INGLEWOOD", "OVERBROOK", "GLENBURY", "DORMONT", "MT LEBANON", "POPLAR", "CASTLE SHANON", "DORMONT", "GLEBURY", "OVERBROOK", "INGLEWOOD", "CENTRAL"};

        //Sheet redSchedule = workbook.getSheetAt(1);
        Sheet peopleSheet = workbook.createSheet("Personnel");
        Sheet redSheet = workbook.createSheet("Red");
        Sheet greenSheet = workbook.createSheet("Green");
        employeeNumber = 0;
        startTime = "6.05.00";
        int trainID = -1;
        int dayOff1 = 0;
        int dayOff2 = dayOff1+1;
        int dayOffInterval = 7/numTrains;
        String startTimeDay = "06.05.00";
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
                for(i=0;i<5;i++){
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
                    else if(i==1){
                        cell.setCellValue(startTime);
                    }
                    else if(i==2){
                        cell.setCellValue(incrementTime(startTime,"240.0"));
                    }
                    else if(i==3){
                        cell.setCellValue(incrementTime(startTime,"270.0"));
                    }
                    else if(i==4){
                        cell.setCellValue(incrementTime(startTime,"510.0"));
                    }
                    
                    
                }
                   
                for(i=0;i<3;i++){
                    Cell cell = redRow.createCell(i);
                    if(i==0){
                        cell.setCellValue(trainID);
                    }
                    else if(i==1){
                        cell.setCellValue(driverNames[j]);
                    }
                    else if(i==2){
                       // if((j &1)==0){
                        cell.setCellValue(startTimeDay);
                        startTimeDay = incrementTime(startTimeDay,"15.0");
                       //startTimeDay  = convertTime(startTimeDay);
                    //}
                      //  else{
                           //cell.setCellValue(startTimeEvening);
                            //startTimeEvening = incrementTime(startTimeEvening,"7.0"); 
                           // startTimeEvening  = convertTime(startTimeEvening);
                       // }
                    }
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
        
        
        
        //generate green
        startTimeDay = "06.05.00";
        for(j=0;j<numTrains+1;j++){
            isOff = 0;
            testTrigger = 0;
            Row personRow = peopleSheet.createRow(j);
            Row greenRow = greenSheet.createRow(j);
            if(j==0){
                for(i=0;i<5;i++){
                    Cell cell = personRow.createCell(i);
                    cell.setCellValue(personnelHeaders[i]);
                }
                for(i=0;i<11;i++){
                    Cell greenCell = greenRow.createCell(i);
                    if(j==0){
                    greenCell.setCellValue(trainHeaders[i]);
                    greenCell.setCellValue(trainHeaders[i]);
                    }
                    else{
                        greenCell.setCellValue("");
                    }
                }
            }
            else{
                String[] driverNames = new String[numTrains+1];
                for(i=0;i<5;i++){
                    Cell cell = personRow.createCell(i);
                    if(i==0){
                        driverNames[j]="Employee "+employeeNumber;
                        cell.setCellValue("Employee "+employeeNumber);
                    }
                    else if(i==1){
                        cell.setCellValue(convertTime(startTime));
                    }
                    else if(i==2){
                        String temp1 = incrementTime(startTime,"60.0");
                        String temp2 = incrementTime(temp1,"60.0");
                        String temp3 = incrementTime(temp2,"60.0");
                        String temp4 = incrementTime(temp3,"60.0");
                        cell.setCellValue(convertTime(temp4));
                    }
                    else if(i==3){
                        String temp1 = incrementTime(startTime,"60.0");
                        String temp2 = incrementTime(temp1,"60.0");
                        String temp3 = incrementTime(temp2,"60.0");
                        String temp4 = incrementTime(temp3,"60.0");
                        String temp5 = incrementTime(temp4,"30.0");
                        cell.setCellValue(convertTime(temp5));
                    }
                    else if(i==4){
                        String temp1 = incrementTime(startTime,"60.0");
                        String temp2 = incrementTime(temp1,"60.0");
                        String temp3 = incrementTime(temp2,"60.0");
                        String temp4 = incrementTime(temp3,"60.0");
                        String temp5 = incrementTime(temp4,"60.0");
                        String temp6 = incrementTime(temp5,"60.0");
                        String temp7 = incrementTime(temp6,"60.0");
                        String temp8 = incrementTime(temp7,"60.0");
                        String temp9 = incrementTime(temp8,"30.0");
                        cell.setCellValue(convertTime(temp9));
                    }
                    
                    
                }
                   if(isOff == 0){
                for(i=0;i<3;i++){
                    Cell cell = greenRow.createCell(i);
                    if(i==0){
                        cell.setCellValue(trainID);
                    }
                    else if(i==1){
                        cell.setCellValue(driverNames[j]);
                    }
                    else if(i==2){
                        //if((j &1)==0){
                        cell.setCellValue(startTimeDay);
                        startTimeDay = incrementTime(startTimeDay,"15.0");
                       //startTimeDay  = convertTime(startTimeDay);
                    //}
                       // else{
                          // cell.setCellValue(startTimeEvening);
                           // startTimeEvening = incrementTime(startTimeEvening,"7.0"); 
                           // startTimeEvening  = convertTime(startTimeEvening);
                       // }
                    }
                }
                            
                   }
                   else{
                       Row deleted = greenSheet.getRow(j);
                       greenSheet.removeRow(deleted);
                       /*
                       System.out.println("J+1: "+(j+1)+" Last row: "+numTrains);
                       if(j+1<numTrains){
                           System.out.println("SHIFTING");
                        greenSheet.shiftRows(j, j, 1);
                       
                       }
                       */
                   }
            }
            startTime = incrementTime(startTime,"10.0");
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
        }// end generate green
        
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
    
    public ArrayList<String> getTimes(int stationID, int blockID){
        if (stationID == 1) {
            return getCastleShannonTimes();
        }
        else if (stationID == 2 && blockID == 39) {
            return getCentralTimes();
        }
        else if (stationID == 2 && blockID == 141) {
            return getCentralTimes();
        }
        else if (stationID == 3 && blockID == 73) {
            return getDormontTimes();
        }
        else if (stationID == 3 && blockID == 105) {
            return getDormontInboundTimes();
        }
        else if (stationID == 4) {
            return getEdgebrookTimes();
        }
        else if (stationID == 5) {
            return getFirstAveTimes();
        }
        else if (stationID == 6 && blockID == 65) {
            return getGlenburyTimes();
        }
        else if (stationID == 6 && blockID == 114) {
            return getGlenburyInboundTimes();
        }
        else if (stationID == 7) {
            return getHerronTimes();
        }
        else if (stationID == 8 && blockID == 48) {
            return getInglewoodTimes();
        }
        else if (stationID == 8 && blockID == 132) {
            return getInglewoodTimes();
        }
        else if (stationID == 9) {
            return getMtLebanonTimes();
        }
        else if (stationID == 10 && blockID == 57) {
            return getOverbrookTimes();
        }
        else if (stationID == 10 && blockID == 123) {
            return getOverbrookInboundTimes();
        }
        else if (stationID == 11) {
            return getPennStTimes();
        }
        else if (stationID == 12) {
            return getPioneerTimes();
        }
        else if (stationID == 13) {
            return getPittTimes();
        }
        else if (stationID == 14) {
            return getPoplarTimes();
        }
        else if (stationID == 15) {
            return getShadysideTimes();
        }
        else if (stationID == 16) {
            return getSthBankTimes();
        }
        else if (stationID == 17) {
            return getSthHillsTimes();
        }
        else if (stationID == 18) {
            return getStSquareTimes();
        }
        else if (stationID == 19) {
            return getSteelPlazaTimes();
        }
        else if (stationID == 20) {
            return getSwissvaleTimes();
        }
        else if (stationID == 21) {
            return getWhitedTimes();
        }
        else {
            return new ArrayList<String>();
        }
    }
    
//    private ArrayList<String> herron = new ArrayList();
    public ArrayList<String> getShadysideTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][3].toString());
        }
        return times;
    }
    public ArrayList<String> getHerronTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][4].toString());
        }
        return times;
    }
//     private ArrayList<String> swissvale = new ArrayList();
    public ArrayList<String> getSwissvaleTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][5].toString());
        }
        return times;
    }
//     private ArrayList<String> penn = new ArrayList();
    public ArrayList<String> getPennStTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][6].toString());
        }
        return times;
    }
//     private ArrayList<String> plaza = new ArrayList();
    public ArrayList<String> getSteelPlazaTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][7].toString());
        }
        return times;
    }
//     private ArrayList<String> first = new ArrayList();
    public ArrayList<String> getFirstAveTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][8].toString());
        }
        return times;
    }
//     private ArrayList<String> square = new ArrayList();
    public ArrayList<String> getStSquareTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][9].toString());
        }
        return times;
    }
//     private ArrayList<String> hills = new ArrayList();
    public ArrayList<String> getSthHillsTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numRedTrains; i++){
            times.add(redData[i][10].toString());
        }
        return times;
    }
//     //green stations (start @ row 4)
//     private ArrayList<String> pioneer = new ArrayList();
     public ArrayList<String> getPioneerTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][3].toString());
        }
        return times;
    }
//     private ArrayList<String> edgebrook = new ArrayList();
     public ArrayList<String> getEdgebrookTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][4].toString());
        }
        return times;
    }
//     private ArrayList<String> pitt = new ArrayList();
     public ArrayList<String> getPittTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][5].toString());
        }
        return times;
    }
//     private ArrayList<String> whited = new ArrayList();
     public ArrayList<String> getWhitedTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][6].toString());
        }
        return times;
    }
//     private ArrayList<String> bank = new ArrayList();
     public ArrayList<String> getSthBankTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][7].toString());
        }
        return times;
    }
     public ArrayList<String> getCentralTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][8].toString());
        }
        return times;
    }
//     private ArrayList<String> central = new ArrayList();
//     private ArrayList<String> inglewood = new ArrayList();
     public ArrayList<String> getInglewoodTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][9].toString());
        }
        return times;
    }
//     private ArrayList<String> overbrook = new ArrayList();
     public ArrayList<String> getOverbrookTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][10].toString());
        }
        return times;
    }
//     private ArrayList<String> glenbury = new ArrayList();
     public ArrayList<String> getGlenburyTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][11].toString());
        }
        return times;
    }
//     private ArrayList<String> dormont = new ArrayList();
     public ArrayList<String> getDormontTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][12].toString());
        }
        return times;
    }
//     private ArrayList<String> lebo = new ArrayList();
     public ArrayList<String> getMtLebanonTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][13].toString());
        }
        return times;
    }
//     private ArrayList<String> poplar = new ArrayList();
     public ArrayList<String> getPoplarTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][14].toString());
        }
        return times;
    }
//     private ArrayList<String> castle = new ArrayList();
     public ArrayList<String> getCastleShannonTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][15].toString());
        }
        return times;
    }
//     private ArrayList<String> dormontInbound = new ArrayList();
     public ArrayList<String> getDormontInboundTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][16].toString());
        }
        return times;
    }
//     private ArrayList<String> glenburyInbound = new ArrayList();
     public ArrayList<String> getGlenburyInboundTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][17].toString());
        }
        return times;
    }
//     private ArrayList<String> overbrookInbound = new ArrayList();
     public ArrayList<String> getOverbrookInboundTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][18].toString());
        }
        return times;
    }
     public ArrayList<String> getInglewoodInboundTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][19].toString());
        }
        return times;
    }
     public ArrayList<String> getCentralInboundTimes(){
        ArrayList<String> times = new ArrayList();
        for(int i=1; i<numGreenTrains; i++){
            times.add(greenData[i][20].toString());
        }
        return times;
    }
     public Object[][] getGreenData(){
         return greenData;
     }
     public Object[][] getRedData(){
         return redData;
     }
     public void setGreenData(Object[][] gData){
         greenData = gData;
         DefaultTableModel model = new DefaultTableModel(gData, greenCols);
        scheduleGUI.jTable3.setModel(model);
     }
     public void setRedData(Object[][] rData){
         redData = rData;
         DefaultTableModel model = new DefaultTableModel(rData, redCols);
        scheduleGUI.redTable.setModel(model);
     }
     
     /**
      * Returns string corresponding to current MBO mode
      * "Fixed Block" or "Moving Block"
      * @return string corresponding to current automatic mode
      */
     public String getMode(){
         return trainSystem.getMBO().getMode();
     }
}
