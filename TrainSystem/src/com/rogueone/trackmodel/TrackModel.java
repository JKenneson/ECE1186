/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import com.rogueone.global.UnitConversion;
import com.rogueone.trackmodel.gui.TrackModelGUI;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
    Yard yard = new Yard();
    
    public static void main(String[] args) throws InterruptedException {
        TrackModelGUI trackModelGUI = new TrackModelGUI(new TrackModel());
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trackModelGUI);
        frame.pack();
        frame.setVisible(true);
    }
    
    public void reset() {
        lines = new ArrayList<Line>();
        sections = new ArrayList<Section>();
        blocks = new ArrayList<Block>();
        stations = new ArrayList<Station>();
        switches = new ArrayList<Switch>();
        yard = new Yard();
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
        
        //System.out.println("\nLINES:");
        //printLines();
        //System.out.println("\nSECTIONS:");
        //printSections();
        System.out.println("\nBLOCKS:");
        printBlocks();
        System.out.println("\nSTATIONS:");
        printStations();
        System.out.println("\nSWITCHES:");
        printSwitches();
       
    }
    
    private void connectBlocks() {
        for (Block b : blocks) {
            if(b.getPortAID() == 0) {
                b.setPortA(yard);
            }
            else {
                b.setPortA(getBlock(b.getLine(),b.getPortAID()));
            }
            if(b.getPortBID() == -1) {
                b.setPortB(getSwitch(b.getSwitchID()));
            }
            else {
                b.setPortB(getBlock(b.getLine(),b.getPortBID()));
            }
            //Error checking
            if(b.getPortA() == null) {
                System.err.println("ERROR: Port A not assigned to block " + b.getID());
            }
            if(b.getPortB() == null) {
                System.err.println("ERROR: Port B not assigned to block " + b.getID());
            }
        }
    }
    
    public Block getBlock(Line line, Section section, int block) {
        for (Line l : lines) {
            if (l.equals(line)) {
                return l.getBlock(section, block);
            }
        }
        System.err.println("Block " + line + ":" + section + ":" + block + " not found");
        return null;
    }
    
    public Block getBlock(Line line, int block) {
        for (Line l : lines) {
            if (l.equals(line)) {
                return l.getBlock(block);
            }
        }
        System.err.println("Block " + line + ":" + block + " not found");
        return null;
    }
    
    public Switch getSwitch(int switchID) {
        for (Switch s : switches) {
            if(s.getID() == switchID) {
                return s;
            }
        }
        System.err.println("Switch " + switchID + " not found.");
        return null;
    }
    
    public ArrayList<Line> getLines() {
        return lines;
    }
    
    public ArrayList<Section> getSections() {
        return sections;
    }
    
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    
    public ArrayList<Station> getStations() {
        return stations;
    }
    
    public ArrayList<Switch> getSwitches() {
        return switches;
    }
    
    private void parseLines(XSSFSheet sheet) {

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);      
            Global.Line tempLine = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Line newLine = new Line(tempLine);        
            addLine(newLine);
        }
    }
    
    private void addLine(Line l) {
        lines.add(l);
    }
    
    private void parseSections(XSSFSheet sheet) {
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);      
            Global.Line tempLineID = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Global.Section tempSectionID = Global.Section.valueOf(rowTemp.getCell(1).getStringCellValue());
            addSection(new Section(tempSectionID, getLineByID(tempLineID)));
        }
    }
    
    private void addSection(Section s) {
        //Add reference to top-level array
        sections.add(s);
        //Add reference to Line objects
        for(Line l : lines) {
            if(l.equals(s.getLine())) {
                l.addSection(s);
            }
        }
    }
    
    private void parseBlocks(XSSFSheet sheet) {
  
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);
            //Parse Enums
            Global.Line tempLineID = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Global.Section tempSectionID = Global.Section.valueOf(rowTemp.getCell(1).getStringCellValue());
            //Parse ints
            int tempBlockID = (int) rowTemp.getCell(4).getNumericCellValue();
            int tempPortAID = (int) rowTemp.getCell(5).getNumericCellValue();
            int tempPortBID = (int) rowTemp.getCell(6).getNumericCellValue();
            int tempSwitchID = (int) rowTemp.getCell(7).getNumericCellValue();
            int tempStationID = (int) rowTemp.getCell(13).getNumericCellValue();
            //Parse doubles
            double tempGrade = rowTemp.getCell(9).getNumericCellValue();
            double tempLength = UnitConversion.metersToFeet(rowTemp.getCell(8).getNumericCellValue());
            double tempSpeedLimit = UnitConversion.metersToFeet(rowTemp.getCell(10).getNumericCellValue());
            double tempElevation = UnitConversion.metersToFeet(rowTemp.getCell(14).getNumericCellValue());
            double tempCumulativeElevation = UnitConversion.metersToFeet(rowTemp.getCell(15).getNumericCellValue()); 
            //Parse booleans
            boolean tempIsHead = rowTemp.getCell(2) != null && rowTemp.getCell(2).getStringCellValue().equals("Y");
            boolean tempIsTail = rowTemp.getCell(3) != null && rowTemp.getCell(3).getStringCellValue().equals("Y");
            boolean tempContainsCrossing = rowTemp.getCell(11) != null && rowTemp.getCell(11).getStringCellValue().equals("Y");
            boolean tempIsUnderground = rowTemp.getCell(12) != null && rowTemp.getCell(12).getStringCellValue().equals("Y");
            boolean tempIsStaticSwitchBlock = rowTemp.getCell(16) != null && rowTemp.getCell(16).getStringCellValue().equals("Y");
            //Associate lineID and sectionID with objects
            Line tempLine = getLineByID(tempLineID);
            Section tempSection = getSectionByLineIDAndSectionID(tempLineID, tempSectionID);
            
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
    
    private void addBlock(Block b) {
        //Add reference to top-level array
        blocks.add(b);
        //Add reference to Section objects
        for(Line l : lines) {
            if(b.getLine().equals(l)) {
                for(Section s : l.getSections()) {
                    if(s.equals(b.getSection())) {
                        s.addBlock(b);
                    }
                }
            }
        }
    } 
    
    private void parseStations(XSSFSheet sheet) {
   
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);
            //Parse ints
            int tempStationID = (int) rowTemp.getCell(0).getNumericCellValue();
            int tempBlockAID = (int) rowTemp.getCell(3).getNumericCellValue();
            // Parse Strings
            String tempStationName = rowTemp.getCell(1).getStringCellValue();
            //Parse enums
            Global.Line tempLineID = Global.Line.valueOf(rowTemp.getCell(2).getStringCellValue());
            Global.Section tempSectionAID = Global.Section.valueOf(rowTemp.getCell(4).getStringCellValue());
            //Parse booleans
            boolean tempRightSide = rowTemp.getCell(7) != null && rowTemp.getCell(7).getStringCellValue().equals("Y");
            boolean tempLeftSide = rowTemp.getCell(8) != null && rowTemp.getCell(8).getStringCellValue().equals("Y");
            //Associate IDs with objects
            Line tempLine = getLineByID(tempLineID);
            Block tempBlockA = getBlockByLineIDAndSectionIDAndBlockID(tempLineID,tempSectionAID,tempBlockAID);
            Section tempSectionA = getSectionByLineIDAndSectionID(tempLineID, tempSectionAID);
            //Parse second block, if applicable
            Block tempBlockB = null;
            Section tempSectionB = null;
            if(rowTemp.getCell(5) != null && rowTemp.getCell(6) != null) {
                int tempBlockBID = (int) rowTemp.getCell(5).getNumericCellValue();
                Global.Section tempSectionBID = Global.Section.valueOf(rowTemp.getCell(6).getStringCellValue());
                tempBlockB = getBlockByLineIDAndSectionIDAndBlockID(tempLineID,tempSectionBID,tempBlockBID);
                tempSectionB = getSectionByLineIDAndSectionID(tempLineID, tempSectionBID);
            } 
            
            //Formatting is weird, but easier to develop (for now)
            Station newStation = new Station(
                tempStationID,
                tempStationName,
                tempLine,
                tempBlockA,
                tempSectionA,
                tempBlockB,
                tempSectionB,
                tempRightSide,
                tempLeftSide );
            addStation(newStation);
        }
    }
    private void addStation(Station st) {
        //Add reference to top-level array
        stations.add(st);
        //Add reference to blocks
        Block block = (Block)st.getBlockA();
        if(block != null) {
            block.setStation(st);
        }
        block = (Block)st.getBlockB();
        if(block != null) {
            block.setStation(st);
        }       
    }
    
    private void parseSwitches(XSSFSheet sheet) {
 
        //Iterate over all rows in the first column
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);
            // Parse ints
            int tempSwitchID = (int) rowTemp.getCell(0).getNumericCellValue();
            int tempBlockAID = (int) rowTemp.getCell(2).getNumericCellValue();
            int tempBlockBID = (int) rowTemp.getCell(3).getNumericCellValue();
            int tempBlockCID = (int) rowTemp.getCell(4).getNumericCellValue();
            // Parse enums
            Global.Line tempLineID = Global.Line.valueOf(rowTemp.getCell(1).getStringCellValue());
            //Associate IDs with objects
            Line tempLine = getLineByID(tempLineID);
            Block tempBlockA = getBlockByLineIDAndBlockID(tempLineID, tempBlockAID);
            Block tempBlockB = getBlockByLineIDAndBlockID(tempLineID, tempBlockBID);
            Block tempBlockC = getBlockByLineIDAndBlockID(tempLineID, tempBlockCID);
            
            Switch newSwitch = new Switch(
                tempSwitchID,
                tempLine,
                tempBlockA,
                tempBlockB,
                tempBlockC );
            
            addSwitch(newSwitch);
        }     
    }
    
    private void addSwitch(Switch sw) {
        //Add reference to top-level array
        switches.add(sw);
        //Will be added to blocks in connectBlocks
    }
    
    //UTILITY ACCESS METHODS
    
    public Line getLineByID(Global.Line line) {
        for (Line l : lines) {
            if (l.getLineID() == line) {
                return l;
            }
        }
        System.err.println("Line " + line + " not found");
        return null;
    }
    
    public Section getSectionByLineIDAndSectionID(Global.Line line, Global.Section section) {
        for (Line l : lines) {
            if (l.getLineID() == line) {
                for (Section s : l.getSections()) {
                    if (s.getSectionID() == section) {
                        return s;
                    }
                }
            }
        }
        System.err.println("Section " + line + ":" + section + " not found");
        return null;
    }
    
    public Block getBlockByLineIDAndSectionIDAndBlockID(Global.Line line, Global.Section section, int block) {
        for (Line l : lines) {
            if (l.getLineID() == line) {
                for (Section s : sections) {
                    if (s.getSectionID() == section) {
                        for (Block b : s.getBlocks()) {
                            if (b.getID() == block) {
                                return b;
                            }
                        }
                    }
                }
            }
        }
        System.err.println("Block " + line + ":" + section + ":" + block + " not found");
        return null;
    }
    
    public Block getBlockByLineIDAndBlockID(Global.Line line, int block) {
        for (Line l : lines) {
            if (l.getLineID() == line) {
                for (Section s : sections) {  
                    for (Block b : s.getBlocks()) {
                        if (b.getID() == block) {
                            return b;
                        }
                    }
                }
            }
        }
        System.err.println("Block " + line + ":" + block + " not found");
        return null;
    }
    
    public void printLines() {
        for(Line l : lines) {
            System.out.println(l.getLineID());
        }
    }
    
    public void printSections() {
        for(Section s : sections) {
            System.out.println(s.toStringDetail());
        }
    }
    
    public void printBlocks() {
        for(Block b : blocks) {
            System.out.println(b.toStringDetail());
        }
    }
    
    public void printStations() {
        for(Station s : stations) {
            System.out.println(s.toStringDetail());
        }
    }
    
    public void printSwitches() {
        for(Switch s : switches) {
            System.out.println(s.toStringDetail());
        }
    }
}