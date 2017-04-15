/**
* The top-level Track Model class, which owns all Lines, Sections, and Blocks.
*
* @author: Dan Bednarczyk
* @creation date: 02/01/2017
* @modification date: 04/20/2017
*/

package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import com.rogueone.global.UnitConversion;
import com.rogueone.trackmodel.gui.TrackModelGUI;
import com.rogueone.trainsystem.TrainSystem;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TrackModel {
    
    private static final String DEFAULT_PATH = "src/com/rogueone/assets/TrackData.xlsx";
    private static final int FIRST_BLOCK_GREEN = 152;
    private static final int FIRST_BLOCK_RED = 77;
    private ArrayList<Line> lines = new ArrayList<Line>();
    private ArrayList<Section> sections = new ArrayList<Section>();
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Station> stations = new ArrayList<Station>();
    private ArrayList<Switch> switches = new ArrayList<Switch>();
    private ArrayList<Beacon> beacons = new ArrayList<Beacon>();
    private Yard yard = new Yard();
    private TrackModelGUI trackModelGUI = null;
    private TrainSystem trainSystem = null;
    
    //Used only when running standalone TrackModel
    public static void main(String[] args) throws InterruptedException {
        //Initialize track model
        TrackModel trackModel= new TrackModel(null, new File(DEFAULT_PATH));
        //Open GUI
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(trackModel.getGUI());
        frame.pack();
        frame.setVisible(true); 
    }
    
    /**
     * Initializes TrackModel with data file
     * @author Dan Bednarczyk
     * @param ts the main TrainSystem
     * @param file the File to load
     */
    public TrackModel(TrainSystem ts, File file) {
        trainSystem = ts;
        parseDataFile(file);
        trackModelGUI = new TrackModelGUI(this);
    }
    
    /**
     * updates GUI to reflect any changes
     * @author Dan Bednarczyk
     */
    public void updateGUI() {
        trackModelGUI.updateSummaryPanel();
        trackModelGUI.updateDetailsPanel();
    }
    
    /**
     * Gets affiliated GUI
     * @author Dan Bednarczyk
     * @return the TrackModelGUI object
     */
    public TrackModelGUI getGUI() {
        return trackModelGUI;
    }
    
    /**
     * Trigger updates for passengers and temperature at stations
     * @author Dan Bednarczyk
     */
    public void updateStationEnvironments() {
        for(Station station : stations) {
            station.updatePassengers();
            station.updateTemperature();
        }
    }
    
    /**
     * Gets the first block after the yard on the specified line
     * @author Dan Bednarczyk
     * @param line the line that the train is entering
     * @return the first block on the line
     */
    public Block enterTrack(Global.Line line) {
        Block firstBlock = null;
        if(line == Global.Line.GREEN) { 
            firstBlock = getBlock(line, FIRST_BLOCK_GREEN);
            firstBlock.setOccupancy(true);
        }
        else if(line == Global.Line.RED) {
            firstBlock = getBlock(line, FIRST_BLOCK_RED);
            firstBlock.setOccupancy(true);
        }
        else {
            System.err.println("Line could not be found");
        }
        if(firstBlock == null) {
            System.err.println("First block on line " + line + " could not be found");
        }
        return firstBlock;
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
        beacons = new ArrayList<Beacon>();
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
    public void parseDataFile(File file) {
        
        XSSFWorkbook workbook = null;
        
        try {
            workbook = new XSSFWorkbook(file);
        }
        catch (Exception ex) {
            System.err.println("Error reading data file.");
        }

        if (workbook != null) {
            try {
                parseLines(workbook.getSheetAt(0));
                parseSections(workbook.getSheetAt(1));
                parseBlocks(workbook.getSheetAt(2));
                parseStations(workbook.getSheetAt(3));
                parseSwitches(workbook.getSheetAt(4));
                parseBeacons(workbook.getSheetAt(5));
                connectBlocks();
                for (Line l : lines) {
                    l.updateTotalLength();
                }
                System.out.println("Track loaded successfully.");
            }
            catch (Exception ex) {
                System.err.println("Data file incorrectly formatted.");
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
            double tempSpeedLimit = UnitConversion.kilometersPerHourToMilesPerHour(rowTemp.getCell(10).getNumericCellValue());
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
                tempIsUnderground);
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
                tempSectionB);
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
    
    /**
     * Parses Excel sheet into Beacon objects.
     * @author Dan Bednarczyk
     * @param sheet the sheet containing switch information
     */
    private void parseBeacons(XSSFSheet sheet) {
 
        //Iterate over all rows in the first column
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row rowTemp = sheet.getRow(i);
            // Parse ints
            int tempBeaconID = (int) rowTemp.getCell(0).getNumericCellValue();
            int tempBlockID = (int) rowTemp.getCell(2).getNumericCellValue();
            int tempStationID = (int) rowTemp.getCell(3).getNumericCellValue();
            // Parse enums
            Global.Line tempLineID = Global.Line.valueOf(rowTemp.getCell(1).getStringCellValue());
            // Parse doubles
            double tempDistance = rowTemp.getCell(4).getNumericCellValue();
            // Parse booleans
            boolean tempIsRightSide = rowTemp.getCell(5) != null && rowTemp.getCell(5).getStringCellValue().equals("Y");
            //Associate IDs with objects
            Block tempBlock = this.getBlock(tempLineID, tempBlockID);
            Station tempStation = this.getStation(tempStationID);
            
            Beacon newBeacon = new Beacon(
                tempBeaconID,
                tempBlock,
                tempStation,
                tempDistance,
                tempIsRightSide);
            
            addBeacon(newBeacon);
        }     
    }
    
    /**
     * Adds Beacon to top-level array.
     * @author Dan Bednarczyk
     * @param beacon the beacon to be added
     */
    private void addBeacon(Beacon beacon) {
        //Add reference to top-level array
        beacons.add(beacon);
        //Add reference to blocks
        Block block = (Block)beacon.getBlock();
        if(block != null) {
            block.setBeacon(beacon);
        }
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
                for (Section s : l.getSections()) {
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
                for (Section s : l.getSections()) {  
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
     * Gets specified Beacon.
     * @author Dan Bednarczyk
     * @param beaconID the ID of the beacon
     * @return the beacon specified, null otherwise
     */
    public Beacon getBeacon(int beaconID) {
        for (Beacon b : beacons) {
            if(b.getID() == beaconID) {
                return b;
            }
        }
        System.err.println("Beacon " + beaconID + " not found.");
        return null;
    }
    
    /**
     * Gets specified Beacon.
     * @author Dan Bednarczyk
     * @param stationID the ID of the beacon
     * @return the beacon specified, null otherwise
     */
    public Station getStation(int stationID) {
        if(stationID == 0) {
            return null;
        }
        for (Station s : stations) {
            if(s.getID() == stationID) {
                return s;
            }
        }
        System.err.println("Station " + stationID + " not found.");
        return null;
    }
    
    public Yard getYard() {
        return yard;
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
            System.out.println(l.toStringDetail());
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
    
    /**
     * Prints top-level ArrayList of Beacons in detail.
     * @author Dan Bednarczyk
     */
    public void printBeacons() {
        for(Beacon b : beacons) {
            System.out.println(b.toString());
        }
    }
    
    public TrainSystem getTrainSystem(){
        return this.trainSystem;
    }
}