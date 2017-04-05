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
public class Section {
    Global.Section sectionID;
    Line line;
    ArrayList<Block> blocks = new ArrayList<Block>();
    
    public Section (Global.Section newSection, Line newLine) {
        sectionID = newSection;
        line = newLine;
    }
    
    /**
    * Get ID of section
    * @author Dan Bednarczyk
    * @return section ID
    */
    public Global.Section getSectionID() {
        return sectionID;
    }
    
    /**
    * Get Line
    * @author Dan Bednarczyk
    * @return Line object that Block is on
    */
    public Line getLine() {
        return line;
    }
    
    /**
    * Get array of Blocks in Section
    * @author Dan Bednarczyk
    * @return ArrayList of Blocks
    */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    
    /**
    * Add Block to Section
    * @author Dan Bednarczyk
    * @param b Block to add
    */
    public void addBlock(Block b) {
        blocks.add(b);
    }
    
    /**
    * Get block in section
    * @author Dan Bednarczyk
    * @param block int specifying block to find
    * @return Block requested, null otherwise
    */
    public Block getBlock(int block) {
        for (Block b : blocks) {
            if (b.getID() == block) {
                return b;
            }
        }
        System.err.println("Block " + line + ":" + sectionID + ":" + block + " not found in section " + sectionID);
        return null;
    }
    
    /**
    * Get String representation of Section
    * @author Dan Bednarczyk
    * @return String representation of Section ID
    */
    @Override
    public String toString() {
        return sectionID.toString();
    }
    
    /**
    * Get detailed String representation of Section
    * @author Dan Bednarczyk
    * @return String representation of Section ID and Line ID
    */
    public String toStringDetail() {
        return sectionID.toString() + " (" + line.toString() + ")";
    }
    
    /**
    * Compare equality with another section
    * @author Dan Bednarczyk
    * @param otherSection section to compare
    * @return boolean indicating equality
    */
    public boolean equals(Section otherSection) {
        return this.line.getLineID() == otherSection.getLine().getLineID() && this.sectionID == otherSection.getSectionID();
    }
}
