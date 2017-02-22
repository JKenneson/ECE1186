/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trainsystem;

import com.rogueone.global.Global;
import com.rogueone.mainframe.*;
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

    public static int timeToRefresh;
    public TrackModel trackModel;
    public TrainHandler trainHandler;
    public Timer timer;
    public Action task;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Create a new MainFrame and an interface selector
        TrainSystem ts = new TrainSystem();
        ts.initializeTrainSystem();

    }


    public void initializeTrainSystem() {
        

        timeToRefresh = 1000;

        this.trackModel = new TrackModel(new File("src/com/rogueone/assets/TrackData.xlsx"));
        this.trainHandler = new TrainHandler(this);
        this.timer = new Timer();
        this.task = new Action(this);
        timer.schedule(task, 0, timeToRefresh);
        // this is where the user request a new interval, 2 sec.
        
        MainFrame mf = new MainFrame(this);
        mf.setLayout(new BorderLayout());

        InterfaceSelector is = new InterfaceSelector(mf);
        mf.getContentPane().add(is, BorderLayout.CENTER);
        mf.setVisible(true);
        
        
        
        dispatchTrain(25, 5000, 1, "GREEN");
    }

    public TrackModel getTrackModel() {
        return trackModel;
    }

    public void dispatchTrain(int speed, int authortiy, int numCars, String line) {
        trainHandler.dispatchNewTrain(speed, authortiy, numCars, line);
        
    }

    /**
     * This method gets called every second or 10th of a second
     */
    void updateTrainSystem() {
        this.trainHandler.updateTrains();
        //this.trackModel.updateGUI();
    }

    public void updateTimer(int timeToRefresh) {
        this.task.cancel();
        this.timeToRefresh = timeToRefresh;
        this.task = new Action(this);
        this.timer.schedule(this.task, 0, this.timeToRefresh);
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
