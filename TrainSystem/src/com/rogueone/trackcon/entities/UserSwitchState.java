/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import com.rogueone.global.Global;
import java.util.AbstractMap;
import java.util.LinkedList;

/**
 * mapping of switch ID to its current switch state (Default or Alternate)
 * @author kylemonto
 */
public class UserSwitchState {
    
    private LinkedList<AbstractMap.SimpleEntry<Integer, Global.SwitchState>> userSwitchStates;

    public UserSwitchState() {
        this.userSwitchStates = new LinkedList<AbstractMap.SimpleEntry<Integer, Global.SwitchState>>();
    }

    public void addUserSwitchState(AbstractMap.SimpleEntry<Integer, Global.SwitchState> userSwitchState){
        this.userSwitchStates.add(userSwitchState);
    }

    public LinkedList<AbstractMap.SimpleEntry<Integer, Global.SwitchState>> getUserSwitchStates() {
        return userSwitchStates;
    }
    
    
    
    
    
    
}
