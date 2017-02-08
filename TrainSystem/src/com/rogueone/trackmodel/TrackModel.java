/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.Global;
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
    
    ArrayList<Line> lines = new ArrayList<Line>();
    ArrayList<Section> sections = new ArrayList<Section>();
    ArrayList<Block> blocks = new ArrayList<Block>();
    ArrayList<Station> stations = new ArrayList<Station>();
    ArrayList<Switch> switches = new ArrayList<Switch>();
    
    public static void main(String[] args) throws InterruptedException {
        TrackModelGUI trackModelGUI = new TrackModelGUI();
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trackModelGUI);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void parseDataFile(File file) throws IOException, InvalidFormatException {
        //Expected column order in data file for blocks:
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
        //13    stationID
        //14    elevation
        //15    cumulativeElevation
        //16    isStaticSwitchBlock
        
        XSSFWorkbook testWorkbook = new XSSFWorkbook(file);
  
        parseLines(testWorkbook.getSheetAt(0));
        parseSections(testWorkbook.getSheetAt(1));
        parseBlocks(testWorkbook.getSheetAt(2));
        parseStations(testWorkbook.getSheetAt(3));
        parseSwitches(testWorkbook.getSheetAt(4));
        connectBlocks();
        
        System.out.println("LINES:");
        printLines();
        System.out.println("SECTIONS:");
        printSections();
        System.out.println("BLOCKS:");
        printBlocks();
        System.out.println("STATIONS:");
        printStations();
        System.out.println("SWITCHES:");
        printSwitches();
       
    }
    
    private void connectBlocks() {
        for (Block b : blocks) {
            if(b.getPortA() == null) {
                b.setPortA(getBlock(b.getLine(),b.getPortAID()));
            }
            if(b.getPortB() == null) {
                b.setPortB(getBlock(b.getLine(),b.getPortBID()));
            }
        }
    }
    
    public Block getBlock(Global.Line line, Global.Section section, int block) {
        for (Line l : lines) {
            if (l.getLine() == line) {
                return l.getBlock(section, block);
            }
        }
        return null;
    }
    
    public Block getBlock(Global.Line line, int block) {
        for (Line l : lines) {
            if (l.getLine() == line) {
                return l.getBlock(block);
            }
        }
        return null;
    }
    
    private void parseLines(XSSFSheet sheet) {

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);      
            Global.Line tempLine = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Line newLine = new Line(tempLine);        
            addLine(newLine);
        }
    }
    
    private void parseSections(XSSFSheet sheet) {
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);      
            Global.Line tempLine = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Global.Section tempSection = Global.Section.valueOf(rowTemp.getCell(1).getStringCellValue());
            Section newSection = new Section(tempSection, tempLine);        
            addSection(newSection);
        }
    }
    
    private void parseBlocks(XSSFSheet sheet) {
  
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);
            //Parse Enums
            Global.Line tempLine = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Global.Section tempSection = Global.Section.valueOf(rowTemp.getCell(1).getStringCellValue());
            //Parse ints
            int tempBlockID = (int) rowTemp.getCell(4).getNumericCellValue();
            int tempPortAID = (int) rowTemp.getCell(5).getNumericCellValue();
            int tempPortBID = (int) rowTemp.getCell(6).getNumericCellValue();
            int tempSwitchID = (int) rowTemp.getCell(7).getNumericCellValue();
            int tempStationID = (int) rowTemp.getCell(13).getNumericCellValue();
            //Parse doubles
            double tempGrade = rowTemp.getCell(9).getNumericCellValue();
            double tempLength = rowTemp.getCell(8).getNumericCellValue();
            double tempSpeedLimit = rowTemp.getCell(10).getNumericCellValue();
            double tempElevation = rowTemp.getCell(14).getNumericCellValue();
            double tempCumulativeElevation = rowTemp.getCell(15).getNumericCellValue(); 
            //Parse booleans
            boolean tempIsHead = rowTemp.getCell(2) != null && rowTemp.getCell(2).getStringCellValue().equals("Y");
            boolean tempIsTail = rowTemp.getCell(3) != null && rowTemp.getCell(3).getStringCellValue().equals("Y");
            boolean tempContainsCrossing = rowTemp.getCell(11) != null && rowTemp.getCell(11).getStringCellValue().equals("Y");
            boolean tempIsUnderground = rowTemp.getCell(12) != null && rowTemp.getCell(12).getStringCellValue().equals("Y");
            boolean tempIsStaticSwitchBlock = rowTemp.getCell(16) != null && rowTemp.getCell(16).getStringCellValue().equals("Y");
            
            //Formatting is weird, but easier to develop (for now)
            Block newBlock = new Block(
                tempLine, 
                tempSection, 
                tempBlockID,
                tempPortAID,
                tempPortBID,
                tempSwitchID, 
                tempIsStaticSwitchBlock, 
                tempStationID,
                tempLength,
                tempGrade,
                tempSpeedLimit,
                tempElevation,
                tempCumulativeElevation, 
                tempIsHead, 
                tempIsTail, 
                tempContainsCrossing, 
                tempIsUnderground );
            addBlock(newBlock);
        }
    }
    
    private void parseStations(XSSFSheet sheet) {
   
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);
            
            int tempStationID = (int) rowTemp.getCell(0).getNumericCellValue();
            String tempStationName = rowTemp.getCell(1).getStringCellValue();
            Global.Line tempLine = Global.Line.valueOf(rowTemp.getCell(2).getStringCellValue());
            boolean tempRightSide = rowTemp.getCell(7) != null && rowTemp.getCell(7).getStringCellValue().equals("Y");
            boolean tempLeftSide = rowTemp.getCell(8) != null && rowTemp.getCell(8).getStringCellValue().equals("Y");
            
            //Formatting is weird, but easier to develop (for now)
            Station newStation = new Station(
                tempStationID,
                tempStationName,
                tempLine,
                null,                   //Block A
                Global.Section.INVALID, //Section A
                null,                   //Block B
                Global.Section.INVALID, //Section B
                tempRightSide,
                tempLeftSide );
            addStation(newStation);
        }
    }
    
    private void parseSwitches(XSSFSheet sheet) {
 
        //Iterate over all rows in the first column
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);
            
            int tempSwitchID = (int) rowTemp.getCell(0).getNumericCellValue();
            Global.Line tempLine = Global.Line.valueOf(rowTemp.getCell(1).getStringCellValue());
            
            Switch newSwitch = new Switch(
                tempSwitchID,
                tempLine,
                null,   //Port A
                null,   //Port B
                null ); //Port C
            
            addSwitch(newSwitch);
        }     
    }
    
    private void addLine(Line l) {
        lines.add(l);
    }
    
    private void addSection(Section s) {
        //Add reference to top-level array
        sections.add(s);
        //Add reference to Line objects
        for(Line l : lines) {
            if(s.getLine() == l.getLine()) {
                l.addSection(s);
            }
        }
    }
    
    private void addBlock(Block b) {
        //Add reference to top-level array
        blocks.add(b);
        //Add reference to Section objects
        for(Line l : lines) {
            if(b.getLine() == l.getLine()) {
                for(Section s : l.getSections()) {
                    if(b.getSection() == s.getSection()) {
                        s.addBlock(b);
                    }
                }
            }
        }
    }
    
    private void addStation(Station st) {
        //Add reference to top-level array
        stations.add(st);
        for(Block b : blocks) {
            if(b.getLine() == st.getLine() && b.getStationID() == st.getStationID()) {
                //Assign station to block
                if(b.getStation() == null) {
                    b.setStation(st);
                }
                else {
                    System.out.println("Block " + b.getID() + " can't own station " + st.getStationID() + " because it already owns station " + b.getStation().getStationID());
                }
                //Assign block to station
                if(st.getBlockA() == null) {
                    st.setBlockA(b);
                    st.setBlockASection(b.getSection());
                }
                else if(st.getBlockB() == null) {
                    st.setBlockB(b);
                    st.setBlockBSection(b.getSection());
                }
                else {
                    System.out.println("Station already has two blocks.");
                }
                
            }
        }
    }
    
    private void addSwitch(Switch sw) {
        //Add reference to top-level array
        switches.add(sw);
        for(Block b : blocks) {
            if(b.getSwitchID() == sw.getID()) {
                //Assign switch to block
                if(b.getPortB() == null) {
                    b.setPortB(sw);
                }
                //Assign block to switch
                if(b.isStaticSwitchBlock()) {
                    sw.setPortA(b);
                }
                else if(sw.getPortB() == null) {
                    sw.setPortB(b);
                }
                else if(sw.getPortC() == null) {
                    sw.setPortC(b);
                }
                else {
                    System.out.println("Error in data file. Too many blocks assigned to single switch.");
                }
            }
        }
    }
    
    public void printLines() {
        for(Line l : lines) {
            System.out.println(l.getLine());
        }
    }
    
    public void printSections() {
        for(Section s : sections) {
            System.out.println(s.getSection() + " (" + s.getLine() + ")");
        }
    }
    
    public void printBlocks() {
        for(Block b : blocks) {
            System.out.println(b);
        }
    }
    
    public void printStations() {
        for(Station s : stations) {
            System.out.println(s);
        }
    }
    
    public void printSwitches() {
        for(Switch s : switches) {
            System.out.println(s);
        }
    }
}