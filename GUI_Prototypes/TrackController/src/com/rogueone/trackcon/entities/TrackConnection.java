/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.ss.usermodel.Cell;

/**
 *
 * @author kylemonto
 */
public class TrackConnection {
    
    private Global.Section section1;
    private Global.Section section2;
    private int blockID1;
    private int blockID2;

    public TrackConnection(String stringCellValue) {
        Pattern r = Pattern.compile("(\\w+)-(\\d+):(\\w+)-(\\d+)");
        Matcher m = r.matcher(stringCellValue);
        if(m.find()){
            section1 = Global.Section.valueOf(m.group(1));
            section2 = Global.Section.valueOf(m.group(3));
            blockID1 = Integer.parseInt(m.group(2));
            blockID2 = Integer.parseInt(m.group(4));
        } else {
            System.out.println("Nothing found when parsing Track Connnection");
        }
        
    }

    

    public Global.Section getSection1() {
        return section1;
    }

    public void setSection1(Global.Section section1) {
        this.section1 = section1;
    }

    public Global.Section getSection2() {
        return section2;
    }

    public void setSection2(Global.Section section2) {
        this.section2 = section2;
    }

    public int getBlockID1() {
        return blockID1;
    }

    public void setBlockID1(int blockID1) {
        this.blockID1 = blockID1;
    }

    public int getBlockID2() {
        return blockID2;
    }

    public void setBlockID2(int blockID2) {
        this.blockID2 = blockID2;
    }
    
    
}
