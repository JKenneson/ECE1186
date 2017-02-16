/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author kylemonto
 */
public class StateSet {
    
    private Set<State> stateSet;

    public StateSet() {
        stateSet = new HashSet<State>();
    }
    
    public void addState(State s){
        stateSet.add(s);
    }
    
    public Set<State> getSet() {
        return stateSet;
    }
    
    
    
}
