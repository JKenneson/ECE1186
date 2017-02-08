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
    
    Global.Line line;
    ArrayList<Section> sections = new ArrayList<Section>();
    
    public Line (Global.Line newLine) {
        line = newLine;
    }
    
    public Global.Line getLine() {
        return line;
    }
    
    public ArrayList<Section> getSections() {
        return sections;
    }
    
    public void addSection(Section s) {
        sections.add(s);
    }
    
}
