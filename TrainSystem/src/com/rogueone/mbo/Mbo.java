/**
 * The MovingF Block Overlay (MBO) serves as an autonomous dispatcher for the train.
 * The MBO also contains the scheduler, which generated weekly schedules for employees, as well as
 * daily schedules for trains
 *
 * @author Brian Stevenson
 * @creation date 2/7/17
 * @modification date 4/17/17
 */
package com.rogueone.mbo;
import com.rogueone.global.Global;
import com.rogueone.global.Global.PieceType;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import java.util.ArrayList;
import java.io.IOException;
import com.rogueone.mbo.gui.MovingBlockGUI;
import com.rogueone.trackcon.entities.PresenceBlock;
import com.rogueone.trackmodel.Block;
import com.rogueone.trackmodel.Section;
import com.rogueone.trackmodel.Station;
import com.rogueone.trackmodel.TrackPiece;
import com.rogueone.traincon.GPSMessage;
import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trainsystem.TrainSystem;
import java.text.DecimalFormat;
import javax.swing.JTable;
import javax.swing.table.*;

/**
 * Main class for the MBO module
 * @author Brian Stevenson
 */
public class Mbo{
    
    Global.Line line;
     private static ArrayList<MboTrain> trainList = new ArrayList<MboTrain>();
    public static MovingBlockGUI mboGui;
   private static File file = new File("src/com/rogueone/assets/altSchedule.xlsx");
   private static int trainIndex;
   private ArrayList<TrainModel> prevTrains = new ArrayList<>();
   private TrainSystem trainSystem;
   private String mode = "Fixed Block";
   private String opMode = "Manual";
   private ArrayList<TrainModel> sameTrains = new ArrayList();
   private double movingBlockSpeed = 0;
   private double fixedBlockSpeed = 0;
   
   /**
    * Constructor for MBO class
    * @param ts Global trainSystem
    * @author Brian Stevenson
    */
   public Mbo(TrainSystem ts) throws IOException, InvalidFormatException {
       trainSystem = ts;
       //System.out.println("Reading Schedules");
        //Scheduler.readPersonnelSchedule(file);
        mboGui = new MovingBlockGUI(trainSystem);
   }
    
   /**
    * Accesses the GUI attached to the MBO
    * @return MovingBlockGUI Gui
    * @author Brian Stevenson
    */
   public MovingBlockGUI getGUI() throws IOException, InvalidFormatException{
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
  

    
    /**
     * Converts time from 12.10.55 format to 12:10:55 format
     * @param time Time value to be converted
     * @return returns time in proper format
     * @author Brian Stevenson
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
     * @return incremented time
     * @author Brian Stevenson
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
        
        if(seconds>=60)
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
     * Updates GUI table with active train information
     * @author Brian Stevenson
     */
    public void displayCurrentTrains(){
        
        //int length = trainList.size();
        int length = trainSystem.getTrainHandler().getTrains().size();
        Object[][] data = new Object[6][10];
        Object[] columnNames={"TRAIN ID", "TRAIN LINE", "TRACK SECTION", "BLOCK", "NEXT STATION", "TIME OF ARRIVAL", "AUTHORITY", "CURRENT SPEED", "SUGGESTED SPEED", "PASSENGERS","DISTANCE INTO BLOCK(ft)"};
        int i=0,j=0;
        
        for(i=0;i<length;i++){
            
           int tid = trainSystem.getTrainHandler().getTrains().get(i).trainID;
           String temp = String.valueOf(tid);
           
        }
       
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        mboGui.trainTable.setModel(model);
        model.fireTableDataChanged();
        mboGui.trainTable.repaint();
    }
    
    /**
     * Changes the automatic system to Moving Block Mode
     * @author Brian Stevenson
     */
    public void changeToMovingBlock(){
        mode = "Moving Block";
    }
    
    /**
     * Changes the automatic system to Fixed Block mode
     * @author Brian Stevenson
     */
    public void changeToFixedBlock(){
        for(int i = 0; i < trainList.size(); i++){
            
        }
        mode = "Fixed Block";
    }
    
    /**
     * Updates GUI information on each clock tick
     * @author Brian Stevenson
     */
    public void update(){
        ArrayList<TrainModel> stoppedTrains = new ArrayList();
        updateTrains();
        mboGui.update();
            int numTrains = trainSystem.getTrainHandler().getTrains().size();
            for(int i =0; i<numTrains;i++){  
                double cumulativeDist = 250;
                ArrayList<TrainModel> trains = trainSystem.getTrainHandler().getTrains();
                ArrayList<TrainModel> visitDormont = new ArrayList();
                ArrayList<TrainModel> visitOverbrook = new ArrayList();
                ArrayList<TrainModel> visitGlenbury = new ArrayList();
                ArrayList<TrainModel> visitInglewood = new ArrayList();
                ArrayList<TrainModel> visitCentral = new ArrayList();
                TrainModel currTrain = trains.get(i);
                GPSMessage message = currTrain.requestGPSMessage();
                Block currBlock = message.getCurrBlock();
                Object[][] newData;
                String[] useArr;
                int index = 3;
                int width;
                int currId = currTrain.trainID;
                if(currTrain.getCurrSpeed()==0){
                    stoppedTrains.add(currTrain);
                }
                if(currBlock.getStation()!=null && currTrain.getCurrSpeed() == 0){
                        Station currStation = currBlock.getStation();
                        String name = currStation.getName();
                        if(currBlock.getLine().toString().equals("RED")){
                            newData = trainSystem.getScheduler().getRedData();
                            width = 11;
                            useArr = trainSystem.getScheduler().redIncrements;
                        }
                        else{
                             newData = trainSystem.getScheduler().getGreenData();
                             width = 21;
                             useArr = trainSystem.getScheduler().greenIncrements;
                        }
                        if(name.equals("SHADYSIDE")||name.equals("PIONEER"))
                        {
                            index = 3;
                        }
                        else if(name.equals("HERRON AVE")||name.equals("EDGEBROOK")){
                            index = 4;
                        }
                        else if(name.equals("PITT")||name.equals("SWISSVALE")){
                            index = 5;
                        }
                        else if(name.equals("PENN STATION")||name.equals("WHITED")){
                            index = 6;
                        }
                        else if(name.equals("STEEL PLAZA")||name.equals("SOUTH BANK")){
                            index = 7;
                        }
                        else if(name.equals("FIRST AVE")){
                            index = 8;
                        }
                        else if(name.equals("CENTRAL")){
                            int check =0;
                            for(int j=0;j<visitCentral.size();j++){
                                if(visitDormont.get(j).trainID==currId)
                                {
                                    index = 20;
                                    visitCentral.remove(currTrain);
                                    check=1;
                                    break;
                                }
                                else{
                                    index = 8;
                                }
                            }
                            if(check==0){
                                visitCentral.add(currTrain);
                            }
                        }
                        else if(name.equals("STATION SQUARE")){
                            index = 9;
                        }
                        else if(name.equals("INGLEWOOD")){
                            int check =0;
                            for(int j=0;j<visitInglewood.size();j++){
                                if(visitDormont.get(j).trainID==currId)
                                {
                                    index = 19;
                                    visitInglewood.remove(currTrain);
                                    check=1;
                                    break;
                                }
                                else{
                                    index = 9;
                                }
                            }
                            if(check==0){
                                visitInglewood.add(currTrain);
                            }
                        }
                        else if(name.equals("SOUTH HILLS JUNCTION")){
                            index = 10;
                        }
                        else if(name.equals("OVERBROOK")){
                            int check =0;
                            for(int j=0;j<visitOverbrook.size();j++){
                                if(visitDormont.get(j).trainID==currId)
                                {
                                    index = 18;
                                    visitOverbrook.remove(currTrain);
                                    check=1;
                                    break;
                                }
                                else{
                                    index = 10;
                                }
                            }
                            if(check==0){
                                visitOverbrook.add(currTrain);
                            }
                        }
                        else if(name.equals("GLENBURY")){
                            int check =0;
                            for(int j=0;j<visitGlenbury.size();j++){
                                if(visitDormont.get(j).trainID==currId)
                                {
                                    index = 17;
                                    visitGlenbury.remove(currTrain);
                                    check=1;
                                    break;
                                }
                                else{
                                    index = 11;
                                }
                            }
                            if(check==0){
                                visitGlenbury.add(currTrain);
                            }
                        }
                        else if(name.equals("DORMONT")){
                            int check =0;
                            for(int j=0;j<visitDormont.size();j++){
                                if(visitDormont.get(j).trainID==currId)
                                {
                                    index = 16;
                                    visitDormont.remove(currTrain);
                                    check=1;
                                    break;
                                }
                                else{
                                    index = 12;
                                }
                            }
                            if(check==0){
                                visitDormont.add(currTrain);
                            }
                            
                        }
                        else if(name.equals("MT LEBANON")){
                            index =13;
                        }
                        else if(name.equals("POPLAR")){
                            index = 14;
                        }
                        else if(name.equals("CASTLE SHANNON")){
                            index = 15;
                        }
                        //HERE
                        else if(name.equals("CENTRAL")){
                            index = 20;
                        }
                        int hr = trainSystem.getClock().getHour();
                        int min = trainSystem.getClock().getMinute();
                        int sec = trainSystem.getClock().getSecond();
                        
                            int id = currTrain.trainID/2 +1;
                       
                        String currTime = "";
                        for(int m =index;m<width;m++){
                            
                            
                            if(m-index==0){
                                currTime = hr+"."+min+"."+sec;
                                newData[id][m] = convertTime(currTime);
                                //System.out.println(currTime);
                            }
                            else{
                                currTime = incrementTime(currTime, useArr[m-3]);
                                newData[id][m] = convertTime(currTime);
                            }
                        }
                        
                        if(currBlock.getLine().toString().equals("RED")){
                            trainSystem.getScheduler().setRedData(newData);
                        }
                        else{
                             trainSystem.getScheduler().setGreenData(newData);
                        }
                        
                    }
                TrackPiece next = currBlock.getNext(trains.get(i).getCurrBlock().getPortA());
                TrackPiece lookahead = null;
                TrackPiece tempPrev = null;
                int count = 0;
           // if(mode.equals("Moving Block")){
                
                //currTrain.MBOUpdateSpeedAndAuthority(10, 9000);
                //System.out.println("ID: "+currTrain.trainID);
                cumulativeDist = cumulativeDist + message.getDistanceIntoBlock();
                lookahead = currBlock;
                Block temp = (Block)lookahead;
                System.out.println("START");
                for(int n = 0; n< 3; n++){
                    cumulativeDist = 0;
                    temp = (Block)lookahead;
                    if(lookahead.getNext(temp.getPortA())!=null){
                        if(lookahead.getNext(temp.getPortA()).getType()==PieceType.BLOCK){
                            
                            lookahead = lookahead.getNext(temp.getPortA());
                            temp = (Block)lookahead;
                            
                            System.out.println(temp.getSection()+":"+temp.getID()+" Occupied: "+temp.isOccupied());
                            //System.out.println(temp.getLine()+" "+temp.isOccupied());
                            boolean occupied = temp.isOccupied();
                            if(currBlock.getID()==temp.getID()){
                                occupied = false;
                            }
                            
                            if(occupied && !temp.getSection().toString().equals("YY") && (!temp.getSection().toString().equals("U")&&temp.getID()!=77) && (!temp.getSection().toString().equals("J")&&temp.getID()!=62)){//&& (!temp.getSection().toString().equals("M")&&temp.getID()!=76)&& (!temp.getSection().toString().equals("N")&&temp.getID()!=85)){
                               //cumulativeDist = cumulativeDist + temp.getLength();
                               //System.out.println(temp.getSection()+":"+temp.getID());
                                System.out.println("Distance available: "+cumulativeDist+" Stopping dist: "+message.getStoppingDistance());
                                //if(cumulativeDist < message.getStoppingDistance()-100){
                                    movingBlockSpeed = 0;
                                //fixedBlockSpeed = 0;
                                    if(mode.equals("Moving Block")){
                                    currTrain.MBOUpdateSpeedAndAuthority(-1, 0); 
                                    }
                                    System.out.println("STOPPING");
                                    //break; 
                                //}
                            }
                            
                            else{
                                //currTrain.MBOUpdateSpeedAndAuthority((int)message.getCurrSpeed(), (int)currTrain.getAuthority());
                                cumulativeDist = cumulativeDist + temp.getLength();
                                movingBlockSpeed = currTrain.requestGPSMessage().getCurrBlock().getSpeedLimit();
                                //fixedBlockSpeed = currBlock.getSpeedLimit();
                                if(mode.equals("Moving Block")&&message.getCurrSpeed()==0){
                                currTrain.MBOUpdateSpeedAndAuthority((int)currBlock.getSpeedLimit(), 90000); 
                                }
                                sameTrains.clear();
                                int found = 0;
                                for(int p = 0; p<numTrains; p++){
                                    TrainModel testTrain = trainSystem.getTrainHandler().getTrains().get(p);
                                    if(currTrain.requestGPSMessage().getCurrBlock() == testTrain.requestGPSMessage().getCurrBlock() && currTrain.requestGPSMessage().getLine().equals(testTrain.requestGPSMessage().getLine())){
                                        for(int r = 0; r<sameTrains.size();r++){
                                            if(currTrain.trainID==sameTrains.get(r).trainID){
                                                found = 1;
                                                break;
                                            }
                                        }
                                        if(found==0){
                                            sameTrains.add(testTrain);
                                        }
                                        
                                    }
                                    
                                }
                                
                                for(int q = 0; q< sameTrains.size();q++){
                                    //sameTrains.get(q).MBOUpdateSpeedAndAuthority((int)currBlock.getSpeedLimit(), (int)currTrain.getAuthority());
                                    //System.out.println(sameTrains.get(q).trainID);
                                    if(q==0){
                                        //sameTrains.get(q).MBOUpdateSpeedAndAuthority((int)currBlock.getSpeedLimit(), (int)currTrain.getAuthority());
                                    }
                                    else{
                                        //sameTrains.get(q).MBOUpdateSpeedAndAuthority(-1, (int)currTrain.getAuthority());
                                    }
                                }
                                
                            }
                        }
                        
                    }
                    
                }
            //}
            }
        
    }
    
    /**
     * Updates GUI train information table
     * @author Brian Stevenson
     */
    public void updateTrains(){
        DecimalFormat df = new DecimalFormat("#.##");
        String[] columnNames = {"TRAIN ID","TRAIN LINE","SECTION:BLOCK","NEXT STATION","AUTHORITY(ft)","CURR SPEED(mph)","SPEED LIMIT","VARIANCE","PASSENGERS","DIST INTO BLOCK(ft)"};
        int numTrains = trainSystem.getTrainHandler().getTrains().size();
        Object[][] data = new Object[numTrains][12];
        trainList.clear();
        for(int i =0;i<numTrains;i++){
            MboTrain tempTrain = new MboTrain();
            tempTrain.setTrainId(trainSystem.getTrainHandler().getTrains().get(i).trainID);
            tempTrain.setBlock(trainSystem.getTrainHandler().getBlockForTrain(trainSystem.getTrainHandler().getTrains().get(i).trainID));
            tempTrain.setPassengers(trainSystem.getTrainHandler().getTrains().get(i).getPassengersOnBaord());
            tempTrain.setCurrentSpeed(trainSystem.getTrainHandler().getTrains().get(i).getCurrSpeedMPH());
            trainList.add(tempTrain);
            trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getLine().toString();
            data[i][0]=trainSystem.getTrainHandler().getTrains().get(i).trainID;
            data[i][1]=trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getLine().toString();
            data[i][2]=trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSection().toString()+":"+trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().toString();
            data[i][3]=trainSystem.getTrainHandler().getTrains().get(i).getApproachingStation();
            data[i][4]=df.format(trainSystem.getTrainHandler().getTrains().get(i).getAuthority());
            data[i][5]=df.format(trainSystem.getTrainHandler().getTrains().get(i).getCurrSpeed());
            data[i][6]=df.format(trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSpeedLimit());
            //data[i][7] =df.format(trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSpeedLimit() - trainSystem.getTrainHandler().getTrains().get(i).getCurrSpeed());
            if(trainSystem.getTrainHandler().getTrains().get(i).requestGPSMessage().getCurrSpeed()==0){
               data[i][7] = 0; 
            }
            else{
            data[i][7] =df.format(trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSpeedLimit() - trainSystem.getTrainHandler().getTrains().get(i).getCurrSpeed());
            }
            data[i][8]=trainSystem.getTrainHandler().getTrains().get(i).getPassengersOnBaord();
            data[i][9]=df.format(trainSystem.getTrainHandler().getTrains().get(i).requestGPSMessage().getDistanceIntoBlock());
        }
        DefaultTableModel table = new DefaultTableModel(data, columnNames);
        mboGui.trainTable.setModel(table);
    }
    
    /**
     * Returns current mode the system is in(Moving or Fixed)
     * @return "Moving Block" or "Fixed Block"
     * @author Brian Stevenson
     */
    public String getMode(){
        return mode;
    }
    
    public static void main(String[] args) throws IOException, InvalidFormatException{
    }
    
}
