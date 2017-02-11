/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author kylemonto
 */
public class State {
    
    private Global.TrackGroups group;
    private Global.Presence presence;
    
    public State(Global.TrackGroups group, Global.Presence presence) {
        this.group = group;
        this.presence = presence;
    }

    public Global.TrackGroups getGroup() {
        return group;
    }

    public void setGroup(Global.TrackGroups group) {
        this.group = group;
    }

    public Global.Presence getPresence() {
        return presence;
    }

    public void setPresence(Global.Presence presence) {
        this.presence = presence;
    }

    @Override
    public boolean equals(Object o){
        if(this.group == ((State) o).getGroup() && this.presence == ((State) o).getPresence() ){
            return true;
        } else {
            return false;
        }
    }

    
}
