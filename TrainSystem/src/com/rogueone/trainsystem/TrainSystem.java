/**
 * The train system is the driving force behind the entire train operation
 * All modules are created here and can interact with each other from this class.
 * The modules are also called to update from this class depending on the speed of the timer
 *
 * @authors Jonathan Kenneson, Kyle Monto, Dan Bednarczyk, Tyler Protivnak, Brian Stevenson, Rob Goldshear
 * @Creation 2/5/17
 * @Modification 3/24/17
 */
package com.rogueone.trainsystem;

import com.rogueone.ctc.gui.CommandTrackControlGUI;
import com.rogueone.global.Clock;
import com.rogueone.global.Global;
import com.rogueone.mainframe.*;
import com.rogueone.mbo.Mbo;
import com.rogueone.mbo.Scheduler;
import com.rogueone.trackcon.TrackControllerHandler;
import com.rogueone.trackmodel.TrackModel;
import com.rogueone.trackview.TrackView;
import com.rogueone.trainmodel.TrainHandler;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;
import java.util.Timer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 *
 * @author kylemonto
 * @author Jonathan Kenneson
 */
public class TrainSystem {


    // Time utilities
    private Timer timer;
    private SystemTimer task;
    private Clock clock;
    private int stationBufferCount = 0;
    private static final int STATION_BUFFER_CONSTANT = 10;
    public static final int NORMAL_TIME = 1000;
    public static final int x10_TIME = 100;
    public static final int x25_TIME = 45;
    public static final int x50_TIME = 20;
    public static final int x100_TIME = 10;
    
    // Modules
    private CommandTrackControlGUI ctc;
    private TrackControllerHandler trackControllerHandler;
    private TrackModel trackModel;
    private TrainHandler trainHandler;
    private static Mbo mbo;
    private Scheduler scheduler;
    private TrackView trackViewGreen;
    private TrackView trackViewRed;
    //private Schedule schedule;
    
    // GUI
    private MainFrame mainFrame;
    
    //OTHER
    private static final String trackDataFilePath = "src/com/rogueone/assets/TrackData.xlsx";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InvalidFormatException {
        TrainSystem ts = new TrainSystem();
        ts.initializeTrainSystem();
    }

    /**
     * Initialize an instance of a TrainSystem.
     */
    public void initializeTrainSystem() throws IOException, InvalidFormatException  {
        
        //Initialize the clock
        this.clock = new Clock();
        
        // Initialize modules
        this.trackModel = new TrackModel(this, new File(trackDataFilePath));
        this.trackModel.updateGUI();
        this.trackControllerHandler = new TrackControllerHandler(this);
        this.trainHandler = new TrainHandler(this);
        this.mbo = new Mbo(this);
        this.scheduler = new Scheduler(this);
        this.trackViewGreen = new TrackView(this, Global.Line.GREEN);
        this.trackViewRed = new TrackView(this, Global.Line.RED);
        this.ctc = new CommandTrackControlGUI(this);

        
        // Initialize GUI
        mainFrame = new MainFrame(this);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().add(ctc, BorderLayout.CENTER);
        mainFrame.setVisible(true);
        
        //initial run for track Controller tables
        this.trackControllerHandler.updateTrack(Global.Line.GREEN);
        this.trackControllerHandler.updateTrack(Global.Line.RED);
          
        // Initialize timer and scheduler
        this.timer = new Timer();
        this.task = new SystemTimer(this);
        timer.schedule(this.task, 0, NORMAL_TIME);
        
        //dispatchTrain(45, 40000, 1, "GREEN", 1);
    }
    
    // UPDATES
    void updateTrainSystem() {
        this.clock.updateClock();               //Increment the clock
        this.mainFrame.timeLabel.setText(this.clock.printClock() + "     ");
        
        this.trainHandler.updateTrains();
        if(stationBufferCount == STATION_BUFFER_CONSTANT) {
            this.trackModel.updateStationEnvironments();
            stationBufferCount = 0;
        }
        else {
            stationBufferCount++;
        }
        this.ctc.updateGUI();
        this.trackControllerHandler.updateTrackView(Global.Line.GREEN);
        this.trackControllerHandler.updateTrackView(Global.Line.RED);
        this.mbo.update();
    }

    public void updateTimer(int timeToRefresh) {
        this.task.cancel();
        this.task = new SystemTimer(this);
        this.timer.schedule(this.task, 0, timeToRefresh);
    }
    
    // TESTING
    public void dispatchTrain(int speed, int authortiy, int numCars, String line, int trainID) {
        trainHandler.dispatchNewTrain(speed, authortiy, numCars, line, trainID);  
    }
    
    // GETTERS
    
    /**
     * Get shared CTC object.
     * @return CommandTrackControlGUI The global CTC object
     */
    public CommandTrackControlGUI getCTC() {
        return ctc;
    }
    
    /**
     * Get shared TrackControllerHandler object.
     * @return TrackControllerHandler The global TrackControllerHandler object
     */
    public TrackControllerHandler getTrackControllerHandler() {
        return trackControllerHandler;
    }
    
    /**
     * Get shared TrackModel object.
     * @return TrackModel The global TrackModel object
     */
    public TrackModel getTrackModel() {
        return trackModel;
    }
    
    /**
     * Get shared TrainHandler object.
     * @return TrainHandler The global TrainHandler object
     */
    public TrainHandler getTrainHandler() {
        return trainHandler;
    }
    
    /**
     * Get shared MBO object.
     * @return Mbo The global MBO object
     */
    public static Mbo getMBO() {
        return mbo;
    }
    
    /**
     * Get shared MBO object.
     * @return Scheduler The global scheduler object
     */
    public Scheduler getScheduler() {
        return scheduler;
    }

    /**
     * Get Shared TrackViewGreen object
     * @return  TrackView global object
     */
    public TrackView getTrackViewGreen() {
        return trackViewGreen;
    }
    
    /**
     * Get Shared TrackViewRed object
     * @return  TrackView global object
     */
    public TrackView getTrackViewRed() {
        return trackViewRed;
    }
    
    
    
    /**
     * Getter for ref to global clock
     * @return clock reference
     */
    public Clock getClock() {
        return clock;
    }
}

class SystemTimer extends TimerTask {

    private long last;
    TrainSystem trainSystem;

    public SystemTimer(TrainSystem trainSystem) {
        this.trainSystem = trainSystem;
    }

    public void run() {
        last = scheduledExecutionTime();
        //System.out.println("tick last = " + last);
        this.trainSystem.updateTrainSystem();
    }

    public long getLast() {
        return last;
    }
}
