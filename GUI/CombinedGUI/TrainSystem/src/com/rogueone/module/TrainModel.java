/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.module;

import com.rogueone.module.gui.*;
import javax.swing.*;

/**
 *
 * @author Jon Kenneson
 */
public class TrainModel {
    
    public static void main(String[] args) {
        //Create a GUI object
        TrainModelGUI trainModelGUI = new TrainModelGUI();
        
        //Initialize a JFrame to hold the GUI in (Since it is only a JPanel)
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trainModelGUI);
        frame.pack();
        frame.setVisible(true);     //Make sure to set it visible
        
        //Initialize the GUI
        initializeGUI(trainModelGUI);
        
    }
    
    //Set some initial properties to variables in the train model window
    public static void initializeGUI(TrainModelGUI gui) {
        //Input stuff
        
        gui.tempInputSpinner.setValue(72);
        
        
        //Output stuff
        gui.authorityState.setText("0");
    }

    public static void printMe() {
        System.out.println("Print Me!");
    }
    
}
