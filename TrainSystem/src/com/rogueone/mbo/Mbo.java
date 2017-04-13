/**
 * The MovingF Block Overlay (MBO) serves as an autonomous dispatcher for the train.
 * The MBO also contains the scheduler, which generated weekly schedules for employees, as well as
 * daily schedules for trains.
 *
 * @author Brian Stevenson
 * @creation date 2/7/17
 * @modification date 4/13/17
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
import com.rogueone.trackmodel.TrackPiece;
import com.rogueone.traincon.GPSMessage;
import com.rogueone.trainmodel.TrainModel;
import com.rogueone.trainsystem.TrainSystem;
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
   private static File file = new File("src/com/rogueone/assets/schedule.xlsx");
   private static int trainIndex;
   private ArrayList<TrainModel> prevTrains = new ArrayList<>();
  //private static String[] dummyDataRed = {"100","Red","U","77","SHADYSIDE","6:04am","164ft","10mph","35mph","0","0"};
   //private static String[] dummyDataGreen = {"101","Green","YY","152","PIONEER","6:04am","164ft","12mph","35mph","0","0"};
   //private static String[][] dummyData = {dummyDataRed, dummyDataGreen};
   private TrainSystem trainSystem;
   private String mode = "Fixed Block";
   private String opMode = "Manual";
   
   public Mbo(TrainSystem ts) throws IOException, InvalidFormatException {
       trainSystem = ts;
       //System.out.println("Reading Schedules");
        //Scheduler.readPersonnelSchedule(file);
        mboGui = new MovingBlockGUI(trainSystem);
   }
    
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
     * Updates table of active trains
     */
    public void displayCurrentTrains(){
        
        //int length = trainList.size();
        int length = trainSystem.getTrainHandler().getTrains().size();
        Object[][] data = new Object[6][10];
        Object[] columnNames={"TRAIN ID", "TRAIN LINE", "TRACK SECTION", "BLOCK", "NEXT STATION", "TIME OF ARRIVAL", "AUTHORITY", "CURRENT SPEED", "SUGGESTED SPEED", "PASSENGERS"};
        int i=0,j=0;
        //mboGui.TrainDropdown.removeAllItems();
        
        for(i=0;i<length;i++){
            
           int tid = trainSystem.getTrainHandler().getTrains().get(i).trainID;
           String temp = String.valueOf(tid);
           //mboGui.TrainDropdown.addItem(temp);
                 // data[i][0]= trainList.get(i).getTrainId(); 
                  //data[i][1]= trainList.get(i).getPosition();
                  //data[i][2]= trainList.get(i).getPosition();
                  //data[i][3] = trainList.get(i).getPosition();
                  //data[i][4] = trainList.get(i).NEXTSTATOIN
                  //data[i][5] = TIMEOFARRIVAL
                  //data[i][6] = trainList.get(i).getAuthority();
                  //data[i][7] = trainList.get(i).getCurrSpeed();
                  //data[i][8] = trainList.get(i).getSuggestedSpeed();
                  //data[i][9] = trainList.get(i).getPassengers();
           
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
        //String newID = String.valueOf(mboGui.TrainDropdown.getSelectedItem());
        //if(newID.compareTo("100")==0){
            //mboGui.CurrentSpeedValue.setText("30 mph");
            //mboGui.SuggestedSpeedValue.setText("35 mph");
           // mboGui.DifferenceValue.setText("(+5 mph)");
       // }
        //else{
           // mboGui.CurrentSpeedValue.setText("40 mph");
           // mboGui.SuggestedSpeedValue.setText("30 mph");
            //mboGui.DifferenceValue.setText("(-10 mph)");
        //}
        //System.out.println(newID);
        //mboGui.TrainIdValue.setText(newID);
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
        /////FIX THIS ///////
        //newTrain.setTrainId(IDs[0]);
        trainList.add(newTrain);
        trainIndex=trainIndex+1;
         
        displayCurrentTrains();
        
   }  
  
    
    
    /**
     * Updates speed and authority in present trains display
     */
    public void updateSpeed(){
        trainSystem.getTrainHandler().getTrains().get(1).getCurrBlock().getSpeedLimit();
        //String tempSpeed = mboGui.MboSuggestedSpeedField.getText();
       // String tempAuthority = mboGui.MboSuggestedAuthorityField.getText();
       // String newID = String.valueOf(mboGui.TrainDropdown.getSelectedItem());
        if(mode.equals("Moving Block")){
            for(int i=0;i<trainList.size();i++){
            
            }
        }
        else if(mode.equals("Fixed Block")){
           for(int i=0;i<trainList.size();i++){
               //double speedLimit = trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSpeedLimit();
               //trainList.get(i).setSpeed(speedLimit);
               //trainList.get(i).setAuthority(90000);
               //trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getNext(trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock());
            } 
        }
        
        //if(newID.compareTo("100")==0){
            //dummyData[0][8] = tempSpeed+" mph";
            //dummyData[0][6] = tempAuthority+" ft";
        //}
        //else{
            //dummyData[1][8] = tempSpeed+" mph";
            //dummyData[1][6] = tempAuthority+" ft";
        //}
        displayCurrentTrains();
    }
    
    public void changeToMovingBlock(){
        mode = "Moving Block";
    }
    public void changeToFixedBlock(){
        for(int i = 0; i < trainList.size(); i++){
            
        }
        mode = "Fixed Block";
    }
    
    public void update(){
        double cumulativeDist = 0;
        updateTrains();
        //updateSpeed();
        mboGui.update();
            //trainList = trainSystem.getTrainHandler().getTrains();
            int numTrains = trainSystem.getTrainHandler().getTrains().size();
        //trainList.clear();
            if(mode.equals("Moving Block")){
                
                //System.out.println("MOVING BLOCK");
                ///TYPING ISSUES OF PREVBLOCK
                //Block prevBlk = trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getPortA().getType();
                //Block blk = (Block)trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getNext(prevBlk);
                
//                //trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().
//                //trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getNext(previous);
//                PresenceBlock lookaheadBlock = new PresenceBlock(trainSystem, line);
//                lookaheadBlock.getNextBlock();
//                Block a = trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock();
//                TrackPiece b = a.getNext(a);
//                //if(b.getType()==Global.PieceType.BLOCK)
//                {
//                    //Block c = (Block)b;
//                    
//                }
                //new speed and authority
                //trainSystem.getTrainHandler().getTrains().get(1).MBOUpdateSpeedAndAuthority(, );
                ArrayList<TrainModel> trains = trainSystem.getTrainHandler().getTrains();
                //prevTrains = trains;
                for(int i =0; i<numTrains;i++){
                    
                    //trainSystem.getTrainHandler().getTrains().get(i).MBOUpdateSpeedAndAuthority(20, 3000);
                    
                    
                
                TrainModel currTrain = trains.get(i);
                GPSMessage message = currTrain.requestGPSMessage();
                Block currBlock = message.getCurrBlock();
                //if(prevTrains.size()==numTrains){
                    
                //if(prevTrains.get(i)!=null){
                    //if(prevTrains.get(i).getCurrBlock().toString().equals(currBlock.toString())){
                        //prevTrains.set(i, currTrain);
                        cumulativeDist = trains.get(i).getDistanceTraveledFeet();
                    TrackPiece next = currBlock.getNext(trains.get(i).getCurrBlock().getPortA());
                    double addDist = 0;
                    while(next.getType()==PieceType.BLOCK){
                        //System.out.println("CUMULATIVE DIST: "+cumulativeDist);
                        Block nextBlock = (Block)next;
                        Boolean occupied = nextBlock.isOccupied();
                        if(occupied == true){
                            System.out.println("occupied");
                            for(int k = 0; k < numTrains; k++){
                                if(trains.get(k).getCurrBlock().toString().equals( nextBlock.toString())){
                                    addDist = trains.get(k).getDistanceTraveledFeet();
                                    break;
                                }
                            }
                            cumulativeDist = cumulativeDist + addDist;
                        }
                        else{
                            cumulativeDist = cumulativeDist + nextBlock.getLength();
                            if(cumulativeDist > message.getStoppingDistance()){
                                break;
                            }
                        }
                        
                        
                        next = nextBlock.getNext(nextBlock.getPortA());
                    }
                    
                    if(cumulativeDist<=message.getStoppingDistance()){
                            //System.out.println("Reccomended: "+ cumulativeDist+" Actual: "+message.getStoppingDistance());
                            currTrain.MBOUpdateSpeedAndAuthority(0, 0);
                            break;
                        }
                        else{
                            int speedLimit = (int)currBlock.getSpeedLimit();
                            int authortiy = (int)currTrain.getAuthority();
                            //System.out.println("Speedlimit: "+ speedLimit+" Authortiity: "+authortiy);
                            currTrain.MBOUpdateSpeedAndAuthority(speedLimit, authortiy);
                        }
                    //cumulativeDist = 0;
                    
                    if(next.getType()==PieceType.BLOCK){
                        Block nextBlock = (Block)next;
                        Boolean occupied = nextBlock.isOccupied();
                        //System.out.println(occupied);
                    }
                //}
                   // }
                    //else{
                    //prevTrains.set(i, currTrain);
                   // }
                //}
                //else{
                    //TrainModel temp = null;
                    //System.out.println("SETTING TRAIN ARRAY");
                    //int difference = trains.size() - prevTrains.size();
                    //for(int j = 0; j<difference; j++){
                    //    prevTrains.add(temp);
                    //}
                    
                    
                }
                //}
                
               //prevTrains = trains;
            }
        
    }
    
    public void updateTrains(){
        String[] columnNames = {"TRAIN ID","TRAIN LINE","SECTION","BLOCK","NEXT STATION","AUTHORITY","CURRENT SPEED","SUGGESTED SPEED","VARIANCE","PASSENGERS"};
        int numTrains = trainSystem.getTrainHandler().getTrains().size();
        Object[][] data = new Object[numTrains][11];
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
            data[i][2]=trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSection().toString();
            data[i][3]=trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().toString();
            data[i][4]=trainSystem.getTrainHandler().getTrains().get(i).getApproachingStation();
            data[i][5]=trainSystem.getTrainHandler().getTrains().get(i).getAuthority();
            data[i][6]=trainSystem.getTrainHandler().getTrains().get(i).getCurrSpeed();
            data[i][7]=trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSpeedLimit();
            data[i][8] = trainSystem.getTrainHandler().getTrains().get(i).getCurrBlock().getSpeedLimit() - trainSystem.getTrainHandler().getTrains().get(i).getCurrSpeed();
            data[i][9]=trainSystem.getTrainHandler().getTrains().get(i).getPassengersOnBaord();
        }
        DefaultTableModel table = new DefaultTableModel(data, columnNames);
        mboGui.trainTable.setModel(table);
    }
    
    public String getMode(){
        return mode;
    }
    
    public static void main(String[] args) throws IOException, InvalidFormatException{
        //ArrayList<String> trains = new ArrayList<String>();
        //MovingBlockGUI mboGui = new MovingBlockGUI();
        
        
    }
    
}