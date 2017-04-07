/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import java.util.ArrayList;

/**
 *
 * @author Dan
 */
public class Line {
    
    Global.Line lineID;
    double totalLength = 0;
    ArrayList<Section> sections = new ArrayList<Section>();
    
    public Line (Global.Line newLine) {
        lineID = newLine;
    }
    
    /**
    * Get ID of line
    * @author Dan Bednarczyk
    * @return line ID
    */
    public Global.Line getLineID() {
        return lineID;
    }
    
    /**
    * Get array of sections in line
    * @author Dan Bednarczyk
    * @return ArrayList of Sections
    */
    public ArrayList<Section> getSections() {
        return sections;
    }
    
    /**
    * Add section to Line
    * @author Dan Bednarczyk
    * @param s Section to add
    */
    public void addSection(Section s) {
        sections.add(s);
    }
    
    /**
    * Get block in line
    * @author Dan Bednarczyk
    * @param block int specifying block to find
    * @return Block requested, null otherwise
    */
    public Block getBlock(int block) {
        for (Section s : sections) {
            for(Block b : s.getBlocks()) {
                if(b.getID() == block) {
                    return b;
                }
            }
        }
        System.err.println("Block " + lineID + ":" + block + " not found in line " + lineID);
        return null;
    }
    
    /**
    * Get block in line
    * @author Dan Bednarczyk
    * @param section Section containing block
    * @param block int specifying block to find
    * @return Block requested, null otherwise
    */
    public Block getBlock(Section section, int block) {
        for (Section s : sections) {
            if (s.equals(section)) {
                return s.getBlock(block);
            }
        }
        System.err.println("Block " + lineID + ":" + section + ":" + block + " not found in " + lineID);
        return null;
    }
    
    /**
    * Get total length of track
    * @author Dan Bednarczyk
    * @return double total length of track
    */
    public double getTotalLength() {
        return totalLength;
    }
    
    /**
    * Recalculate total length. Must be called after adding new sections.
    * @author Dan Bednarczyk
    */
    public void updateTotalLength() {
        totalLength = 0;
        for (Section s : sections) {
            totalLength += s.getTotalLength();
            //System.out.println(s + " " + s.getTotalLength());
        }
    }
    
    /**
    * Get String representation of Line
    * @author Dan Bednarczyk
    * @return String representation of Line ID
    */
    @Override
    public String toString() {
        return lineID.toString();
    }
    
    /**
    * Get detailed String representation of Line
    * @author Dan Bednarczyk
    * @return detailed String representation of Line
    */
    public String toStringDetail() {
        return "Line: " + lineID.toString() + ", Length: " + totalLength + " m";
    }
    
    /**
    * Compare equality with another line
    * @author Dan Bednarczyk
    * @param otherLine line to compare
    * @return boolean indicating equality
    */
    public boolean equals(Line otherLine) {
        return this.lineID == otherLine.getLineID();
    }
}
