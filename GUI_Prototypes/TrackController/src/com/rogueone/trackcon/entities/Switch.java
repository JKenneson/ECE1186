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
public class Switch {
    
    private int switchID;
    private Global.Section section;
    private int blockID;
    private SwitchState switchState;

    public Switch(int switchID, String sectionBlock, SwitchState switchState) {
        this.switchID = switchID;
        Pattern r = Pattern.compile("(.+)-(\\d+)");
        Matcher m = r.matcher(sectionBlock);
        if(m.find()){
            this.section = Global.Section.valueOf(m.group(1));
            this.blockID = Integer.parseInt(m.group(2));
            this.switchState = switchState;
        } else {
            System.out.println("Nothing found when parsing switch");
        }
        
    }


    public int getSwitchID() {
        return switchID;
    }

    public void setSwitchID(int switchID) {
        this.switchID = switchID;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public SwitchState getSwitchState() {
        return switchState;
    }

    public void setSwitchState(SwitchState switchState) {
        this.switchState = switchState;
    }
    
    
    
}
