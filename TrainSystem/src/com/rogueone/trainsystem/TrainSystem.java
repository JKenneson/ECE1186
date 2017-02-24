/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trainsystem;

import com.rogueone.ctc.gui.CommandTrackControlGUI;
import com.rogueone.global.Global;
import com.rogueone.mainframe.*;
import com.rogueone.mbo.Mbo;
import com.rogueone.trackcon.TrackControllerHandler;
import com.rogueone.trackmodel.TrackModel;
import com.rogueone.trainmodel.TrainHandler;
import java.awt.BorderLayout;
import java.io.File;
import java.util.TimerTask;
import java.util.Timer;

/**
 *
 * @author kylemonto
 * @author Jonathan Kenneson
 */
public class TrainSystem {

    // Time utilities
    public static int timeToRefresh;
    public Timer timer;
    public Action task;
    
    // Modules
    private CommandTrackControlGUI ctc;
    private TrackControllerHandler trackControllerHandler;
    private TrackModel trackModel;
    private TrainHandler trainHandler;
    private Mbo mbo;
    
    // GUI
    private MainFrame mainFrame;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TrainSystem ts = new TrainSystem();
        ts.initializeTrainSystem();
    }

    public void initializeTrainSystem() {
        
        // Initialize clock
        timeToRefresh = 1000;
        this.timer = new Timer();
        this.task = new Action(this);
        timer.schedule(task, 0, timeToRefresh);
        
        // Initialize modules
        this.ctc = new CommandTrackControlGUI(this);
        this.trackControllerHandler = new TrackControllerHandler(this);
        this.trackModel = new TrackModel(this, new File("src/com/rogueone/assets/TrackData.xlsx"));
        this.trainHandler = new TrainHandler(this);
        this.mbo = new Mbo(this);
        
        // Initialize GUI
        mainFrame = new MainFrame(this);
        mainFrame.setLayout(new BorderLayout());
        mainFrame.getContentPane().add(ctc, BorderLayout.CENTER);
        mainFrame.setVisible(true);
          
        //dispatchTrain(25, 5000, 1, "GREEN");
    }
    
    void updateTrainSystem() {
        //this.trainHandler.updateTrains();
        //this.trackModel.updateGUI();
    }

    public void updateTimer(int timeToRefresh) {
        this.task.cancel();
        this.timeToRefresh = timeToRefresh;
        this.task = new Action(this);
        this.timer.schedule(this.task, 0, this.timeToRefresh);
    }
    
    public void dispatchTrain(int speed, int authortiy, int numCars, String line) {
        trainHandler.dispatchNewTrain(speed, authortiy, numCars, line);  
    }
    
    // GETTERS
    public CommandTrackControlGUI getCTC() {
        return ctc;
    }
    
    public TrackControllerHandler getTrackControllerHandler() {
        return trackControllerHandler;
    }
    
    public TrackModel getTrackModel() {
        return trackModel;
    }
    
    public TrainHandler getTrainHandler() {
        return trainHandler;
    }
    
    public Mbo getMBO() {
        return mbo;
    }
}

class Action extends TimerTask {

    private long last;
    TrainSystem trainSystem;

    public Action(TrainSystem trainSystem) {
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
