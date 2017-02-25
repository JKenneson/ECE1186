/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel.gui;

import com.rogueone.global.Global;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Dan
 */
public class FailureRenderer extends DefaultTableCellRenderer {
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value.toString().equalsIgnoreCase("Ok")) {
            renderer.setBackground(Global.GREEN);
        }
        else if (value.toString().equalsIgnoreCase("Failure")) {
            renderer.setBackground(Global.RED);
        }                
        return renderer;
    }
    
}
