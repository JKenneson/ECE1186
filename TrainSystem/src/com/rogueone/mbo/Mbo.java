/**
 * The Moving Block Overlay (MBO) serves as an autonomous dispatcher for the train.
 * The MBO also contains the scheduler, which generated weekly schedules for employees, as well as
 * daily schedules for trains.
 *
 * @author Brian Stevenson
 * @creation date 2/7/17
 * @modification date 2/12/17
 */
package com.rogueone.mbo;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.*;
import java.io.*;
import org.apache.poi.ss.usermodel.Row;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.rogueone.mbo.gui.MovingBlockGUI;
import com.rogueone.mbo.gui.TrainScheduleGUI;
import com.rogueone.global.Global;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Main class for the MBO module
 * @author Brian Stevenson
 */
public class Mbo{
    public static ArrayList<String> trains = new ArrayList<String>();
    public static MovingBlockGUI mboGui = new MovingBlockGUI();
   public static File file = new File("src\\com\\rogueone\\assets\\schedule.xlsx");
   public static int trainIndex;
    
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
   
    /**
     * Reads information from excel sheet regarding train schedules, then outputs them to the GUI
     * @param gui GUI to be updated with train schedule information 
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static void readRedSchedule(TrainScheduleGUI gui, File file) throws IOException, InvalidFormatException{
        
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet redSchedule = workbook.getSheetAt(1);
        int numTrains = redSchedule.getLastRowNum();
        String[] redIncrements = {"3.7", "2.3", "1.5", "1.8" , "2.1" , "2.1" , "1.7" , "2.3"};
         Object[] columnNames = new Object[11];
        
        Object[][] data = new Object[numTrains][11];
        int i,j;
        String oldInfo = "";
        String info = "";
        for(i = 1; i < numTrains; i++){
            Row currRow = redSchedule.getRow(i);
            
            for(j = 0; j < 11; j++){
                if(currRow.getCell(j) != null){
                    info = currRow.getCell(j).toString();
                    if(i==0){
                        columnNames[j] = info;
                    }
                        if(j==2){
                            oldInfo = info;
                            info = convertTime(info);
                        }
                    
                }
                else{
                    oldInfo = incrementTime(oldInfo, redIncrements[j-3]);
                    info = convertTime(oldInfo);
                }
                data[i][j] = info;
            }
        }
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        gui.redTable.setModel(model);
    }
    
    public static String convertTime(String time){
        String[] times = time.split("\\.");
        String AMPM = "am";
        int hours=0, minutes=0, seconds=0;
        hours=Integer.parseInt(times[0]);
        minutes=Integer.parseInt(times[1]);
        seconds=Integer.parseInt(times[2]);
        
        time = hours+":"+minutes+AMPM;
        if(seconds>60)
        {
            seconds=seconds-60;
            minutes++;
        }
        if(minutes > 60)
        {
            minutes=minutes-60;
            hours++;
        }
        if(hours>12)
        {
            hours=hours-12;
            AMPM="pm";
        }
        if(minutes<10)
        {
            time = hours+":0"+minutes+AMPM;
        }
        //System.out.println(time);
        return time;
    }
    
    public static String incrementTime(String time, String increment){
        String[] times = time.split("\\.");
        String[] increments = increment.split("\\.");
        String AMPM = "am";
        int hours=0, minutes=0, seconds=0, tens = 0, decimal = 0;
        hours=Integer.parseInt(times[0]);
        minutes=Integer.parseInt(times[1]);
        seconds=Integer.parseInt(times[2]);
        tens = Integer.parseInt(increments[0]);
        decimal = Integer.parseInt(increments[1]);
        
        minutes = minutes+tens;
        seconds = seconds+(60*(decimal/10));
        time = hours+"."+minutes+"."+seconds;
        
        return time;
    }
    
    /**
     * 
     */
    public static void displayCurrentTrains(){
        String[] dummyDataRed = {"Red","U","77","SHADYSIDE","6:04am","164ft","10mph","35mph","0","0"};
        String[] dummyDataGreen = {"Green","YY","152","PIONEER","6:04am","164ft","12mph","35mph","0","0"};
        String[][] dummyData = {dummyDataRed, dummyDataGreen};
        int length = trains.size();
        Object[] trainArray = trains.toArray();
        Object[][] data = new Object[6][9];
        Object[] columnNames={"TRAIN ID", "TRAIN LINE", "TRACK SECTION", "BLOCK", "NEXT STATION", "TIME OF ARRIVAL", "AUTHORITY", "CURRENT SPEED", "SUGGESTED SPEED", "PASSENGERS"};
        int i=0,j=0;
        mboGui.TrainDropdown.removeAllItems();
        
        for(i=0;i<length;i++){
           
           mboGui.TrainDropdown.addItem(trainArray[i].toString());
           for(j=0;j<9;j++){
                if(j==0){
                  data[i][0]= trainArray[i]; 
                  
                }
                else{
                    data[i][j]=dummyData[i][j-1];
                }
           }
        }
       
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        mboGui.trainTable.setModel(model);
        model.fireTableDataChanged();
        mboGui.trainTable.repaint();
    }
    
    public void updateTrainID(int newIdIndex){
        if(trains.size()>0){
        String newID = trains.get(newIdIndex);
        mboGui.TrainIdValue.setText(newID);
    }
    }
    
   public void deploy() throws IOException, InvalidFormatException{
       File file = new File("src\\com\\rogueone\\assets\\schedule.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet redSchedule = workbook.getSheetAt(1);
        Row currRow = redSchedule.getRow(trainIndex);
        String trainID = currRow.getCell(0).toString();
        String[] IDs = trainID.split("\\.");
        
        trains.add(IDs[0]);
        trainIndex=trainIndex+1;
         
        displayCurrentTrains();
        
   }  
   
   public static void refresh(MovingBlockGUI gui){
       
   }
    /**
     * Function to generate a new employee schedule in excel
     * @param file excel file to be written to
     */
    public void generatePersonnelSchedule(File file) throws IOException, InvalidFormatException{
        int numEmployees = 0;
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
        
        numEmployees = names.size();
        
        //for(int i=0; i<numEmployees; i=i+3){
        //}
            
        
    }
    
    /**
     * Generates a new train schedule and writes it to an excel sheet
     */
    public void generateTrainSchedule(){
        
    }
    
    public static void main(String[] args) throws IOException, InvalidFormatException{
        //ArrayList<String> trains = new ArrayList<String>();
        //MovingBlockGUI mboGui = new MovingBlockGUI();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mboGui);
        frame.pack();
        trainIndex = 1;
        //deploy(mboGui);
        //deploy(mboGui);
        displayCurrentTrains();
        readPersonnelSchedule(file);
        frame.setVisible(true);
        
    }
}