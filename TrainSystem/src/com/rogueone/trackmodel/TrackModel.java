/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.trackmodel.gui.TrackModelGUI;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/**
 *
 * @author Dan Bednarczyk
 */
public class TrackModel {
    
    ArrayList<String> lines = new ArrayList<String>();
    ArrayList<String> sections = new ArrayList<String>();
    ArrayList<Block> blocks = new ArrayList<Block>();
    
    public static void main(String[] args) throws InterruptedException {
        TrackModelGUI trackModelGUI = new TrackModelGUI();
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trackModelGUI);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void parseDataFile(File file) throws IOException, InvalidFormatException {
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
        
        XSSFWorkbook testWorkbook = new XSSFWorkbook(file);
        XSSFSheet sheetBlocks = testWorkbook.getSheetAt(0);
        
        Row rowHeader = sheetBlocks.getRow(0);
        Cell cells[] = new Cell[rowHeader.getLastCellNum()];  
        
        //Iterate over all rows in the first column
        for (int i = 1; i <= sheetBlocks.getLastRowNum(); i++) {
            Row rowTemp = sheetBlocks.getRow(i);
            String tempLine = rowTemp.getCell(0).getStringCellValue();
            String tempSection = rowTemp.getCell(1).getStringCellValue();
            boolean tempIsHead = false;
            if(rowTemp.getCell(2) != null) {
                tempIsHead = true;
            }
            boolean tempIsTail = false;
            if(rowTemp.getCell(3) != null) {
                tempIsTail = true;
            }
            int tempBlockID = (int) rowTemp.getCell(4).getNumericCellValue();
            int tempSwitchID = (int) rowTemp.getCell(7).getNumericCellValue();
            int tempLength = (int) rowTemp.getCell(8).getNumericCellValue();
            double tempGrade = rowTemp.getCell(9).getNumericCellValue();
            int tempSpeedLimit = (int) rowTemp.getCell(10).getNumericCellValue();  
            boolean tempContainsCrossing = false;
            if(rowTemp.getCell(11) != null) {
                tempContainsCrossing = true;
            }
            boolean tempIsUnderground = false;
            if(rowTemp.getCell(12) != null) {
                tempIsUnderground = true;
            }
            String tempStationName = rowTemp.getCell(13).getStringCellValue();
            double tempElevation = rowTemp.getCell(14).getNumericCellValue();
            double tempCumulativeElevation = rowTemp.getCell(15).getNumericCellValue(); 
            boolean tempIsStaticSwitchBlock = false;
            if(rowTemp.getCell(16) != null) {
                tempIsStaticSwitchBlock = true;
            }
            Block newBlock = new Block(tempLine, tempSection, tempBlockID, 
                    tempSwitchID, tempIsStaticSwitchBlock, tempStationName, 
                    tempLength, tempGrade, tempSpeedLimit, tempElevation, 
                    tempCumulativeElevation, tempIsHead, tempIsTail, 
                    tempContainsCrossing, tempIsUnderground);
            blocks.add(newBlock);
            
            //Check for existing line
            if(lines.indexOf(newBlock.getLine()) == -1) {
                //Line does not exist, add it
                lines.add(newBlock.getLine());
            }
            //Check for existing section
        }
        
        
        printBlocks();
    }
    
    public void printBlocks() {
        for(Block b : blocks) {
            System.out.println(b);
        }
    }
}