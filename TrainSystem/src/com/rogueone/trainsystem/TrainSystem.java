/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trainsystem;

import com.rogueone.mainframe.*;
import java.awt.BorderLayout;
import java.util.TimerTask;
import java.util.Timer;

/**
 *
 * @author kylemonto
 * @author Jonathan Kenneson
 */
public class TrainSystem {

    public static int timeToRefresh;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Create a new MainFrame and an interface selector
        MainFrame mf = new MainFrame();
        mf.setLayout(new BorderLayout());

        InterfaceSelector is = new InterfaceSelector(mf);
        mf.getContentPane().add(is, BorderLayout.CENTER);
        mf.setVisible(true);

        timeToRefresh = 1000;

        Action task = null;
        try {
            Timer timer = new Timer();
            task = new Action();
            timer.schedule(task, 0, timeToRefresh);
            Thread.sleep(4000);
            // this is where the user request a new interval, 2 sec.
            task.cancel();
            timeToRefresh += 3000;
            timer.schedule(new Action(), 0, timeToRefresh);
            Thread.sleep(8000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        task.cancel();

    }

}

class Action extends TimerTask {

    private long last;

    public void run() {
        last = scheduledExecutionTime();
        System.out.println("tick");
    }

    public long getLast() {
        return last;
    }
}
