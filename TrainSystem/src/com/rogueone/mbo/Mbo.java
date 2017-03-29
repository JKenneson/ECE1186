/**
 * The Moving Block Overlay (MBO) serves as an autonomous dispatcher for the train.
 * The MBO also contains the scheduler, which generated weekly schedules for employees, as well as
 * daily schedules for trains.
 *
 * @author Brian Stevenson
 * @creation date 2/7/17
 * @modification date 2/16/17
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
 * Main class for the MBO module
 * @author Brian Stevenson
 */
public class Mbo{
    
     private static ArrayList<MboTrain> trainList = new ArrayList<MboTrain>();
    public static MovingBlockGUI mboGui = new MovingBlockGUI();
   private static File file = new File("src\\com\\rogueone\\assets\\schedule.xlsx");
   private static int trainIndex;
  private static String[] dummyDataRed = {"100","Red","U","77","SHADYSIDE","6:04am","164ft","10mph","35mph","0","0"};
   private static String[] dummyDataGreen = {"101","Green","YY","152","PIONEER","6:04am","164ft","12mph","35mph","0","0"};
   private static String[][] dummyData = {dummyDataRed, dummyDataGreen};
   private TrainSystem trainSystem;
   
   public Mbo(TrainSystem ts) throws IOException, InvalidFormatException {
       trainSystem = ts;
       System.out.println("Reading Schedules");
        Scheduler.readPersonnelSchedule(file);
   }
    
   public MovingBlockGUI newGui() throws IOException, InvalidFormatException{
       //JFrame frame = new JFrame();
       // frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.getContentPane().add(mboGui);
        //frame.pack();
        trainIndex = 1;
        //deploy(mboGui);
        //deploy(mboGui);
        displayCurrentTrains();
        //System.out.println("Reading Schedules");
        Scheduler.readPersonnelSchedule(file);
        //frame.setVisible(true);
        return mboGui;
   }
   
   public MovingBlockGUI getGUI() {
       return mboGui;
   }
  

    
    /**
     * Converts time from 12.10.55 format to 12:10:55 format
     * @param time Time value to be converted
     * @return returns time in proper format
     */
    public static String convertTime(String time){
        String[] times = time.split("\\.");
        String AMPM = "am";
        int hours=0, minutes=0, seconds=0;
        //System.out.println(times[0]+" "+times[1]+" "+times[2]);
        hours=Integer.parseInt(times[0]);
        minutes=Integer.parseInt(times[1]);
        seconds=Integer.parseInt(times[2]);
        
        
        
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
        if(hours==2){
            AMPM = "pm";
        }
        time = hours+":"+minutes+AMPM;
        if(minutes<10)
        {
            time = hours+":0"+minutes+AMPM;
        }
        
        //System.out.println(time);
        return time;
    }
    
    /**
     * Calculates time intervals for train schedules, then outputs them
     * @param time Starting time of train
     * @param increment Increment of time to be added
     * @return 
     */
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
        time = hours+"."+minutes+"."+seconds;
        return time;
    }
    
    /**
     * Updates table of active trains
     */
    public static void displayCurrentTrains(){
        
        int length = trainList.size();
        Object[][] data = new Object[6][10];
        Object[] columnNames={"TRAIN ID", "TRAIN LINE", "TRACK SECTION", "BLOCK", "NEXT STATION", "TIME OF ARRIVAL", "AUTHORITY", "CURRENT SPEED", "SUGGESTED SPEED", "PASSENGERS"};
        int i=0,j=0;
        mboGui.TrainDropdown.removeAllItems();
        
        for(i=0;i<length;i++){
           
           mboGui.TrainDropdown.addItem(trainList.get(i).getTrainId());
           for(j=0;j<10;j++){
                if(j==0){
                  data[i][0]= trainList.get(i).getTrainId(); 
                  
                }
                else{
                    data[i][j]=dummyData[i][j];
                }
           }
        }
       
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        mboGui.trainTable.setModel(model);
        model.fireTableDataChanged();
        mboGui.trainTable.repaint();
    }
    
    /**
     * Updates train ID label and speeds in variance panel
     */
    public void updateTrainID(){
        //if(trains.size()>0){
        //String newID = trains.get(newIdIndex);
        String newID = String.valueOf(mboGui.TrainDropdown.getSelectedItem());
        if(newID.compareTo("100")==0){
            mboGui.CurrentSpeedValue.setText("30 mph");
            mboGui.SuggestedSpeedValue.setText("35 mph");
            mboGui.DifferenceValue.setText("(+5 mph)");
        }
        else{
            mboGui.CurrentSpeedValue.setText("40 mph");
            mboGui.SuggestedSpeedValue.setText("30 mph");
            mboGui.DifferenceValue.setText("(-10 mph)");
        }
        //System.out.println(newID);
        mboGui.TrainIdValue.setText(newID);
    //}
    }
    
    /**
     * Deploys next available train
     * @throws IOException
     * @throws InvalidFormatException 
     */
   public void dispatch() throws IOException, InvalidFormatException{
       File file = new File("src\\com\\rogueone\\assets\\schedule.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet redSchedule = workbook.getSheetAt(1);
        Row currRow = redSchedule.getRow(trainIndex+1);
        String trainID = currRow.getCell(0).toString();
        String[] IDs = trainID.split("\\.");
        MboTrain newTrain = new MboTrain();
        newTrain.setTrainId(IDs[0]);
        trainList.add(newTrain);
        trainIndex=trainIndex+1;
         
        displayCurrentTrains();
        
   }  
  
    
    
    /**
     * Updates speed and authority in present trains display
     */
    public void updateSpeed(){
        String tempSpeed = mboGui.MboSuggestedSpeedField.getText();
        String tempAuthority = mboGui.MboSuggestedAuthorityField.getText();
        String newID = String.valueOf(mboGui.TrainDropdown.getSelectedItem());
        if(newID.compareTo("100")==0){
            dummyData[0][8] = tempSpeed+" mph";
            dummyData[0][6] = tempAuthority+" ft";
        }
        else{
            dummyData[1][8] = tempSpeed+" mph";
            dummyData[1][6] = tempAuthority+" ft";
        }
        displayCurrentTrains();
    }
    
    public void changeToMovingBlock(){
        
    }
    public void changeToFixedBlock(){
        for(int i = 0; i < trainList.size(); i++){
            
        }
    }
    
    public static void main(String[] args) throws IOException, InvalidFormatException{
        //ArrayList<String> trains = new ArrayList<String>();
        //MovingBlockGUI mboGui = new MovingBlockGUI();
        
        
    }
}