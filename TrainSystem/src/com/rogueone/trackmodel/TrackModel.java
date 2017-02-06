/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.trackmodel.gui.TrackModelGUI;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author Dan Bednarczyk
 */
public class TrackModel {
    public static void main(String[] args) throws InterruptedException {
        TrackModelGUI trackModelGUI = new TrackModelGUI();
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trackModelGUI);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void parseDataFile(File file) throws IOException, InvalidFormatException {
        XSSFWorkbook testWorkbook = new XSSFWorkbook(file);
        XSSFSheet sheetBlocks = testWorkbook.getSheetAt(0);
        
        Row rowHeader = sheetBlocks.getRow(0);
        Cell cells[] = new Cell[rowHeader.getLastCellNum()];
        
        //Expected column order in data file:
        //0     line
        //1     section
        //2     isHead
        //3     isTail
        //4     blockID
        //5     portA
        //6     portB
        //7     switchID
        //8     length
        //9     grade
        //10    speedLimit
        //11    containsCrossing
        //12    isUnderground
        //13    stationName
        //14    elevation
        //15    cumulativeElevation
        //16    isStaticSwitchBlock
        
        //Iterate over all rows in the first column
        for (int i = 1; i <= sheetBlocks.getLastRowNum(); i++) {
            Row rowTemp = sheetBlocks.getRow(i);
            for (int j = 0; j < cells.length; j++) {
                cells[j] = rowTemp.getCell(j);
                System.out.print(rowHeader.getCell(j) + ": " + rowTemp.getCell(j) + " ");
            }
            System.out.println();
        }
    }
}