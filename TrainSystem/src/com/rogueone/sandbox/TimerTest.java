/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.sandbox;

import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author Jon Kenneson
 */
public class TimerTest {
    
    public static int timeToRefresh;
    
    public static void main(String args[]) throws InterruptedException {
        timeToRefresh = 1000;
        
        Timer timer = new Timer();
        Action task = new Action();
        timer.schedule( task, 0, timeToRefresh );
        Thread.sleep( 4000 );
        // this is where the user request a new interval, 2 sec.
        task.cancel();
        timeToRefresh += 3000;
        timer.schedule( new Action(), 0, timeToRefresh );
        Thread.sleep( 8000 );
        task.cancel();
        
        
    }
}
    
    class Action extends TimerTask {
        private long last;
    
        public void run(){
            last = scheduledExecutionTime();
            System.out.println( "tick" );
        }

        public long getLast(){
            return last;
        } 
    }
