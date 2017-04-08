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
 * State class that serves as a mapping for a track group to a particular presence
 * @author kylemonto
 */
public class State {
    
    private Global.TrackGroupsGreen group;
    private Global.TrackGroupsRed groupRed;
    private Global.Presence presence;
    
    public State(Global.TrackGroupsGreen group, Global.Presence presence) {
        this.group = group;
        this.presence = presence;
    }
    
    public State(Global.TrackGroupsRed groupRed, Global.Presence presence){
        this.groupRed = groupRed;
        this.presence = presence;
    }

    public Global.TrackGroupsGreen getGroup() {
        return group;
    }

    public void setGroup(Global.TrackGroupsGreen group) {
        this.group = group;
    }

    public Global.TrackGroupsRed getGroupRed() {
        return groupRed;
    }

    public void setGroupRed(Global.TrackGroupsRed groupRed) {
        this.groupRed = groupRed;
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
