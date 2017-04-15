/**
* Contains Line information
*
* @author: Dan Bednarczyk
* @creation date: 02/01/2017
* @modification date: 04/20/2017
*/

package com.rogueone.trackmodel;

import com.rogueone.global.Global;
import java.util.ArrayList;

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
            if (lineID == Global.Line.GREEN) {
                if (s.getSectionID() == Global.Section.N || s.getSectionID() == Global.Section.F || s.getSectionID() == Global.Section.E || s.getSectionID() == Global.Section.D) {
                    totalLength += s.getTotalLength();
                }
            } else {
                if (s.getSectionID() == Global.Section.U || s.getSectionID() == Global.Section.A || s.getSectionID() == Global.Section.B || s.getSectionID() == Global.Section.C
                        || s.getSectionID() == Global.Section.F || s.getSectionID() == Global.Section.G || s.getSectionID() == Global.Section.H || s.getSectionID() == Global.Section.I
                        || s.getSectionID() == Global.Section.J ) {
                    totalLength += s.getTotalLength();
                } else if (s.getSectionID() == Global.Section.D || s.getSectionID() == Global.Section.E || s.getSectionID() == Global.Section.S || s.getSectionID() == Global.Section.T
                        || s.getSectionID() == Global.Section.Q || s.getSectionID() == Global.Section.R || s.getSectionID() == Global.Section.P || s.getSectionID() == Global.Section.O ) {
                    totalLength -= s.getTotalLength();
                }
            }
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
