package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import com.rogueone.global.UnitConversion;
import com.rogueone.trackmodel.gui.TrackModelGUI;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * The top-level Track Model class, which owns all Lines, Sections, and Blocks.
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
    
    /**
     * Resets the Track Model class by deleting all Lines, Sections, Blocks, Stations, Switches, and Yards.
     * @author Dan Bednarczyk
     */
    public void reset() {
        lines = new ArrayList<Line>();
        sections = new ArrayList<Section>();
        blocks = new ArrayList<Block>();
        stations = new ArrayList<Station>();
        switches = new ArrayList<Switch>();
        yard = new Yard();
    }
    
    /**
     * Connects all track components after they have been loaded, parsed, and instantiated.
     * @author Dan Bednarczyk
     */
    private void connectBlocks() {
        for (Block b : blocks) {
            if(b.getPortAID() == 0) {
                b.setPortA(yard);
            }
            else {
                b.setPortA(TrackModel.this.getBlock(b.getLine(),b.getPortAID()));
            }
            if(b.getPortBID() == -1) {
                b.setPortB(getSwitch(b.getSwitchID()));
            }
            else {
                b.setPortB(TrackModel.this.getBlock(b.getLine(),b.getPortBID()));
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
    
    // PARSING METHODS
    
    /**
     * Loads track data file and parses into objects.
     * @author Dan Bednarczyk
     * @param file the track data file
     */
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
        //System.out.println("\nBLOCKS:");
        //printBlocks();
        //System.out.println("\nSTATIONS:");
        //printStations();
        //System.out.println("\nSWITCHES:");
        //printSwitches();
        
    }
    
    public void simulateTrain() {
        Block prev = getBlock(Global.Line.GREEN, Global.Section.A, 1);
        Block cur = getBlock(Global.Line.GREEN, Global.Section.A, 2);
        Block next = null;
        Block curTemp = null;
        while (cur != null) {
            System.out.println(cur);
            next = cur.exitBlock(prev);
            curTemp = cur;
            cur = next;
            prev = curTemp;
        
            try {
                TimeUnit.MILLISECONDS.sleep(250);
            } catch (InterruptedException ex) {
                Logger.getLogger(TrackModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Parses Excel sheet into Line objects.
     * @author Dan Bednarczyk
     * @param sheet the sheet containing line information
     */
    private void parseLines(XSSFSheet sheet) {

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);      
            Global.Line tempLine = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Line newLine = new Line(tempLine);        
            addLine(newLine);
        }
    }
    
    /**
     * Adds Line to top-level array.
     * @author Dan Bednarczyk
     * @param l the line to be added
     */
    private void addLine(Line l) {
        lines.add(l);
    }
    
    /**
     * Parses Excel sheet into Section objects.
     * @author Dan Bednarczyk
     * @param sheet the sheet containing section information
     */
    private void parseSections(XSSFSheet sheet) {
        
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);      
            Global.Line tempLineID = Global.Line.valueOf(rowTemp.getCell(0).getStringCellValue());
            Global.Section tempSectionID = Global.Section.valueOf(rowTemp.getCell(1).getStringCellValue());
            addSection(new Section(tempSectionID, getLine(tempLineID)));
        }
    }
    
    /**
     * Adds Section to top-level array and Lines.
     * @author Dan Bednarczyk
     * @param s the section to be added
     */
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
    
    /**
     * Parses Excel sheet into Block objects.
     * @author Dan Bednarczyk
     * @param sheet the sheet containing block information
     */
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
            Line tempLine = getLine(tempLineID);
            Section tempSection = getSection(tempLineID, tempSectionID);
            
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
    
    /**
     * Adds Block to top-level array and Sections.
     * @author Dan Bednarczyk
     * @param b the block to be added
     */
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
    
    /**
     * Parses Excel sheet into Station objects.
     * @author Dan Bednarczyk
     * @param sheet the sheet containing Station information
     */
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
            Line tempLine = getLine(tempLineID);
            Block tempBlockA = getBlock(tempLineID,tempSectionAID,tempBlockAID);
            Section tempSectionA = getSection(tempLineID, tempSectionAID);
            //Parse second block, if applicable
            Block tempBlockB = null;
            Section tempSectionB = null;
            if(rowTemp.getCell(5) != null && rowTemp.getCell(6) != null) {
                int tempBlockBID = (int) rowTemp.getCell(5).getNumericCellValue();
                Global.Section tempSectionBID = Global.Section.valueOf(rowTemp.getCell(6).getStringCellValue());
                tempBlockB = getBlock(tempLineID,tempSectionBID,tempBlockBID);
                tempSectionB = getSection(tempLineID, tempSectionBID);
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
    
    /**
     * Adds Station to top-level array and Blocks.
     * @author Dan Bednarczyk
     * @param st the station to be added
     */
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
    
    /**
     * Parses Excel sheet into Switch objects.
     * @author Dan Bednarczyk
     * @param sheet the sheet containing switch information
     */
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
            Line tempLine = getLine(tempLineID);
            Block tempBlockA = TrackModel.this.getBlock(tempLineID, tempBlockAID);
            Block tempBlockB = TrackModel.this.getBlock(tempLineID, tempBlockBID);
            Block tempBlockC = TrackModel.this.getBlock(tempLineID, tempBlockCID);
            
            Switch newSwitch = new Switch(
                tempSwitchID,
                tempLine,
                tempBlockA,
                tempBlockB,
                tempBlockC );
            
            addSwitch(newSwitch);
        }     
    }
    
    /**
     * Adds Switch to top-level array.
     * @author Dan Bednarczyk
     * @param sw the switch to be added
     */
    private void addSwitch(Switch sw) {
        //Add reference to top-level array
        switches.add(sw);
        //Will be added to blocks in connectBlocks
    }
    
    //GETTER METHODS
    
    /**
     * Gets specified Line.
     * @author Dan Bednarczyk
     * @param line the enum of the line
     * @return the line specified, null otherwise
     */
    public Line getLine(Global.Line line) {
        for (Line l : lines) {
            if (l.getLineID() == line) {
                return l;
            }
        }
        System.err.println("Line " + line + " not found");
        return null;
    }
    
    /**
     * Gets specified Section.
     * @author Dan Bednarczyk
     * @param line the enum of the line
     * @param section the enum of the section
     * @return the section specified, null otherwise
     */
    public Section getSection(Global.Line line, Global.Section section) {
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
    
    /**
     * Gets specified Block.
     * @author Dan Bednarczyk
     * @param line the enum of the line
     * @param section the enum of the section
     * @param block the ID of the block
     * @return the block specified, null otherwise
     */
    public Block getBlock(Global.Line line, Global.Section section, int block) {
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
    
    /**
     * Gets specified Block.
     * @author Dan Bednarczyk
     * @param line the enum of the line
     * @param block the ID of the block
     * @return the block specified, null otherwise
     */
    public Block getBlock(Global.Line line, int block) {
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
    
    /**
     * Gets specified Block.
     * @author Dan Bednarczyk
     * @param line the Line object containing the Block
     * @param section the Section object containing the block
     * @param block the ID of the block
     * @return the block specified, null otherwise
     */
    public Block getBlock(Line line, Section section, int block) {
        for (Line l : lines) {
            if (l.equals(line)) {
                return l.getBlock(section, block);
            }
        }
        System.err.println("Block " + line + ":" + section + ":" + block + " not found");
        return null;
    }
    
    /**
     * Gets specified Block.
     * @author Dan Bednarczyk
     * @param line the Line object containing the Block
     * @param block the ID of the block
     * @return the block specified, null otherwise
     */
    public Block getBlock(Line line, int block) {
        for (Line l : lines) {
            if (l.equals(line)) {
                return l.getBlock(block);
            }
        }
        System.err.println("Block " + line + ":" + block + " not found");
        return null;
    }
    
    /**
     * Gets specified Switch.
     * @author Dan Bednarczyk
     * @param switchID the ID of the switch
     * @return the switch specified, null otherwise
     */
    public Switch getSwitch(int switchID) {
        for (Switch s : switches) {
            if(s.getID() == switchID) {
                return s;
            }
        }
        System.err.println("Switch " + switchID + " not found.");
        return null;
    }
    
    /**
     * Gets all Lines as ArrayList.
     * @author Dan Bednarczyk
     * @return the top-level ArrayList of Lines
     */
    public ArrayList<Line> getLineArray() {
        return lines;
    }
    
    /**
     * Gets all Sections as ArrayList.
     * @author Dan Bednarczyk
     * @return the top-level ArrayList of Sections
     */
    public ArrayList<Section> getSectionArray() {
        return sections;
    }
    
    /**
     * Gets all Blocks as ArrayList.
     * @author Dan Bednarczyk
     * @return the top-level ArrayList of Blocks
     */
    public ArrayList<Block> getBlockArray() {
        return blocks;
    }
    
    /**
     * Gets all Stations as ArrayList.
     * @author Dan Bednarczyk
     * @return the top-level ArrayList of Stations
     */
    public ArrayList<Station> getStationArray() {
        return stations;
    }
    
    /**
     * Gets all Switches as ArrayList.
     * @author Dan Bednarczyk
     * @return the top-level ArrayList of Switches
     */
    public ArrayList<Switch> getSwitchArray() {
        return switches;
    }
    
    //PRINTING METHODS
    
    /**
     * Prints top-level ArrayList of Lines in detail.
     * @author Dan Bednarczyk
     */
    public void printLines() {
        for(Line l : lines) {
            System.out.println(l.getLineID());
        }
    }
    
    /**
     * Prints top-level ArrayList of Sections in detail.
     * @author Dan Bednarczyk
     */
    public void printSections() {
        for(Section s : sections) {
            System.out.println(s.toStringDetail());
        }
    }
    
    /**
     * Prints top-level ArrayList of Blocks in detail.
     * @author Dan Bednarczyk
     */
    public void printBlocks() {
        for(Block b : blocks) {
            System.out.println(b.toStringDetail());
        }
    }
    
    /**
     * Prints top-level ArrayList of Stations in detail.
     * @author Dan Bednarczyk
     */
    public void printStations() {
        for(Station s : stations) {
            System.out.println(s.toStringDetail());
        }
    }
    
    /**
     * Prints top-level ArrayList of Switches in detail.
     * @author Dan Bednarczyk
     */
    public void printSwitches() {
        for(Switch s : switches) {
            System.out.println(s.toStringDetail());
        }
    }
}