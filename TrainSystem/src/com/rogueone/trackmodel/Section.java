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
    
    public Global.Section getSectionID() {
        return sectionID;
    }
    
    public Line getLine() {
        return line;
    }
    
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    
    public void addBlock(Block b) {
        blocks.add(b);
    }
    
    public Block getBlock(int block) {
        for (Block b : blocks) {
            if (b.getID() == block) {
                return b;
            }
        }
        return null;
    }
    
    public String toString() {
        return sectionID.toString();
    }
    
    public String toStringDetail() {
        return sectionID.toString() + " (" + line.toString() + ")";
    }
    
    public boolean equals(Section otherSection) {
        return this.line.equals(otherSection.getLine()) && this.sectionID == otherSection.getSectionID();
    }
    //NOTE: This cannot be overloaded with a Global.Section argument becuase lineID is needed to be certain of equality
}
