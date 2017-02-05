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
    public static void main(String[] args) throws InterruptedException {
        // TODO code application logic here
        TrainModelGUI trainModelGUI = new TrainModelGUI();
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trainModelGUI);
        frame.pack();
        frame.setVisible(true);
        
        trainModelGUI.tempInputSpinner.setValue(72);
    }
}
