/**
 * The Moving Block Overlay (MBO) serves as an autonomous dispatcher for the train.
 * The MBO also contains the scheduler, which generated weekly schedules for employees, as well as
 * daily schedules for trains.
 *
 * @author Brian Stevenson
 * @creation date 2/7/17
 * @modification date 2/9/17
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
    /**
     * Reads th excel file for the personnel schedule, then outputs it to the GUI
     * @param gui MBO GUI to be updated with personnel schedule information
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static void readPersonnelSchedule(MovingBlockGUI gui) throws IOException, InvalidFormatException{
         String tableInfo="";
        //Openning Excel Sheet
        File file = new File("src\\com\\rogueone\\assets\\schedule.xlsx");
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
                System.out.println(tableInfo);
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
      gui.pScheduleTable.setModel(model);
     
    }
   
    /**
     * Reads information from excel sheet regarding train schedules, then outputs them to the GUI
     * @param gui GUI to be updated with train schedule information 
     * @throws IOException
     * @throws InvalidFormatException 
     */
    public static void readTrainSchedule(MovingBlockGUI gui) throws IOException, InvalidFormatException{
        
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
        MovingBlockGUI mboGui = new MovingBlockGUI();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mboGui);
        frame.pack();
        frame.setVisible(true);
        readPersonnelSchedule(mboGui);
    }
}