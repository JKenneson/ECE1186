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
public class Light {
    
    private Global.Section section;
    private int blockID;
    private Global.LightState lightState;

    public Light(String stringCellValue) {
        Pattern r = Pattern.compile("(\\w+)-(\\d+):(\\w+)");
        Matcher m = r.matcher(stringCellValue);
        if(m.find()){
            section = Global.Section.valueOf(m.group(1));
            blockID = Integer.parseInt(m.group(2));
            lightState = Global.LightState.valueOf(m.group(3));
        } else {
            System.out.println("Nothing found in regex for Light");
        }
        
    }


    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public Global.LightState getLightState() {
        return lightState;
    }

    public void setLightState(Global.LightState lightState) {
        this.lightState = lightState;
    }

    public Global.Section getSection() {
        return section;
    }

    public void setSection(Global.Section section) {
        this.section = section;
    }
    
    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(this.section + "-" + this.blockID + ":" + this.lightState);
        return b.toString();
    }
    
    
    
    
    
}
