/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel.gui;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dan
 */
public class NonEditableTableModel extends DefaultTableModel {
    
      public NonEditableTableModel(String[] colNames) {
          super(colNames, 0);
      }
    
      @Override
      public boolean isCellEditable(int row, int column){
          if(this.getColumnName(column).equalsIgnoreCase("WAITING") || this.getColumnName(column).equalsIgnoreCase("TEMPERATURE")) {
              return true;
          }
          return false;  
      }

}