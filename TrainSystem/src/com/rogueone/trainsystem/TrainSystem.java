/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trainsystem;

import com.rogueone.mainframe.*;
import java.awt.BorderLayout;

/**
 *
 * @author kylemonto
 */
public class TrainSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainFrame mf = new MainFrame();
        mf.setLayout(new BorderLayout());
        
        InterfaceSelector is = new InterfaceSelector(mf);
        mf.getContentPane().add(is, BorderLayout.CENTER);
        
        mf.setVisible(true);
    }
    
}
