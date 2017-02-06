/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trackmodel;

/**
 *
 * @author Dan
 */
public class Block {
    
    private String line;
    private String section;
    private int blockID;
    protected Block portA;
    protected Block portB;
    private int switchID;
    private boolean isStaticSwitchBlock;
    private String stationName;
    private int length;
    private double grade;
    private double elevation;
    private double cumulativeElevation;
    private boolean isHead;
    private boolean isTail;
    private boolean containsCrossing;
    private boolean isUnderground;
    private boolean isCrossingDown;
    
    public Block(String newLine, String newSection, int newBlockID, 
            int newSwitchID, boolean newIsStaticSwitchBlock, 
            String newStationName, int newLength, double newGrade, 
            double newElevation, double newCumulativeElevation, 
            boolean newIsHead, boolean newIsTail, boolean newContainsCrossing, 
            boolean newIsUnderground) {
        line = newLine;
        section = newSection;
        blockID = newBlockID;
        portA = null;
        portB = null;
        switchID = newSwitchID;
        isStaticSwitchBlock = newIsStaticSwitchBlock;
        stationName = newStationName;
        length = newLength;
        grade = newGrade;
        elevation = newElevation;
        cumulativeElevation = newCumulativeElevation;
        isHead = newIsHead;
        isTail = newIsTail;
        containsCrossing = newContainsCrossing;
        isUnderground = newIsUnderground;
        isCrossingDown = false;
    }
    
}
