/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogueone.trackcon.entities;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Set that will hold State of the track
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
    
    @Override
    public String toString(){
        StringBuilder b = new StringBuilder();
        Iterator itr = stateSet.iterator();
        while(itr.hasNext()){
            State s = (State) itr.next();
            b.append(s.getGroup() + " => " + s.getPresence() + " ");
        }
        return b.toString();
    }
    
    @Override
    public boolean equals(Object o){
        Iterator iteratorThisSet = this.stateSet.iterator();
//        Iterator iteratorObjectSet = ((StateSet) o).getSet().iterator();
        int requiredCount = this.stateSet.size();
        int count = 0;
        boolean result = false;
        while(iteratorThisSet.hasNext()){
            State thisState = (State) iteratorThisSet.next();
            Iterator iteratorObjectSet = ((StateSet) o).getSet().iterator();
            while(iteratorObjectSet.hasNext()){
                State objectState = (State) iteratorObjectSet.next();
                if(thisState.equals(objectState)){
                    count++;
                    break;
                } 
//                System.out.println("Count = " + count);
            }
        }
        if(count == requiredCount){
            return true;
        } else {
            return false;
        }
    }


}
